package blam;

import com.google.common.hash.Funnels;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class BlamFilterTest {
  @Test
  public void stringsCheckContains() {
    BlamFilter<CharSequence> blam =
      new BlamFilter<>(Funnels.unencodedCharsFunnel(), 10000);

    for (int i = 1; i < 1000; i++) {
      String s = Integer.toString(i);

      blam.put(s);
      assertThat("string check " + i, blam.mightContain(s), is(true));
    }
  }
}
