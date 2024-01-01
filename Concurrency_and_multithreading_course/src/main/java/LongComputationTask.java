import java.math.BigInteger;

public class LongComputationTask {
    public static void main(String... args){
        Thread thread = new Thread(new LCmpTask(BigInteger.valueOf(120000), BigInteger.valueOf(3289)));
//        thread.setDaemon(true);
        thread.start();
        thread.interrupt();
    }
    public static class LCmpTask implements Runnable{
        private BigInteger base;
        private BigInteger power;

        public LCmpTask(BigInteger base, BigInteger power){
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(this.base + "^" + this.power  + "=" + pow(this.base, this.power));
        }
        private BigInteger pow(BigInteger base, BigInteger power){
            BigInteger result = BigInteger.ONE;
            for(BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0 ; i = i.add(BigInteger.ONE)){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("interrupted");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
