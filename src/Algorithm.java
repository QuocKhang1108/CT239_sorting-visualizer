import java.util.Objects;

public class Algorithm {
    private static int comparisonsBubbleSort, swapsBubbleSort;
    private static int comparisonsSelectionSort, swapsSelectionSort;
    private static int comparisonsInsertionSort, swapsInsertionSort;
    private static int comparisonsMergeSort;
    private static int comparisonsQuickSort, swapsQuickSort;

    public static void bubbleSort(Integer[] arr) {
        comparisonsBubbleSort = swapsBubbleSort = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                comparisonsBubbleSort++;
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapsBubbleSort++;
                }
            }
        }
    }

    public static void selectionSort(Integer[] arr) {
        comparisonsSelectionSort = swapsSelectionSort = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            int min = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                comparisonsSelectionSort++;
                if (arr[j] < min) {
                    minIndex = j;
                    min = arr[j];
                }
            }
            if (minIndex != i) {
                swap(arr, i, minIndex);
                swapsSelectionSort++;
            }
        }
    }

    public static void insertionSort(Integer[] arr) {
        comparisonsInsertionSort = swapsInsertionSort = 0;
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                comparisonsInsertionSort++;
                if (arr[j] > key) {
                    swap(arr, j, j + 1);
                    swapsInsertionSort++;
                } else {
                    break;
                }
                j--;
            }
        }
    }

    public static void mergeSort(Integer[] arr) {
        comparisonsMergeSort=0;
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(Integer[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            //sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            //merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    private static void merge(Integer[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        Integer[] L = new Integer[n1];
        Integer[] R = new Integer[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[l + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[m + 1 + j];
        }

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            comparisonsMergeSort++;

            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void quickSort(Integer[] arr) {
        comparisonsQuickSort = swapsQuickSort = 0;
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(Integer[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }

    private static int partition(Integer[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            comparisonsQuickSort++;
            if (arr[j] < pivot) {
                i++;
                if (i != j) {
                    swap(arr, i, j);
                    swapsQuickSort++;
                }
            }
        }
        if (i + 1 != high && !Objects.equals(arr[i + 1], arr[high])) {
            swap(arr, i + 1, high);
            swapsQuickSort++;
        }
        return i + 1;
    }

    private static void swap(Integer[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    public static void runAll(Integer[] arr) {
        bubbleSort(arr.clone());
        selectionSort(arr.clone());
        insertionSort(arr.clone());
        mergeSort(arr.clone());
        quickSort(arr.clone());
    }

    public static int getComparisonsBubbleSort() {
        return comparisonsBubbleSort;
    }

    public static int getSwapsBubbleSort() {
        return swapsBubbleSort;
    }

    public static int getComparisonsSelectionSort() {
        return comparisonsSelectionSort;
    }

    public static int getSwapsSelectionSort() {
        return swapsSelectionSort;
    }

    public static int getComparisonsInsertionSort() {
        return comparisonsInsertionSort;
    }

    public static int getSwapsInsertionSort() {
        return swapsInsertionSort;
    }

    public static int getComparisonsMergeSort() {
        return comparisonsMergeSort;
    }

    public static int getComparisonsQuickSort() {
        return comparisonsQuickSort;
    }

    public static int getSwapsQuickSort() {
        return swapsQuickSort;
    }
}
