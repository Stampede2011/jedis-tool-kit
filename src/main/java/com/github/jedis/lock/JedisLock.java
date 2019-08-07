package com.github.jedis.lock;

import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis distributed lock implementation
 * (fork by  Bruno Bossola <bbossola@gmail.com>)
 * (fork by  Milton Lai <millton.lai@gmail.com>)
 * 
 * @author Alois Belaska <alois.belaska@gmail.com>
 */
public class JedisLock {

    public static final int ONE_SECOND = 1000;
    public static final int DEFAULT_EXPIRY_TIME_MILLIS = Integer.getInteger("com.github.jedis.lock.expiry.millis", 60 * ONE_SECOND);
    public static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = Integer.getInteger("com.github.jedis.lock.acquiry.millis", 10 * ONE_SECOND);
    public static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = Integer.getInteger("com.github.jedis.lock.acquiry.resolution.millis", 100);
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

    private final JedisPool pool;

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
     * @param lockKey
     *            lock key (ex. account:1, ...)
     */
    public JedisLock(JedisPool pool, String lockKey) {
        this(pool, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     * 
     * @param pool
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     */
    public JedisLock(JedisPool pool, String lockKey, int acquireTimeoutMillis) {
        this(pool, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor.
     * 
     * @param pool
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     */
    public JedisLock(JedisPool pool, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this(pool, lockKey, acquireTimeoutMillis, expiryTimeMillis, UUID.randomUUID());
    }

    /**
     * Detailed constructor.
     * 
     * @param pool
     * @param lockKey
     *            lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis
     *            acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis
     *            lock expiration in miliseconds (default: 60000 msecs)
     * @param uuid
     *            unique identification of this lock
     */
    public JedisLock(JedisPool pool, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis, UUID uuid) {
        this.pool = pool;
        this.lockKeyPath = lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis+1;
        this.lockUUID = uuid;;
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
            Object result = eval(COMMAND_LOCK, 1, lockKeyPath, getId(), lockExpiryInMillis + "");
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
        Object result = eval(COMMAND_RENEW, 1, lockKeyPath, getId(), lockExpiryInMillis + "");
        return (result != null);
    }

    /**
     * Acquired lock release.
     */
    public void release() {
        Object result = eval(COMMAND_UNLOCK, 1, lockKeyPath, getId(), lockExpiryInMillis+"");
        if (result == null) {
            this.counter = 0;
        } else {
            this.counter = (Long)result;
        }
    }

    /**
     * Lock threads counter
     */
    public long getCounter() { return counter; }

    /**
     * Returns the expiry time of this lock
     * @return  the expiry time in millis (or null if not locked)
     */
    public long getLockExpiryTimeInMillis() {
        return (Long)execute((Jedis jedis)->jedis.pttl(lockKeyPath));
    }

    public String getId() {
        return lockUUID + "-" + Thread.currentThread().getId();
    }

    public Object execute(RedisCallback callback) {
        // Assure that the resources will be closed after execution
        try (Jedis jedis = pool.getResource()) {
            return callback.doWithRedis(jedis);
        }
    }

    public Object eval(String script, int keyCount, String ... params) {
        return execute((Jedis jedis)->jedis.eval(script, keyCount, params));
    }

    public interface RedisCallback {
        Object doWithRedis(Jedis var1);
    }
}
