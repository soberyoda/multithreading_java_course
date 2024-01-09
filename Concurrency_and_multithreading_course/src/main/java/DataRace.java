public class DataRace {
    public static void main(String... args){
        SharedData sharedData = new SharedData();
        Thread thread = new Thread(()->{
            for(int i = 0; i < Integer.MAX_VALUE; i++){
                sharedData.increment();
            }
        });
        Thread thread1 = new Thread(()-> {
            for(int i = 0; i < Integer.MAX_VALUE; i ++){
                sharedData.checForDataRace();
            }
        });
        thread.start();
        thread1.start();
    }
    private static class SharedData {
        private int x = 0;
        private int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public void checForDataRace() {
            if (y > x) {
                System.out.println("y > x - data race is detected");
            }
        }
    }

}
