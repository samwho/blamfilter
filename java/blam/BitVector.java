package blam;

public interface BitVector {
  public void set(int position);
  public boolean get(int position);
  public int getSize();
  public int estimatedByteSize();
}
