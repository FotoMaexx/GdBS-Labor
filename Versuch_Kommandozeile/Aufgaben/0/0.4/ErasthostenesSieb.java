public class ErasthostenesSieb {

    public static void main(String[] args) {
        boolean[] arr = new boolean[100];
        int x = 0;

        for(int i=2; i<100; i++) {
            arr[i] = true;
        }
        for(int i=2; i<100; i++) {
            if(isPrim(Long.valueOf(i)) == false){
                 arr[i] = false;
                 x = i;
                 do {
                     arr[x] = false;
                     x = x * 2;
                 } while(x*2 < 100);
            }
        }

        System.out.print("Primzahlen:");
        for(int i=2; i<100; i++){
            if(arr[i] == true) {
                System.out.print(" " + i);
            }
        }
        System.out.println();
    }

    public static boolean isPrim(final long value) {
        if (value <= 2) {
            return (value == 2);
        }
        for (long i = 2; i * i <= value; i++) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }
}
