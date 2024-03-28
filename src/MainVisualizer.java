import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainVisualizer {
    private static final int paddingMainVisualizer = 10;
    private Column[] columns;
    //each unit height is equivalent to 6,
    // the greatest height is 100<=>106, the smallest height is 0<=>6
    private static final int maxColumnHeight = 606, minColumnHeight = 6;

    private Integer[] arr;
    private int capacity, speed;    //capacity = number of columns
    private long startTime, totalTime;
    private int comparisons, swaps;
    private SortedColumnListener listener;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;
    private boolean hasArray, isBubbleSorted, isInsertSorted, isSelectionSorted,
            isMergeSorted, isQuickSorted, isSwapped;

    public MainVisualizer(int capacity, int fps, SortedColumnListener listener) {
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
    public void createRandomArray(int widthCanvas, int heightCanvas) {
        startTime = totalTime = comparisons = swaps = 0;
        listener.updateInformation(totalTime, comparisons, swaps);
        isSwapped = isBubbleSorted = isInsertSorted = isSelectionSorted = isMergeSorted = isQuickSorted = false;
        arr = new Integer[capacity];
        columns = new Column[capacity];
        hasArray = true;

//        define layout space for the columns, have padding = 20
        double x = paddingMainVisualizer, y = heightCanvas - paddingMainVisualizer;
//        define the width of all columns based on the capacity
        double width = (double) (widthCanvas - paddingMainVisualizer * 2) / capacity;

        graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(CustomColor.canvasBackground);
        graphics.fillRect(0, 0, widthCanvas, heightCanvas);

        Random random = new Random();
        int value;
        Column column;
        for (int i = 0; i < arr.length; i++) {
            value = random.nextInt(maxColumnHeight) + minColumnHeight;
            arr[i] = value;
            column = new Column((int) x, (int) y, (int) width, value, CustomColor.originalColor);
            column.draw(graphics);
            columns[i] = column;
            x += width;
        }
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

            delay();

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
            Algorithm.bubbleSort(arr.clone());

            comparisons = 0;
            swaps = 0;

            for (int i = 0; i < arr.length - 1; i++) {
                int j;
                for (j = 0; j < arr.length - i - 1; j++) {
                    drawColorComparingColumns(j, j + 1, CustomColor.comparingColor);
                    comparisons++;
                    if (arr[j] > arr[j + 1]) {
                        swaps++;
                        swap(j, j + 1);
                    }
                }
                drawColorColumn(j, CustomColor.sortedColor);
            }
            long tempTime = System.currentTimeMillis();
            totalTime = tempTime - startTime;

            scanAllColumns();

            graphics.dispose();

            listener.updateInformation(totalTime, comparisons, swaps);
        } else errorMessage("Let's create array!!!", "Array creation error!");
    }

    public void insertSort() {
        if (hasArray) {
            graphics = bufferStrategy.getDrawGraphics();

            startTime = System.currentTimeMillis();
            Algorithm.insertionSort(arr.clone());

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
        } else errorMessage("Let's create array!!!", "Array creation error!");
    }

    public void selectionSort() {

    }

    public void mergeSort() {

    }

    public void quickSort() {

    }
////////////////////////////////*ALGORITHMS VISUALIZER*//////////////////////////////////

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFPS(int fps) {
        this.speed = 1000 / fps;
    }

    public interface SortedColumnListener {
        void updateInformation(long totalTime, int comparisons, int swaps);

        BufferStrategy getBufferStrategy();
    }
}
