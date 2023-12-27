public class Stack {
    private int[] array;
    private int stackTop;
    public Stack(int capacity){
        array = new int[capacity];
        stackTop = -1;
    }

    public boolean stackIsEmpty(){
        return stackTop < 0;
    }
    public boolean stackIsFull(){
        return stackTop >= array.length-1;
    }
    public synchronized boolean push(int element) throws InterruptedException {
            if(stackIsFull()) return false;
            ++stackTop;
            try{
                Thread.sleep(1000);
            }catch (Exception ignored){}
            array[stackTop] = element;
            return true;
    }
    public synchronized int pop() throws InterruptedException {
            if (stackIsEmpty()) return Integer.MIN_VALUE;
            int obj = array[stackTop];
            array[stackTop] = Integer.MIN_VALUE;
            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
            stackTop--;
            return obj;
    }
}
