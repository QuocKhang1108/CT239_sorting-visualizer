import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ComparisonFrame extends JFrame {
    private static final int widthFrame = 480, heightFrame = 300;

    public ComparisonFrame() {
        super();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMaximumSize(new Dimension(widthFrame, heightFrame + 50));
        setMinimumSize(new Dimension(widthFrame, heightFrame));
        setPreferredSize(new Dimension(widthFrame, heightFrame + 20));
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        setTitle("Algorithm Comparisons");

        initialize();
    }

    public void initialize() {
        String[] columnNames = {"Algorithm", "Comparisons", "Swaps"};
        Object[][] data = {
                {"Bubble Sort", Algorithm.getComparisonsBubbleSort(), Algorithm.getSwapsBubbleSort()},
                {"Selection Sort", Algorithm.getComparisonsSelectionSort(), Algorithm.getSwapsSelectionSort()},
                {"Insertion Sort", Algorithm.getComparisonsInsertionSort(), Algorithm.getSwapsInsertionSort()},
                {"Merge Sort", Algorithm.getComparisonsMergeSort(), "-"},
                {"Quick Sort", Algorithm.getComparisonsQuickSort(), Algorithm.getSwapsQuickSort()}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Center align data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setRowHeight(heightFrame/5);
        table.getTableHeader().setReorderingAllowed(false);

        // Set background color
        table.setBackground(CustomColor.btnPressed);
        // Set header background color
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(CustomColor.btnPressed);
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.setShowGrid(false);
        table.setForeground(CustomColor.textSelected);
        table.setFont(new Font("Arial", Font.BOLD, 18));

        add(table.getTableHeader(), BorderLayout.PAGE_START);
        add(table, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }
}