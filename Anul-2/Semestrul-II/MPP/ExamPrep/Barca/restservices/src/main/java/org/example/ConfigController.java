package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:5177")
@RequestMapping("examen/games")
public class ConfigController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @RequestMapping(method = RequestMethod.POST)
    private Configuration updateConfig(@RequestBody Configuration request){
        String[] value1=request.getValue1().split(",");
        String[] value2=request.getValue2().split(",");
        String[] value3=request.getValue3().split(",");

        if(value1[0].equals(value2[0]) && value1[0].equals(value3[0])){
            return configurationRepository.add(request).orElse(null);
        }
        else if(value1[1].equals(value2[1]) && value1[1].equals(value3[1])){
            return configurationRepository.add(request).orElse(null);
        }
        else return null;
    }
}
