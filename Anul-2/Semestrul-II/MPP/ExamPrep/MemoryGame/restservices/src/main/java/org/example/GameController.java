package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:5173")
@RequestMapping("examen/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;


    @RequestMapping(value="/{name}",method = RequestMethod.GET)
    private Iterable<Game> findAllGames(@PathVariable String name){
        return gameRepository.findAllFinished(name);
    }
}
