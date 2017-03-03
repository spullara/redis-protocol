package redis.client;

import com.google.common.base.Charsets;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.reply.BulkReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.StatusReply;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static redis.client.RedisClientBase.ALPHA;
import static redis.client.RedisClientBase.BY;
import static redis.client.RedisClientBase.DESC;
import static redis.client.RedisClientBase.GET;
import static redis.client.RedisClientBase.LIMIT;
import static redis.client.RedisClientBase.STORE;
import static redis.client.RedisClientBase.WEIGHTS;
import static redis.client.RedisClientBase.WITHSCORES;

public class AllCommandsTest {

  private static RedisClient rc;
  private static RedisClient.Pipeline p;
  private static RedisClient rc2;

  @BeforeClass
  public static void setup() throws IOException {
    rc = new RedisClient("localhost", 6379);
    p = rc.pipeline();
    rc2 = new RedisClient("localhost", 6379);
  }

  @AfterClass
  public static void shutdown() throws IOException {
    rc.close();
    rc2.close();
  }
  

  @Test
  public void testDefaultAuthDB() throws IOException {
  	rc.config_set("requirepass", "test");
  	RedisClient test = new RedisClient("localhost", 6379, 1, "test");
  	try {
  	test.set("foo", "bar");
  	} catch (RedisException e) {
  		fail("default passwd failed");
  	}
  	test.config_set("requirepass", "");
  	rc.select(1);
  	if (!new String(rc.get("foo").data()).equals("bar"))
  		fail("default db failed");
  	rc.del("foo");
  }

  @Test
  public void testauth() throws IOException {
    rc.config_set("requirepass", "test");
    RedisClient authtest = new RedisClient("localhost", 6379);
    try {
      authtest.info(null);
      fail("should have thrown");
    } catch (RedisException re) {
      assertEquals("ERR operation not permitted", re.getMessage());
    }
    rc.auth("test");
    rc.config_set("requirepass", "");
    authtest.info(null);
  }

  @Test
  public void append() {
    rc.del(a("mykey"));
    eq(5, rc.append("mykey", "Hello"));
    eq(11, rc.append("mykey", " World"));
    eq("Hello World", rc.get("mykey"));
  }

  @Test
  public void bitcount() {
    rc.set("bitkey", "A");
    eq(2, rc.bitcount("bitkey", null, null));
  }

  @Test
  public void bitop() {
    rc.set("bitkey1", "A");
    rc.set("bitkey2", "B");
    eq(1, rc.bitop("xor", "bitkey", new Object[] { "bitkey1", "bitkey2" }));
    eq("\03".getBytes(), rc.get("bitkey").data());
  }

  @Test
  public void blpop() {
    rc.del(a("list1", "list2"));
    eq(3, rc.rpush("list1", a("a", "b", "c")));
    eq(a("list1", "a"), rc.blpop(a("list1", "list2", "0")));
  }

  @Test
  public void brpop() {
    rc.del(a("list1", "list2"));
    eq(3, rc.rpush("list1", a("a", "b", "c")));
    eq(a("list1", "c"), rc.brpop(a("list1", "list2", "0")));
  }

  @Test
  public void decr() {
    eq("OK", rc.set("mykey", "10"));
    eq(9, rc.decr("mykey"));
    eq("OK", rc.set("mykey", "234293482390480948029348230948"));
    try {
      rc.decr("mykey");
      fail("Should have failed");
    } catch (RedisException e) {
      // failed
    }
  }

  @Test
  public void decrby() {
    eq("OK", rc.set("mykey", "10"));
    eq(5, rc.decrby("mykey", 5));
  }

  @Test
  public void del() {
    rc.del(a("key1", "key2", "key3"));
    eq("OK", rc.set("key1", "Hello"));
    eq("OK", rc.set("key2", "World"));
    eq(2, rc.del(a("key1", "key2", "key3")));
  }

  @Test
  public void eval() {
    // Commenting out for now until I upgrade my Redis
//    eq(a("key1", "key2", "first", "second"),
//            (MultiBulkReply) rc.eval("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", 2,
//                    a("key1", "key2", "first", "second")));
  }

  @Test
  public void exists() {
    rc.del_("key1", "key2");
    eq("OK", rc.set("key1", "value"));
    eq(1, rc.exists("key1"));
    eq(0, rc.exists("key2"));
  }

  @Test
  public void get() {
    rc.del_("key");
    eq("OK", rc.set("key", "value"));
    eq("value", rc.get("key"));
  }

  @Test
  public void sort() {
    rc.del_("list");
    eq(5, rc.lpush_("list", "a", "b", "c", "ab", "bc"));
    eq(a("a", "ab", "b", "bc", "c"), (MultiBulkReply) rc.sort_("list", ALPHA));
    eq(a("a", "ab", "b"), (MultiBulkReply) rc.sort_("list", ALPHA, LIMIT, 0, 3));
    eq(a("c", "bc", "b", "ab", "a"), (MultiBulkReply) rc.sort_("list", ALPHA, DESC));
    eq("OK", rc.mset_("w_a", 1, "w_b", 2, "w_c", 3, "w_ab", 4, "w_bc", 5));
    eq(a("a", "b", "c", "ab", "bc"), (MultiBulkReply) rc.sort_("list", BY, "w_*"));
    eq("OK", rc.mset_("o_a", 1, "o_b", 2, "o_c", 3, "o_ab", 4, "o_bc", 5));
    eq(a("1", "2", "3", "4", "5"), (MultiBulkReply) rc.sort_("list", BY, "w_*", GET, "o_*"));
    eq(5, (IntegerReply) rc.sort_("list", ALPHA, STORE, "result"));
    eq(a("a", "ab", "b", "bc", "c"), rc.lrange("result", 0, 10));
  }

  @Test
  public void zadd() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "1", "uno")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(0, rc.zadd(a("myzset", "3", "two")));
    eq(a("one", "1", "uno", "1", "two", "3"), rc.zrange("myzset", "0", "-1", WITHSCORES));
  }

  @Test
  public void zcard() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(2, rc.zcard("myzset"));
  }

  @Test
  public void zcount() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(3, rc.zcount("myzset", "-inf", "inf"));
    eq(2, rc.zcount("myzset", "(1", "3"));
  }

  @Test
  public void zincrby() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq("3", rc.zincrby("myzset", "2", "one"));
    eq(a("two", "2", "one", "3"), rc.zrange("myzset", "0", "-1", WITHSCORES));
  }

  @Test
  public void zinterstore() {
    rc.del(a("zset1", "zset2"));
    eq(1, rc.zadd(a("zset1", "1", "one")));
    eq(1, rc.zadd(a("zset1", "2", "two")));
    eq(1, rc.zadd(a("zset2", "1", "one")));
    eq(1, rc.zadd(a("zset2", "2", "two")));
    eq(1, rc.zadd(a("zset2", "3", "three")));
    eq(2, rc.zinterstore("out", 2, a("zset1", "zset2", WEIGHTS, "2", "3")));
    eq(a("one", "5", "two", "10"), rc.zrange("out", "0", "-1", WITHSCORES));
  }

  @Test
  public void zrange() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(a("one", "two", "three"), rc.zrange_("myzset", "0", "-1"));
    eq(a("three"), rc.zrange_("myzset", "2", "3"));
    eq(a("two", "three"), rc.zrange_("myzset", "-2", "-1"));
  }

  @Test
  public void zrangebyscore() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(a("one", "two", "three"), rc.zrangebyscore_("myzset", "-inf", "inf"));
    eq(a("one", "two"), rc.zrangebyscore_("myzset", "1", "2"));
    eq(a("two"), rc.zrangebyscore_("myzset", "(1", "2"));
    eq(a(), rc.zrangebyscore_("myzset", "(1", "(2"));
  }

  @Test
  public void zrank() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(2, (IntegerReply) rc.zrank("myzset", "three"));
    eq(null, (BulkReply) rc.zrank("myzset", "four"));
  }

  @Test
  public void zrem() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(1, rc.zrem_("myzset", "two"));
    eq(a("one", "1", "three", "3"), rc.zrange("myzset", "0", "-1", WITHSCORES));
  }

  @Test
  public void zremrangebyrank() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(2, rc.zremrangebyrank("myzset", "0", "1"));
    eq(a("three", "3"), rc.zrange("myzset", "0", "-1", WITHSCORES));
  }

  @Test
  public void zremrangebyscore() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(1, rc.zremrangebyscore("myzset", "-inf", "(2"));
    eq(a("two", "2", "three", "3"), rc.zrange("myzset", "0", "-1", WITHSCORES));
  }

  @Test
  public void zrevrange() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(a("three", "two", "one"), rc.zrevrange_("myzset", 0, -1));
    eq(a("one"), rc.zrevrange_("myzset", 2, 3));
    eq(a("two", "one"), rc.zrevrange_("myzset", -2, -1));
  }

  @Test
  public void zrevrangebyscore() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(a("three", "two", "one"), rc.zrevrangebyscore_("myzset", "+inf", "-inf"));
    eq(a("two", "one"), rc.zrevrangebyscore_("myzset", 2, 1));
    eq(a("two"), rc.zrevrangebyscore_("myzset", 2, "(1"));
    eq(a(), rc.zrevrangebyscore_("myzset", "(2", "(1"));
  }

  @Test
  public void zrevrank() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq(1, rc.zadd(a("myzset", "2", "two")));
    eq(1, rc.zadd(a("myzset", "3", "three")));
    eq(2, (IntegerReply) rc.zrevrank("myzset", "one"));
    eq(null, (BulkReply) rc.zrevrank("myzset", "four"));
  }

  @Test
  public void zscore() {
    rc.del(a("myzset"));
    eq(1, rc.zadd(a("myzset", "1", "one")));
    eq("1", rc.zscore("myzset", "one"));
  }

  @Test
  public void zunionstore() throws ExecutionException, InterruptedException {
    RedisClient.Pipeline rc = p;
    rc.del(a("zset1", "zset2"));
    eq(1, rc.zadd(a("zset1", "1", "one")));
    eq(1, rc.zadd(a("zset1", "2", "two")));
    eq(1, rc.zadd(a("zset2", "1", "one")));
    eq(1, rc.zadd(a("zset2", "2", "two")));
    eq(1, rc.zadd(a("zset2", "3", "three")));
    eq(3, rc.zunionstore_("out", 2, "zset1", "zset2", WEIGHTS, 2, 3));
    eq(a("one", "5", "three", "9", "two", "10"), rc.zrange_("out", 0, -1, WITHSCORES));
  }

  private void eq(Object[] a, CompletableFuture<MultiBulkReply> out) throws ExecutionException, InterruptedException {
    eq(a, out.get());
  }

  private void eq(int i, CompletableFuture<IntegerReply> zadd) throws ExecutionException, InterruptedException {
    eq(i, zadd.get());
  }

  private void eq(String exepcted, StatusReply actual) {
    assertEquals(exepcted, actual.data());
  }

  private void eq(Object[] expected, MultiBulkReply actual) {
    assertEquals(Arrays.asList(expected), actual.asStringList(Charsets.UTF_8));
  }

  private void eq(String expected, BulkReply actual) {
    assertEquals(expected, actual.asUTF8String());
  }

  private void eq(long expected, IntegerReply actual) {
    assertEquals(expected, (long) actual.data());
  }

  private void eq(byte[] bytes1, byte[] bytes2) {
    assertEquals(bytes1.length, bytes2.length);
    for (int i = 0; i < bytes1.length; i++) {
      assertEquals(bytes1[i], bytes2[i]);
    }
  }

  private Object[] a(Object... args) {
    return args;
  }
}
