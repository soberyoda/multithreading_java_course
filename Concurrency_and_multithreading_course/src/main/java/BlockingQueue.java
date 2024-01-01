import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {
    private Queue<Integer> q;
    private int capacity;

    BlockingQueue(int capacity){
        this.capacity = capacity;
        this.q = new LinkedList<>();
    }
    public boolean add(int element){
        synchronized (q){
            while (this.q.size() == this.capacity){
                try {
                    this.q.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.q.add(element);
            this.q.notifyAll();
            return true;
        }
    }

    public int remove(){
        synchronized (q){
            while (this.q.size() == 0){
                try {
                    this.q.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.q.notifyAll();
            return this.q.poll();
        }
    }

}
