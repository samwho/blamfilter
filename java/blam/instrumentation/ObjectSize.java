package blam.instrumentation;

import java.lang.instrument.Instrumentation;

public class ObjectSize {
  private static Instrumentation instrumentation;

  public static void premain(String args, Instrumentation inst) {
    instrumentation = inst;
  }

  public static long get(Object o) {
    return instrumentation.getObjectSize(o);
  }
}
