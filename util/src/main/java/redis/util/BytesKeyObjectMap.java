package redis.util;

import java.util.HashMap;

/**
 * Map that uses byte[]s for keys. Wraps them for you. Passing a non-byte[] or
 * non-BytesKey will result in a CCE.
*/
public class BytesKeyObjectMap<V> extends HashMap<Object, V> {

  private BytesKey makeKey(Object key) {
    return key instanceof byte[] ? new BytesKey((byte[]) key) : (BytesKey) key;
  }

  @Override
  public V get(Object key) {
    return super.get(makeKey(key));
  }

  public V get(byte[] key) {
    return super.get(new BytesKey(key));
  }

  public V get(BytesKey key) {
    return super.get(key);
  }

  @Override
  public boolean containsKey(Object key) {
    return super.containsKey(makeKey(key));
  }

  public boolean containsKey(byte[] key) {
    return super.containsKey(new BytesKey(key));
  }

  public boolean containsKey(BytesKey key) {
    return super.containsKey(key);
  }

  @Override
  public V put(Object key, V value) {
    return super.put(makeKey(key), value);
  }

  public V put(byte[] key, V value) {
    return super.put(new BytesKey(key), value);
  }

  public V put(BytesKey key, V value) {
    return super.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return super.remove(makeKey(key));
  }

  public V remove(byte[] key) {
    return super.remove(new BytesKey(key));
  }

  public V remove(BytesKey key) {
    return super.remove(key);
  }
}
