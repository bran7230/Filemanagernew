// Import the FlatLightLaf library for setting up the look and feel of the UI
import com.formdev.flatlaf.FlatLightLaf;
// Import Apache PDFBox classes for PDF manipulation (reading, writing, fonts, etc.)
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
// Import standard AWT classes for GUI components and events
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// Import IO classes for file handling
import java.io.File;
import java.io.IOException;
// Import Swing components for building the GUI
import javax.swing.*;
// Import UIManager classes for custom UI properties
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
// Import classes for file system operations (Paths, Files)
import java.nio.file.*;

public class guiFile extends FileManager {

    // Define color constants for consistent theming
    private static final Color BACKGROUND_COLOR = new Color(0x2D2D2D); // Main background color
    private static final Color SECONDARY_BACKGROUND = new Color(0x3D3D3D); // Secondary background color
    private static final Color TEXT_COLOR = Color.WHITE; // Color used for text
    private static final Color ACCENT_COLOR = new Color(0x4A90E2); // Accent color for highlights

    // Instantiate FileManager (could be used for file operations)
    static FileManager fm = new FileManager();
    // Create a persistent JTextArea for displaying file contents
    private static JTextArea textArea = new JTextArea();


    public static void main(String[] args) throws InterruptedException {

        // Setup the FlatLightLaf look and feel for the UI
        FlatLightLaf.setup();
        // Customize UIManager properties for various UI elements with the chosen colors
        UIManager.put("MenuBar.background", new ColorUIResource(BACKGROUND_COLOR)); // Menu bar background
        UIManager.put("MenuBar.foreground", new ColorUIResource(TEXT_COLOR)); // Menu bar text color
        UIManager.put("PopupMenu.background", new ColorUIResource(SECONDARY_BACKGROUND)); // Popup menu background
        UIManager.put("PopupMenu.foreground", new ColorUIResource(TEXT_COLOR)); // Popup menu text color

        // Enable decorated frames to allow custom title bar styling
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the main frame (used as a backdrop with an image)
        JFrame frameMain = new JFrame();
        frameMain.setSize(1440, 1000); // Set base size of the main frame
        frameMain.setMinimumSize(new Dimension(600, 400)); // Set minimum allowed size
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setResizable(false);

        // Load an image and set it as a background image
        //image credit: openartAi
        ImageIcon image = new ImageIcon("images/openart-video_ba79f20e_1740178718915.gif");
        JLabel imageLabel = new JLabel(image);
        imageLabel.setLayout(new BorderLayout());

            // Create the primary application frame where file operations will occur
        JFrame frame = createFrame();

        // Create a "Save" menu item and apply styling to it
        JMenuItem save = new JMenuItem("Save");
        styleMenuItem(save);

        // Create and set up the menu bar for the file frame
        JMenuBar menubar = createMenuBar(frame, save);
        frame.setJMenuBar(menubar);
        frame.setLocationRelativeTo(null);
        frame.setSize(1920, 1080);
        frame.setMinimumSize(new Dimension(600, 400));

        // Configure the main frame with the background image and set window properties
        frameMain.setLocationRelativeTo(null);
        frameMain.add(imageLabel);
        frameMain.setIconImage(image.getImage());
        frameMain.setTitle("File Manager");

        // Customize title bar properties for both frames
        frameMain.getRootPane().putClientProperty("JRootPane.titleBarBackground", Color.black);
        frameMain.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.white);
        frame.getRootPane().putClientProperty("JRootPane.titleBarBackground", SECONDARY_BACKGROUND);
        frame.getRootPane().putClientProperty("JRootPane.titleBarForeground", TEXT_COLOR);
        frameMain.setVisible(true);

        // Add a mouse listener to the image label to switch frames when clicked
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frameMain.dispose(); // Close the main frame
                frame.setVisible(true); // Show the file manager frame
            }
        });
    }

    // Method to create the main file manager frame
    private static JFrame createFrame(){
        JFrame fileFrame = new JFrame("Welcome to File Manager");
        fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize frame on launch
        fileFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Set UI properties for file chooser and other components to match the theme
        UIManager.put("FileChooser.listViewBackground", SECONDARY_BACKGROUND);
        UIManager.put("FileChooser.listViewForeground", TEXT_COLOR);
        UIManager.put("FileChooser.foreground", TEXT_COLOR);
        UIManager.put("FileChooser.detailsViewBackground", SECONDARY_BACKGROUND);
        UIManager.put("FileChooser.detailsViewForeground", TEXT_COLOR);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Panel.foreground", TEXT_COLOR);
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("Button.background", BACKGROUND_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);

        return fileFrame;
    }

    // Method to create and style the menu bar for the file manager frame
    private static JMenuBar createMenuBar(JFrame frame, JMenuItem save) {
        JMenuBar menuBar = new JMenuBar();
        styleMenuBar(menuBar);
        menuBar.setLayout(new FlowLayout());
        JMenu fileMenu = new JMenu("File");
        styleMenu(fileMenu);
        JMenu wordMenu = new JMenu("Word");

        // Create menu items for file operations
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem createMenuItem = new JMenuItem("Create");
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        JMenuItem copyMenuItem = new JMenuItem("Copy");


        // Apply styling to each menu item
        styleMenuItem(openMenuItem);
        styleMenuItem(exitMenuItem);
        styleMenuItem(createMenuItem);
        styleMenuItem(deleteMenuItem);
        styleMenuItem(save);


        // Add menu items to the "File" menu in the desired order
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(createMenuItem);
        fileMenu.add(deleteMenuItem);
        fileMenu.add(save);


        // Add the "File" menu to the menu bar
        menuBar.add(fileMenu);


        // Add action listener for the "Open" menu item
        openMenuItem.addActionListener(_ -> {
            // Confirm the action with the user, warning about deletion of current on-screen text
            int verify = JOptionPane.showConfirmDialog(frame, "Are you sure you want to open file?\n Deletes current on screen text", "Open", JOptionPane.YES_NO_OPTION);
            if (verify == JOptionPane.YES_OPTION) {
                try {
                    openFileFrame(frame, save);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        // Add action listener to exit the application when "Exit" is clicked
        exitMenuItem.addActionListener(_ -> System.exit(0));
        // Add action listener for creating a file
        createMenuItem.addActionListener(_ -> createFileframe());
        // Add action listener for deleting a file
        deleteMenuItem.addActionListener(_ -> deleteFileframe());

        return menuBar;
    }

    // Method to create a frame for file creation input
    public static JFrame createFileframe(){
        JFrame createframe = new JFrame("Enter name for a File you want to create");
        createframe.setSize(600, 400);
        createframe.setLocationRelativeTo(null);

        // Set layout and background for the container
        Container container = createframe.getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(BACKGROUND_COLOR);

        // Create a text field for entering the file name and style it
        JTextField textField = new JTextField(20);
        styleTextField(textField);

        // Create a combo box for selecting the file type (PDF or TXT)
        String[] fileTypes = {"PDF", "TXT"};
        JComboBox<String> fileTypeComboBox = new JComboBox<>(fileTypes);

        // Create a submit button and style it
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);

        // Add action listener to the submit button to process file creation
        submitButton.addActionListener(_ -> {
            String name = textField.getText();
            String fileType = (String) fileTypeComboBox.getSelectedItem();
            String filePath = "fileLocations/" + name + "." + fileType.toLowerCase();
            try {
                if (Files.exists(Path.of(filePath))) {
                    // Alert if the file already exists
                    JOptionPane.showMessageDialog(null, "File already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    createframe.dispose();
                } else {
                    // Create a PDF or TXT file based on the selection
                    if ("PDF".equals(fileType)) {
                        createPDF(filePath);
                    } else if ("TXT".equals(fileType)) {
                        File file = new File(filePath);
                        if (file.createNewFile()) {
                            System.out.println("File created");
                        } else {
                            System.out.println("File already exists");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            createframe.dispose();
        });

        // Add components to the container
        container.add(textField);
        container.add(fileTypeComboBox);
        container.add(submitButton);
        createframe.setVisible(true);
        return createframe;
    }

    // Method to create a frame for opening an existing file and displaying its content
    public static JFrame openFileFrame(JFrame frame, JMenuItem save) throws IOException {
        // Clear existing content and reset the text area
        frame.getContentPane().removeAll();
        textArea.setText("");
        styleTextArea(textArea);

        // Add the text area within a scroll pane for better usability
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);

        // Set up a file chooser dialog for selecting a file to open
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.updateUI();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("fileLocations"));

        // If the user approves a file selection, process the file
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            frame.setTitle(file.getName());
            try {
                // Read file content differently if it is a PDF or a TXT file
                if (file.getName().endsWith(".pdf")) {
                    textArea.setText(readPDF(file));
                }

                else if (file.getName().endsWith(".txt")) {
                    textArea.setText(fm.readFile(String.valueOf(file)));
                }

            } catch(IOException e) {
                textArea.setText("Error reading file " + e.getMessage());
            }
        }
        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

        // Remove any existing action listeners from the save menu item before adding a new one
        for (ActionListener listener : save.getActionListeners()) {
            save.removeActionListener(listener);
        }

        // Add an action listener to the save menu item to write the content back to the file
        save.addActionListener(_ -> {
            String content = textArea.getText().replaceAll("[\\u0000-\\u001F]", ""); // Remove control characters

            // If saving to a PDF, create a new PDF document and write the content using PDFBox
            if (file.getName().endsWith(".pdf")) {
                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    PDPageContentStream contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, 750);
                    // Load a font from file for the PDF text
                    PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\brand\\IdeaProjects\\Filemanager\\fonts\\dejavu-sans\\ttf\\DejaVuSans.ttf"));
                    contentStream.setFont(font, 12); // Set font size
                    contentStream.showText(content);
                    contentStream.endText();
                    contentStream.close();
                    document.save(file);
                } catch (IOException e) {

                    throw new RuntimeException(e);

                }

            }
            else if (file.getName().endsWith(".txt")) {
                // For .txt files
                try {
                    fm.writeFile(String.valueOf(file), content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        // Refresh the frame to show updated content
        frame.revalidate();
        frame.repaint();
        return frame;
    }

    // Method to create a frame for deleting a file
    public static JFrame deleteFileframe(){
        JFrame deleteFileframe = new JFrame("Delete a file");
        deleteFileframe.setSize(800, 600);
        deleteFileframe.setLocationRelativeTo(null);
        deleteFileframe.getContentPane().setBackground(BACKGROUND_COLOR);

        Container container = deleteFileframe.getContentPane();
        container.setLayout(new FlowLayout());

        // Label prompting the user to enter the file name
        JLabel label = new JLabel("Enter a file name: ");
        label.setForeground(TEXT_COLOR);

        // Text field for user input and apply styling
        JTextField textField = new JTextField(20);
        styleTextField(textField);

        // Submit button for confirming file deletion
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);

        // When the submit button is clicked, open a confirmation dialog
        submitButton.addActionListener(e -> createConfirmationDialog(textField.getText()));

        // Add components to the container
        container.add(label);
        container.add(textField);
        container.add(submitButton);

        deleteFileframe.setVisible(true);
        return deleteFileframe;
    }

    // Method to create a confirmation dialog before file deletion
    private static void createConfirmationDialog(String filename) {
        JFrame choiceframe = new JFrame("Are You Sure?");
        choiceframe.setSize(600, 400);
        choiceframe.setLocationRelativeTo(null);
        choiceframe.getContentPane().setBackground(BACKGROUND_COLOR);
        choiceframe.setLayout(new FlowLayout());

        // Non-editable text field displaying a warning message
        JTextField field = new JTextField(50);
        field.setText("Action is permanent and cannot be undone");
        styleTextField(field);
        field.setEditable(false);

        // YES and NO buttons for confirming or cancelling the action
        JButton yesButton = new JButton("YES");
        JButton noButton = new JButton("NO");
        styleButton(yesButton);
        styleButton(noButton);
        yesButton.setFocusPainted(false);
        noButton.setFocusPainted(false);

        // If "YES" is clicked, delete the file and notify the user
        yesButton.addActionListener(_ -> {
            try {
                fm.deleteFile("fileLocations/" + filename);
                JOptionPane.showMessageDialog(choiceframe, "File Deleted Successfully");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            choiceframe.dispose();
        });

        // If "NO" is clicked, simply close the dialog
        noButton.addActionListener(_ -> choiceframe.dispose());

        // Add components to the dialog frame and display it
        choiceframe.add(field);
        choiceframe.add(yesButton);
        choiceframe.add(noButton);
        choiceframe.setVisible(true);
    }

    // Style helper methods for a consistent UI appearance

    // Style the menu bar with background and foreground colors
    private static void styleMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(SECONDARY_BACKGROUND);
        menuBar.setForeground(TEXT_COLOR);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
    }

    // Style the individual menu with background and text colors
    private static void styleMenu(JMenu menu) {
        menu.setBackground(SECONDARY_BACKGROUND);
        menu.setForeground(TEXT_COLOR);
        menu.setBorder(BorderFactory.createEmptyBorder());
    }

    // Style each menu item with background, text color, and border
    private static void styleMenuItem(JMenuItem item) {
        item.setBackground(SECONDARY_BACKGROUND);
        item.setForeground(TEXT_COLOR);
        item.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));
    }

    // Style buttons with the accent color, text color, and padding
    private static void styleButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    // Style text fields with background color, text color, and a custom border
    private static void styleTextField(JTextField textField) {
        textField.setBackground(BACKGROUND_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    // Style the text area with background, text color, padding, and a monospaced font
    private static void styleTextArea(JTextArea textArea) {
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setForeground(TEXT_COLOR);
        textArea.setCaretColor(TEXT_COLOR);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    }
}
