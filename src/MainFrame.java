import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.text.NumberFormat;


public class MainFrame extends JFrame implements BtnPanel.SortButtonListener, CustomCanvas.CanvasListener, MainVisualizer.VisualizerListener, PropertyChangeListener, ChangeListener {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem, saveMenuItem, exitMenuItem;
    private JFileChooser fileChooser;
    private static final int widthFrame = 1280, heightFrame = 800;
    private JPanel mainPanel, sliderPanel, dataLabel;
    private BtnPanel buttonPanel;
    private CustomCanvas canvas;
    private static final int widthCanvas = widthFrame - 250 - 14, heightCanvas = heightFrame - 150;
    private MainVisualizer mainVisualizer;
    private static final int CAPACITY = 20, FPS = 200;
    private JLabel capacityLabel, fpsLabel, arrayLabel, timeLabel, comparingLabel, swappingLabel;
    private JFormattedTextField capacityField;
    private JSlider fpsSlider;
    private JTextArea inputArea;
    private JScrollPane scrollPane;

    public MainFrame() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(widthFrame, heightFrame + 50));
        setMinimumSize(new Dimension(widthFrame, heightFrame));
        setPreferredSize(new Dimension(widthFrame, heightFrame + 20));
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Sorting Visualizer");

        initialize();
        addMenuBar();
    }

    private void initialize() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(CustomColor.mainBackground);
        add(mainPanel);

        capacityLabel = new JLabel("Capacity:");
        capacityLabel.setForeground(CustomColor.text);
        capacityLabel.setFont(new Font(null, Font.BOLD, 15));
        capacityLabel.setBounds(35, 20, 125, 20);
        mainPanel.add(capacityLabel);

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        CustomFormatterTextField customFormatter = new CustomFormatterTextField(numberFormat);
        customFormatter.setValueClass(Integer.class);
        customFormatter.setMinimum(0);
        customFormatter.setMaximum(500);
        customFormatter.setAllowsInvalid(false);
        customFormatter.setCommitsOnValidEdit(true);
        capacityField = new JFormattedTextField(customFormatter);
        capacityField.setValue(CAPACITY);
//        capacityField.setColumns(3);
        capacityField.setFont(new Font(null, Font.ITALIC, 15));
        capacityField.setToolTipText("Enter the capacity of the array");
        capacityField.setCaretColor(CustomColor.text);
        capacityField.setForeground(CustomColor.textInput);
        capacityField.setBackground(CustomColor.boxInput);
//        capacityField.setCaretColor(CustomColor.red);
        capacityField.setBorder(null);
//        capacityField.setBorder(BorderFactory.createLineBorder(CustomColor.mainBackground));
        capacityField.addPropertyChangeListener("value", this);
        capacityField.setBounds(160, 20, 50, 20);
        capacityLabel.setLabelFor(capacityField);
        mainPanel.add(capacityField);

        fpsLabel = new JLabel("Speed:");
        fpsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        fpsLabel.setFont(new Font(null, Font.BOLD, 15));
        fpsLabel.setForeground(CustomColor.text);

        // slider
        fpsSlider = new JSlider(JSlider.CENTER, 5, 505, FPS);
        fpsSlider.setMajorTickSpacing(100);
        fpsSlider.setMinorTickSpacing(25);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintLabels(true);
        fpsSlider.setPaintTrack(true);
        fpsSlider.setForeground(CustomColor.text);
        fpsSlider.setBackground(CustomColor.mainBackground);
        fpsSlider.addChangeListener(this);

        sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.LINE_AXIS));
        sliderPanel.setBackground(CustomColor.mainBackground);
        sliderPanel.add(fpsLabel);
        sliderPanel.add(fpsSlider);
        sliderPanel.setBounds(10, 50, 230, 50);
        mainPanel.add(sliderPanel);

        arrayLabel = new JLabel("Array (0-100):");
        arrayLabel.setFont(new Font(null, Font.BOLD, 15));
        arrayLabel.setForeground(CustomColor.text);
        arrayLabel.setBounds(10, 105, 100, 20);
        mainPanel.add(arrayLabel);

        inputArea = new CustomTextArea();
        inputArea.setToolTipText("Enter the array to sort. Ex: 1,0,2,4,3,5..");

        scrollPane = new JScrollPane(inputArea);
        scrollPane.setBounds(10, 130, 230, 210);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CustomColor.mainBackground);
        mainPanel.add(scrollPane);

        buttonPanel = new BtnPanel(this);
        buttonPanel.setBounds(0, 350, 250, 500);
        buttonPanel.setBackground(CustomColor.mainBackground);
        mainPanel.add(buttonPanel);

        canvas = new CustomCanvas(this);
        canvas.setFocusable(false);
        canvas.setMaximumSize(new Dimension(widthCanvas, heightCanvas));
        canvas.setMinimumSize(new Dimension(widthCanvas, heightCanvas));
        canvas.setPreferredSize(new Dimension(widthCanvas, heightCanvas));
        canvas.setBounds(250, 0, widthCanvas, heightCanvas);
        mainPanel.add(canvas);
        pack();

        mainVisualizer = new MainVisualizer(CAPACITY, FPS, this);
//        mainVisualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());

        timeLabel = new JLabel("Total Time: 0.0s");
        timeLabel.setFont(new Font(null, Font.ITALIC, 25));
        timeLabel.setForeground(CustomColor.text);

        comparingLabel = new JLabel("Comparisons: 0");
        comparingLabel.setFont(new Font(null, Font.ITALIC, 25));
        comparingLabel.setForeground(CustomColor.text);

        swappingLabel = new JLabel("Swaps: 0");
        swappingLabel.setFont(new Font(null, Font.ITALIC, 25));
        swappingLabel.setForeground(CustomColor.text);

        dataLabel = new JPanel(new GridLayout(1, 0));
        dataLabel.add(timeLabel);
        dataLabel.add(comparingLabel);
        dataLabel.add(swappingLabel);
        dataLabel.setBackground(CustomColor.mainBackground);
        dataLabel.setBounds(265, 690, 1250, 30);
        mainPanel.add(dataLabel);
    }

    private void addMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");

        openMenuItem = new JMenuItem("Open file");
        saveMenuItem = new JMenuItem("Save file");
        exitMenuItem = new JMenuItem("Exit");

        openMenuItem.addActionListener(e -> {
            try {
                // Set the look and feel to 'Windows'
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            fileChooser = getFileChooser();
        });

        saveMenuItem.addActionListener(e -> {
            try {
                // Set the look and feel to 'Windows'
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            saveToFileChooser();
        });

        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private JFileChooser getFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File("./file"));
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text file (*.txt)";
            }
        });
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() == 0) {
                JOptionPane.showMessageDialog(null, "File is empty. Please select a different file.", "Error", JOptionPane.ERROR_MESSAGE);
                return fileChooser;
            }

            String firstLine = "";
            try (BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                firstLine = br.readLine(); // get the first line
                firstLine = firstLine.trim();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            String[] numbers = firstLine.split(",");    // sub string the first line to get the numbers
            capacityField.setValue(numbers.length);

            mainVisualizer.createArray(firstLine, canvas.getWidth(), canvas.getHeight()); // create the array from the file
        }
        return fileChooser;
    }

    private void saveToFileChooser() {
        if (inputArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Array has not been created. Please create an array before saving.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save to file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File("./file"));
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text file (*.txt)";
            }
        });
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
                selectedFile = new File(filePath);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) { // Open file in write mode
                bw.write(inputArea.getText());
                bw.newLine();
                bw.write("\nBubble Sort:");
                bw.write("\n        Comparisons: " + Algorithm.getComparisonsBubbleSort());
                bw.write("\n              Swaps: " + Algorithm.getSwapsBubbleSort());
                bw.write("\nSelection Sort:");
                bw.write("\n        Comparisons: " + Algorithm.getComparisonsSelectionSort());
                bw.write("\n              Swaps: " + Algorithm.getSwapsSelectionSort());
                bw.write("\nInsertion Sort:");
                bw.write("\n        Comparisons: " + Algorithm.getComparisonsInsertionSort());
                bw.write("\n              Swaps: " + Algorithm.getSwapsInsertionSort());
                bw.write("\nMerge Sort:");
                bw.write("\n        Comparisons: " + Algorithm.getComparisonsMergeSort());
                bw.write("\nQuick Sort:");
                bw.write("\n        Comparisons: " + Algorithm.getComparisonsQuickSort());
                bw.write("\n              Swaps: " + Algorithm.getSwapsQuickSort());
                bw.write("\n\n" + new java.util.Date());
                bw.write("\n--------------------------------------------------------------------------------\n");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void sortButtonClicked(int id) {
        if (id == 0) mainVisualizer.createArray(inputArea.getText(), canvas.getWidth(), canvas.getHeight());
        else if (id == 1) mainVisualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
        else if (id == 2) mainVisualizer.bubbleSort();
        else if (id == 3) mainVisualizer.selectionSort();
        else if (id == 4) mainVisualizer.insertSort();
        else if (id == 5) mainVisualizer.mergeSort();
        else if (id == 6) mainVisualizer.quickSort();
    }

    @Override
    public void onDrawArray() {
//		if (mainVisualizer != null)
//			mainVisualizer.drawArray();
    }

    @Override
    public void updateInformation(long totalTime, int comparisons, int swaps) {
        timeLabel.setText("Total Time: " + (totalTime / 1000.0) + "s");
        comparingLabel.setText("Comparisons: " + comparisons);
        swappingLabel.setText("Swaps: " + swaps);
    }

    @Override
    public void updateArray(Integer[] arr) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i];
            if (i < arr.length - 1) str += ", ";
        }
        inputArea.setText(str);
    }

    public BufferStrategy getBufferStrategy() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        if (bufferStrategy == null) {
            canvas.createBufferStrategy(2);
            bufferStrategy = canvas.getBufferStrategy();
        }
        return bufferStrategy;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int value = ((Number) capacityField.getValue()).intValue();
        mainVisualizer.setCapacity(value);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!fpsSlider.getValueIsAdjusting()) {
            int value = fpsSlider.getValue();
            mainVisualizer.setFPS(value);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);

            }
        });
    }
}
