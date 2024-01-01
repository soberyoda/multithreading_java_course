package monte_carlo_kolokwium;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;

public class MonteCarlo {
    Queue<MCThread> queue = new LinkedList<>();
    static class MCThread implements Runnable{
        Function<Double, Double> f;
        double a;
        double b;
        int n;
        MCThread(Function<Double, Double> f, double a, double b, int n){
            this.f = f;
            this.a = a;
            this.b = b;
            this.n = n;
        }

        @Override
        public void run() {
            double sum = 0;
            Random r = new Random();
            for(int i = 1; i < n ; i ++){
                double x = a + (b-a) * r.nextDouble();
                sum += f.apply(x) * (b-a) /n ;
            }
        }

        public void integrateSequential(int n){
            MCThread thread1 = new MCThread((x) -> Math.sin(x),0, Math.PI,n);
            thread1.run();
        }
        public void integrateParallel(int k, int n){
            for(int i = 0; i < k; i++){

            }
        }
    }
    public static void main(String... args){
    }
}
