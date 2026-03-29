package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:5176")
@RequestMapping("examen/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;


    @RequestMapping(value="/{name}",method = RequestMethod.GET)
    private Iterable<Game> findAllGames(@PathVariable String name){
        return gameRepository.getAllFinished(name);
    }

    @RequestMapping(method = RequestMethod.POST)
    private Game updateGame(@RequestBody ConfigRequest request){
        Game games=gameRepository.findById(request.gameID()).orElse(null);
        char[] config=games.getConfig().toCharArray();
        int index=3*request.i()+request.j();
        config[index]='X';
        String newConfig=new String(config);
        games.setConfig(newConfig);
        gameRepository.update(games);
        return games;
    }
}
