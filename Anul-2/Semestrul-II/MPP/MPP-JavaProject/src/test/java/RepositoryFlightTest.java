import org.example.domain.Employee;
import org.example.domain.Flight;
import org.example.domain.Ticket;
import org.example.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class RepositoryFlightTest {
    @DisplayName("First Test")
    @Test
    public void repositoryFlightFirstTest(){
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        FlightRepository repository=new FlightDBRepository(props);
        Flight flight=new Flight("Bucuresti","Cluj-Napoca",240,"Otopeni", LocalDateTime.now());
        repository.add(flight);
        repository.findAll().forEach(System.out::println);

        TicketRepository ticketRepository=new TicketDBRepository(props,repository);
        ticketRepository.findAll().forEach(System.out::println);

        EmployeeRepository employeeRepository=new EmployeeDBRepository(props);
        Employee employee= new Employee("aaa3333","psss","aa","aaa");
        employeeRepository.add(employee);

        employeeRepository.findAll().forEach(System.out::println);


    }
}
