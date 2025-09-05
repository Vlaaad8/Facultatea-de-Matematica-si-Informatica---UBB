import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filePath = "D:\\Anul 2\\MAP\\Laborator\\Laborator-4-InClasa-IO\\src\\example.txt";  // Path to the file
        List<String> lista = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                lista.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int max = -1;
        String word = "";
        for (String line : lista) {
            if (line.contains("test")) {
                System.out.println(line);
            }
            if (line.length() > max) {
                max = line.length();
                word = line;
            }

        }
        System.out.println("Maximum word is:" + word);
        String fileOutput = "D:\\Anul 2\\MAP\\Laborator\\Laborator-4-InClasa-IO\\src\\output.txt";
        List<String> randomNumbers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            randomNumbers.add(String.valueOf(Math.random()));
        }
        try (BufferedWriter b = new BufferedWriter(new FileWriter(fileOutput))) {
            for (String ed : randomNumbers) {
                b.write(ed);
                b.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
