import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.ComputerRepairRequestRepository;
import repository.ComputerRepairedFormRepository;
import repository.file.ComputerRepairRequestFileRepository;
import repository.file.ComputerRepairedFormFileRepository;
import repository.jdbc.ComputerRepairRequestJdbcRepository;
import repository.jdbc.ComputerRepairedFormJdbcRepository;
import services.ComputerRepairServices;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties properties=new Properties();
        try{
            System.out.println(System.getProperty("Searching bd.config in directory "+ ((new File(".")).getAbsolutePath())));
            properties.load(new FileReader("bd.config"));
        }catch(IOException e){
            System.err.println(e);
        }
        return properties;
     
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
        return new ComputerRepairRequestFileRepository("D:\\Anul-2\\Semestrul-II\\MPP\\MPP-InClass\\laborator-4IClass\\ComputerRequests.txt");

    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
       return new ComputerRepairedFormFileRepository("D:\\Anul-2\\Semestrul-II\\MPP\\MPP-InClass\\laborator-4IClass\\RepairedForms.txt",requestsRepo());

    }

    @Bean
    ComputerRepairServices services(){
       return new ComputerRepairServices(requestsRepo(),formsRepo());

    }

}
