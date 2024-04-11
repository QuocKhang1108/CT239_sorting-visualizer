import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainVisualizer {
    private static final int paddingMainVisualizer = 10;
    private Column[] columns;

    //each unit height is equivalent to 6,
    // the greatest height is 100<=>606, the smallest height is 0<=>6
    private static final int maxRandom = 100, minRandom = 0;
    private Integer[] arr;
    private int capacity, speed;
    private long startTime, totalTime;
    private int comparisons, swaps;
    private VisualizerListener listener;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;
    private boolean hasArray;

    public MainVisualizer(int capacity, int fps, VisualizerListener listener) {
        this.capacity = capacity;
        this.speed = 1000 / fps;
        this.listener = listener;
        bufferStrategy = listener.getBufferStrategy();
        hasArray = false;
    }

    private void errorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /////////////////////////////////////////DRAW////////////////////////////////////////////
    public void createArray(String stringArr, int widthCanvas, int heightCanvas) {
        stringArr = stringArr.trim();
        if (stringArr.endsWith(",")) {
            stringArr = stringArr.substring(0, stringArr.length() - 1);
        }

        startTime = totalTime = comparisons = swaps = 0;
        listener.updateInformation(totalTime, comparisons, swaps);
        arr = new Integer[capacity];
        columns = new Column[capacity];

        String[] strArr = stringArr.split(",");
        Pattern regEx = Pattern.compile("^(100|[1-9]?[0-9])(\\s*,\\s*(100|[1-9]?[0-9]))*$");

        if (stringArr.isEmpty()) {
            errorMessage("Array creation error!", "Array is empty!");
            hasArray = false;
        } else if (!regEx.matcher(stringArr).matches()) {
            errorMessage("Array creation error!", "Invalid array format. Ex: 1,2,3,...");
            hasArray = false;
        } else if (strArr.length != capacity) {
            errorMessage("Array creation error!", "Array length must be " + capacity);
            hasArray = false;
        } else {
            double x = paddingMainVisualizer, y = heightCanvas - paddingMainVisualizer;
            double width = (double) (widthCanvas - paddingMainVisualizer * 2) / capacity;

            graphics = bufferStrategy.getDrawGraphics();
            graphics.setColor(CustomColor.canvasBackground);
            graphics.fillRect(0, 0, widthCanvas, heightCanvas);

            for (int i = 0; i < arr.length; i++) {
                arr[i] = Integer.parseInt(strArr[i].trim());
                Column column = new Column((int) x, (int) y, (int) width, arr[i], CustomColor.originalColor);
                column.draw(graphics);
                columns[i] = column;
                x += width;
            }
            hasArray = true;

            listener.updateArray(arr);
            bufferStrategy.show();
            graphics.dispose();

        }
    }

    public void createRandomArray(int widthCanvas, int heightCanvas) {
        startTime = totalTime = comparisons = swaps = 0;
        listener.updateInformation(totalTime, comparisons, swaps);
        arr = new Integer[capacity];
        columns = new Column[capacity];
        hasArray = true;

//        define layout space for the columns, have padding = 10
        double x = paddingMainVisualizer, y = heightCanvas - paddingMainVisualizer;
//        define the width of all columns based on the capacity
        double width = (double) (widthCanvas - paddingMainVisualizer * 2) / capacity;

        graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(CustomColor.canvasBackground);
        graphics.fillRect(0, 0, widthCanvas, heightCanvas);

        Random random = new Random();
        Column column;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(maxRandom) + minRandom;
            column = new Column((int) x, (int) y, (int) width, arr[i], CustomColor.originalColor);
            column.draw(graphics);
            columns[i] = column;
            x += width;
        }
        listener.updateArray(arr);
        bufferStrategy.show();
        graphics.dispose();
    }

    private void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawColorColumn(int i, Color color) {
        columns[i].clear(graphics);
        columns[i].setColor(color);
        columns[i].draw(graphics);
        bufferStrategy.show();
    }

    public void drawColorComparingColumns(int i, int j, Color color) {
        Color color1 = columns[i].getColor();
        Color color2 = columns[j].getColor();

        drawColorColumn(i, color);
        drawColorColumn(j, color);

        delay();

        drawColorColumn(i, color1);
        drawColorColumn(j, color2);
    }

    private void scanAllColumns() {
        for (int i = 0; i < arr.length; i++) {
            drawColorColumn(i, CustomColor.scanColor);
            drawColorColumn(i, CustomColor.sortedColor);
        }
    }

    /////////////////////////////////////////*DRAW*////////////////////////////////////////////
    ////////////////////////////////ALGORITHMS VISUALIZER////////////////////////////////////
    private void swap(int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;

        columns[i].clear(graphics);
        columns[j].clear(graphics);

        columns[j].setHeight(columns[i].getHeight());
        columns[i].setHeight(temp);

        drawColorComparingColumns(i, j, CustomColor.swappingColor);
    }

    public void bubbleSort() {
        if (hasArray) {
            graphics = bufferStrategy.getDrawGraphics();

            startTime = System.currentTimeMillis();

            comparisons = 0;
            swaps = 0;

            for (int i = 0; i < arr.length - 1; i++) {
                int j;
                for (j = arr.length-1; j > i; j--) {
                    drawColorComparingColumns(j, j - 1, CustomColor.comparingColor);
                    comparisons++;
                    if (arr[j] < arr[j - 1]) {
                        swaps++;
                        swap(j, j - 1);
                    }
                }
                drawColorColumn(i, CustomColor.sortedColor);
            }
            long tempTime = System.currentTimeMillis();
            totalTime = tempTime - startTime;

            scanAllColumns();

            graphics.dispose();

            listener.updateInformation(totalTime, comparisons, swaps);
        } else errorMessage("Array creation error!", "Let's create array!!!");
    }

    public void insertSort() {
        if (hasArray) {
            graphics = bufferStrategy.getDrawGraphics();

            startTime = System.currentTimeMillis();

            comparisons = 0;
            swaps = 0;

            for (int i = 1; i < arr.length; i++) {
                int key = arr[i];
                int j = i - 1;
                drawColorColumn(i, CustomColor.sortedColor);
                drawColorColumn(j, CustomColor.sortedColor);

                while (j >= 0) {
                    drawColorComparingColumns(j, j + 1, CustomColor.comparingColor);
                    comparisons++;

                    if (arr[j] > key) {
                        swap(j, j + 1);
                        swaps++;
                    } else {
                        break;
                    }
                    j--;
                }
            }
            long tempTime = System.currentTimeMillis();
            totalTime = tempTime - startTime;

            scanAllColumns();

            graphics.dispose();

            listener.updateInformation(totalTime, comparisons, swaps);
        } else errorMessage("Array creation error!", "Let's create array!!!");
    }

    public void selectionSort() {
        if (hasArray) {
            graphics = bufferStrategy.getDrawGraphics();

            startTime = System.currentTimeMillis();

            comparisons = 0;
            swaps = 0;
            int i;
            for (i = 0; i < arr.length - 1; i++) {
                int minIndex = i;
                int min = arr[i];

                for (int j = i + 1; j < arr.length; j++) {
                    drawColorComparingColumns(minIndex, j, CustomColor.comparingColor);
                    comparisons++;

                    if (arr[j] < min) {
                        minIndex = j;
                        min = arr[j];
                    }

                }

                if (minIndex != i) {
                    swap(i, minIndex);
                    swaps++;
                }
                drawColorColumn(i, CustomColor.sortedColor);
            }
            drawColorColumn(i, CustomColor.sortedColor);

            long tempTime = System.currentTimeMillis();
            totalTime = tempTime - startTime;

            scanAllColumns();

            graphics.dispose();

            listener.updateInformation(totalTime, comparisons, swaps);
        } else errorMessage("Array creation error!", "Let's create array!!!");
    }

    public void mergeSort() {
        if (hasArray) {
            graphics = bufferStrategy.getDrawGraphics();

            startTime = System.currentTimeMillis();

            comparisons = 0;
            swaps = 0;

            mergeSort(0, arr.length - 1);

            long tempTime = System.currentTimeMillis();
            totalTime = tempTime - startTime;

//            scanAllColumns();

            graphics.dispose();

            listener.updateInformation(totalTime, comparisons, swaps);
        } else errorMessage("Array creation error!", "Let's create array!!!");
    }

    private void mergeSort(int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            //sort first and second halves
            mergeSort(l, m);
            mergeSort(m + 1, r);

            //merge the sorted halves
            merge(l, m, r);
        }
    }

    private void merge(int l, int m, int r) {
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
            drawColorColumn(k, CustomColor.comparingColor);
            comparisons++;

            columns[k].clear(graphics);

            if (L[i] <= R[j]) {
                arr[k] = L[i];
                columns[k].setHeight(L[i]);
                i++;
            } else {
                arr[k] = R[j];
                columns[k].setHeight(R[j]);
                j++;
            }
            delay();
            drawColorColumn(k, CustomColor.sortedColor);

            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            columns[k].clear(graphics);
            columns[k].setHeight(L[i]);
            drawColorColumn(k, CustomColor.sortedColor);

            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            columns[k].clear(graphics);
            columns[k].setHeight(R[j]);
            drawColorColumn(k, CustomColor.sortedColor);

            j++;
            k++;
        }
    }

    public void quickSort() {
        if (hasArray) {
            graphics = bufferStrategy.getDrawGraphics();

            startTime = System.currentTimeMillis();

            comparisons = 0;
            swaps = 0;

            quickSort(0, arr.length - 1);

            long tempTime = System.currentTimeMillis();
            totalTime = tempTime - startTime;

            scanAllColumns();

            graphics.dispose();

            listener.updateInformation(totalTime, comparisons, swaps);
        } else errorMessage("Array creation error!", "Let's create array!!!");
    }

    private void quickSort(int start, int end) {
        if (start < end) {
            int pivot = partition(start, end);

            drawColorColumn(pivot, CustomColor.pivotColor);

            quickSort(start, pivot - 1);
            for (int i = start; i <= pivot - 1; i++) {
                drawColorColumn(i, CustomColor.scanColor);
                drawColorColumn(i, CustomColor.sortedColor);
            }

            quickSort(pivot + 1, end);
            for (int i = pivot + 1; i <= end; i++) {
                drawColorColumn(i, CustomColor.scanColor);
                drawColorColumn(i, CustomColor.sortedColor);
            }

        }
    }

    private int partition(int start, int end) {
        int pivot = arr[end];
        int i = start - 1;
        for (int j = start; j < end; j++) {
            drawColorComparingColumns(j, end, CustomColor.comparingColor);
            comparisons++;

            if (arr[j] < pivot) {
                i++;
                if (i != j) {
                    swap(i, j);
                    swaps++;
                }
            }
        }
        if (i + 1 != end && !Objects.equals(arr[i + 1], arr[end])) {
            swap(i + 1, end);
            swaps++;
        }
        return i + 1;
    }
////////////////////////////////*ALGORITHMS VISUALIZER*//////////////////////////////////

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFPS(int fps) {
        this.speed = 1000 / fps;
    }

    public interface VisualizerListener {
        void updateInformation(long totalTime, int comparisons, int swaps);

        void updateArray(Integer[] arr);

        BufferStrategy getBufferStrategy();
    }
}
