package com.rockbb.jedis.toolkit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisPooledClient implements JedisGenericClient {
    private JedisPoolAbstract pool;

    public JedisPooledClient(JedisPoolAbstract pool) {
        this.pool = pool;
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        return execute((Jedis jedis)->jedis.eval(script, keyCount, params));
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        return execute((Jedis jedis)->jedis.eval(script, keys, args));
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        return (List<String>)execute((Jedis jedis)->jedis.blpop(timeout, key));
    }

    @Override
    public List<String> blpop(int timeout, String... keys) {
        return (List<String>)execute((Jedis jedis)->jedis.blpop(timeout, keys));
    }

    @Override
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return (List<byte[]>)execute((Jedis jedis)->jedis.blpop(timeout, keys));
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        return (List<String>)execute((Jedis jedis)->jedis.brpop(timeout, key));
    }

    @Override
    public List<String> brpop(int timeout, String... keys) {
        return (List<String>)execute((Jedis jedis)->jedis.brpop(timeout, keys));
    }

    @Override
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return (List<byte[]>)execute((Jedis jedis)->jedis.brpop(timeout, keys));
    }

    @Override
    public Long del(String key) {
        return (Long)execute((Jedis jedis)->jedis.del(key));
    }

    @Override
    public Long del(String... keys) {
        return (Long)execute((Jedis jedis)->jedis.del(keys));
    }

    @Override
    public Long exists(String... keys) {
        return (Long)execute((Jedis jedis)->jedis.exists(keys));
    }

    @Override
    public Boolean exists(String key) {
        return (Boolean)execute((Jedis jedis)->jedis.exists(key));
    }

    @Override
    public Long expire(byte[] key, int seconds) {
        return (Long)execute((Jedis jedis)->jedis.expire(key, seconds));
    }

    @Override
    public String get(String key) {
        return (String)execute((Jedis jedis)->jedis.get(key));
    }

    @Override
    public byte[] get(byte[] key) {
        return (byte[])execute((Jedis jedis)->jedis.get(key));
    }

    @Override
    public Long hdel(String key, String... fields) {
        return (Long)execute((Jedis jedis)->jedis.hdel(key, fields));
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return (byte[])execute((Jedis jedis)->jedis.hget(key, field));
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return (Map<byte[], byte[]>)execute((Jedis jedis)->jedis.hgetAll(key));
    }

    @Override
    public Long hlen(byte[] key) {
        return (Long)execute((Jedis jedis)->jedis.hlen(key));
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return (Long)execute((Jedis jedis)->jedis.hset(key, field, value));
    }

    @Override
    public Long llen(String key) {
        return (Long)execute((Jedis jedis)->jedis.llen(key));
    }

    @Override
    public Long lpush(String key, String... strings) {
        return (Long)execute((Jedis jedis)->jedis.lpush(key, strings));
    }

    @Override
    public Long pttl(String key) {
        return (Long)execute((Jedis jedis)->jedis.pttl(key));
    }

    @Override
    public Long rpush(String key, String... strings) {
        return (Long)execute((Jedis jedis)->jedis.rpush(key, strings));
    }

    @Override
    public ScanResult<String> scan(String cursor) {
        return (ScanResult<String>)execute((Jedis jedis)->jedis.scan(cursor));
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return (ScanResult<String>)execute((Jedis jedis)->jedis.scan(cursor, params));
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor) {
        return (ScanResult<byte[]>)execute((Jedis jedis)->jedis.scan(cursor));
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        return (ScanResult<byte[]>)execute((Jedis jedis)->jedis.scan(cursor, params));
    }

    @Override
    public String set(String key, String value) {
        return (String)execute((Jedis jedis)->jedis.set(key, value));
    }

    @Override
    public String set(byte[] key, byte[] value) {
        return (String)execute((Jedis jedis)->jedis.set(key, value));
    }

    @Override
    public String set(String key, String value, SetParams params) {
        return (String)execute((Jedis jedis)->jedis.set(key, value, params));
    }

    @Override
    public String set(byte[] key, byte[] value, SetParams params) {
        return (String)execute((Jedis jedis)->jedis.set(key, value, params));
    }

    @Override
    public String setex(String key, int seconds, String value) {
        return (String)execute((Jedis jedis)->jedis.setex(key, seconds, value));
    }

    @Override
    public String setex(byte[] key, int seconds, byte[] value) {
        return (String)execute((Jedis jedis)->jedis.setex(key, seconds, value));
    }

    @Override
    public Long sadd(String key, String... members) {
        return (Long)execute((Jedis jedis)->jedis.sadd(key, members));
    }

    @Override
    public Set<String> smembers(String key) {
        return (Set<String>)execute((Jedis jedis)->jedis.smembers(key));
    }

    @Override
    public Boolean sismember(String key, String member) {
        return (Boolean) execute((Jedis jedis)->jedis.sismember(key, member));
    }

    @Override
    public String spop(String key) {
        return (String) execute((Jedis jedis)->jedis.spop(key));
    }

    @Override
    public Long srem(String key, String... members) {
        return (Long)execute((Jedis jedis)->jedis.srem(key, members));
    }

    @Override
    public Long ttl(String key) {
        return (Long)execute((Jedis jedis)->jedis.ttl(key));
    }

    @Override
    public Long ttl(byte[] key) {
        return (Long)execute((Jedis jedis)->jedis.ttl(key));
    }

    @Override
    public Long zcard(String key) {
        return (Long)execute((Jedis jedis)->jedis.zcard(key));
    }

    @Override
    public Long zcard(byte[] key) {
        return (Long)execute((Jedis jedis)->jedis.zcard(key));
    }

    @Override
    public Set<String> zrange(String key, long start, long stop) {
        return (Set<String>)execute((Jedis jedis)->jedis.zrange(key, start, stop));
    }

    @Override
    public Set<byte[]> zrange(byte[] key, long start, long stop) {
        return (Set<byte[]>)execute((Jedis jedis)->jedis.zrange(key, start, stop));
    }

    @Override
    public Long zrem(String key, String... members) {
        return (Long)execute((Jedis jedis)->jedis.zrem(key, members));
    }

    @Override
    public Long zrem(byte[] key, byte[]... members) {
        return (Long)execute((Jedis jedis)->jedis.zrem(key, members));
    }

    @Override
    public Long zremrangeByRank(byte[] key, long start, long stop) {
        return (Long)execute((Jedis jedis)->jedis.zremrangeByRank(key, start, stop));
    }

    @Override
    public Long zremrangeByScore(byte[] key, double min, double max) {
        return (Long)execute((Jedis jedis)->jedis.zremrangeByScore(key, min, max));
    }

    @Override
    public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        return (Long)execute((Jedis jedis)->jedis.zremrangeByScore(key, min, max));
    }

    @Override
    public Long zremrangeByScore(String key, String min, String max) {
        return (Long)execute((Jedis jedis)->jedis.zremrangeByScore(key, min, max));
    }

    private Object execute(RedisCallback callback) {
        try (Jedis jedis = pool.getResource()) {
            return callback.doWithRedis(jedis);
        }
    }

    public interface RedisCallback {
        Object doWithRedis(Jedis var1);
    }
}
