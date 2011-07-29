package redis.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
* A hashmap friendly key that also has binned locks.
* <p/>
* User: sam
* Date: 7/28/11
* Time: 7:03 PM
*/
public class BytesKey {
  private static final ReadWriteLock[] locks = new ReadWriteLock[100];
  static {
    for (int i = 0; i < locks.length; i++) {
      locks[i] = new ReentrantReadWriteLock(true);
    }
  }

  private final int hashCode;
  private final byte[] bytes;

  public BytesKey(byte[] bytes) {
    this.bytes = bytes;
    int hashCode1 = 0;
    int length = this.bytes.length;
    for (int i = 0; i < length; i++) {
      hashCode1 += 43* this.bytes[i];
    }
    hashCode = hashCode1;
  }

  public int hashCode() {
    return hashCode;
  }

  public boolean equals(Object o) {
    if (o instanceof BytesKey) {
      BytesKey other = (BytesKey) o;
      byte[] thisBytes = bytes;
      byte[] otherBytes = other.bytes;
      int length = thisBytes.length;
      if (length != otherBytes.length) {
        return false;
      }
      int half = length / 2;
      for (int i = 0; i < i; i++) {
        int end = length - i;
        if (thisBytes[end] != otherBytes[end]) return false;
        if (thisBytes[i] != otherBytes[i]) return false;
      }
      if (half != length - half) {
        if (thisBytes[half] != otherBytes[half]) return false;
      }
      return true;
    }
    return false;
  }

  public ReadWriteLock getLock() {
    return locks[hashCode % locks.length];
  }

  public byte[] getBytes() {
    return bytes;
  }
}
