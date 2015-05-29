package com.hqbrowser.management;

import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.api.core.management.Parameter;
import org.hornetq.api.core.management.ResourceNames;

/**
 *
 * @author Pragalathan M
 */
public class HornetQServerControlImpl implements HornetQServerControl {

    private final CoreMessagingProxy proxy;

    public HornetQServerControlImpl(ClientSession session) throws Exception {
        this.proxy = new CoreMessagingProxy(session, ResourceNames.CORE_SERVER);
    }

    @Override
    public boolean isSharedStore() {
        return (Boolean) proxy.retrieveAttributeValue("sharedStore");
    }

    @Override
    public boolean closeConnectionsForAddress(final String ipAddress) throws Exception {
        return (Boolean) proxy.invokeOperation("closeConnectionsForAddress", ipAddress);
    }

    public boolean closeConsumerConnectionsForAddress(final String address) throws Exception {
        return (Boolean) proxy.invokeOperation("closeConsumerConnectionsForAddress", address);
    }

    public boolean closeConnectionsForUser(final String userName) throws Exception {
        return (Boolean) proxy.invokeOperation("closeConnectionsForUser", userName);
    }

    @Override
    public boolean commitPreparedTransaction(final String transactionAsBase64) throws Exception {
        return (Boolean) proxy.invokeOperation("commitPreparedTransaction", transactionAsBase64);
    }

    @Override
    public void createQueue(final String address, final String name) throws Exception {
        proxy.invokeOperation("createQueue", address, name);
    }

    @Override
    public void createQueue(final String address, final String name, final String filter, final boolean durable) throws Exception {
        proxy.invokeOperation("createQueue", address, name, filter, durable);
    }

    @Override
    public void createQueue(final String address, final String name, final boolean durable) throws Exception {
        proxy.invokeOperation("createQueue", address, name, durable);
    }

    @Override
    public void deployQueue(final String address, final String name, final String filter, final boolean durable) throws Exception {
        proxy.invokeOperation("deployQueue", address, name, filter, durable);
    }

    @Override
    public void deployQueue(final String address, final String name, final String filterString) throws Exception {
        proxy.invokeOperation("deployQueue", address, name);
    }

    @Override
    public void destroyQueue(final String name) throws Exception {
        proxy.invokeOperation("destroyQueue", name);
    }

    @Override
    public void disableMessageCounters() throws Exception {
        proxy.invokeOperation("disableMessageCounters");
    }

    @Override
    public void enableMessageCounters() throws Exception {
        proxy.invokeOperation("enableMessageCounters");
    }

    @Override
    public String getBindingsDirectory() {
        return (String) proxy.retrieveAttributeValue("bindingsDirectory");
    }

    @Override
    public int getConnectionCount() {
        return (Integer) proxy.retrieveAttributeValue("connectionCount");
    }

    @Override
    public long getConnectionTTLOverride() {
        return (Long) proxy.retrieveAttributeValue("connectionTTLOverride", Long.class);
    }

    @Override
    public Object[] getConnectors() throws Exception {
        return (Object[]) proxy.retrieveAttributeValue("connectors");
    }

    @Override
    public String getConnectorsAsJSON() throws Exception {
        return (String) proxy.retrieveAttributeValue("connectorsAsJSON");
    }

    @Override
    public String[] getAddressNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("addressNames"));
    }

    @Override
    public String[] getQueueNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("queueNames"));
    }

    @Override
    public int getIDCacheSize() {
        return (Integer) proxy.retrieveAttributeValue("IDCacheSize");
    }

    @Override
    public String[] getInterceptorClassNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("incomingInterceptorClassNames"));
    }

    @Override
    public String[] getIncomingInterceptorClassNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("incomingInterceptorClassNames"));
    }

    @Override
    public String[] getOutgoingInterceptorClassNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("outgoingInterceptorClassNames"));
    }

    @Override
    public String getJournalDirectory() {
        return (String) proxy.retrieveAttributeValue("journalDirectory");
    }

    @Override
    public int getJournalFileSize() {
        return (Integer) proxy.retrieveAttributeValue("journalFileSize");
    }

    @Override
    public int getJournalMaxIO() {
        return (Integer) proxy.retrieveAttributeValue("journalMaxIO");
    }

    @Override
    public int getJournalMinFiles() {
        return (Integer) proxy.retrieveAttributeValue("journalMinFiles");
    }

    @Override
    public String getJournalType() {
        return (String) proxy.retrieveAttributeValue("journalType");
    }

    @Override
    public String getLargeMessagesDirectory() {
        return (String) proxy.retrieveAttributeValue("largeMessagesDirectory");
    }

    @Override
    public String getManagementAddress() {
        return (String) proxy.retrieveAttributeValue("managementAddress");
    }

    @Override
    public String getManagementNotificationAddress() {
        return (String) proxy.retrieveAttributeValue("managementNotificationAddress");
    }

    @Override
    public int getMessageCounterMaxDayCount() {
        return (Integer) proxy.retrieveAttributeValue("messageCounterMaxDayCount");
    }

    @Override
    public long getMessageCounterSamplePeriod() {
        return (Long) proxy.retrieveAttributeValue("messageCounterSamplePeriod", Long.class);
    }

    @Override
    public long getMessageExpiryScanPeriod() {
        return (Long) proxy.retrieveAttributeValue("messageExpiryScanPeriod", Long.class);
    }

    @Override
    public long getMessageExpiryThreadPriority() {
        return (Long) proxy.retrieveAttributeValue("messageExpiryThreadPriority", Long.class);
    }

    @Override
    public String getPagingDirectory() {
        return (String) proxy.retrieveAttributeValue("pagingDirectory");
    }

    @Override
    public int getScheduledThreadPoolMaxSize() {
        return (Integer) proxy.retrieveAttributeValue("scheduledThreadPoolMaxSize");
    }

    @Override
    public int getThreadPoolMaxSize() {
        return (Integer) proxy.retrieveAttributeValue("threadPoolMaxSize");
    }

    @Override
    public long getSecurityInvalidationInterval() {
        return (Long) proxy.retrieveAttributeValue("securityInvalidationInterval", Long.class);
    }

    @Override
    public long getTransactionTimeout() {
        return (Long) proxy.retrieveAttributeValue("transactionTimeout", Long.class);
    }

    @Override
    public long getTransactionTimeoutScanPeriod() {
        return (Long) proxy.retrieveAttributeValue("transactionTimeoutScanPeriod", Long.class);
    }

    @Override
    public String getVersion() {
        return (String) proxy.retrieveAttributeValue("version");
    }

    @Override
    public boolean isBackup() {
        return (Boolean) proxy.retrieveAttributeValue("backup");
    }

    @Override
    public boolean isClustered() {
        return (Boolean) proxy.retrieveAttributeValue("clustered");
    }

    @Override
    public boolean isCreateBindingsDir() {
        return (Boolean) proxy.retrieveAttributeValue("createBindingsDir");
    }

    @Override
    public boolean isCreateJournalDir() {
        return (Boolean) proxy.retrieveAttributeValue("createJournalDir");
    }

    @Override
    public boolean isJournalSyncNonTransactional() {
        return (Boolean) proxy.retrieveAttributeValue("journalSyncNonTransactional");
    }

    @Override
    public boolean isJournalSyncTransactional() {
        return (Boolean) proxy.retrieveAttributeValue("journalSyncTransactional");
    }

    @Override
    public void setFailoverOnServerShutdown(boolean failoverOnServerShutdown) throws Exception {
        proxy.invokeOperation("setFailoverOnServerShutdown", failoverOnServerShutdown);
    }

    @Override
    public boolean isFailoverOnServerShutdown() {
        return (Boolean) proxy.retrieveAttributeValue("failoverOnServerShutdown");
    }

    @Override
    public boolean isMessageCounterEnabled() {
        return (Boolean) proxy.retrieveAttributeValue("messageCounterEnabled");
    }

    @Override
    public boolean isPersistDeliveryCountBeforeDelivery() {
        return (Boolean) proxy.retrieveAttributeValue("persistDeliveryCountBeforeDelivery");
    }

    @Override
    public boolean isAsyncConnectionExecutionEnabled() {
        return (Boolean) proxy.retrieveAttributeValue("asyncConnectionExecutionEnabled");
    }

    @Override
    public boolean isPersistIDCache() {
        return (Boolean) proxy.retrieveAttributeValue("persistIDCache");
    }

    @Override
    public boolean isSecurityEnabled() {
        return (Boolean) proxy.retrieveAttributeValue("securityEnabled");
    }

    @Override
    public boolean isStarted() {
        return (Boolean) proxy.retrieveAttributeValue("started");
    }

    @Override
    public boolean isWildcardRoutingEnabled() {
        return (Boolean) proxy.retrieveAttributeValue("wildcardRoutingEnabled");
    }

    @Override
    public String[] listConnectionIDs() throws Exception {
        return (String[]) proxy.invokeOperation("listConnectionIDs");
    }

    @Override
    public String[] listPreparedTransactions() throws Exception {
        return (String[]) proxy.invokeOperation("listPreparedTransactions");
    }

    @Override
    public String listPreparedTransactionDetailsAsJSON() throws Exception {
        return (String) proxy.invokeOperation("listPreparedTransactionDetailsAsJSON");
    }

    @Override
    public String listPreparedTransactionDetailsAsHTML() throws Exception {
        return (String) proxy.invokeOperation("listPreparedTransactionDetailsAsHTML");
    }

    @Override
    public String[] listHeuristicCommittedTransactions() throws Exception {
        return (String[]) proxy.invokeOperation("listHeuristicCommittedTransactions");
    }

    @Override
    public String[] listHeuristicRolledBackTransactions() throws Exception {
        return (String[]) proxy.invokeOperation("listHeuristicRolledBackTransactions");
    }

    @Override
    public String[] listRemoteAddresses() throws Exception {
        return (String[]) proxy.invokeOperation("listRemoteAddresses");
    }

    @Override
    public String[] listRemoteAddresses(final String ipAddress) throws Exception {
        return (String[]) proxy.invokeOperation("listRemoteAddresses", ipAddress);
    }

    @Override
    public String[] listSessions(final String connectionID) throws Exception {
        return (String[]) proxy.invokeOperation("listSessions", connectionID);
    }

    @Override
    public void resetAllMessageCounterHistories() throws Exception {
        proxy.invokeOperation("resetAllMessageCounterHistories");
    }

    @Override
    public void resetAllMessageCounters() throws Exception {
        proxy.invokeOperation("resetAllMessageCounters");
    }

    @Override
    public boolean rollbackPreparedTransaction(final String transactionAsBase64) throws Exception {
        return (Boolean) proxy.invokeOperation("rollbackPreparedTransaction", transactionAsBase64);
    }

    @Override
    public void sendQueueInfoToQueue(final String queueName, final String address) throws Exception {
        proxy.invokeOperation("sendQueueInfoToQueue", queueName, address);
    }

    @Override
    public void setMessageCounterMaxDayCount(final int count) throws Exception {
        proxy.invokeOperation("setMessageCounterMaxDayCount", count);
    }

    @Override
    public void setMessageCounterSamplePeriod(final long newPeriod) throws Exception {
        proxy.invokeOperation("setMessageCounterSamplePeriod", newPeriod);
    }

    @Override
    public int getJournalBufferSize() {
        return (Integer) proxy.retrieveAttributeValue("JournalBufferSize");
    }

    @Override
    public int getJournalBufferTimeout() {
        return (Integer) proxy.retrieveAttributeValue("JournalBufferTimeout");
    }

    @Override
    public int getJournalCompactMinFiles() {
        return (Integer) proxy.retrieveAttributeValue("JournalCompactMinFiles");
    }

    @Override
    public int getJournalCompactPercentage() {
        return (Integer) proxy.retrieveAttributeValue("JournalCompactPercentage");
    }

    @Override
    public boolean isPersistenceEnabled() {
        return (Boolean) proxy.retrieveAttributeValue("PersistenceEnabled");
    }

    @Override
    public void addSecuritySettings(String addressMatch,
            String sendRoles,
            String consumeRoles,
            String createDurableQueueRoles,
            String deleteDurableQueueRoles,
            String createNonDurableQueueRoles,
            String deleteNonDurableQueueRoles,
            String manageRoles) throws Exception {
        proxy.invokeOperation("addSecuritySettings",
                addressMatch,
                sendRoles,
                consumeRoles,
                createDurableQueueRoles,
                deleteDurableQueueRoles,
                createNonDurableQueueRoles,
                deleteNonDurableQueueRoles,
                manageRoles);
    }

    @Override
    public void removeSecuritySettings(String addressMatch) throws Exception {
        proxy.invokeOperation("removeSecuritySettings", addressMatch);
    }

    @Override
    public Object[] getRoles(String addressMatch) throws Exception {
        return (Object[]) proxy.invokeOperation("getRoles", addressMatch);
    }

    @Override
    public String getRolesAsJSON(String addressMatch) throws Exception {
        return (String) proxy.invokeOperation("getRolesAsJSON", addressMatch);
    }

    @Override
    public void addAddressSettings(@Parameter(desc = "an address match", name = "addressMatch") String addressMatch,
            @Parameter(desc = "the dead letter address setting", name = "DLA") String DLA,
            @Parameter(desc = "the expiry address setting", name = "expiryAddress") String expiryAddress,
            @Parameter(desc = "the expiry delay setting", name = "expiryDelay") long expiryDelay,
            @Parameter(desc = "are any queues created for this address a last value queue", name = "lastValueQueue") boolean lastValueQueue,
            @Parameter(desc = "the delivery attempts", name = "deliveryAttempts") int deliveryAttempts,
            @Parameter(desc = "the max size in bytes", name = "maxSizeBytes") long maxSizeBytes,
            @Parameter(desc = "the page size in bytes", name = "pageSizeBytes") int pageSizeBytes,
            @Parameter(desc = "the max number of pages in the soft memory cache", name = "pageMaxCacheSize") int pageMaxCacheSize,
            @Parameter(desc = "the redelivery delay", name = "redeliveryDelay") long redeliveryDelay,
            @Parameter(desc = "the redelivery delay multiplier", name = "redeliveryMultiplier") double redeliveryMultiplier,
            @Parameter(desc = "the maximum redelivery delay", name = "maxRedeliveryDelay") long maxRedeliveryDelay,
            @Parameter(desc = "the redistribution delay", name = "redistributionDelay") long redistributionDelay,
            @Parameter(desc = "do we send to the DLA when there is no where to route the message", name = "sendToDLAOnNoRoute") boolean sendToDLAOnNoRoute,
            @Parameter(desc = "the policy to use when the address is full", name = "addressFullMessagePolicy") String addressFullMessagePolicy) throws Exception {
        proxy.invokeOperation("addAddressSettings",
                addressMatch,
                DLA,
                expiryAddress,
                expiryDelay,
                lastValueQueue,
                deliveryAttempts,
                maxSizeBytes,
                pageSizeBytes,
                pageMaxCacheSize,
                redeliveryDelay,
                redeliveryMultiplier,
                maxRedeliveryDelay,
                redistributionDelay,
                sendToDLAOnNoRoute,
                addressFullMessagePolicy);
    }

    @Override
    public void removeAddressSettings(String addressMatch) throws Exception {
        proxy.invokeOperation("removeAddressSettings", addressMatch);
    }

    @Override
    public void createDivert(String name,
            String routingName,
            String address,
            String forwardingAddress,
            boolean exclusive,
            String filterString,
            String transformerClassName) throws Exception {
        proxy.invokeOperation("createDivert",
                name,
                routingName,
                address,
                forwardingAddress,
                exclusive,
                filterString,
                transformerClassName);
    }

    @Override
    public void destroyDivert(String name) throws Exception {
        proxy.invokeOperation("destroyDivert", name);
    }

    @Override
    public String[] getBridgeNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("bridgeNames"));
    }

    @Override
    public void destroyBridge(String name) throws Exception {
        proxy.invokeOperation("destroyBridge", name);
    }

    @Override
    public void forceFailover() throws Exception {
        proxy.invokeOperation("forceFailover");
    }

    public String getLiveConnectorName() throws Exception {
        return (String) proxy.retrieveAttributeValue("liveConnectorName");
    }

    @Override
    public String getAddressSettingsAsJSON(String addressMatch) throws Exception {
        return (String) proxy.invokeOperation("getAddressSettingsAsJSON", addressMatch);
    }

    @Override
    public String[] getDivertNames() {
        return toStringArray((Object[]) proxy.retrieveAttributeValue("divertNames"));
    }

    @Override
    public void createBridge(String name,
            String queueName,
            String forwardingAddress,
            String filterString,
            String transformerClassName,
            long retryInterval,
            double retryIntervalMultiplier,
            int initialConnectAttempts,
            int reconnectAttempts,
            boolean useDuplicateDetection,
            int confirmationWindowSize,
            long clientFailureCheckPeriod,
            String connectorNames,
            boolean useDiscovery,
            boolean ha,
            String user,
            String password) throws Exception {
        proxy.invokeOperation("createBridge",
                name,
                queueName,
                forwardingAddress,
                filterString,
                transformerClassName,
                retryInterval,
                retryIntervalMultiplier,
                initialConnectAttempts,
                reconnectAttempts,
                useDuplicateDetection,
                confirmationWindowSize,
                clientFailureCheckPeriod,
                connectorNames,
                useDiscovery,
                ha,
                user,
                password);
    }

    @Override
    public String listProducersInfoAsJSON() throws Exception {
        return (String) proxy.invokeOperation("listProducersInfoAsJSON");
    }

    private static String[] toStringArray(final Object[] res) {
        String[] names = new String[res.length];
        for (int i = 0; i < res.length; i++) {
            names[i] = res[i].toString();
        }
        return names;
    }

}
