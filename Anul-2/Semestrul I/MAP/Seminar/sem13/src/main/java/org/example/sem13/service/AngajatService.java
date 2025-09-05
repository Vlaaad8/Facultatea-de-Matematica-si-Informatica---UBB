package org.example.sem13.service;

import org.example.sem13.domain.Angajat;
import org.example.sem13.domain.Nivel;
import org.example.sem13.domain.Pontaj;
import org.example.sem13.repository.AngajatInFileRepository;
import org.example.sem13.repository.PontajInFileRepository;

import java.util.*;
import java.util.stream.Collectors;

public class AngajatService {
    private AngajatInFileRepository angajatRepo;
    private PontajInFileRepository pontajRepo;

    public AngajatService(AngajatInFileRepository angajatRepo) {
        this.angajatRepo = angajatRepo;
    }

    public void genereazaRaport() {
        Enumeration<Angajat> angajatiEnum = angajatRepo.findAll();
        List<Angajat> angajatiList = new ArrayList<>();
        while (angajatiEnum.hasMoreElements()) {
            angajatiList.add(angajatiEnum.nextElement());
        }
        Map<Nivel, List<Angajat>> grupuri = angajatiList.stream()
                .collect(Collectors.groupingBy(Angajat::getNivel));

        grupuri.forEach((nivel, listaAngajati) -> {
            System.out.println("Nivel: " + nivel);
            listaAngajati.stream()
                    .sorted(Comparator.comparingDouble(Angajat::getVenitPeOra).reversed())
                    .forEach(angajat -> System.out.println(angajat.getNume() + " - Venit pe oră: " + angajat.getVenitPeOra()));
            System.out.println();
        });
    }
    public void mediumTime(){
        Enumeration<Pontaj> angajatEnumeration=pontajRepo.findAll();
        List<Pontaj> angajatiList=new ArrayList<>();
        while(angajatEnumeration.hasMoreElements()){
            angajatiList.add(angajatEnumeration.nextElement());
        }
        Map<Nivel,Integer> map=new HashMap<>();
        for(Pontaj pontaj:angajatiList){
            Nivel nivel=pontaj.getAngajat().getNivel();
            Integer time
            if(time==null){
                time=0;
            }
            time+=pontaj.getSarcina().getDurata();
            map.put(nivel,time);
        }
    }




}