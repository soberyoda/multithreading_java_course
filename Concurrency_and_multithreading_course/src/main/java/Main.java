
public class Main {
    public static void main(String ... args){
//        System.out.println("main start");
//        Thread thread1 = new Thread1("isThe1thread");
//        thread1.start();
//
//        Thread thread2 = new Thread(new Thread2(), "thread2");
//        thread2.start();
//
//        Thread thread3 = new Thread(()->{
//            for(int i = 0; i < 5; i++){
//                System.out.println("Name: " + Thread.currentThread().getName());
//            }
//        }, "thread3");
//        thread3.start();
//        System.out.println("main end");

        Stack stack = new Stack(3);
        new Thread(()->{
           int counter = 0;
           while (++ counter < 10 ){
               try {
                   System.out.println(stack.push(100));
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        }, "pusher").start();

        new Thread(()->{
            int counter = 0;
            while (++ counter < 10 ){
                try {
                    System.out.println(stack.pop());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "popper").start();
    }
}
