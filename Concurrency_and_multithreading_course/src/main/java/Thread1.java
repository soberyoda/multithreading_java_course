public class Thread1 extends Thread {
    Thread1(String threadName){
        super(threadName);
    }
    @Override
    public void run(){
        for(int i = 0; i< 5; i ++){
            System.out.println("Name: " + Thread.currentThread().getName() + i);
        }
    }
}
