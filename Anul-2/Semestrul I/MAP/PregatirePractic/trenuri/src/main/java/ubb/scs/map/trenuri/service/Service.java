package ubb.scs.map.trenuri.service;
import ubb.scs.map.trenuri.domain.Cautari;
import ubb.scs.map.trenuri.domain.City;
import ubb.scs.map.trenuri.domain.TrainStation;
import ubb.scs.map.trenuri.repository.CityDBRepository;
import ubb.scs.map.trenuri.repository.TrainStationDBRepository;
import ubb.scs.map.trenuri.utils.observer.ObservableImplementat;

import java.util.ArrayList;
import java.util.List;

public class Service extends ObservableImplementat{
    private CityDBRepository cityRepo;
    private TrainStationDBRepository trainStationRepo;
    private List<Cautari> cautari=new ArrayList<>();

    public Service(CityDBRepository cityRepo, TrainStationDBRepository trainStationRepo) {
        this.cityRepo=cityRepo;
        this.trainStationRepo=trainStationRepo;
    }

    public List<City> getCities() {
        List<City> citiesList = new ArrayList<>();
        cityRepo.findAll().forEach(citiesList::add);
        return citiesList;
    }

    public List<TrainStation> getTrainStations() {
        List<TrainStation> stationsList = new ArrayList<>();
        trainStationRepo.findAll().forEach(stationsList::add);
        return stationsList;
    }

    public String getNameById(String idOras){
        Long idOrasLong = Long.parseLong(idOras);
        for (City c : getCities()) {
            if (c.getID().equals(idOrasLong))
                return c.getName();
        }
        return null;
    }

    public void addCautare(String idOm, String idDeparture, String idDestination){
        Cautari c = new Cautari(idOm, idDeparture, idDestination);
        cautari.removeIf(cautare -> cautare.getIdOm().equals(idOm));
        cautari.add(c);

        for (Cautari ca : cautari) {
            System.out.println(ca);
        }
        /*
        List<Cautari> cautariBun=new ArrayList<>();
        for(Cautari caut: cautari){
            if(!caut.getIdOm().equals(idOm))
                cautariBun.add(caut);
        }

        Cautari c=new Cautari(idOm,idDeparture,idDestination);
        cautari.clear();
        cautari.addAll(cautariBun);
        cautari.add(c);

        for(Cautari ca: cautari){
            System.out.println(ca);
        }*/
    }

    public Integer nrPersoane(String from,String to){
        int cnt=0;
        for(Cautari c: cautari)
        {
            System.out.println("IN NRPERSOANE" + c.toString());
            System.out.println("FROM: " +  from + " DEPARTURE: "+c.getIdDeparture());
            if(c.getIdDeparture().equals(from) && c.getIdDestination().equals(to)) {
                cnt++;
                System.out.println("SI AICI INTRA");

            }
        }
        return cnt-1;
    }
}