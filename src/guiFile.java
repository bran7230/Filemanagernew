import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class guiFile extends FileManager {
    static FileManager fm = new FileManager();
    public static void main(String[] args) {
        JFrame frame = createFrame();
        JMenuItem save = new JMenuItem("Save");//for later add save to create menubar
        JMenuBar menubar = createMenuBar(frame, save);
        frame.setJMenuBar(menubar);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static JFrame createFrame(){
        JFrame fileFrame = new JFrame("Welcome to File Manager");
        fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        return fileFrame;
    }

    private static JMenuBar createMenuBar(JFrame frame, JMenuItem save){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setMnemonic('O');

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic('X');

        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.setMnemonic('E');

        JMenuItem createMenuItem = new JMenuItem("Create");
        createMenuItem.setMnemonic('C');

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setMnemonic('D');

        JMenuItem saveMenuItem = save;
        fileMenu.add(editMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(createMenuItem);
        fileMenu.add(deleteMenuItem);
        fileMenu.add(saveMenuItem);

        menuBar.add(fileMenu);

        openMenuItem.addActionListener(_->{
            try {
                openFileFrame(frame, menuBar,fileMenu, saveMenuItem);
            }
            catch(IOException e){
               System.out.println(e);
            }
        });



        exitMenuItem.addActionListener(_->{
            System.exit(0);
        });

        editMenuItem.addActionListener(_->{

        });

        createMenuItem.addActionListener(_->{
        createFileframe();
        });

        deleteMenuItem.addActionListener(_->{
            deleteFileframe();
        });

        return menuBar;
    }
    public static JFrame createFileframe(){
        JFrame createframe = new JFrame("Enter name for a File you want to create");
        createframe.setSize(600,400);
        createframe.setLocationRelativeTo(null);

        Container container = createframe.getContentPane();
        container.setLayout(new FlowLayout());

        JTextField textField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        textField.setText("");

        createframe.add(textField);
        createframe.add(submitButton);

        submitButton.addActionListener(_->{
            String name = textField.getText();
            File file = new File("fileLocations/"+name+".txt");
            try{
                if(file.createNewFile()){
                    System.out.println("File created");
                }
                else {
                    System.out.println("File already exists");
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
            createframe.dispose();
        });

        createframe.setVisible(true);
        return createframe;

    }
    public static JFrame openFileFrame(JFrame frame, JMenuBar menuBar, JMenu fileMenu, JMenuItem save) throws IOException {

        JFrame openFileframe = frame;
        openFileframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuBar.add(fileMenu);
        openFileframe.setJMenuBar(menuBar);
        JMenuItem saveMenuItem = save;
        openFileframe.getContentPane().removeAll();

        JTextArea textField = new JTextArea();
        textField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textField.setLayout(new FlowLayout(FlowLayout.CENTER));
        textField.setVisible(true);
        openFileframe.add(textField);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("fileLocations"));
        if (fileChooser.showOpenDialog(openFileframe) == JFileChooser.APPROVE_OPTION){

            File file = fileChooser.getSelectedFile();
            frame.setTitle(file.getName());
            try {
                textField.setText(fm.readFile(String.valueOf(file)));

            }
            catch(IOException e){
                textField.setText("Error reading file "+e.getMessage());
            }
        }

        saveMenuItem.addActionListener(_->{
           String content = textField.getText();
           File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fm.writeFile(String.valueOf(file), content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        menuBar.add(fileMenu);

        openFileframe.setVisible(true);
        return openFileframe;

    }



    public static JFrame deleteFileframe(){

        JFrame deleteFileframe = new JFrame("Delete a file");
        deleteFileframe.setSize(800, 600);
        deleteFileframe.setLocationRelativeTo(null);

        Container container = deleteFileframe.getContentPane();
        container.setLayout(new FlowLayout());

        JTextField textField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        submitButton.setFocusable(false);

        submitButton.addActionListener(e -> {
            JFrame choiceframe = new JFrame("Are You Sure?");

            choiceframe.setSize(600, 400);

            choiceframe.setLocationRelativeTo(null);
            choiceframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            choiceframe.setVisible(true);

            JTextField field = new JTextField(50);
            field.setText("Action is permanent and cannot be undone");
            field.setEditable(false);

            choiceframe.setLayout(new FlowLayout());

            JButton yesButton = new JButton("YES");
            JButton noButton = new JButton("NO");

            choiceframe.add(field);
            choiceframe.add(yesButton);
            choiceframe.add(noButton);

            yesButton.addActionListener(_-> {
                String filename = textField.getText();
                try {
                    fm.deleteFile("fileLocations/"+filename+".txt");

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                choiceframe.dispose();

            });
            noButton.addActionListener(_->{

                try{
                    Thread.sleep(2000);
                }
                catch(InterruptedException ex){
                    throw new RuntimeException(ex);
                }
                choiceframe.dispose();

            });
        });

        container.add(new JLabel("Enter a file name: "));
        container.add(textField);
        container.add(submitButton);

        deleteFileframe.setVisible(true);
        return deleteFileframe;
    }

}
