import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class QuickSortComparisons {
    private static final int PIVOT_FIRST = 0;
    private static final int PIVOT_LAST = 1;
    private static final int PIVOT_MEDIAN = 2;
    private static int[] intArray = new int[10000];
    private static int comparisons;

    static int partition(int[] inputArr, int left, int right) {
        int pivot = inputArr[left];
        int i = left + 1;
        for (int j = left + 1; j <= right; j++) {
            if (inputArr[j] < pivot) {
                swap(inputArr, j, i);
                i = i + 1;
            }
        }
        swap(inputArr, left, i - 1);
        return i - 1;
    }

    static void quickSort(int[] inputArr, int left, int right, int pivotPos) {
        if (right <= left)
            return;

        if (PIVOT_LAST == pivotPos) {
            swap(inputArr, left, right);
        } else if (PIVOT_MEDIAN == pivotPos) {
            int firstElem = inputArr[left];
            int lastElem = inputArr[right];
            int midIndex;
            if (right - left % 2 == 0) {
                midIndex = left + (right - left) / 2 + 1;
            } else {
                midIndex = left + (right - left) / 2;
            }
            int middleElem = inputArr[midIndex];
            if (isMedian(firstElem, middleElem, lastElem)) {
                swap(inputArr, left, midIndex);
            } else if (isMedian(firstElem, lastElem, middleElem)) {
                swap(inputArr, left, right);
            }
        }
        comparisons += right - left;
        int partition = partition(inputArr, left, right);
        quickSort(inputArr, left, partition - 1, pivotPos);
        quickSort(inputArr, partition + 1, right, pivotPos);
    }

    static boolean isMedian(int i, int j, int k) {
        if ((i < j) && (j < k) ||
                (i > j && j > k))
            return true;
        return false;
    }

    static void swap(int[] inputArr, int i, int j) {
        int temp = inputArr[i];
        inputArr[i] = inputArr[j];
        inputArr[j] = temp;
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

    static void quickSort(int[] inputArr, int pivotPos) {
        quickSort(intArray, 0, intArray.length - 1, pivotPos);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java QuickSortComparisons <input.txt>");
            System.exit(-1);
        }
        // Question 1
        readInput(new File(args[0]));
        quickSort(intArray, PIVOT_FIRST);
        System.out.println(comparisons);
        // Question 2
        comparisons = 0;
        readInput(new File(args[0]));
        quickSort(intArray, PIVOT_LAST);
        System.out.println(comparisons);
        // Question 3
        comparisons = 0;
        readInput(new File(args[0]));
        quickSort(intArray,PIVOT_MEDIAN);
        System.out.println(comparisons);
    }
}
