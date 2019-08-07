# Jedis-lock

Jedis-lock is easy to use and simple implementation of distributed lock using Redis database and Jedis driver.

Because the [master branch](https://github.com/abelaska/jedis-lock) is no longer updated, this fork will add following features:

* Use LUA scripts instead of `SETNX` for better atomicity
* ReentrantLock support
* JedisCluster support

## How to use it?

To use it just:

```java
JedisPoolConfig config = new JedisPoolConfig();
JedisPool pool = new JedisPool(config, host, port, 2000, password, database, clientName, false);
JedisLock lock = new JedisLock(pool, "foobar");
```
or
```java
Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
hostAndPortsSet.add(new HostAndPort("192.168.1.2", 6379));
hostAndPortsSet.add(new HostAndPort("192.168.1.3", 6379));

JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
jedisPoolConfig.setTestOnBorrow(true);
JedisCluster cluster = new JedisCluster(hostAndPortsSet, jedisPoolConfig);
JedisLock lock = new JedisLock(cluster, "foobar");
```
then
```java
lock.acquire();
try {
  // do some stuff
}
finally {
  lock.release();
}
```
That's it.

## License

The Apache Software License, Version 2.0
