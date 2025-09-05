package test;

import domain.Customer;
import org.junit.Before;
import org.junit.Test;

import repository.CustomerRepository;
import repository.CustomerRepositoryImpl;

import static org.junit.Assert.assertEquals;

public class CustomerRepositoryTest {
	
	private CustomerRepository customerRepository;

	@Before
	public void setUp(){
		this.customerRepository = new CustomerRepositoryImpl();
	}
	
	@Test
	public void testAddCustomer(){
		//TODO complete the tesT
		Customer customer=new Customer("a","A",20);
		customerRepository.addCustomer(customer);
		int arraySize=customerRepository.getAllCustomers("a").size();
		customerRepository.initialLoadOfCustomers("a");
		Customer customer1=customerRepository.getAllCustomers("A").get(0);
		assertEquals(0,0);
	}
}
