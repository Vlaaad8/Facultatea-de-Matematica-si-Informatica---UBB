package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:5177")
@RequestMapping("examen/games")
public class ConfigController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @RequestMapping(method = RequestMethod.PUT)
    private Configuration updateConfig(@RequestBody Configuration request){
        return configurationRepository.update(request);
    }
}
