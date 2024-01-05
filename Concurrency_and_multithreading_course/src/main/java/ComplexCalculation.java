import java.math.BigInteger;

public class ComplexCalculation {
    public static void main(String ... args) throws InterruptedException {
        System.out.println(calculateResult(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(3), BigInteger.valueOf(3)));
    }
    public static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);
        thread1.start();
        thread2.start();
        thread1.join(2000);
        thread2.join(2000);
        result = thread1.getResult().add(thread2.getResult());

        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
            this.result = calculation(this.base, this.power);
        }

        public BigInteger getResult() { return result; }
        public BigInteger calculation(BigInteger base, BigInteger power){
            return base.pow(power.intValue());
        }
    }
}