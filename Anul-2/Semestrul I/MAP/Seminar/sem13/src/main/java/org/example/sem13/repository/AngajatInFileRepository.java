package org.example.sem13.repository;

import org.example.sem13.domain.Angajat;
import org.example.sem13.domain.Nivel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AngajatInFileRepository extends InFileRepository<String, Angajat> {
    @Override
    void loadFromFile() {
        {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    Angajat angajat = new Angajat(
                            parts[0],
                            parts[1],
                            Double.valueOf(parts[2]),
                            Nivel.valueOf(parts[3])
                    );
                    elemente.put(parts[0], angajat);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AngajatInFileRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }
}
