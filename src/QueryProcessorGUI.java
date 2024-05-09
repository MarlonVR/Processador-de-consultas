import javax.swing.*;
import java.awt.*;

public class QueryProcessorGUI extends JFrame {
    public QueryProcessorGUI() {
        setTitle("SQL to Relational Algebra Converter");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JTextArea sqlInputArea = new JTextArea(10, 50);
        sqlInputArea.setLineWrap(true);
        sqlInputArea.setWrapStyleWord(true);
        JScrollPane sqlScrollPane = new JScrollPane(sqlInputArea);
        JTextArea algebraOutputArea = new JTextArea(5, 50);
        algebraOutputArea.setLineWrap(true);
        algebraOutputArea.setWrapStyleWord(true);
        algebraOutputArea.setEditable(false);
        JScrollPane algebraScrollPane = new JScrollPane(algebraOutputArea);
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(e -> {
            String sqlText = sqlInputArea.getText();
            String[] sqlLines = sqlText.split("\\n");
            SQLToAlgebraConverter.Node queryGraph = SQLToAlgebraConverter.buildQueryGraph(sqlLines, new Schema());
            String algebraExpression = SQLToAlgebraConverter.generateAlgebra(queryGraph);
            algebraOutputArea.setText(algebraExpression + "\n\n" +
            SQLToAlgebraConverter.printExecutionOrder(queryGraph));
            new DisplayGraph(queryGraph);
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(new JLabel("SQL Query:"), BorderLayout.NORTH);
        inputPanel.add(sqlScrollPane, BorderLayout.CENTER);
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(new JLabel("Relational Algebra:"), BorderLayout.NORTH);
        outputPanel.add(algebraScrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(convertButton);
        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
