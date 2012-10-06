package netty.mystery;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;
import redis.RedisProtocol;
import redis.netty.BulkReply;
import redis.netty.MultiBulkReply;
import redis.netty.RedisDecoder;
import redis.netty.Reply;
import redis.netty4.RedisReplyDecoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BenchmarkTest {

  public static final int CALLS = 1000000;

  @Test
  public void channelBufferBench() throws IOException {
    byte[] multiBulkReply = getBytes();
    long start = System.currentTimeMillis();
    RedisDecoder redisDecoder = new RedisDecoder();
    ChannelBuffer cb = ChannelBuffers.wrappedBuffer(multiBulkReply);
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < CALLS; j++) {
        Reply receive = redisDecoder.receive(cb);
        cb.resetReaderIndex();
      }
      long end = System.currentTimeMillis();
      long diff = end - start;
      System.out.println(diff + " " + ((double)diff)/ CALLS);
      start = end;
    }
  }

  @Test
  public void byteBufBench() throws IOException {
    byte[] multiBulkReply = getBytes();
    long start = System.currentTimeMillis();
    RedisReplyDecoder redisDecoder = new RedisReplyDecoder();
    ByteBuf bb = Unpooled.wrappedBuffer(multiBulkReply);
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < CALLS; j++) {
        redis.netty4.Reply receive = redisDecoder.receive(bb);
        bb.resetReaderIndex();
      }
      long end = System.currentTimeMillis();
      long diff = end - start;
      System.out.println(diff + " " + ((double)diff)/CALLS);
      start = end;
    }
  }

  @Test
  public void baisBench() throws IOException {
    byte[] multiBulkReply = getBytes();
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < CALLS; j++) {
        RedisProtocol.receive(new ByteArrayInputStream(multiBulkReply));
      }
      long end = System.currentTimeMillis();
      long diff = end - start;
      System.out.println(diff + " " + ((double)diff)/CALLS);
      start = end;
    }
  }

  private byte[] getBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(MultiBulkReply.MARKER);
    baos.write("100\r\n".getBytes());
    for (int i = 0; i < 100; i++) {
      baos.write(BulkReply.MARKER);
      baos.write("6\r\n".getBytes());
      baos.write("foobar\r\n".getBytes());
    }
    return baos.toByteArray();
  }

}
