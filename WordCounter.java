import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class  WordCounter extends JFrame {
    private JTextArea textArea;
    private JButton openButton;
    private JLabel wordCountLabel;
    private JTextArea mostFrequentWordsArea;
    private JLabel lineCountLabel;

    public  WordCounter() {
        setTitle("Word Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        textArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        openButton = new JButton("Open File");
        wordCountLabel = new JLabel("Word Count: ");
        mostFrequentWordsArea = new JTextArea(10, 20);
        JScrollPane frequentWordsScrollPane = new JScrollPane(mostFrequentWordsArea);
        lineCountLabel = new JLabel("Line Count: ");

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                        StringBuilder fileContent = new StringBuilder();
                        String line;
                        List<String> words = new ArrayList<>();
                        int lineCount = 0;

                        while ((line = reader.readLine()) != null) {
                            fileContent.append(line).append("\n");
                            words.addAll(Arrays.asList(line.split("\\s+")));
                            lineCount++;
                        }

                        // Word count
                        int wordCount = words.size();
                        wordCountLabel.setText("Word Count: " + wordCount);

                        // Most frequent words
                        Map<String, Integer> wordFrequency = new HashMap<>();
                        for (String word : words) {
                            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                        }
                        List<Map.Entry<String, Integer>> sortedWords = wordFrequency.entrySet()
                                .stream()
                                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                                .collect(Collectors.toList());

                        StringBuilder frequentWordsText = new StringBuilder();
                        int maxWordsToShow = 10; // You can adjust this to show more or fewer words
                        for (int i = 0; i < Math.min(maxWordsToShow, sortedWords.size()); i++) {
                            Map.Entry<String, Integer> entry = sortedWords.get(i);
                            frequentWordsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                        }
                        mostFrequentWordsArea.setText(frequentWordsText.toString());

                        // Line count
                        lineCountLabel.setText("Line Count: " + lineCount);

                        textArea.setText(fileContent.toString());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error reading the file.");
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(openButton);
        panel.add(wordCountLabel);
        panel.add(lineCountLabel);

        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BorderLayout());
        wordPanel.add(new JLabel("Most Frequent Words:"), BorderLayout.NORTH);
        wordPanel.add(frequentWordsScrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(wordPanel, BorderLayout.EAST);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new  WordCounter();
            }
        });
    }
}
