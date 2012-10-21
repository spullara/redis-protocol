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
public class BytesKey extends BytesValue implements Comparable<BytesKey> {
  private static final Comparator<byte[]> COMPARATOR = SignedBytes.lexicographicalComparator();

  private final int hashCode;

  @Override
  public boolean equals(Object o) {
    BytesKey other = (BytesKey) o;
    return o instanceof BytesKey && hashCode == other.hashCode && equals(bytes, other.bytes);
  }

  public BytesKey(byte[] bytes) {
    super(bytes);
    int hashCode = 0;
    for (byte aByte : this.bytes) {
      hashCode += 43 * aByte;
    }
    this.hashCode = hashCode;
  }

  public int hashCode() {
    return hashCode;
  }

  @Override
  public int compareTo(BytesKey o) {
    return COMPARATOR.compare(this.bytes, o.bytes);
  }
}
