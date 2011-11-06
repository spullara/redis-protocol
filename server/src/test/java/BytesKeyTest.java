import org.junit.Test;
import redis.util.BytesKey;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 6:33 PM
 */
public class BytesKeyTest {
  @Test
  public void equals() {
    assertFalse(new BytesKey("te2st".getBytes()).equals(new BytesKey("te3st".getBytes())));
    assertFalse(new BytesKey("test2".getBytes()).equals(new BytesKey("test3".getBytes())));
    assertFalse(new BytesKey("test".getBytes()).equals(new BytesKey("test3".getBytes())));
    assertTrue(new BytesKey("test".getBytes()).equals(new BytesKey("test".getBytes())));
    assertTrue(new BytesKey("test2".getBytes()).equals(new BytesKey("test2".getBytes())));
  }
}
