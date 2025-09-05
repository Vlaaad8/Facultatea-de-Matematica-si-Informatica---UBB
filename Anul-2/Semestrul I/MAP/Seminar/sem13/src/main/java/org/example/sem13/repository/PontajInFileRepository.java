package org.example.sem13.repository;

import org.example.sem13.domain.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;

public class PontajInFileRepository extends InFileRepository<String, Pontaj> {
    private String angajatiFileName;
    private String sarcinaFileName;

    @Override
    void loadFromFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Dictionary<String, Angajat> angajati = new Hashtable<>();
        Dictionary<String, Sarcina> sarcini = new Hashtable<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sarcinaFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Sarcina angajat = new Sarcina(
                        parts[0],
                        Dificultate.valueOf(parts[1]),
                        Integer.parseInt(parts[2])
                );
                sarcini.put(parts[0], angajat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(angajatiFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Angajat angajat = new Angajat(
                        parts[0],
                        parts[1],
                        Double.valueOf(parts[2]),
                        Nivel.valueOf(parts[3])
                );
                angajati.put(parts[0], angajat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Pontaj pontaj = new Pontaj(
                        angajati.get(parts[0]),
                        sarcini.get(parts[1]),
                        LocalDateTime.parse(parts[2], formatter)
                );
                elemente.put(pontaj.getId(), pontaj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PontajInFileRepository(String fileName, String angajatiFileName, String sarcinaFileName) {
        super(fileName);
        this.angajatiFileName = angajatiFileName;
        this.sarcinaFileName = sarcinaFileName;
        loadFromFile();
    }
}
