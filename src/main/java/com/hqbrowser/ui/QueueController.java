package com.hqbrowser.ui;

import com.hqbrowser.queue.JMSMessage;
import com.hqbrowser.queue.LogicalQueue;
import com.hqbrowser.queue.QueueManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.jms.JMSException;
import org.hornetq.api.core.HornetQException;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Pragalathan M
 */
@Named
@SessionScoped
public class QueueController implements Serializable {

    @EJB
    private QueueManager queueManager;

    private List<LogicalQueue> queues;
    private List<LogicalQueue> filteredQueues;
    private LogicalQueue selectedQueue;
    private List<JMSMessage> selectedMessages;
    private String newQueueName;

    private static final Logger logger = Logger.getLogger(QueueController.class.getName());
    private MessageModel messageModel = new MessageModel();

    @PostConstruct
    private void init() {
        reload();
        reset();
    }

    private void reload() {
        try {
            queues = queueManager.listQueues();
            Collections.sort(queues, queueComparator);
            Map<String, LogicalQueue> queueToDlqMap = new HashMap<>();
            for (LogicalQueue queue : queues) {
                queueToDlqMap.put(queue.getName(), queue);
            }
            for (LogicalQueue queue : queues) {
                LogicalQueue counterPart = queueToDlqMap.get(queue.getDeadLetterAddress());
                if (counterPart != null) {
                    counterPart.setCounterPart(queue);
                    queue.setCounterPart(counterPart);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void reset() {
        try {
            for (LogicalQueue queue : queues) {
                queue.calculateMessageCount();
            }
            for (LogicalQueue logicalQueue : queues) {
                logicalQueue.getMessages().clear();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void moveMessage() throws JMSException, Exception {
        if (selectedMessages.isEmpty()) {
            addMessage("Error", "Please select a message");
            return;
        }
        try {
            selectedQueue.moveMessages(selectedMessages);
            addMessage("Success", "Messages have been moved");
        } catch (Exception ex) {
            addMessage("Some messages have not been moved", ex.getMessage());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void removeMessage() throws JMSException, Exception {
        if (selectedMessages.isEmpty()) {
            addMessage("Error", "Please select a message");
            return;
        }
        try {
            selectedQueue.removeMessages(selectedMessages);
            addMessage("Success", "Messages have been removed");
        } catch (Exception ex) {
            addMessage("Some messages have not been removed ", ex.getMessage());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void initDialog() {
        this.newQueueName = "";
        RequestContext.getCurrentInstance().reset(":newQueueDialogForm:newQueuePanel");
    }

    public void createQueue() throws HornetQException {
        queueManager.createQueue(newQueueName);
        reload();
        addMessage("Success", "Queue has been created.");
    }

    public void pauseQueue() {
        if (selectedQueue == null) {
            addMessage("Error", "Please select a queue");
            return;
        }
        try {
            selectedQueue.pause();
            addMessage("Success", "Queue has been Paused.");
        } catch (Exception ex) {
            addMessage("Error occured!", ex.getMessage());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void resumeQueue() {
        if (selectedQueue == null) {
            addMessage("Error", "Please select a queue");
            return;
        }
        try {
            selectedQueue.resume();
            addMessage("Success", "Queue has been resumed.");
        } catch (Exception ex) {
            addMessage("Error occured!", ex.getMessage());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void drainQueue() {
        if (selectedQueue == null) {
            addMessage("Error", "Please select a queue");
            return;
        }
        try {
            selectedQueue.drain();
            addMessage("Success", "Queue has been drained.");
            selectedQueue.getMessages().clear();
            selectedQueue.calculateMessageCount();
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":listForm:dataTable1");
            dataTable.setFirst(0);
        } catch (Exception ex) {
            addMessage("Error occured!", ex.getMessage());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        reset();
    }

    public void onRowSelect(SelectEvent event) {
        if (selectedQueue == null) {
            messageModel.setWrappedData(Collections.<JMSMessage>emptyList());
        } else {
            messageModel.setWrappedData(selectedQueue.getMessages());
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":listForm:dataTable1");
            dataTable.setFirst(0);
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        messageModel.setWrappedData(Collections.<JMSMessage>emptyList());
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public LogicalQueue getSelectedQueue() {
        return selectedQueue;
    }

    public void setSelectedQueue(LogicalQueue selectedQueue) {
        this.selectedQueue = selectedQueue;
    }

    public List<LogicalQueue> getQueues() {
        return queues;
    }

    public List<LogicalQueue> getFilteredQueues() {
        return filteredQueues;
    }

    public void setFilteredQueues(List<LogicalQueue> filteredQueues) {
        this.filteredQueues = filteredQueues;
    }

    public List<JMSMessage> getSelectedMessages() {
        return selectedMessages;
    }

    public void setSelectedMessages(List<JMSMessage> selectedMessages) {
        this.selectedMessages = selectedMessages;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public String getNewQueueName() {
        return newQueueName;
    }

    public void setNewQueueName(String newQueueName) {
        this.newQueueName = newQueueName;
    }

    private Comparator<LogicalQueue> queueComparator = new Comparator<LogicalQueue>() {

        @Override
        public int compare(LogicalQueue q1, LogicalQueue q2) {
            int index = nutralize(q1.getName()).compareTo(nutralize(q2.getName()));
            return index == 0 ? q1.getName().compareTo(q2.getName()) : index;
        }

        private String nutralize(String name) {
            if (name.contains("DLQ")) {
                return name.substring(0, name.length() - 3);
            }
            return name;
        }
    };

    public class MessageModel extends LazyDataModel<JMSMessage> {

        final int WINDOW_SIZE = 40;
        int windowStart = 0;
        int windowEnd = -1;

        @Override
        public int getRowCount() {
            if (selectedQueue != null) {
                return selectedQueue.getMessageCount();
            }
            return 0;
        }

        @Override
        public JMSMessage getRowData(String id) {
            if (id == null) {
                return null;
            }
            for (JMSMessage message : selectedQueue.getMessages()) {
                if (message.getId().toString().equals(id)) {
                    return message;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(JMSMessage message) {
            return message.getId();
        }

        private boolean isIndexLoaded(int index) {
            return windowStart <= index && windowEnd >= index;
        }

        @Override
        public List<JMSMessage> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            if (selectedQueue != null) {
                if (selectedQueue.getMessages().isEmpty()) {
                    windowStart = windowEnd = 0;
                }
                if (!isIndexLoaded(first) || !isIndexLoaded(first + pageSize)) {
                    try {
                        int from = first - pageSize;
                        if (from < 0) {
                            from = 0;
                        }
                        queueManager.loadMessage(selectedQueue, from, WINDOW_SIZE);
                        windowStart = from;
                        windowEnd = windowStart + selectedQueue.getMessages().size();
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }

                // ToDo:
                //  2015-03-26 18:50:38,585 SEVERE [javax.enterprise.resource.webcontainer.jsf.context] java.lang.IllegalArgumentException: fromIndex(10) > toIndex(0)
                //  at java.util.ArrayList.subListRangeCheck(ArrayList.java:1006)
                //  at java.util.ArrayList.subList(ArrayList.java:996)
                //  at com.hqbrowser.ui.QueueController$MessageModel.load(QueueController.java:328)
                if (selectedQueue.getMessages().isEmpty()) {
                    return Collections.<JMSMessage>emptyList();
                }
                return selectedQueue.getMessages().subList(first - windowStart, Math.min(selectedQueue.getMessages().size(), first - windowStart + pageSize));
            }
            return null;
        }
    }
}
