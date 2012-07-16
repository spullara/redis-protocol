package redis.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;

public class BytesKeyTest {

  BytesKey $(String s) { return new BytesKey(s.getBytes()); }

  @Test
  public void testBoundaries() {
    BytesKey t1 = $("1");
    BytesKey t2 = $("2");
    BytesKey t3 = $("12");
    BytesKey t4 = $("13");
    BytesKey t5 = $("32");
    BytesKey t6 = $("123");
    BytesKey t7 = $("143");
    BytesKey t8 = $("223");
    BytesKey t9 = $("124");
    BytesKey t10 = $("12345");
    BytesKey t11 = $("12645");

    assertFalse(t1.equals(t2));
    assertFalse(t2.equals(t3));
    assertFalse(t3.equals(t4));
    assertFalse(t4.equals(t5));
    assertFalse(t5.equals(t6));
    assertFalse(t6.equals(t7));
    assertFalse(t7.equals(t8));
    assertFalse(t8.equals(t9));
    assertFalse(t9.equals(t10));
    assertFalse(t10.equals(t11));
    assertEquals(t1, t1);
    assertEquals(t2, t2);
    assertEquals(t3, t3);
    assertEquals(t4, t4);
    assertEquals(t5, t5);
    assertEquals(t6, t6);
    assertEquals(t7, t7);
    assertEquals(t8, t8);
    assertEquals(t9, t9);
    assertEquals(t10, t10);
    assertEquals(t11, t11);
  }
}
