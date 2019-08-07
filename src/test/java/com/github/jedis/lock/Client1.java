package com.github.jedis.lock;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Client1 {
    private JedisLock lock;

    public Client1(String host, int port, String password, int database) {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, host, port, 2000, password, database, "client", false);
        lock = new JedisLock(pool, "foobar");
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
        Client1 client = new Client1("192.168.31.108", 16379, "foobar", 1);

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
