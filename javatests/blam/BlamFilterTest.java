package blam;

import com.google.common.hash.Funnels;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class BlamFilterTest {
  @Test
  public void simpleCorrectBitsSet() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.put(1);
    blam.put(2);
    blam.put(1);
    blam.put(2);

    assertThat("check bits", blam.numSetBits(), lessThanOrEqualTo(2L));
    assertThat("check size", blam.calculatedSize(), equalTo(blam.getSize()));
  }

  @Test
  public void lessSimpleCorrectBitsSet() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10000);

    for (int i = 1; i < 1000; i++) {
      blam.put(i);
      assertThat("check bits", blam.numSetBits(), lessThanOrEqualTo((long)i));
      assertThat("check size", blam.calculatedSize(), equalTo(blam.getSize()));
    }
  }

  public void stringsCheckContains() {
    BlamFilter<CharSequence> blam =
      new BlamFilter<>(Funnels.unencodedCharsFunnel(), 10000);

    for (int i = 1; i < 1000; i++) {
      String s = Integer.toString(i);

      blam.put(s);
      assertThat("string check " + i, blam.mightContain(s), is(true));
    }
  }

  @Test
  public void setFirstBit() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(0);

    assertThat(blam.toBinaryString(), equalTo("1000000000"));
    assertThat(blam.numEntries(), equalTo(2));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setSecondBit() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(1);

    assertThat(blam.toBinaryString(), equalTo("0100000000"));
    assertThat(blam.numEntries(), equalTo(3));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setFirstTwoBits() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(0);
    blam.setBit(1);

    assertThat(blam.toBinaryString(), equalTo("1100000000"));
    assertThat(blam.numEntries(), equalTo(2));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setLastBit() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(9);

    assertThat(blam.toBinaryString(), equalTo("0000000001"));
    assertThat(blam.numEntries(), equalTo(2));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setLastTwoBits() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(9);
    blam.setBit(8);

    assertThat(blam.toBinaryString(), equalTo("0000000011"));
    assertThat(blam.numEntries(), equalTo(2));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setLastTwoBitsDifferentWay() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(8);
    blam.setBit(9);

    assertThat(blam.toBinaryString(), equalTo("0000000011"));
    assertThat(blam.numEntries(), equalTo(2));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setMiddleBitsWithMerge() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(5);
    blam.setBit(7);
    blam.setBit(6);

    assertThat(blam.toBinaryString(), equalTo("0000011100"));
    assertThat(blam.numEntries(), equalTo(3));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }

  @Test
  public void setAllBits() {
    BlamFilter<Integer> blam = new BlamFilter<>(Funnels.integerFunnel(), 10);

    blam.setBit(6);
    blam.setBit(5);
    blam.setBit(0);
    blam.setBit(9);
    blam.setBit(1);
    blam.setBit(2);
    blam.setBit(4);
    blam.setBit(3);
    blam.setBit(7);
    blam.setBit(8);

    assertThat(blam.toBinaryString(), equalTo("1111111111"));
    assertThat(blam.numEntries(), equalTo(1));
    assertThat(blam.calculatedSize(), equalTo(10L));
  }
}
