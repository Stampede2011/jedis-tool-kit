package com.rockbb.jedis.toolkit;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

import java.util.UUID;

/**
 * Redis distributed lock implementation
 * (fork by  Bruno Bossola bbossola@gmail.com)
 * (fork by  Milton Lai millton.lai@gmail.com)
 *
 * @author Alois Belaska alois.belaska@gmail.com
 */
public class JedisLock {

    public static final int ONE_SECOND = 1000;
    public static final int DEFAULT_EXPIRY_TIME_MILLIS = 60 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = 10 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    // Note: index of key&argv starts from 1
    private static final String COMMAND_LOCK =
            "if (redis.call('exists', KEYS[1]) == 0) then " +
                "redis.call('hset', KEYS[1], ARGV[1], 1); " +
                "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                "return 1; " +
            "end; " +
            "if (redis.call('hexists', KEYS[1], ARGV[1]) == 1) then " +
                "local counter = redis.call('hincrby', KEYS[1], ARGV[1], 1); " +
                "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                "return counter; " +
            "end; " +
            "return nil;";

    private static final String COMMAND_UNLOCK =
            "if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then " +
                "return nil;" +
            "end; " +
            "local counter = redis.call('hincrby', KEYS[1], ARGV[1], -1); " +
            "if (counter > 0) then " +
                "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                "return counter; " +
            "else " +
                "redis.call('del', KEYS[1]); " +
                "return 0; "+
            "end; " +
            "return nil;";

    private static final String COMMAND_RENEW =
            "if (redis.call('hexists', KEYS[1], ARGV[1]) == 1) then " +
                "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                "return 1; " +
            "end; " +
            "return nil;";

    private final JedisGenericClient client;

    private final String lockKeyPath;

    private final int lockExpiryInMillis;
    private final int acquiryTimeoutInMillis;
    private final UUID lockUUID;

    private volatile long counter;

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock
     * expiration of 60000 msecs.
     *
     * @param pool
     *            JedisPool
     * @param lockKey
     *            lock key (ex. account:1, ...)
     */
    public JedisLock(JedisPool pool, String lockKey) {
        this(pool, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor.
     *
     * @param pool
 *                JedisPool
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     */
    public JedisLock(JedisPool pool, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this.client = new JedisPoolClient(pool);
        this.lockKeyPath = lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis+1;
        this.lockUUID = UUID.randomUUID();
    }

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock
     * expiration of 60000 msecs.
     *
     * @param cluster
     *            JedisCluster
     * @param lockKey
     *            lock key (ex. account:1, ...)
     */
    public JedisLock(JedisCluster cluster, String lockKey) {
        this(cluster, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor.
     *
     * @param cluster
     *            JedisCluster
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     */
    public JedisLock(JedisCluster cluster, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this.client = new JedisClusterClient(cluster);
        this.lockKeyPath = lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis+1;
        this.lockUUID = UUID.randomUUID();
    }

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock
     * expiration of 60000 msecs.
     *
     * @param client
     *            BasicClient
     * @param lockKey
     *            lock key (ex. account:1, ...)
     */
    public JedisLock(JedisGenericClient client, String lockKey) {
        this(client, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS, UUID.randomUUID());
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     *
     * @param client
     *            BasicClient
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     */
    public JedisLock(JedisGenericClient client, String lockKey, int acquireTimeoutMillis) {
        this(client, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS, UUID.randomUUID());
    }

    /**
     * Detailed constructor.
     *
     * @param client
     *            BasicClient
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     */
    public JedisLock(JedisGenericClient client, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this(client, lockKey, acquireTimeoutMillis, expiryTimeMillis, UUID.randomUUID());
    }

    /**
     * Detailed constructor.
     *
     * @param client
     *            BasicClient
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     * @param uuid
     *            unique identification of this lock
     */
    public JedisLock(JedisGenericClient client, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis, UUID uuid) {
        this.client = client;
        this.lockKeyPath = lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis+1;
        this.lockUUID = uuid;
    }

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock
     * expiration of 60000 msecs.
     *
     * @param pooled
     *            JedisPooled
     * @param lockKey
     *            lock key (ex. account:1, ...)
     */
    public JedisLock(JedisPooled pooled, String lockKey) {
        this(pooled, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor.
     *
     * @param pooled
     *            JedisPooled
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     */
    public JedisLock(JedisPooled pooled, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this.client = new JedisPooledClient(pooled);
        this.lockKeyPath = lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis+1;
        this.lockUUID = UUID.randomUUID();
    }

    /**
     * @return lock uuid
     */
    public UUID getLockUUID() {
        return lockUUID;
    }

    /**
     * @return lock key path
     */
    public String getLockKeyPath() {
        return lockKeyPath;
    }

    /**
     * Acquire lock.
     *
     * @return true if lock is acquired, false acquire timeouted
     */
    public boolean acquire() {
        int timeout = acquiryTimeoutInMillis;
        while (timeout >= 0) {
            Object result = client.eval(COMMAND_LOCK, 1, lockKeyPath, getId(), lockExpiryInMillis + "");
            if (result == null) {
                timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
                try {
                    Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
                } catch (InterruptedException e) {
                    // Do nothing
                }
            } else {
                this.counter = (Long)result;
                return true;
            }

        }
        return false;
    }

    /**
     * Renew lock.
     *
     * @return true if lock is acquired, false otherwise
     */
    public boolean renew() {
        Object result = client.eval(COMMAND_RENEW, 1, lockKeyPath, getId(), lockExpiryInMillis + "");
        return (result != null);
    }

    /**
     * Acquired lock release.
     */
    public void release() {
        Object result = client.eval(COMMAND_UNLOCK, 1, lockKeyPath, getId(), lockExpiryInMillis + "");
        if (result == null) {
            this.counter = 0;
        } else {
            this.counter = (Long)result;
        }
    }

    /**
     * Lock threads counter
     *
     * @return counter
     */
    public long getCounter() { return counter; }

    /**
     * Returns the expiry time of this lock
     * @return  the expiry time in millis (or null if not locked)
     */
    public long getLockExpiryTimeInMillis() {
        return client.pttl(lockKeyPath);
    }

    public String getId() {
        return lockUUID + "-" + Thread.currentThread().getId();
    }
}
