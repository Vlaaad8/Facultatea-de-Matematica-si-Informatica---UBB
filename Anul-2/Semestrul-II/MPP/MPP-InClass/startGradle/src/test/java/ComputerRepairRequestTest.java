import org.example.model.ComputerRepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputerRepairRequestTest {
    @Test
    @DisplayName("First test")
    public void testExample(){
        ComputerRepairRequest computerRepairRequest = new ComputerRepairRequest();
        assertEquals("", computerRepairRequest.getOwnerName());
        assertEquals("",computerRepairRequest.getModel());
    }
    @Test
    @DisplayName("Second test")
    public void testTwo(){
        assertEquals(2,2,"Numerele ar trebui sa fie egale!");

    }

      
}
