package blam;

import com.google.common.hash.Funnels;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class CompressedBitVectorTest {
  @Test
  public void setFirstBit() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(0);

    assertThat(bv.toBinaryString(), equalTo("1000000000"));
    assertThat(bv.numEntries(), equalTo(2));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setSecondBit() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(1);

    assertThat(bv.toBinaryString(), equalTo("0100000000"));
    assertThat(bv.numEntries(), equalTo(3));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setFirstTwoBits() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(0);
    bv.set(1);

    assertThat(bv.toBinaryString(), equalTo("1100000000"));
    assertThat(bv.numEntries(), equalTo(2));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setLastBit() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(9);

    assertThat(bv.toBinaryString(), equalTo("0000000001"));
    assertThat(bv.numEntries(), equalTo(2));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setLastTwoBits() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(9);
    bv.set(8);

    assertThat(bv.toBinaryString(), equalTo("0000000011"));
    assertThat(bv.numEntries(), equalTo(2));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setLastTwoBitsDifferentWay() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(8);
    bv.set(9);

    assertThat(bv.toBinaryString(), equalTo("0000000011"));
    assertThat(bv.numEntries(), equalTo(2));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setMiddleBitsWithMerge() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(5);
    bv.set(7);
    bv.set(6);

    assertThat(bv.toBinaryString(), equalTo("0000011100"));
    assertThat(bv.numEntries(), equalTo(3));
    assertThat(bv.calculatedSize(), equalTo(10));
  }

  @Test
  public void setAllBits() {
    CompressedBitVector bv = new CompressedBitVector(10);

    bv.set(6);
    bv.set(5);
    bv.set(0);
    bv.set(9);
    bv.set(1);
    bv.set(2);
    bv.set(4);
    bv.set(3);
    bv.set(7);
    bv.set(8);

    assertThat(bv.toBinaryString(), equalTo("1111111111"));
    assertThat(bv.numEntries(), equalTo(1));
    assertThat(bv.calculatedSize(), equalTo(10));
  }
}
