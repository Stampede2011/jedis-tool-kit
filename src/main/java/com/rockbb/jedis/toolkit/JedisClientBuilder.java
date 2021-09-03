package com.rockbb.jedis.toolkit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.LinkedHashSet;
import java.util.Set;

public class JedisClientBuilder {
    private JedisGenericClient client;

    public JedisClientBuilder(
            GenericObjectPoolConfig poolConfig,
            String hosts, int timeout, String password, int database, String clientName) {
        if (hosts.indexOf(',') > 0) {
            // cluster config
            Set<HostAndPort> nodes = new LinkedHashSet<>();
            for (String host : hosts.split(",[ ]*")) {
                String[] hostParts = host.split(":");
                if ((hostParts.length != 2) || !(hostParts[1].matches("\\d+"))) {
                    throw new RuntimeException("Invalid host name set for redis cluster: " + host);
                }
                nodes.add(new HostAndPort(hostParts[0], Integer.parseInt(hostParts[1])));
            }
            JedisCluster cluster = new JedisCluster(nodes, timeout, poolConfig);
            this.client = new JedisClusterClient(cluster);

        } else {
            // pool config
            password = (password == null || password.trim().length() == 0)? null : password.trim();

            String[] hostParts = hosts.split(":");
            if ((hostParts.length != 2) || !(hostParts[1].matches("\\d+"))) {
                throw new RuntimeException("Invalid host name set for redis cluster: " + hosts);
            }
            JedisPool pool = new JedisPool(
                    poolConfig, hostParts[0], Integer.parseInt(hostParts[1]), timeout, password, database, clientName);
            this.client = new JedisPooledClient(pool);
        }
    }

    public JedisGenericClient getClient() { return client; }
}
