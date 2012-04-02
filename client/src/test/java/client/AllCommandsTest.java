package client;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.base.Charsets;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.client.RedisClient;
import redis.client.RedisException;
import redis.reply.BulkReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.StatusReply;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class AllCommandsTest {

  private static RedisClient rc;
  private static RedisClient rc2;

  @BeforeClass
  public static void setup() throws IOException {
    rc = new RedisClient("localhost", 6379);
    rc2 = new RedisClient("localhost", 6379);
  }

  @AfterClass
  public static void shutdown() throws IOException {
    rc.close();
    rc2.close();
  }

  @Test
  public void append() {
    rc.del($("mykey"));
    eq(5, rc.append("mykey", "Hello"));
    eq(11, rc.append("mykey", " World"));
    eq("Hello World", rc.get("mykey"));
  }

  @Test
  public void blpop() {
    rc.del($("list1", "list2"));
    eq(3, rc.rpush("list1", $("a", "b", "c")));
    eq($("list1", "a"), rc.blpop($("list1", "list2", "0")));
  }

  @Test
  public void brpop() {
    rc.del($("list1", "list2"));
    eq(3, rc.rpush("list1", $("a", "b", "c")));
    eq($("list1", "c"), rc.brpop($("list1", "list2", "0")));
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
    rc.del($("key1", "key2", "key3"));
    eq("OK", rc.set("key1", "Hello"));
    eq("OK", rc.set("key2", "World"));
    eq(2, rc.del($("key1", "key2", "key3")));
  }

  @Test
  public void eval() {
//    eq($("key1", "key2", "first", "second"),
//            (MultiBulkReply) rc.eval("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", 2,
//                    $("key1", "key2", "first", "second")));
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

  private Object[] $(Object... args) {
    return args;
  }
}
