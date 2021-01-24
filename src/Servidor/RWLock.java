package Servidor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWLock {
    private int readers;
    private int writers;
    private int writeRequests;
    private int readRequests;
    private int readersConsecutive;
    private int writersConsecutive;
    private ReentrantLock rl;
    private Condition waitReaders;
    private Condition waitWriters;

    public RWLock(){
        this.readers = 0;
        this.writers = 0;
        this.writeRequests = 0;
        this.readRequests = 0;
        this.readersConsecutive=0;
        this.writersConsecutive=0;
        this.rl = new ReentrantLock();
        this.waitReaders = this.rl.newCondition();
        this.waitWriters = this.rl.newCondition();
    }

    public void readLock() {
        this.rl.lock();
        try {
            this.readRequests++;
            while (this.writers > 0|| (this.writeRequests > 0 && this.readersConsecutive > 5)) {
                this.waitReaders.await();
            }
            this.readRequests--;
        } catch (InterruptedException ie) {System.out.println("500 - Internal Server Error - RWREADLOCK");}
        this.writersConsecutive = 0;
        this.readersConsecutive++;
        this.readers++;
        this.rl.unlock();
    }

    public void writeLock() {
        this.rl.lock();
        try {
            this.writeRequests++;
            while (this.writers > 0 || this.readers > 0 || (this.readRequests > 0 && this.writersConsecutive > 5)) {
                this.waitWriters.await();
            }
            this.writeRequests--;
        } catch (InterruptedException ie) {System.out.println("500 - Internal Server Error - RWWRITELOCK");}
        this.readersConsecutive = 0; // COMBO BREAK
        this.writersConsecutive++;
        this.writers++;
        this.rl.unlock();
    }

    public void readUnlock() {
        this.rl.lock();
        this.readers--;
        if (this.readers == 0) {
            this.waitWriters.signalAll();
        }
        this.rl.unlock();
    }

    public void writeUnlock() {
        this.rl.lock();
        this.writers--;
        this.waitReaders.signalAll();
        this.waitWriters.signalAll();
        this.rl.unlock();
    }
}
