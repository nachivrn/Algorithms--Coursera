import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The text file contains a list of the integers from 1 to 10000 in unsorted order; 
 * you should treat this as a stream of numbers, arriving one by one. Letting xi denote 
 * the ith number of the file, the kth median mk is defined as the median of the numbers x1,…,xk. 
 * (So, if k is odd, then mk is ((k+1)/2)th smallest number among x1,…,xk; if k is even, then mk 
 * is the (k/2)th smallest number among x1,…,xk.)
 */

public class MedianMaintenance {
    static List<Integer> integerList = new ArrayList<Integer>();
    static List<Integer> medianList = new ArrayList<Integer>();
    static Heap<Integer> minHeap = new Heap<Integer>();
    static Heap<Integer> maxHeap = new Heap<Integer>();


    static void readInput(File input) throws Exception {
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String str;
        while ((str = br.readLine()) != null) {
            integerList.add(Integer.parseInt(str));
        }
    }

    static void computeMedian() {
        for (int integer : integerList) {
            if (minHeap.size() == 0 && maxHeap.size() == 0) {
                minHeap.insert(-integer);
            } else if (Math.abs(minHeap.peek()) < integer) {
                maxHeap.insert(integer);
            } else {
                minHeap.insert(-integer);
            }
            adjustMaxMinHeap();
            if (minHeap.size() == maxHeap.size()) {
                medianList.add(Math.abs(minHeap.peek()));
            } else if (minHeap.size() > maxHeap.size()) {
                medianList.add(Math.abs(minHeap.peek()));
            } else {
                medianList.add(maxHeap.peek());
            }

        }
    }

    private static void adjustMaxMinHeap() {
        if (maxHeap.size() - minHeap.size() > 1) {
            int minimum = maxHeap.getMin();
            minHeap.insert(-minimum);
        } else if (minHeap.size() - maxHeap.size() > 1) {
            int maximum = Math.abs(minHeap.getMin());
            maxHeap.insert(maximum);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java MedianMaintenance <input.txt>");
            System.exit(-1);
        }
        readInput(new File(args[0]));
        computeMedian();
        long sumOfMedian = 0;
        for (int median : medianList) {
            sumOfMedian += median;
        }
        System.out.println("Average Median " + (sumOfMedian % 10000));
    }

    private static class Heap<T> {
        List<T> heapArray;
        public static final int ROOT_INDEX = 0;

        public Heap() {
            heapArray = new ArrayList<T>();
        }

        public T getMin() {
            T root = heapArray.get(ROOT_INDEX);
            heapArray.remove(ROOT_INDEX);
            if (heapArray.size() < 2) {
                return root;
            }
            T last = heapArray.get(heapArray.size() - 1);
            heapArray.add(ROOT_INDEX, last);
            heapArray.remove(heapArray.size() - 1);
            bubbleDown(ROOT_INDEX + 1);
            return root;
        }

        public void insert(T element) {
            if (heapArray.size() == 0) {
                heapArray.add(element);
                return;
            }
            heapArray.add(element);
            bubbleUp(heapArray.size());
        }

        public T peek() {
            return heapArray.get(ROOT_INDEX);
        }

        public int size() {
            return heapArray.size();
        }


        private void bubbleUp(int index) {
            if (index <= 1)
                return;
            int parentIndex = index / 2;
            T parent = heapArray.get(parentIndex - 1);
            T element = heapArray.get(index - 1);
            if (((Comparable<T>) parent).compareTo(element) <= 0) {
                return;
            } else {
                exch(parentIndex - 1, index - 1);
                bubbleUp(parentIndex);
            }
        }

        private void bubbleDown(int index) {
            int childIndex;
            if (2 * index < heapArray.size()) {
                childIndex = 2 * index;
                if (childIndex <= heapArray.size() && childIndex + 1 <= heapArray.size()) {
                    T leftChild = heapArray.get(childIndex - 1);
                    T rightChild = heapArray.get(childIndex);
                    if (((Comparable<T>) rightChild).compareTo(leftChild) < 0)
                        childIndex = childIndex + 1;
                }
                T root = heapArray.get(index - 1);
                T child = heapArray.get(childIndex - 1);
                if (((Comparable<T>) root).compareTo(child) > 0) {
                    exch(index - 1, childIndex - 1);
                    bubbleDown(childIndex);
                } else {
                    return;
                }
            }
        }

        private void exch(int from, int to) {
            T a = heapArray.get(from);
            T b = heapArray.get(to);
            heapArray.remove(from);
            heapArray.add(from, b);
            heapArray.remove(to);
            heapArray.add(to, a);
        }
    }
}


