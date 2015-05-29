package com.hqbrowser.queue;

import com.hqbrowser.config.ServerConfiguration;
import com.hqbrowser.management.HornetQServerControlImpl;
import com.hqbrowser.management.QueueControlImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.api.core.management.QueueControl;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

/**
 *
 * @author Pragalathan M
 */
public class HornetQInstance {

    private static final String USERNAME = "hornetqadmin";
    private static final String password = "82b3f9e60b8";
    public static final int DEFAULT_PORT = 5445;
    public static final String DEFAULT_HOST = "localhost";
    public static final String MANAGEMENT_QUEUE = "jms.queue.hornetq.management";

    private ClientSession session;
    private ServerLocator locator;
    private boolean backup;
    private final HornetQServerControl hornetQServerControl;
    private Map<String, QueueControl> queueControls = new HashMap<>();
    private final ClientSessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(HornetQInstance.class.getName());

    public HornetQInstance() throws Exception {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public HornetQInstance(String host) throws Exception {
        this(host, DEFAULT_PORT);
    }

    public HornetQInstance(String host, int port) throws HornetQException, Exception {
        this(host, port, false);
    }

    public HornetQInstance(ServerConfiguration configuration) throws HornetQException, Exception {
        this(configuration.getHost(), configuration.getPort(), false);
    }

    public HornetQInstance(ServerConfiguration configuration, boolean backup) throws HornetQException, Exception {
        this(configuration.getHost(), configuration.getPort(), backup);
    }

    public HornetQInstance(String host, int port, boolean backup) throws HornetQException, Exception {
        Map<String, Object> map = new HashMap<>();
        map.put(TransportConstants.HOST_PROP_NAME, host);
        map.put(TransportConstants.PORT_PROP_NAME, port);
        TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), map);
        locator = HornetQClient.createServerLocatorWithoutHA(transportConfiguration);
        locator.setReconnectAttempts(3);
        sessionFactory = locator.createSessionFactory();
        session = sessionFactory.createSession(USERNAME, password, false, true, true, false, 1);
        session.start();
        hornetQServerControl = new HornetQServerControlImpl(session);
        this.backup = backup;
    }

    String getAddress() {
        return sessionFactory.getConnection().getRemoteAddress();
    }

    List<String> getQueueNames() {
        return Arrays.asList(hornetQServerControl.getQueueNames());
    }

    QueueControl getQueueControl(String queueName) throws Exception {
        QueueControl control = queueControls.get(queueName);
        if (control == null) {
            control = new QueueControlImpl(session, new SimpleString(queueName));
            queueControls.put(queueName, control);
        }
        return control;
    }

    ClientConsumer createBrowser(String queueName) throws HornetQException {
        return session.createConsumer(queueName, true);
    }

    boolean createQueue(String queueName) throws HornetQException {
        Set<String> existingQueues = new HashSet<>(getQueueNames());
        if (existingQueues.contains(queueName)) {
            return false;
        }
        session.createQueue(queueName, queueName, true);
        return true;
    }

    void cleanup() throws Exception {
        if (session == null) {
            return;
        }
        logger.log(Level.INFO, "Shutting down HornetQInstance: {0} ...", getAddress());
        session.close();
        session = null;
        locator.close();
        locator = null;
    }
}
