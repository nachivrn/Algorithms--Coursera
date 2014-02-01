import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MergeSortInversion {
    private static long inversions;
    private static int[] intArray = new int[100000];

    static void mergesort(int[] arr, int left, int right) {
        if (left >= right)
            return;
        int mid = (left + right) / 2;
        mergesort(arr, left, mid);
        mergesort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    static void merge(int[] arr, int start, int middle, int end) {
        int left = start;
        int right = middle + 1;
        while (left <= middle && right <= end) {
            if (arr[left] <= arr[right]) {
                left++;
            } else {
                int temp = arr[right];
                System.arraycopy(arr, left, arr, left + 1, right - left);
                arr[left] = temp;
                inversions += right - left;
                left++;
                middle++;
                right++;
            }
        }
    }

    static void readInput(File input) throws Exception {
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String str;
        int count = 0;
        while ((str = br.readLine()) != null) {
            intArray[count] = Integer.parseInt(str.trim());
            count++;
        }
        br.close();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java MergeSortInversion <input.txt>");
            System.exit(-1);
        }
        readInput(new File(args[0]));
        mergesort(intArray, 0, intArray.length - 1);
        System.out.println("Number of inversions " + inversions);
    }
}
