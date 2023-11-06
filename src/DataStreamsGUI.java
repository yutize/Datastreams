import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchTextField;
    private Path currentFilePath;

    public DataStreamsGUI() {
        setTitle("Java Data Stream Processing");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        searchTextField = new JTextField();
        JButton loadButton = new JButton("Load a File");
        JButton searchButton = new JButton("Search the File");
        JButton quitButton = new JButton("Quit");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    currentFilePath = fileChooser.getSelectedFile().toPath();
                    try {
                        Stream<String> lines = Files.lines(currentFilePath);
                        originalTextArea.setText(lines.collect(Collectors.joining("\n")));
                        lines.close();
                        searchButton.setEnabled(true);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = searchTextField.getText();
                try {
                    Stream<String> lines = Files.lines(currentFilePath);
                    Stream<String> filteredLines = lines.filter(line -> line.contains(search));
                    filteredTextArea.setText(filteredLines.collect(Collectors.joining("\n")));
                    lines.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error searching the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);

        JPanel textAreaPanel = new JPanel(new GridLayout(1, 2));
        textAreaPanel.add(new JScrollPane(originalTextArea));
        textAreaPanel.add(new JScrollPane(filteredTextArea));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Enter Search String: "), BorderLayout.WEST);
        searchPanel.add(searchTextField, BorderLayout.CENTER);

        add(searchPanel, BorderLayout.NORTH);
        add(textAreaPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DataStreamsGUI gui = new DataStreamsGUI();
                gui.setVisible(true);
            }
        });
    }
}
