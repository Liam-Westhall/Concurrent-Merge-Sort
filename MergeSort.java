import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
public class MergeSort {
    private static final Random RNG    = new Random(10982755L);
    private static final int    LENGTH = 1000000;

    public static void main(String... args) {
        int[] arr = randomIntArray();
        long start = System.currentTimeMillis();
        concurrentMergeSort(arr);
        long end = System.currentTimeMillis();
        if (!sorted(arr)) {
            System.err.println("The final array is not sorted");
            System.exit(0);
        }
        System.out.printf("%10d numbers: %6d ms%n", LENGTH, end - start);
    }

    private static int[] randomIntArray() {
        int[] arr = new int[LENGTH];
        for (int i = 0; i < arr.length; i++)
            arr[i] = RNG.nextInt(LENGTH * 10);
        return arr;
    }

    public static boolean sorted(int[] arr) {
        return IntStream.range(1, arr.length)
                .mapToObj(i -> arr[i - 1] <= arr[i])
                .reduce(true, (x, y) -> (x && y));
    }
    public static void concurrentMergeSort(int[] arr){
        concurrentMergeSort(arr, 2);
}
    public static void merge(int[] arr, int[] left, int[] right){
        int x = 0;
        int y = 0;
        for(int i = 0; i < arr.length; i++){
            if(y >= right.length || (x < left.length && left[x] <= right[y])) {
                arr[i] = left[x];
                x++;
            }
            else{
                arr[i] = right[y];
                y++;
            }
        }
    }

    public static void concurrentMergeSort(int[] arr, int threadCount) {
        if(arr.length <= 1){
            return;
        }
        if(threadCount <= 1){
            regularMergeSort(arr);
            return;
        }
        int middle = arr.length/2;
        int[] left = Arrays.copyOfRange(arr, 0, middle);
        int[] right = Arrays.copyOfRange(arr, middle, arr.length);
        Thread leftSort = new Thread(new Sorting(left, threadCount/2));
        Thread rightSort = new Thread(new Sorting(right, threadCount/2));
        leftSort.start();
        rightSort.start();
        for(int i = 0; i < threadCount; i++){

        }
        try{
            leftSort.join();
            rightSort.join();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        merge(arr, left, right);
    }
    public static void regularMergeSort(int[] arr){
        if(arr.length <= 1){
            return;
        }
        int middle = arr.length/2;
        int[] left = Arrays.copyOfRange(arr, 0, middle);
        int[] right = Arrays.copyOfRange(arr, middle, arr.length);
        regularMergeSort(left);
        regularMergeSort(right);
        merge(arr, left, right);
    }

}

