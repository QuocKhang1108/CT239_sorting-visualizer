import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;


public class MainFrame extends JFrame implements BtnPanel.SortButtonListener, CustomCanvas.CanvasListener, MainVisualizer.VisualizerListener, PropertyChangeListener, ChangeListener {
    private static final int widthFrame = 1280, heightFrame = 800;
    private JPanel mainPanel, sliderPanel, dataLabel;
    private BtnPanel buttonPanel;
    private CustomCanvas canvas;
    private static final int widthCanvas = widthFrame - 250 - 14, heightCanvas = heightFrame - 150;
    private MainVisualizer mainVisualizer;
    private static final int CAPACITY = 20, FPS = 200;
    private JLabel capacityLabel, fpsLabel, timeLabel, comparingLabel, swappingLabel;
    private JFormattedTextField capacityField;
    private JSlider fpsSlider;
    private JTextArea inputArea;

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
    }

    private void initialize() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(CustomColor.mainBackground);
        add(mainPanel);

        capacityLabel = new JLabel("Capacity (3-50):");
        capacityLabel.setForeground(CustomColor.text);
        capacityLabel.setFont(new Font(null, Font.BOLD, 15));
        capacityLabel.setBounds(10, 20, 125, 20);
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
        capacityField.setForeground(CustomColor.textInput);
        capacityField.setBackground(CustomColor.boxInput);
//        capacityField.setCaretColor(CustomColor.red);
        capacityField.setBorder(null);
//        capacityField.setBorder(BorderFactory.createLineBorder(CustomColor.mainBackground));
        capacityField.addPropertyChangeListener("value", this);
        capacityField.setBounds(135, 20, 50, 20);
        capacityLabel.setLabelFor(capacityField);
        mainPanel.add(capacityField);

//        inputPanel = new JPanel(new GridLayout(2, 0));
//        inputPanel.add(capacityLabel);
//        inputPanel.add(capacityField);
//        inputPanel.setBackground(CustomColor.mainBackground);
//        inputPanel.setBounds(50, 20, 200, 50);
//        mainPanel.add(inputPanel);

        fpsLabel = new JLabel("Speed");
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
        sliderPanel.setBounds(10, 50, 230, 100);
        mainPanel.add(sliderPanel);

        inputArea = new JTextArea();
        inputArea.setBounds(10, 150, 230, 190);
        inputArea.setFont(new Font(null, Font.ITALIC, 15));
        inputArea.setToolTipText("Enter the array to sort");
        inputArea.setForeground(CustomColor.textInput);
        inputArea.setLineWrap(true);
        inputArea.setBackground(CustomColor.boxInput);
        inputArea.setBorder(null);
        mainPanel.add(inputArea);

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


    public void sortButtonClicked(int id) {
        if (id == 0) mainVisualizer.createArray(inputArea.getText());
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
        timeLabel.setText("Total Time: " + (totalTime / 1000.0)+"s");
        comparingLabel.setText("Comparisons: " + comparisons);
        swappingLabel.setText("Swaps: " + swaps);
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
