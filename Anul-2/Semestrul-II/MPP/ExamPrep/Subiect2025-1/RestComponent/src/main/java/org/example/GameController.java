package org.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="http://localhost:5175")
@RequestMapping("examen/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;


    @RequestMapping(value="/{name}",method = RequestMethod.GET)
    private Iterable<Game> findAllGames(String name){
        return gameRepository.findAll();
    }
}
