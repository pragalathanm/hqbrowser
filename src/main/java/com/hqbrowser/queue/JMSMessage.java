package com.hqbrowser.queue;

import java.util.Date;
import javax.jms.JMSException;
import org.hornetq.api.core.Message;
import org.hornetq.api.core.management.QueueControl;

/**
 *
 * @author Pragalathan M
 */
public class JMSMessage {

    private Long id;
    private Message delegate;
    private String text;
    private String longText;
    private String serverAddress;
    private QueueControl queueControl;

    public JMSMessage(Message delegate, QueueControl queueControl, String serverAddress) {
        this.delegate = delegate;
        this.queueControl = queueControl;
        this.id = delegate.getMessageID();
        this.text = delegate.getBodyBuffer().readString();
        if (text.length() > 100) {
            longText = text;
            text = text.substring(0, 97) + "...";
        }
        this.serverAddress = serverAddress;
    }

    public JMSMessage(Message delegate, QueueControl queueControl) {
        this(delegate, queueControl, null);
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public QueueControl getQueueControl() {
        return queueControl;
    }

    public Long getId() {
        return id;
    }

    public Message getDelegate() {
        return delegate;
    }

    public String getText() {
        return text;
    }

    public String getLongText() {
        return longText;
    }

    public Date getTimestamp() throws JMSException {
        return new Date(delegate.getTimestamp());
    }
}
