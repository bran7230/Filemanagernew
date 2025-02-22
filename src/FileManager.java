import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import java.io.*;
public class FileManager{

    public static void createFile(String fileName) throws IOException {
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
            JOptionPane.showMessageDialog(null, file.getName() + " could not be deleted.");
        }
    }

    public static void createPDF(String fileName) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            document.save(fileName);
            JOptionPane.showMessageDialog(null, "PDF created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }


}
