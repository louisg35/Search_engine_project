package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {


    /*
     * This method takes as input an HashMap with values that are Comparable.
     * It returns an ArrayList containing all the keys from the map, ordered
     * in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort(HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());    //Start with unsorted list of urls

        int N = sortedUrls.size();
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N - i - 1; j++) {
                if (results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j + 1))) < 0) {
                    K temp = sortedUrls.get(j);
                    sortedUrls.set(j, sortedUrls.get(j + 1));
                    sortedUrls.set(j + 1, temp);
                }
            }
        }
        return sortedUrls;
    }


    /*
     * This method takes as input an HashMap with values that are Comparable.
     * It returns an ArrayList containing all the keys from the map, ordered
     * in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n*log(n)), where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<>(results.keySet());
        mergeSort(sortedUrls, 0, sortedUrls.size() - 1, results);
        return sortedUrls;
    }

    private static <K, V extends Comparable<V>> void mergeSort(ArrayList<K> sortedUrls, int left, int right,
                                                               HashMap<K, V> results) {
        if (left < right) {

            // Find the midpoint
            int mid = (left + right) / 2;

            // Sort both halves
            mergeSort(sortedUrls, left, mid, results);
            mergeSort(sortedUrls, mid + 1, right, results);

            // Merge the sorted halves
            merge(sortedUrls, left, mid, right, results);
        }
    }

    private static <K, V extends Comparable<V>> void merge(ArrayList<K> sortedUrls, int left, int mid, int right,
                                                           HashMap<K, V> results) {
        // Find sizes of the subarrays to be merged
        int list1 = mid - left + 1;
        int list2 = right - mid;

        // Create temp arrays
        ArrayList<K> temp1 = new ArrayList<>(list1);
        ArrayList<K> temp2 = new ArrayList<>(list2);

        // Copy to temp arrays
        for (int i = 0; i < list1; i++) temp1.add(i, sortedUrls.get(left + i));
        for (int j = 0; j < list2; j++) temp2.add(j, sortedUrls.get(mid + 1 + j));

        //Merge temp arrays

        int i = 0;
        int j = 0;
        int k = left;
        while (i < list1 && j < list2) {
            if (results.get(temp1.get(i)).compareTo(results.get(temp2.get(j))) >= 0) {
                sortedUrls.set(k, temp1.get(i));
                i++;
            } else {
                sortedUrls.set(k, temp2.get(j));
                j++;
            }
            k++;
        }

        while (i < list1) {
            sortedUrls.set(k, temp1.get(i));
            i++;
            k++;
        }

        while (j < list2) {
            sortedUrls.set(k, temp2.get(j));
            j++;
            k++;
        }
    }
}