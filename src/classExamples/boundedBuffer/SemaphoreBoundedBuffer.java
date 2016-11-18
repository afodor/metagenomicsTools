package classExamples.boundedBuffer;

import java.util.concurrent.*;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using \Semaphore
 *
 * @author Brian Goetz and Tim Peierls
 * modded from http://jcip.net/listings/SemaphoreBoundedBuffer.java
 */
//ThreadSafe
public class SemaphoreBoundedBuffer <E> {
    private final Semaphore availableItems, availableSpaces;
    //GuardedBy("this") 
    private final E[] items;
    //GuardedBy("this") 
    private int putPosition = 0, takePosition = 0;

    @SuppressWarnings("unchecked")
	public SemaphoreBoundedBuffer(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length) ? 0 : i;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return x;
    }
    
    public static void main(String[] args) throws Exception
	{
    	
		SemaphoreBoundedBuffer<Integer> sbb = new SemaphoreBoundedBuffer<>(5);

    	System.out.println(sbb.isEmpty());
		
		for( int x=0; x < 5; x++)
			sbb.put(x);
		
		System.out.println(sbb.isFull());
		
		for( int x=0; x < 5; x++)
			System.out.println(sbb.take());
		
		System.out.println(sbb.isEmpty());
	}
}