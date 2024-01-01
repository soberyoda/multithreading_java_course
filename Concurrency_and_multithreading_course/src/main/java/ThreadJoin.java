import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadJoin {
    public static void main(String ... args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(0L, 3435L, 3543L, 2324L, 23L, 2435L, 5566L);
        List<FactorialThread> threads = new ArrayList<>(inputNumbers.size());
        for(long inputNumber: inputNumbers){
            threads.add(new FactorialThread(inputNumber));
        }
        for(FactorialThread thread: threads){
            thread.setDaemon(true);
            thread.start();
        }
        for(FactorialThread thread: threads){
            thread.join(2000);
        }
        for(int i = 0; i < inputNumbers.size(); i++){
            FactorialThread factorialThread = threads.get(i);
            if(factorialThread.isFinished()){
                System.out.println("factorial of " + inputNumbers.get(i) + " is: " + factorialThread.getResult());
            }else {
                System.out.println("calculation for " + inputNumbers.get(i) + " in progress");
            }
        }
    }
    private static class FactorialThread extends Thread{
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;
        public FactorialThread(long inputNumber){
            this.inputNumber = inputNumber;
        }
        @Override
        public void run(){
            this.result = factorial(this.inputNumber);
            this.isFinished = true;
        }
        public boolean isFinished(){
            return this.isFinished;
        }
        public BigInteger getResult(){
            return result;
        }
        public BigInteger factorial(long inputNumber){
            BigInteger tmpResult = BigInteger.ONE;
            for(long i = inputNumber; i > 0 ; i--){
                tmpResult = tmpResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tmpResult;
        }
    }
}
