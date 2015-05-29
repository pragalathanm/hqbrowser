package com.hqbrowser.service;

import com.hqbrowser.config.ClusterConfiguration;
import com.hqbrowser.queue.QueueManager;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Pragalathan M <pragalathanm@gmail.com>
 */
@Stateless
@Path("/hornetq/configuration")
public class ServerLocator {

    @EJB
    private QueueManager queueManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClusterConfiguration getLiveServers() {
        ClusterConfiguration configuration = new ClusterConfiguration();
        configuration.setLiveServeConfigurations(queueManager.getClusterConfiguration().getLiveServeConfigurations());
        return configuration;
    }
}
