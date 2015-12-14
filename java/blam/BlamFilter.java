package blam;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.logging.Logger;
import java.util.logging.Level;

public class BlamFilter<T> {
  private static final Logger log = Logger.getGlobal();

  private BitVector bv;
  private Funnel<T> funnel;

  public BlamFilter(Funnel<T> funnel, BitVector bv) {
    this.funnel = funnel;
    this.bv = bv;
  }

  public BlamFilter(Funnel<T> funnel, int size) {
    this(funnel, new CompressedBitVector(size));
  }

  private int hash(T object) {
    int hash = Hashing.murmur3_128().newHasher().
      putObject(object, this.funnel).
      hash().
      asInt();

    // We always want this to be a positive number.
    int unsigned = hash < 0 ? -hash : hash;

    return unsigned % this.bv.getSize();
  }

  public void put(T object) {
    this.bv.set(hash(object));
  }

  public boolean mightContain(T object) {
    return this.bv.get(hash(object));
  }
}
