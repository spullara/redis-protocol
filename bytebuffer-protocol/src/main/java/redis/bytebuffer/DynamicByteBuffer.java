package redis.bytebuffer;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Minimal bytebuffer for writing.
 */
public class DynamicByteBuffer {

  private ByteBuffer first;
  private LinkedList<ByteBuffer> byteBuffers;

  public DynamicByteBuffer() {
    make();
  }

  public void put(byte b) {
    ByteBuffer current = current();
    if (current.remaining() == 0) {
      // Make a new buffer
      current = make();
    }
    current.put(b);
  }

  public void put(byte[] bytes) {
    put(bytes, 0, bytes.length);
  }

  private void put(byte[] bytes, int offset, int length) {
    ByteBuffer current = current();
    int remaining = current.remaining();
    if (remaining < length - offset) {
      current.put(bytes, offset, remaining);
      make();
      put(bytes, offset + remaining, bytes.length - remaining);
    } else {
      current.put(bytes);
    }
  }

  public void put(ByteBuffer bytes) {
    put(bytes.array());
  }

  private ByteBuffer current() {
    ByteBuffer current;
    if (byteBuffers == null) {
      // Haven't yet needed to expand
      current = first;
    } else {
      current = byteBuffers.getLast();
    }
    return current;
  }

  private ByteBuffer make() {
    ByteBuffer allocate = ByteBuffer.allocate(1024);
    if (first == null) {
      first = allocate;
    } else {
      if (byteBuffers == null) {
        byteBuffers = new LinkedList<ByteBuffer>();
      }
      byteBuffers.add(allocate);
    }
    return allocate;
  }

  public ByteBuffer asByteBuffer() {
    int size = first.limit() - first.remaining();
    if (byteBuffers != null) {
      for (ByteBuffer byteBuffer : byteBuffers) {
        size += byteBuffer.limit() - byteBuffer.remaining();
      }
    } else {
      first.flip();
      return first;
    }

    ByteBuffer allocate = ByteBuffer.allocate(size);
    first.flip();
    allocate.put(first);
    if (byteBuffers != null) {
      for (ByteBuffer byteBuffer : byteBuffers) {
        byteBuffer.flip();
        allocate.put(byteBuffer);
      }
    }
    allocate.flip();
    return allocate;
  }
}
