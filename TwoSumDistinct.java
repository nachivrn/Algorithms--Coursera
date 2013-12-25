import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Set;

/** compute the number of target values t in the interval [2500,4000]
 * (inclusive) such that there are distinct numbers x,y in the input
 * file that satisfy x+y=t.
 */

public class TwoSumDistinct {

    static HashMap<Integer, Integer> integerMap = new HashMap<Integer, Integer>();

    static boolean computeTwoSum(int target) {
        Set<Integer> keys = integerMap.keySet();
        for (int key : keys) {
            if (integerMap.containsKey(target - key) && (target - key) != key) {
                return true;
            }
        }
        return false;
    }

    static void readInput(File input) throws Exception {
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String str;
        while ((str = br.readLine()) != null) {
            if (Integer.parseInt(str) <= 4000)
                integerMap.put(Integer.parseInt(str), 0);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java TwoSumDistinct <input.txt>");
            System.exit(1);
        }
        readInput(new File(args[0]));
        int distinctValues = 0;
        for (int target = 2500; target <= 4000; target++) {
            if (computeTwoSum(target)) {
                distinctValues++;
            }
        }
        System.out.println("Number of distinct target values " + distinctValues);
    }
}
