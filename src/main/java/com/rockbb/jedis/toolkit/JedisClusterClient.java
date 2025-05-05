package com.rockbb.jedis.toolkit;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.resps.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisClusterClient implements JedisGenericClient {
    private JedisCluster cluster;

    public JedisClusterClient(JedisCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        return cluster.eval(script, keyCount, params);
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        return cluster.eval(script, keys, args);
    }
    @Override
    public List<String> blpop(int timeout, String key) {
        return cluster.blpop(timeout, key);
    }

    @Override
    public List<String> blpop(int timeout, String... keys) {
        return cluster.blpop(timeout, keys);
    }

    @Override
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return cluster.blpop(timeout, keys);
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        return cluster.brpop(timeout, key);
    }

    @Override
    public List<String> brpop(int timeout, String... keys) {
        return cluster.brpop(timeout, keys);
    }

    @Override
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return cluster.brpop(timeout, keys);
    }

    @Override
    public Long del(String key) {
        return cluster.del(key);
    }

    @Override
    public Long del(String... keys) {
        return cluster.del(keys);
    }

    @Override
    public Long exists(String... keys) {
        return cluster.exists(keys);
    }

    @Override
    public Boolean exists(String key) {
        return cluster.exists(key);
    }

    @Override
    public Long expire(byte[] key, int seconds) {
        return cluster.expire(key, seconds);
    }

    @Override
    public String get(String key) {
        return cluster.get(key);
    }

    @Override
    public byte[] get(byte[] key) {
        return cluster.get(key);
    }

    @Override
    public Long hdel(String key, String... fields) {
        return cluster.hdel(key, fields);
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) {
        return cluster.hdel(key, fields);
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return cluster.hget(key, field);
    }

    @Override
    public String hget(String key, String field) {
        return cluster.hget(key, field);
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return cluster.hgetAll(key);
    }

    @Override
    public Long hlen(byte[] key) {
        return cluster.hlen(key);
    }

    @Override
    public Long hlen(String key) {
        return cluster.hlen(key);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return cluster.hset(key, field, value);
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return cluster.hset(key, field, value);
    }

    @Override
    public Long llen(String key) {
        return cluster.llen(key);
    }

    @Override
    public Long lpush(String key, String... strings) {
        return cluster.lpush(key, strings);
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        return cluster.pexpire(key, milliseconds);
    }

    @Override
    public Long pexpire(byte[] key, long milliseconds) {
        return cluster.pexpire(key, milliseconds);
    }

    @Override
    public Long pttl(String key) {
        return cluster.pttl(key);
    }

    @Override
    public Long rpush(String key, String... strings) {
        return cluster.rpush(key, strings);
    }

    @Override
    public ScanResult<String> scan(String cursor) {
        return cluster.scan(cursor, new ScanParams());
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return cluster.scan(cursor, params);
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor) {
        return cluster.scan(cursor, new ScanParams());
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        return cluster.scan(cursor, params);
    }

    @Override
    public String set(String key, String value) {
        return cluster.set(key, value);
    }

    @Override
    public String set(byte[] key, byte[] value) {
        return cluster.set(key, value);
    }

    @Override
    public String set(String key, String value, SetParams params) {
        return cluster.set(key, value, params);
    }

    @Override
    public String set(byte[] key, byte[] value, SetParams params) {
        return cluster.set(key, value, params);
    }

    @Override
    public String setex(String key, int seconds, String value) {
        return cluster.setex(key, seconds, value);
    }

    @Override
    public String setex(byte[] key, int seconds, byte[] value) {
        return cluster.setex(key, seconds, value);
    }

    @Override
    public Long sadd(String key, String... members) {
        return cluster.sadd(key, members);
    }

    @Override
    public Set<String> smembers(String key) {
        return cluster.smembers(key);
    }

    @Override
    public Boolean sismember(String key, String member) {
        return cluster.sismember(key, member);
    }

    @Override
    public String spop(String key) {
        return cluster.spop(key);
    }

    @Override
    public Long srem(String key, String... members) {
        return cluster.srem(key, members);
    }

    @Override
    public Long ttl(String key) {
        return cluster.ttl(key);
    }

    @Override
    public Long ttl(byte[] key) {
        return cluster.ttl(key);
    }

    @Override
    public Long zadd(String key, double score, String member) {
        return cluster.zadd(key, score, member);
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        return cluster.zadd(key, score, member);
    }

    @Override
    public Long zcard(String key) {
        return cluster.zcard(key);
    }

    @Override
    public Long zcard(byte[] key) {
        return cluster.zcard(key);
    }

    @Override
    public Set<String> zrange(String key, long start, long stop) {
        return (Set<String>)cluster.zrange(key, start, stop);
    }

    @Override
    public Set<byte[]> zrange(byte[] key, long start, long stop) {
        return (Set<byte[]>)cluster.zrange(key, start, stop);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return (Set<String>)cluster.zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        return (Set<String>)cluster.zrangeByScore(key, min, max);
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return (Set<byte[]>)cluster.zrangeByScore(key, min, max);
    }

    @Override
    public Long zrem(String key, String... members) {
        return cluster.zrem(key, members);
    }

    @Override
    public Long zrem(byte[] key, byte[]... members) {
        return cluster.zrem(key, members);
    }

    @Override
    public Long zremrangeByRank(byte[] key, long start, long stop) {
        return cluster.zremrangeByRank(key, start, stop);
    }

    @Override
    public Long zremrangeByScore(byte[] key, double min, double max) {
        return cluster.zremrangeByScore(key, min, max);
    }

    @Override
    public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        return cluster.zremrangeByScore(key, min, max);
    }

    @Override
    public Long zremrangeByScore(String key, String min, String max) {
        return cluster.zremrangeByScore(key, min, max);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return (Set<Tuple>)cluster.zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return (Set<Tuple>)cluster.zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        return (Set<Tuple>)cluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return (Set<Tuple>)cluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return (Set<byte[]>)cluster.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return (Set<byte[]>)cluster.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return (Set<byte[]>)cluster.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return (Set<byte[]>)cluster.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return (Set<Tuple>)cluster.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Double zscore(String key, String member) {
        return cluster.zscore(key, member);
    }

    @Override
    public Double zscore(byte[] key, byte[] member) {
        return cluster.zscore(key, member);
    }
}
