package blam;

import java.util.BitSet;

public class StandardBitVector implements BitVector {
  private BitSet bs;
  private int size;

  public StandardBitVector(int size) {
    this.size = size;
    this.bs = new BitSet(this.size);
  }

  @Override
  public void set(int position) {
    this.bs.set(position);
  }

  @Override
  public boolean get(int position) {
    return this.bs.get(position);
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public int estimatedByteSize() {
    // BitSet is efficient in that it uses single bits to represent... Well...
    // bits. Smrt.
    return
      4 + // wordsInUse
      4 + // sizeIsSticky
      8 + // serialVersionUID
      4 + // underlying array length
      (int)Math.ceil(this.size / 8.0f) // actual bits
      ;
  }
}
