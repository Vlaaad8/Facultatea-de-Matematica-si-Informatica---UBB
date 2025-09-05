package org.example.clientfx;

import org.example.clientfx.Hibernate.EmployeeRepositoryH;
import org.example.clientfx.Hibernate.FlightRepositoryH;
import org.example.clientfx.Hibernate.TicketRepositoryH;
import org.example.clientfx.Old.EmployeeDBRepository;
import org.example.clientfx.Old.FlightDBRepository;
import org.example.clientfx.Old.TicketDBRepository;

import java.util.Properties;

public class StartServerRPC {
    private static final int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProperties = new Properties();
        try {
            serverProperties.load(StartServerRPC.class.getResourceAsStream("/server.properties"));

        } catch (Exception e) {
            return;
        }
        EmployeeRepository employeeRepository = new EmployeeRepositoryH();
        FlightRepository flightRepository = new FlightRepositoryH();
        TicketRepository ticketRepository = new TicketRepositoryH();
        IServices services = new ServicesImplementation(employeeRepository, flightRepository, ticketRepository);
        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProperties.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.out.println(nef);

        }
        System.out.println("Server running on port " + chatServerPort);
        AbstractServer server = new ConcurrentServerRPC(chatServerPort, services);
        try {
            System.out.println("Server started");
            ((AbstractServer) server).start();;
        } catch (Exception e) {
            System.out.println("A aparut o problema!");
            System.out.println(e);
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                System.out.println("A aparut o eroare in finnally");
                System.out.println(e);
            }
        }
    }
}


