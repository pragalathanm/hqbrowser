package com.hqbrowser.management;

import org.hornetq.api.config.HornetQDefaultConfiguration;
import org.hornetq.api.core.HornetQObjectClosedException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientRequestor;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.management.ManagementHelper;

/**
 *
 * @author Pragalathan M
 */
public class CoreMessagingProxy {

    private final String resourceName;

    private final ClientSession session;

    private ClientRequestor requestor;

    public CoreMessagingProxy(final ClientSession session, final String resourceName) throws Exception {
        this.session = session;
        this.resourceName = resourceName;
        requestor = new ClientRequestor(session, HornetQDefaultConfiguration.getDefaultManagementAddress());
    }

    private ClientMessage request(ClientMessage request) throws Exception {
        try {
            return requestor.request(request);
        } catch (HornetQObjectClosedException ex) {
            // server might have gone down. so reconnect
            requestor = new ClientRequestor(session, HornetQDefaultConfiguration.getDefaultManagementAddress());
            return requestor.request(request);
        }
    }

    public Object retrieveAttributeValue(final String attributeName) {
        return retrieveAttributeValue(attributeName, null);
    }

    public Object retrieveAttributeValue(final String attributeName, final Class desiredType) {
        ClientMessage m = session.createMessage(false);
        ManagementHelper.putAttribute(m, resourceName, attributeName);
        ClientMessage reply;
        try {
            reply = request(m);
            Object result = ManagementHelper.getResult(reply);

            if (desiredType != null && desiredType != result.getClass()) {
                // Conversions
                if (desiredType == Long.class && result.getClass() == Integer.class) {
                    Integer in = (Integer) result;
                    result = new Long(in.intValue());
                }
            }

            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Object invokeOperation(final String operationName, final Object... args) throws Exception {
        ClientMessage m = session.createMessage(false);
        ManagementHelper.putOperationInvocation(m, resourceName, operationName, args);
        ClientMessage reply = request(m);
        if (ManagementHelper.hasOperationSucceeded(reply)) {
            return ManagementHelper.getResult(reply);
        } else {
            throw new Exception((String) ManagementHelper.getResult(reply));
        }
    }
}
