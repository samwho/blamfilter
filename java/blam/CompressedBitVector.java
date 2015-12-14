package blam;

import com.google.common.annotations.VisibleForTesting;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CompressedBitVector implements BitVector {
  private static final Logger log = Logger.getGlobal();

  private LinkedList<Entry> list;
  private boolean firstIsZero = true;
  private int size;

  public CompressedBitVector(int size) {
    this.size = size;
    this.list = new LinkedList<Entry>();
    this.list.add(new Entry(this.size));
  }

  @Override
  public int estimatedByteSize() {
    return
      8 + // object overhead for this object
      4 + // this.firstIsZero
      4 + // this.size
      this.list.size() * (4 + 8) // Entry object overhead + size;
      ;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public void set(int position) {
    log.fine("asked to set bit in position: " + position);

    ListIterator<Entry> li = this.list.listIterator();

    while (li.hasNext()) {
      int index = li.nextIndex();
      Entry current = li.next();

      if (position == 0) {
        // We've found that we're on the edge of the Entry we're looking at,
        // which means we have to increase its size and decrease the size of the
        // next Entry.
        if (index == 0) {
          // Early break: we're setting a 1. No more work to do.
          if (isEntrySet(index)) break;

          // Edge case: we're setting exactly the first bit.
          log.fine("setting exactly first bit");

          this.firstIsZero = false;
          li.add(new Entry(current.getSize() - 1));
          current.setSize(1);
          break;
        } else {
          // Early break: we're setting a 1. No more work to do.
          if (isEntrySet(index)) break;

          // We're not at the very start, which means the previous Entry is set
          // bits, so we decrement the size of the current entry and increment
          // the size of the previous.
          log.fine("setting bit at start of entry with index: " + index);

          current.dec();

          // For reasons I'm not 100% clear on, you have to call .previous()
          // twice to get the correct previous object returned.
          li.previous();
          Entry previous = li.previous();

          previous.inc();

          // Move pointer forward again.
          li.next();
          li.next();

          // If we've decremented the current Entry to 0, we want to remove it
          // and join up the two Entries either side of it. Garbage collection,
          // if you will.
          if (current.getSize() == 0) {
            log.fine("entry at index " + index + " decremented to 0, removing");
            li.remove();

            // The Entry we removed was not at the end of the list of Entries.
            // This means we need to also remove the next one and increment the
            // previous by its size or we'll end up ruining our data structure.
            if (li.hasNext()) {
              Entry next = li.next();
              previous.incBy(next.getSize());
              li.remove();
            }
          }

          break;
        }
      } else if (position < current.getSize() - 1) {
        // Early break: we're setting a 1. No more work to do.
        if (isEntrySet(index)) break;

        // We've found the right Entry in which this bit belongs, now we have to
        // check if the entry is set or unset. In this code path, we're
        // guaranteed to not be setting the first or last bit in this Entry, so
        // any sets are going to require splitting a single Entry in to 3: 0s
        // follow by a 1 followed by 0s.
        log.fine("setting bit at position " + position + " of entry " + index);

        Entry one = new Entry(1);
        Entry nextZeroes = new Entry(current.getSize() - position - 1);
        current.setSize(position);
        li.add(one);
        li.add(nextZeroes);
        break;
      } else if (position == current.getSize() - 1) {
        // Early break: we're setting a 1. No more work to do.
        if (isEntrySet(index)) break;

        // Edge case: we're setting the last bit in an Entry.
        log.fine("setting last bit in entry " + index);

        current.dec();

        if (li.hasNext()) {
          li.next().inc();
        } else {
          // Edge case: we're actually at the last Entry and need to add a new
          // one.
          li.add(new Entry(1));
        }

        // Note: one of the code paths above has advanced the pointer in this
        // ListIterator. It shouldn't matter, as we're done with iteration at
        // this point, but be mindful of this fact if you're playing around with
        // this implementation.
        break;
      } else {
        // Keep searching!
        position -= current.getSize();
      }
    }
  }

  @Override
  public boolean get(int position) {
    log.fine("asked to find bit at position " + position);

    ListIterator<Entry> li = this.list.listIterator();

    while (li.hasNext()) {
      int index = li.nextIndex();
      Entry current = li.next();

      if (position < current.getSize()) return isEntrySet(index);
      position -= current.getSize();
    }

    return false;
  }

  private boolean isEntrySet(int index) {
    return (this.firstIsZero && (index % 2 == 1)) ||
      (!this.firstIsZero && (index % 2 == 0));
  }

  @VisibleForTesting
  public int numSetBits() {
    boolean doCount = !firstIsZero;
    int total = 0;

    for (Entry e : this.list) {
      if (doCount) total += e.getSize();
      doCount = !doCount;
    }

    return total;
  }

  @VisibleForTesting
  public int numEntries() {
    return this.list.size();
  }

  @VisibleForTesting
  public int calculatedSize() {
    int total = 0;

    for (Entry e : this.list) {
      total += e.getSize();
    }

    return total;
  }

  @VisibleForTesting
  public String toBinaryString() {
    StringBuilder sb = new StringBuilder();
    String sp = firstIsZero ? "0" : "1";

    for (Entry e : this.list) {
      for (int i = 0; i < e.getSize(); i++) {
        sb.append(sp);
      }

      sp = sp.equals("1") ? "0" : "1";
    }

    return sb.toString();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    String sp = firstIsZero ? "0" : "1";

    int index = 0;

    for (Entry e : this.list) {
      sb.append("(i:" + index + " b:" + sp + " s:" + e.getSize() + ")");
      sp = sp.equals("1") ? "0" : "1";
      index++;
    }

    return sb.toString();
  }

  private class Entry {
    private int size;

    public Entry(int size) {
      setSize(size);
    }

    public void setSize(int size) {
      this.size = size;
    }

    public int getSize() {
      return this.size;
    }

    public void dec() {
      this.size--;
    }

    public void inc() {
      this.size++;
    }

    public void incBy(int i) {
      this.size += i;
    }

    public String toString() {
      return "Entry(size: " + this.size + ")";
    }
  }
}
