import java.io.*;
import java.util.Scanner;
public class FileManager {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FileManager fileManager = new FileManager();

        try{

            fileManager.createFile("test.tx");
            fileManager.writeFile("test.txt", "Hello, World");
            System.out.println(fileManager.readFile("test.txt"));
            fileManager.deleteFile("test.txt");

        }
        catch(IOException e){
            e.printStackTrace();
        }
        sc.close();
    }

    public void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }
    }

    public String readFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    public void writeFile(String fileName, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();
    }

    public void deleteFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.delete()) {
            System.out.println("File deleted: " + file.getName());
        }
        else {
            System.out.println("File could not be deleted.");
        }
    }

}
