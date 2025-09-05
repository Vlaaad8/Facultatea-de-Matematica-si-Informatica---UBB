package org.example.sem13;

import org.example.sem13.domain.Angajat;
import org.example.sem13.domain.Pontaj;
import org.example.sem13.domain.Sarcina;
import org.example.sem13.repository.AngajatInFileRepository;
import org.example.sem13.repository.PontajInFileRepository;
import org.example.sem13.repository.SarcinaInFileRepository;
import org.example.sem13.service.AngajatService;

import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        AngajatInFileRepository repositoryAngajati = new AngajatInFileRepository("src/main/java/org/example/sem13/data/angajati");
        SarcinaInFileRepository repositorySarcini = new SarcinaInFileRepository("src/main/java/org/example/sem13/data/sarcini");
        PontajInFileRepository repositoryPontaj = new  PontajInFileRepository("src/main/java/org/example/sem13/data/pontaje", "src/main/java/org/example/sem13/data/angajati","src/main/java/org/example/sem13/data/sarcini");
        AngajatService serviceAngajati = new AngajatService(repositoryAngajati);

        Enumeration<Angajat> angajati = repositoryAngajati.findAll();
        Enumeration<Sarcina> sarcini = repositorySarcini.findAll();
        Enumeration<Pontaj> pontaje = repositoryPontaj.findAll();

        while (angajati.hasMoreElements()) {
            Angajat angajat = angajati.nextElement();
            System.out.println(angajat);
        }

        while (sarcini.hasMoreElements()) {
            Sarcina sarcina = sarcini.nextElement();
            System.out.println(sarcina);
        }

        while (pontaje.hasMoreElements()) {
            Pontaj pontaj = pontaje.nextElement();
            System.out.println(pontaj);
        }

        serviceAngajati.genereazaRaport();

        serviceAngajati.mediumTime();
    }
}
