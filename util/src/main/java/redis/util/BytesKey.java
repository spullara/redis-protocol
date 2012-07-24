package redis.util;

import com.google.common.primitives.SignedBytes;

import java.util.Comparator;

/**
* A hashmap friendly key.
* <p/>
* User: sam
* Date: 7/28/11
* Time: 7:03 PM
*/
public class BytesKey implements Comparable<BytesKey> {
  private static final Comparator<byte[]> COMPARATOR = SignedBytes.lexicographicalComparator();

  private final int hashCode;
  private final byte[] bytes;

  public BytesKey(byte[] bytes) {
    this.bytes = bytes;
    int hashCode = 0;
    for (byte aByte : this.bytes) {
      hashCode += 43 * aByte;
    }
    this.hashCode = hashCode;
  }

  public int hashCode() {
    return hashCode;
  }

  public static boolean equals(byte[] thisBytes, byte[] otherBytes) {
    int length = thisBytes.length;
    if (length != otherBytes.length) {
      return false;
    }
    int half = length / 2;
    for (int i = 0; i < half; i++) {
      int end = length - i - 1;
      if (thisBytes[end] != otherBytes[end]) return false;
      if (thisBytes[i] != otherBytes[i]) return false;
    }
    if (half != length - half) {
      if (thisBytes[half] != otherBytes[half]) return false;
    }
    return true;
  }

  public boolean equals(Object o) {
    return o instanceof BytesKey && equals(bytes, ((BytesKey) o).bytes);
  }

  @Override
  public String toString() {
    return new String(bytes);
  }

  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public int compareTo(BytesKey o) {
    return COMPARATOR.compare(this.bytes, o.bytes);
  }
}
