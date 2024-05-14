package org.healthtracker.main;

import org.healthtracker.db.DatabaseHelper;
import org.healthtracker.exceptions.InvalidNumericValueException;
import org.healthtracker.exceptions.InvalidTextValueException;
import org.healthtracker.tracking.*;
import org.healthtracker.user.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.List;
/**
 * Main GUI class for the Health Tracker application.
 * This class sets up the main window and its various panels for different functionalities.
 */
public class HealthTrackerGUI extends JFrame {
    private DatabaseHelper dbHelper = new DatabaseHelper();
    private User loggedInUser;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
    private int userId;
    /**
     * Constructor for the Health Tracker GUI.
     * Sets up the JFrame properties and initializes the components.
     */
    public HealthTrackerGUI() {
        setTitle("Health Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeComponents();
        setContentPane(cards);
        setFont(new Font("Poppins", Font.PLAIN, 20));
    }
    /**
     * Initializes all components and panels within the JFrame.
     */
    private void initializeComponents() {
        cards.add(loginPanel(), "Login");
        cards.add(registerPanel(), "Register");
        cards.add(activityPanel(), "Activities");
        cards.add(summaryPanel(), "Summary");
        cards.add(resetPanel(), "Reset"); // Add the resetPanel to the cards panel
        cardLayout.show(cards, "Login");
    }
    /**
     * Creates and returns a login panel.
     * return JPanel that contains login form elements.
     */

    private JPanel loginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Fitness Tracker App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.add(titleLabel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setOpaque(false);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30)); // Adjust the size
        usernameField.setMargin(new Insets(5, 5, 5, 5)); // Add margin for better appearance

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30)); // Adjust the size
        passwordField.setMargin(new Insets(5, 5, 5, 5)); // Add margin for better appearance

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30)); // Adjust the size
        loginButton.setBackground(new Color(252, 196, 60)); // Set button color
        loginButton.setForeground(Color.WHITE); // Set text color
        loginButton.addActionListener(e -> {
            try {
                if (dbHelper.validateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                    loggedInUser = new User(1, usernameField.getText(), new String(passwordField.getPassword()), dbHelper);
                    cardLayout.show(cards, "Activities");
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 30)); // Adjust the size
        registerButton.setBackground(new Color(0x77B255));  // Set button color
        registerButton.setForeground(Color.WHITE); // Set text color
        registerButton.addActionListener(e -> cardLayout.show(cards, "Register"));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formPanel.add(inputPanel);
        formPanel.add(buttonPanel);

        panel.add(formPanel, BorderLayout.CENTER);

        // Add the image
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("login.gif"));
        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel, BorderLayout.NORTH);

//        ImageIcon iii1 = new ImageIcon(ClassLoader.getSystemResource("bg.jpg"));
//        Image iii2 = iii1.getImage().getScaledInstance(850,480,Image.SCALE_DEFAULT);
//        ImageIcon iii3 = new ImageIcon(iii2);
//        JLabel iiimage = new JLabel(iii3);
//        iiimage.setBounds(0,0,850,480);
//        add(iiimage);


        setLayout(null);
        setSize(650,400);
        setLocation(450,200);
        setUndecorated(true);
        setVisible(true);

        return panel;
    }


    private void validateRegistrationInput(String username, String password, String name, String ageString, String weightString, String heightString) throws InvalidTextValueException, InvalidNumericValueException {
        // Check if name contains non-alphabetic characters
        if (!name.matches("^[a-zA-Z]+$")) {
            throw new InvalidTextValueException("Name must contain only letters.");
        }

        // Check if age is a valid integer
        try {
            int age = Integer.parseInt(ageString);
            if (age <= 0) {
                throw new InvalidNumericValueException("Age must be a positive integer.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Age must be a valid integer.");
        }

        // Check if weight is a valid numeric value
        try {
            double weight = Double.parseDouble(weightString);
            if (weight <= 0) {
                throw new InvalidNumericValueException("Weight must be a positive number.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Weight must be a valid number.");
        }

        // Check if height is a valid numeric value
        try {
            double height = Double.parseDouble(heightString);
            if (height <= 0) {
                throw new InvalidNumericValueException("Height must be a positive number.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Height must be a valid number.");
        }
    }

    private JPanel registerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setOpaque(false);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField heightField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Gender:"));
        inputPanel.add(genderComboBox);
        inputPanel.add(new JLabel("Weight (kg):"));
        inputPanel.add(weightField);
        inputPanel.add(new JLabel("Height (cm):"));
        inputPanel.add(heightField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0x77B255));
        registerButton.setPreferredSize(new Dimension(120, 30));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> {
            try {
                // Validate input
                validateRegistrationInput(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        nameField.getText(),
                        ageField.getText(),
                        weightField.getText(),
                        heightField.getText()
                );

                // Perform registration
                User.register(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        genderComboBox.getSelectedItem().toString().charAt(0),
                        Double.parseDouble(weightField.getText()),
                        Double.parseDouble(heightField.getText()),
                        dbHelper
                );
                JOptionPane.showMessageDialog(panel, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cards, "Login");
            } catch (InvalidTextValueException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage() + " Enter text value only.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidNumericValueException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage() + " Enter numeric value only.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Login");
        backButton.setBackground(new Color(0xFCC43C));
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(cards, "Login"));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        formPanel.add(inputPanel);
        formPanel.add(buttonPanel);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates and returns an activities panel for logging various health activities.
     * return JPanel that contains interfaces for different activity logging.
     */
    private JPanel activityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10)); // Vertical layout with gaps

        // Diet tracking
        JPanel dietPanel = new JPanel(new FlowLayout());
        dietPanel.setBackground(Color.WHITE); // Set background color
        JTextField foodField = new JTextField(10);
        JTextField caloriesField = new JTextField(10);
        JButton logDietButton = new JButton("Log Diet");
        logDietButton.setBackground(new Color(34, 139, 34)); // Set button background color
        logDietButton.setForeground(Color.WHITE); // Set button text color
        logDietButton.addActionListener(e -> {
            try {
                int userId = loggedInUser.getId();
                new DietActivity(foodField.getText(), Integer.parseInt(caloriesField.getText()), loggedInUser).track();
                JOptionPane.showMessageDialog(this, "Diet logged successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to log diet activity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dietPanel.add(new JLabel("Food:"));
        dietPanel.add(foodField);
        dietPanel.add(new JLabel("Calories:"));
        dietPanel.add(caloriesField);
        dietPanel.add(logDietButton);
        panel.add(dietPanel);

        // Exercise tracking
        JPanel exercisePanel = new JPanel(new FlowLayout());
//        exercisePanel.setBackground(new Color(0xFCC43C)); // Set background color
        JTextField exerciseTypeField = new JTextField(10);
        JTextField durationField = new JTextField(10);
        JTextField caloriesBurnedField = new JTextField(10);
        exercisePanel.setBackground(Color.WHITE); // Set background color
        JButton logExerciseButton = new JButton("Log Exercise");
        logExerciseButton.setBackground(new Color(34, 139, 34)); // Set button background color
        logExerciseButton.setForeground(Color.WHITE); // Set button text color
        logExerciseButton.addActionListener(e -> {
            try {
                String exerciseType = exerciseTypeField.getText();
                int duration = Integer.parseInt(durationField.getText());
                int caloriesBurned = Integer.parseInt(caloriesBurnedField.getText());
                new ExerciseActivity(exerciseType, duration, caloriesBurned, loggedInUser).track();
                JOptionPane.showMessageDialog(this, "Exercise logged successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for duration and calories burned.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to log exercise activity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        exercisePanel.add(new JLabel("Exercise Type:"));
        exercisePanel.add(exerciseTypeField);
        exercisePanel.add(new JLabel("Duration (minutes):"));
        exercisePanel.add(durationField);
        exercisePanel.add(new JLabel("Calories Burned:"));
        exercisePanel.add(caloriesBurnedField);
        exercisePanel.add(logExerciseButton);
        panel.add(exercisePanel);

        // Step tracking
        JPanel stepPanel = new JPanel(new FlowLayout());
        stepPanel.setBackground(Color.WHITE); // Set background color
        JTextField stepsField = new JTextField(10);
        JButton logStepsButton = new JButton("Log Steps");
//        logStepsButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        logStepsButton.setBackground(new Color(34, 139, 34)); // Set button background color
//        logStepsButton.setForeground(Color.WHITE); // Set button text color
        logStepsButton.setForeground(Color.WHITE);
        logStepsButton.addActionListener(e -> {
            try {
                int steps = Integer.parseInt(stepsField.getText());
                new StepActivity(steps, loggedInUser).track();
                JOptionPane.showMessageDialog(this, "Steps logged successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for steps.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to log step activity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        stepPanel.add(new JLabel("Steps:"));
        stepPanel.add(stepsField);
        stepPanel.add(logStepsButton);
        panel.add(stepPanel);

        // Mental health tracking
        JPanel mentalHealthPanel = new JPanel(new FlowLayout());
        mentalHealthPanel.setBackground(Color.WHITE); // Set background color
        String[] emotions = {"Happy", "Neutral", "Angry", "Sad"};
        JComboBox<String> emotionComboBox = new JComboBox<>(emotions);
        JTextField sleepHoursField = new JTextField(10);
        JButton logMentalHealthButton = new JButton("Log Mental Health");
        logMentalHealthButton.setBackground(new Color(34, 139, 34)); // Set button background color
        logMentalHealthButton.setForeground(Color.WHITE); // Set button text color
        logMentalHealthButton.addActionListener(e -> {
            try {
                int mood;
                switch (emotionComboBox.getSelectedIndex()) {
                    case 0:  // Happy
                        mood = 5;
                        break;
                    case 1:  // Neutral
                        mood = 3;
                        break;
                    case 2:  // Angry
                        mood = 2;
                        break;
                    case 3:  // Sad
                        mood = 1;
                        break;
                    default:
                        mood = 3;  // Default to neutral mood
                }
                double sleepHours = Double.parseDouble(sleepHoursField.getText());
                new MentalHealthActivity("Mental Health Tracking", mood, sleepHours, loggedInUser).track();
                JOptionPane.showMessageDialog(this, "Mental health logged successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for sleep hours.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to log mental health activity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mentalHealthPanel.add(new JLabel("Emotion:"));
        mentalHealthPanel.add(emotionComboBox);
        mentalHealthPanel.add(new JLabel("Sleep Hours:"));
        mentalHealthPanel.add(sleepHoursField);
        mentalHealthPanel.add(logMentalHealthButton);
        panel.add(mentalHealthPanel);

        // Water intake tracking
        JPanel waterIntakePanel = new JPanel(new FlowLayout());
        waterIntakePanel.setBackground(Color.WHITE); // Set background color
        JTextField waterIntakeField = new JTextField(10);
        JButton logWaterIntakeButton = new JButton("Log Water Intake");
        logWaterIntakeButton.setBackground(new Color(34, 139, 34)); // Set button background color
        logWaterIntakeButton.setForeground(Color.WHITE); // Set button text color
        logWaterIntakeButton.addActionListener(e -> {
            try {
                double liters = Double.parseDouble(waterIntakeField.getText());
                new WaterIntakeActivity(liters, loggedInUser).track();
                JOptionPane.showMessageDialog(this, "Water intake logged successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for water intake.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to log water intake activity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        waterIntakePanel.add(new JLabel("Liters of Water Consumed:"));
        waterIntakePanel.add(waterIntakeField);
        waterIntakePanel.add(logWaterIntakeButton);
        panel.add(waterIntakePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton summaryButton = new JButton("View Summary");
        summaryButton.setBackground(new Color(0xFCC43C));
        summaryButton.setForeground(Color.WHITE);
        summaryButton.addActionListener(e -> cardLayout.show(cards, "Summary"));
        buttonPanel.add(summaryButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(0xFCC43C));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> cardLayout.show(cards, "Reset"));
        buttonPanel.add(resetButton);

        // Add the nested button panel to the main panel
        panel.add(buttonPanel);

        return panel;
    }


    private JPanel resetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Add GIF
        ImageIcon gifIcon = new ImageIcon(ClassLoader.getSystemResource("reset.gif"));
        Image gif = gifIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        JLabel gifLabel = new JLabel(new ImageIcon(gif));
//        gifLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel gifPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gifPanel.setBackground(Color.WHITE);
        gifPanel.add(gifLabel);
        panel.add(gifPanel, BorderLayout.SOUTH);


//        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("login.gif"));
//        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
//        JLabel imageLabel = new JLabel(new ImageIcon(image));
//        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        panel.add(imageLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton resetDataButton = new JButton("Reset My Data");
        resetDataButton.setBackground(new Color(34, 139, 34)); // Pepe green color
        resetDataButton.setForeground(Color.WHITE);
        resetDataButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset your data? This action cannot be undone.", "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                try {
                    dbHelper.resetUserData(loggedInUser);
                    JOptionPane.showMessageDialog(this, "Your data has been successfully reset.", "Reset Successful", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to reset your data.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(resetDataButton);

        // Back to Login Button
        JButton backButton = new JButton("Back to Login");
        backButton.setBackground(new Color(0xFCC43C));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(cards, "Login"));
        buttonPanel.add(backButton);

        // Add Button Panel to the center of the panel
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates and returns a summary panel for viewing the logged activities.
     * return JPanel that allows the user to select and view activity summaries.
     */
    private JPanel summaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTextArea summaryText = new JTextArea();
        summaryText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(summaryText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Dropdown to select the data category
        String[] categories = {"Diet", "Exercise", "Mental Health", "Steps", "Water Intake"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setBackground(new Color(34, 139, 34));
        categoryComboBox.setForeground(Color.WHITE);


        // Action listener to update the summary text based on the selected category
        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            try {
                // Fetch user-specific data from the database based on the selected category
                List<String> data = fetchUserSpecificData(selectedCategory, loggedInUser);
                summaryText.setText(String.join("\n", data));
            } catch (SQLException ex) {
                summaryText.setText("Failed to fetch data from the database.");
                ex.printStackTrace();
            }
        });

        // Panel to hold the category dropdown and its label
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        categoryPanel.add(new JLabel("Select Category:"), BorderLayout.WEST);
        categoryPanel.add(categoryComboBox, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("Back to Activities");
        backButton.setBackground(new Color(0xFCC43C));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(cards, "Activities"));
        buttonPanel.add(backButton);

        // Add components to the panel
        panel.add(categoryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    /**
     * Fetches data specific to a user and category from the database.
     *  category The category of data to fetch.
     *  user The user whose data is to be fetched.
     * return A list of string representations of the fetched data.
     * throws SQLException If there is an error accessing the database.
     */
    private List<String> fetchUserSpecificData(String category, User user) throws SQLException {
        String tableName = generateTableName(category, user);
        String sql = "SELECT * FROM " + tableName;
        List<String> data = new ArrayList<>();

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            // Assuming the data is being read as strings; adjust based on actual data structure
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnsNumber; i++) {
                    row.append(rsmd.getColumnName(i)).append(": ").append(rs.getString(i)).append(" | ");
                }
                data.add(row.toString());
            }
        }
        return data;
    }
    /**
     * Generates the name of the database table based on the category and user information.
     * category The category of the data.
     * user The user for whom the table name is generated.
     * return The table name.
     */
    private String generateTableName(String category, User user) {
        switch (category) {
            case "Diet":
                return "diet_" + user.getId() + "_" + user.getUsername();
            case "Exercise":
                return "exercise_" + user.getId() + "_" + user.getUsername();
            case "Mental Health":
                return "mental_health_" + user.getId() + "_" + user.getUsername();
            case "Steps":
                return "steps_" + user.getId() + "_" + user.getUsername();
            case "Water Intake":
                return "water_intake_" + user.getId() + "_" + user.getUsername();
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
    /**
     * Main method to run the GUI.
     */
    public static void main(String[] args) {
        new HealthTrackerGUI().setVisible(true);
    }
}
