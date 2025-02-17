import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class guiFile extends FileManager {
    static FileManager fm = new FileManager();
    public static void main(String[] args) {
        JFrame frame = createFrame();
        JMenuBar menubar = createMenuBar();
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

    private static JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setMnemonic('O');

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic('S');

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic('X');

        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.setMnemonic('E');

        JMenuItem createMenuItem = new JMenuItem("Create");
        createMenuItem.setMnemonic('C');

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setMnemonic('D');

        fileMenu.add(editMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.add(createMenuItem);
        fileMenu.add(deleteMenuItem);

        menuBar.add(fileMenu);

        openMenuItem.addActionListener(_->{
            openFileFrame();
            openFileFrame().setVisible(true);
        });

        saveMenuItem.addActionListener(_->{
            saveFileframe();
            saveFileframe().setVisible(true);
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
        JFrame createframe = new JFrame("Enter name for File");
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
            try {
                fm.createFile(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try{
                Thread.sleep(2000);
            }
            catch(InterruptedException e){
                throw new RuntimeException(e);
            }
            createframe.dispose();
        });

        createframe.setVisible(true);
        return createframe;

    }
    public static JFrame openFileFrame(){

        JFrame openFileframe = new JFrame("Choose a file");
        openFileframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        openFileframe.setSize(800, 600);
        openFileframe.setLocationRelativeTo(null);
        return openFileframe;

    }

    public static JFrame saveFileframe(){
        JFrame createFileframe = new JFrame("Nothing right now");
        createFileframe.setSize(800, 600);
        createFileframe.setLocationRelativeTo(null);
        return createFileframe;
    }

    public static JFrame deleteFileframe(){

        JFrame deleteFileframe = new JFrame("Delete a file");
        deleteFileframe.setSize(800, 600);
        deleteFileframe.setLocationRelativeTo(null);

        Container container = deleteFileframe.getContentPane();
        container.setLayout(new FlowLayout());

        JTextField textField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame choiceframe = new JFrame("Are You Sure?");

                choiceframe.setSize(600, 400);

                choiceframe.setLocationRelativeTo(null);
                choiceframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                choiceframe.setVisible(true);

                JTextField field = new JTextField(50);
                field.setText("Action is permanent and cannot be undone");

                choiceframe.setLayout(new FlowLayout());

                JButton yesButton = new JButton("YES");
                JButton noButton = new JButton("NO");

                choiceframe.add(field);
                choiceframe.add(yesButton);
                choiceframe.add(noButton);

                yesButton.addActionListener(_-> {
                    String filename = textField.getText();
                    try {
                        fm.deleteFile(filename);

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
            }
        });

        container.add(new JLabel("Enter a file name: "));
        container.add(textField);
        container.add(submitButton);

        deleteFileframe.setVisible(true);
        return deleteFileframe;
    }

}
