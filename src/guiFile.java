import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

public class guiFile extends FileManager {

    // Color constants
    private static final Color BACKGROUND_COLOR = new Color(0x2D2D2D);
    private static final Color SECONDARY_BACKGROUND = new Color(0x3D3D3D);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(0x4A90E2);

    static FileManager fm = new FileManager();

    public static void main(String[] args) {
        JFrame frame = createFrame();
        JMenuItem save = new JMenuItem("Save");
        styleMenuItem(save);
        JMenuBar menubar = createMenuBar(frame, save);
        frame.setJMenuBar(menubar);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame(){
        JFrame fileFrame = new JFrame("Welcome to File Manager");
        fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fileFrame.getContentPane().setBackground(BACKGROUND_COLOR);
        return fileFrame;
    }

    private static JMenuBar createMenuBar(JFrame frame, JMenuItem save) {
        JMenuBar menuBar = new JMenuBar();
        styleMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        styleMenu(fileMenu);

        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem editMenuItem = new JMenuItem("Edit");
        JMenuItem createMenuItem = new JMenuItem("Create");
        JMenuItem deleteMenuItem = new JMenuItem("Delete");

        // Style all menu items
        styleMenuItem(openMenuItem);
        styleMenuItem(exitMenuItem);
        styleMenuItem(editMenuItem);
        styleMenuItem(createMenuItem);
        styleMenuItem(deleteMenuItem);
        styleMenuItem(save);

        fileMenu.add(editMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(createMenuItem);
        fileMenu.add(deleteMenuItem);
        fileMenu.add(save);

        menuBar.add(fileMenu);


        openMenuItem.addActionListener(_->{
            try {
                openFileFrame(frame, menuBar, fileMenu, save);
            } catch(IOException e) {
                System.out.println(e);
            }
        });

        exitMenuItem.addActionListener(_-> System.exit(0));
        editMenuItem.addActionListener(_-> {});
        createMenuItem.addActionListener(_-> createFileframe());
        deleteMenuItem.addActionListener(_-> deleteFileframe());

        return menuBar;
    }

    public static JFrame createFileframe(){
        JFrame createframe = new JFrame("Enter name for a File you want to create");
        createframe.setSize(600,400);
        createframe.setLocationRelativeTo(null);

        Container container = createframe.getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(BACKGROUND_COLOR);

        JTextField textField = new JTextField(20);
        styleTextField(textField);

        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);

        submitButton.addActionListener(_->{
            String name = textField.getText();
            File file = new File("fileLocations/"+name+".txt");
            try {
                if(file.createNewFile()){
                    System.out.println("File created");
                } else {
                    System.out.println("File already exists");
                }
            } catch(IOException e) {
                System.out.println(e);
            }
            createframe.dispose();
        });

        container.add(textField);
        container.add(submitButton);
        createframe.setVisible(true);
        return createframe;
    }

    public static JFrame openFileFrame(JFrame frame, JMenuBar menuBar, JMenu fileMenu, JMenuItem save) throws IOException {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.getContentPane().removeAll();

        JTextArea textField = new JTextArea();
        styleTextArea(textField);

        JScrollPane scrollPane = new JScrollPane(textField);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);

        UIManager.put("FileChooser.listViewBackground", SECONDARY_BACKGROUND);


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("fileLocations"));




        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            frame.setTitle(file.getName());
            try {
                textField.setText(fm.readFile(String.valueOf(file)));
            } catch(IOException e) {
                textField.setText("Error reading file "+e.getMessage());
            }
        }

        save.addActionListener(_->{
            String content = textField.getText();
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fm.writeFile(String.valueOf(file), content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        frame.revalidate();
        frame.repaint();
        return frame;
    }

    public static JFrame deleteFileframe(){
        JFrame deleteFileframe = new JFrame("Delete a file");
        deleteFileframe.setSize(800, 600);
        deleteFileframe.setLocationRelativeTo(null);
        deleteFileframe.getContentPane().setBackground(BACKGROUND_COLOR);

        Container container = deleteFileframe.getContentPane();
        container.setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter a file name (Don't include .txt): ");
        label.setForeground(TEXT_COLOR);

        JTextField textField = new JTextField(20);
        styleTextField(textField);

        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);

        submitButton.addActionListener(e -> createConfirmationDialog(textField.getText()));

        container.add(label);
        container.add(textField);
        container.add(submitButton);

        deleteFileframe.setVisible(true);
        return deleteFileframe;
    }

    private static void createConfirmationDialog(String filename) {
        JFrame choiceframe = new JFrame("Are You Sure?");
        choiceframe.setSize(600, 400);
        choiceframe.setLocationRelativeTo(null);
        choiceframe.getContentPane().setBackground(BACKGROUND_COLOR);
        choiceframe.setLayout(new FlowLayout());

        JTextField field = new JTextField(50);
        field.setText("Action is permanent and cannot be undone");
        styleTextField(field);
        field.setEditable(false);

        JButton yesButton = new JButton("YES");
        JButton noButton = new JButton("NO");
        styleButton(yesButton);
        styleButton(noButton);

        yesButton.addActionListener(_-> {
            try {
                fm.deleteFile("fileLocations/"+filename+".txt");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            choiceframe.dispose();
        });

        noButton.addActionListener(_-> choiceframe.dispose());

        choiceframe.add(field);
        choiceframe.add(yesButton);
        choiceframe.add(noButton);
        choiceframe.setVisible(true);
    }

    // Style helper methods
    private static void styleMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(SECONDARY_BACKGROUND);
        menuBar.setForeground(TEXT_COLOR);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
    }

    private static void styleMenu(JMenu menu) {
        menu.setBackground(SECONDARY_BACKGROUND);
        menu.setForeground(TEXT_COLOR);
        menu.setBorder(BorderFactory.createEmptyBorder());
    }

    private static void styleMenuItem(JMenuItem item) {
        item.setBackground(SECONDARY_BACKGROUND);
        item.setForeground(TEXT_COLOR);
        item.setBorder(BorderFactory.createEmptyBorder());
    }

    private static void styleButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private static void styleTextField(JTextField textField) {
        textField.setBackground(SECONDARY_BACKGROUND);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private static void styleTextArea(JTextArea textArea) {
        textArea.setBackground(SECONDARY_BACKGROUND);
        textArea.setForeground(TEXT_COLOR);
        textArea.setCaretColor(TEXT_COLOR);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    }
}