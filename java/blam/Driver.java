package blam;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

class Driver {
  private static final Logger log = Logger.getGlobal();
  private static final Level logLevel = Level.ALL;

  public static void main(String args[]) {
    ConsoleHandler ch = new ConsoleHandler();
    ch.setLevel(logLevel);
    log.addHandler(ch);
    log.setLevel(logLevel);

    BlamFilter<CharSequence> blam =
      new BlamFilter<>(Funnels.unencodedCharsFunnel(), 10000);

    blam.setBit(0);
    blam.setBit(1);

    System.out.println(blam.numSetBits());
    System.out.println(blam.toString());
  }
}