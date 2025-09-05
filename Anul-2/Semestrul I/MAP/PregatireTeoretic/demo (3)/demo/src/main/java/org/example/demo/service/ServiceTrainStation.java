package org.example.demo.service;

import org.example.demo.domain.TrainStation;
import org.example.demo.repository.RepositoryCity;
import org.example.demo.repository.RepositoryTrainStation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ServiceTrainStation {
    private RepositoryTrainStation repoTrainStation;
    private RepositoryCity repoCity;

    public ServiceTrainStation(RepositoryTrainStation repoTrainStation,RepositoryCity repoCity) {
        this.repoTrainStation = repoTrainStation;
        this.repoCity = repoCity;

    }

    public Iterable<TrainStation> findAll() {
        try {
            return repoTrainStation.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findDirectRoutes(String from, String to,float pricePerStation) throws SQLException {
        boolean found = false;
        List<String> routes = new ArrayList<>();

        for (TrainStation ts : repoTrainStation.findAll()) {
            String dep = ts.getDepartureCity();
            String dest = ts.getArrivalCity();

            if (dep.equals(from) && dest.equals(to)) {
                found = true;
               routes.add(dep + " --"+ts.getId()+"-->" + dest + " Total Price: "+ pricePerStation*2);
            }
        }

        if (!found) {
           routes.add("There are no available routes");
        }
        return routes;
    }

    public String findIDByStations(String from,String to) {
        try {
            for(TrainStation trainStation: repoTrainStation.findAll()){
                if(trainStation.getDepartureCity().equals(from) && trainStation.getArrivalCity().equals(to)) {
                    return trainStation.getId();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<List<String>> routeGraph(String to, String from) throws SQLException {

        DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);


        repoCity.findAll().forEach(user -> graph.addVertex(user.getCityName()));

        for(TrainStation trainStation: repoTrainStation.findAll()){
            if(trainStation.getDepartureCity().equals(to) && trainStation.getArrivalCity().equals(from)) {
                System.out.println("skip");
            }
            else{
                graph.addEdge(trainStation.getDepartureCity(), trainStation.getArrivalCity());
                }
        }
        AllDirectedPaths<String, DefaultEdge> allDirectedPaths = new AllDirectedPaths<>(graph);
        List<GraphPath<String, DefaultEdge>> paths = allDirectedPaths.getAllPaths(to,from,false,5);
        List<List<String>> all= new ArrayList<>();
        for(GraphPath<String, DefaultEdge> path: paths){
        all.add(path.getVertexList());
        }
        return all;
    }

    public List<String> formatRoute(String from,String to,float price) throws SQLException {
        List<List<String>> station=routeGraph(from,to);
        System.out.println(station.size());
        List<String> all=new ArrayList<>();
        for(List<String> stations:station) {

            StringBuilder route = new StringBuilder();
            for (int i = 0; i < stations.size() - 1; i++) {
                route.append(stations.get(i)).append("--").append(findIDByStations(stations.get(i), stations.get(i + 1))).append("-->");
            }
            route.append(stations.get(stations.size() - 1)).append(" Total Price: " + price * stations.size());
            all.add(route.toString());
        }
        return all;
    }

}
