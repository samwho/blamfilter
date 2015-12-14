package blam.benchmark;

import blam.BlamFilter;
import blam.BitVector;

import com.google.common.hash.Funnels;

import java.util.Random;

public class Task implements Runnable {
  private BlamFilter<Integer> bf;
  private int iterations;

  public Task(BitVector bv, int iterations) {
    this.iterations = iterations;
    this.bf = new BlamFilter<>(Funnels.integerFunnel(), bv);
  }

  @Override
  public void run() {
    Random prng = new Random();

    for (int i = 0; i < this.iterations; i++) {
      this.bf.put(prng.nextInt());
    }
  }
}
