package blam;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

class Driver {
  private static final Logger log = Logger.getGlobal();
  private static final Level logLevel = Level.INFO;

  public static void main(String args[]) {
    ConsoleHandler ch = new ConsoleHandler();
    ch.setLevel(logLevel);
    log.addHandler(ch);
    log.setLevel(logLevel);

    int size = 100 * (int)Math.pow(2, 20);
    CompressedBitVector cbv = new CompressedBitVector(size);
    StandardBitVector sbv = new StandardBitVector(size);

    BlamFilter<CharSequence> blam =
      new BlamFilter<>(Funnels.unencodedCharsFunnel(), cbv);

    BlamFilter<CharSequence> bloom =
      new BlamFilter<>(Funnels.unencodedCharsFunnel(), sbv);

    System.out.println("cbv: " + cbv.estimatedByteSize());
    System.out.println("sbv: " + sbv.estimatedByteSize());

    for (int i = 0; i < 10000; i++) {
      blam.put(Integer.toString(i));
      bloom.put(Integer.toString(i));
    }

    System.out.println("cbv: " + cbv.estimatedByteSize());
    System.out.println("sbv: " + sbv.estimatedByteSize());
  }
}
