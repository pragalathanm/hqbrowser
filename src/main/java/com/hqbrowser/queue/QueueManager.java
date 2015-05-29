package com.hqbrowser.queue;

import com.hqbrowser.config.ClusterConfiguration;
import com.hqbrowser.config.ServerConfiguration;
import static com.hqbrowser.queue.HornetQInstance.MANAGEMENT_QUEUE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Pragalathan M
 */
@Singleton
@LocalBean
public class QueueManager {

    private static final Logger logger = Logger.getLogger(QueueManager.class.getName());

    private List<HornetQInstance> liveInstances = new ArrayList<>();
    private List<HornetQInstance> backupInstances = new ArrayList<>();
    ClusterConfiguration clusterConfiguration;

    @PostConstruct
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("hornetq-servers.xml");
        clusterConfiguration = context.getBean(ClusterConfiguration.class);

        try {
            for (ServerConfiguration liveServeConfiguration : clusterConfiguration.getLiveServeConfigurations()) {
                liveInstances.add(new HornetQInstance(liveServeConfiguration));
            }
            for (ServerConfiguration liveServeConfiguration : clusterConfiguration.getBackupServeConfigurations()) {
                backupInstances.add(new HornetQInstance(liveServeConfiguration));
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public ClusterConfiguration getClusterConfiguration() {
        return clusterConfiguration;
    }

    @PreDestroy
    public void cleanup() {
        for (HornetQInstance instance : liveInstances) {
            try {
                instance.cleanup();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        for (HornetQInstance instance : backupInstances) {
            try {
                instance.cleanup();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<LogicalQueue> listQueues() throws Exception {
        Map<String, LogicalQueue> queues = new HashMap<>();
        for (HornetQInstance instance : liveInstances) {
            for (String name : instance.getQueueNames()) {
                if (queues.containsKey(name)) {
                    queues.get(name).getDelegates().add(instance.getQueueControl(name));
                } else if (!name.startsWith(MANAGEMENT_QUEUE) && name.startsWith("jms.queue")) {
                    queues.put(name, new LogicalQueue(name, instance.getQueueControl(name)));
                }
            }
        }
        return new ArrayList<>(queues.values());
    }

    public void loadMessage(LogicalQueue queue, int from, int count) throws HornetQException, Exception {
        logger.log(Level.FINE, "Loading {0} messages from {1}", new Object[]{count, queue.getDisplayName()});
        List<JMSMessage> messages = queue.getMessages();
        messages.clear();
        int index = 0;
        for (HornetQInstance instance : liveInstances) {
            try (ClientConsumer browser = instance.createBrowser(queue.getName())) {
                ClientMessage message;
                while ((message = browser.receiveImmediate()) != null) {
                    if (index >= from) {
                        messages.add(new JMSMessage(message, instance.getQueueControl(queue.getName()), instance.getAddress()));
                    }
                    index++;
                    if (messages.size() >= count) {
                        return;
                    }
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, null, ex); // incase queue is not present in this instance
            }
        }
    }

    public boolean createQueue(String queueName) throws HornetQException {
        if (queueName == null || queueName.trim().isEmpty()) {
            throw new IllegalArgumentException("Queue name can't be empty");
        }
        String q = queueName;
        if (!q.startsWith("jms.queue.")) {
            q = "jms.queue." + q;
        }

        String dlq = q + "DLQ";
        for (HornetQInstance instance : liveInstances) {
            instance.createQueue(q);
            instance.createQueue(dlq);
        }
//        for (HornetQInstance instance : backupInstances) {
//            instance.createQueue(q);
//            instance.createQueue(dlq);
//        }
        return true;
    }
}
