package com.hqbrowser.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import org.hornetq.api.core.management.QueueControl;

/**
 *
 * @author Pragalathan M
 */
public class LogicalQueue {

    private static final Logger logger = Logger.getLogger(LogicalQueue.class.getName());

    private String displayName;
    private String name;
    private int messageCount;
    private List<QueueControl> delegates = new ArrayList<>();
    private LogicalQueue counterPart;
    private List<JMSMessage> messages = new ArrayList<>();

    public LogicalQueue() {
    }

    public LogicalQueue(String name, QueueControl control) {
        this(name.startsWith("jms.queue.") ? name.substring("jms.queue.".length()) : name, name, control);
    }

    public LogicalQueue(String name, String logicalName, QueueControl control) {
        this.displayName = name;
        this.name = logicalName;
        this.delegates.add(control);
    }

    public void removeMessages(List<JMSMessage> messages) throws JMSException, Exception {
        logger.log(Level.INFO, "Removing {0} message(s) from {1}", new Object[]{messages.size(), displayName});
        for (JMSMessage message : messages) {
            boolean success = message.getQueueControl().removeMessage(message.getId());
            logger.log(Level.INFO, "Message with id[{1}] has {0}been removed", new Object[]{success ? "" : "not ", message.getId()});
            if (success) {
                messageCount--;
            }
        }
        this.messages.clear();
    }

    public void moveMessages(List<JMSMessage> messages) throws JMSException, Exception {
        logger.log(Level.INFO, "Moving {0} message(s) from {1}", new Object[]{messages.size(), displayName});
        int moved = 0;
        for (JMSMessage message : messages) {
            boolean success = message.getQueueControl().sendMessageToDeadLetterAddress(message.getId());
            logger.log(Level.INFO, "Message with id[{1}] has {0}been moved", new Object[]{success ? "" : "not ", message.getId()});
            if (success) {
                messageCount--;
                moved++;
            }
        }
        this.messages.clear();
        if (counterPart != null) {
            counterPart.messageCount += moved;
            counterPart.messages.clear();
        }
    }

    public void drain() throws JMSException, Exception {
        logger.log(Level.INFO, "Draining queue {0}", displayName);
        int result = 0;
        for (QueueControl delegate : delegates) {
            result += delegate.removeMessages(null);
        }
        logger.log(Level.INFO, "Drained {0} messages from the queue: {1}", new Object[]{result, displayName});
        messages.clear();
        messageCount -= result;
    }

    public void pause() throws Exception {
        for (QueueControl delegate : delegates) {
            delegate.pause();
        }
    }

    public void resume() throws Exception {
        for (QueueControl delegate : delegates) {
            delegate.resume();
        }
    }

    public void calculateMessageCount() {
        messageCount = 0;
        for (QueueControl delegate : delegates) {
            messageCount += (int) delegate.getMessageCount();
        }
    }

    List<QueueControl> getDelegates() {
        return delegates;
    }

    public String getDeadLetterAddress() {
        return delegates.get(0).getDeadLetterAddress();
    }

    public LogicalQueue getCounterPart() {
        return counterPart;
    }

    public void clearMessages() {
        messages.clear();
    }

    public void setCounterPart(LogicalQueue counterPart) {
        this.counterPart = counterPart;
    }

    public List<JMSMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<JMSMessage> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPaused() throws Exception {
        boolean paused = false;
        for (QueueControl delegate : delegates) {
            paused |= delegate.isPaused();
        }
        return paused;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void decrementMessageCount(long quantity) {
        messageCount -= quantity;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public String toString() {
        return "LogicalQueue{" + "name=" + displayName + ", logicalName=" + name + ", counterPart=" + counterPart.displayName + '}';
    }
}
