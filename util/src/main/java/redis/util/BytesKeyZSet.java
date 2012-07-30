package redis.util;

import java.util.Collection;
import java.util.TreeSet;

public class BytesKeyZSet extends TreeSet<ZSetEntry> {

  private BytesKeyObjectMap<ZSetEntry> map;

  public BytesKeyZSet() {}

  public BytesKeyZSet(BytesKeyZSet destination) {
    super(destination);
  }

  @Override
  public boolean add(ZSetEntry zSetEntry) {
    getMap().put(zSetEntry.getValue(), zSetEntry);
    return super.add(zSetEntry);
  }

  private BytesKeyObjectMap<ZSetEntry> getMap() {
    if (map == null) {
      map = new BytesKeyObjectMap<ZSetEntry>();
    }
    return map;
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof ZSetEntry) {
      ZSetEntry removed = getMap().remove(((ZSetEntry) o).getValue());
      return removed != null && super.remove(removed);
    }
    return super.remove(o);
  }

  public ZSetEntry get(BytesKey key) {
    return getMap().get(key);
  }

  public ZSetEntry get(byte[] key) {
    return getMap().get(new BytesKey(key));
  }

  @Override
  public boolean addAll(Collection<? extends ZSetEntry> c) {
    boolean changed = super.addAll(c);
    for (ZSetEntry zSetEntry : c) {
      getMap().put(zSetEntry.getValue(), zSetEntry);
    }
    return changed;
  }
}
