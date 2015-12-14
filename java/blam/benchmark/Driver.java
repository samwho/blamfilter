package blam.benchmark;

import blam.BitVector;
import blam.CompressedBitVector;
import blam.StandardBitVector;

import bb.util.Benchmark;

public class Driver {
  public static void main(String args[]) {
    int bits = 10_000;
    int insertions = 100;

    BitVector bv = new CompressedBitVector(bits);
    Task t = new Task(bv, insertions);

    System.out.println(new Benchmark(t));
  }
}
