package com.github.jedis.lock;

import com.rockbb.jedis.toolkit.JedisClientBuilder;
import com.rockbb.jedis.toolkit.JedisGenericClient;
import com.rockbb.jedis.toolkit.JedisLock;
import redis.clients.jedis.JedisPoolConfig;

public class Client1 {
    private JedisLock lock;

    public Client1(String hosts, String password, int database) {
        JedisClientBuilder builder = new JedisClientBuilder(
                new JedisPoolConfig(),
                hosts,
                5000,
                password,
                database,
                "client");
        JedisGenericClient client = builder.getClient();
        lock = new JedisLock(client, "foobar");
    }

    public void show(String in) {
        System.out.println(in + " show try");
        if (lock.acquire()) {
            try {
                System.out.println(in + " show in");
                view(in, 10);
                print(in);
                lock.renew();
            } finally {
                lock.release();
            }
        } else {
            System.out.println(in + " show failed");
        }
    }

    public void view(String in, int level) {
        System.out.println(in + " view try ");
        if (lock.acquire()) {
            try {
                System.out.println(in + " view in ");
                System.out.println(in + " view counter: " + lock.getCounter());
                if (level > 0) {
                    view(in, --level);
                }
            } finally {
                lock.release();
                System.out.println(in + " view counter: " + lock.getCounter());
            }
        } else {
            System.out.println(in + " view failed");
        }
    }

    public void print(String in) {
        System.out.println(in + " print try");
        if (lock.acquire()) {
            try {
                System.out.println(in + " print in");
            } finally {
                lock.release();
            }
        } else {
            System.out.println(in + " print failed");
        }
    }

    public static void main(String[] args) {
        Client1 client = new Client1("192.168.31.108:16379", "foobar", 1);

        new Thread(()->{
            while (true) {
                client.show("1");
            }
        }).start();

        new Thread(()->{
            while (true) {
                client.show("2");
            }
        }).start();

    }
}
