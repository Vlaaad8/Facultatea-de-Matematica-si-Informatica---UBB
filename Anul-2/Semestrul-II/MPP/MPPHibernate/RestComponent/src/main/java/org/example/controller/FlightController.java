package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.clientfx.Flight;
import org.example.clientfx.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins="http://localhost:5174")
@RestController
@RequestMapping({"ryanair/flights"})
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private HttpServletRequest request;

    private void validateJWT(String method){
        String authentication = request.getHeader("Authorization");
        System.out.println("Pentru "+method+authentication);
        if(authentication == null || !authentication.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
        String token = authentication.substring(7);
        JWTUtils.validateToken(token);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Flight> getFlights() {
        validateJWT("FindALL ");
        System.out.println("Printing all the flights...");
        return flightRepository.findAll();

    }
    @RequestMapping(method = RequestMethod.POST)
    public Flight add(@RequestBody Flight flight){
        validateJWT("Adaugare ");
        flightRepository.add(flight);
        return flight;
    }
    @RequestMapping(value ="/{id}",method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Flight flight,@PathVariable int id){
        validateJWT("Update");
        System.out.println("Updating flight");
        if(flight.getId()!=id){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        flightRepository.update(id,flight);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable int id){
        //alidateJWT();
        Flight flight=flightRepository.findById(id).orElse(null);
        if(flight==null){
            return new ResponseEntity<String>("Flight not found", HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(flight, HttpStatus.OK);
        }
    }
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id){
        validateJWT("Delete");
        try{
            Flight flight=new Flight("","",0,"", LocalDateTime.now());
            flight.setId(id);
            Optional<Flight> deletedFlight=flightRepository.delete(flight);
            if(deletedFlight.isPresent()) {
                return new ResponseEntity<>("Flight Updated", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Flight not found", HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    }





