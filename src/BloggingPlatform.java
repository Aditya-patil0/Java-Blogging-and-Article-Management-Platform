import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BloggingPlatform extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<String> categoryBox;
    private JButton saveButton, publishButton;
    private Connection connection;

    public BloggingPlatform() {
        setTitle("Blogging Platform");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        topPanel.add(titleField);
        topPanel.add(new JLabel("Category:"));
        categoryBox = new JComboBox<>();
        loadCategories();
        topPanel.add(categoryBox);

        contentArea = new JTextArea();
        add(new JScrollPane(contentArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        saveButton = new JButton("Save Draft");
        publishButton = new JButton("Publish");
        bottomPanel.add(saveButton);
        bottomPanel.add(publishButton);
        add(bottomPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(new SaveAction());
        publishButton.addActionListener(new PublishAction());

        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BloggingPlatform", "root", "password");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void loadCategories() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BloggingPlatform", "root", "password");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM Categories")) {

            while (rs.next()) {
                categoryBox.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveArticle("Draft");
        }
    }

    private class PublishAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveArticle("Published");
        }
    }

    private void saveArticle(String status) {
        String title = titleField.getText();
        String content = contentArea.getText();
        String category = (String) categoryBox.getSelectedItem();

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Articles (title, content, category, status) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, category);
            stmt.setString(4, status);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Article saved as " + status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BloggingPlatform().setVisible(true));
    }
}
