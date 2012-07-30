package redis.util;

import org.junit.Test;

import java.util.Iterator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Some tests.
 */
public class ZSetTest {

  private static final BytesKey ONE = new BytesKey("one".getBytes());
  private static final BytesKey TWO = new BytesKey("two".getBytes());
  private static final BytesKey THREE = new BytesKey("three".getBytes());

  @Test
  public void testOps() {
    ZSet zs = new ZSet();
    zs.add(ONE, 1);
    zs.add(TWO, 2);
    zs.add(THREE, 3);
    ZSet zs2 = new ZSet();
    zs2.addAll(zs);
    {
      Iterator<ZSetEntry> i = zs.subSet(1.0, 2.0).iterator();
      assertEquals(ONE, i.next().getKey());
      assertEquals(TWO, i.next().getKey());
      assertFalse(i.hasNext());
    }
    {
      Iterator<ZSetEntry> i = zs.subSet(1, 2).iterator();
      assertEquals(TWO, i.next().getKey());
      assertEquals(THREE, i.next().getKey());
      assertFalse(i.hasNext());
    }
    zs.remove(TWO);
    {
      Iterator<ZSetEntry> i = zs2.subSet(1.0, 2.0).iterator();
      assertEquals(ONE, i.next().getKey());
      assertEquals(TWO, i.next().getKey());
      assertFalse(i.hasNext());
    }
    {
      Iterator<ZSetEntry> i = zs2.subSet(1, 2).iterator();
      assertEquals(TWO, i.next().getKey());
      assertEquals(THREE, i.next().getKey());
      assertFalse(i.hasNext());
    }
    {
      Iterator<ZSetEntry> i = zs.subSet(1.0, 2.0).iterator();
      assertEquals(ONE, i.next().getKey());
      assertFalse(i.hasNext());
    }
    {
      Iterator<ZSetEntry> i = zs.subSet(0, 1).iterator();
      assertEquals(ONE, i.next().getKey());
      assertEquals(THREE, i.next().getKey());
      assertFalse(i.hasNext());
    }
  }

  private void println(Object o) {
    System.out.println(String.valueOf(o));
  }
}
