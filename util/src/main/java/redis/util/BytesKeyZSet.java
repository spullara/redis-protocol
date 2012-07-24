package redis.util;

import java.util.TreeSet;

public class BytesKeyZSet extends TreeSet<ZSetEntry> {

  private BytesKeyObjectMap<ZSetEntry> map = new BytesKeyObjectMap<ZSetEntry>();

  public BytesKeyZSet() {}

  public BytesKeyZSet(BytesKeyZSet destination) {
    super(destination);
  }

  @Override
  public boolean add(ZSetEntry zSetEntry) {
    map.put(zSetEntry.getValue(), zSetEntry);
    return super.add(zSetEntry);
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof ZSetEntry) {
      map.remove(((ZSetEntry) o).getValue());
    }
    return super.remove(o);
  }

  public ZSetEntry get(BytesKey key) {
    return map.get(key);
  }

  public ZSetEntry get(byte[] key) {
    return map.get(new BytesKey(key));
  }
}
