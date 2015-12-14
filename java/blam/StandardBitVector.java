package blam;

import java.util.BitSet;

public class StandardBitVector implements BitVector {
  private BitSet bs;
  private int size;

  public StandardBitVector(int size) {
    this.size = size;
    this.bs = new BitSet(this.size);
  }

  public void set(int position) {
    this.bs.set(position);
  }

  public boolean get(int position) {
    return this.bs.get(position);
  }

  public int getSize() {
    return this.size;
  }
}
