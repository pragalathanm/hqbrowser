package com.hqbrowser.config;

/**
 *
 * @author Pragalathan M <pragalathanm@gmail.com>
 */
public class ServerConfiguration {

    private final String host;
    private final int port;
    private final int jgroupsPort;

    public ServerConfiguration() {
        this("localhost", 5445, 8000);
    }

    public ServerConfiguration(String host, int port, int jgroupsPort) {
        this.host = host;
        this.port = port;
        this.jgroupsPort = jgroupsPort;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getJgroupsPort() {
        return jgroupsPort;
    }
}
