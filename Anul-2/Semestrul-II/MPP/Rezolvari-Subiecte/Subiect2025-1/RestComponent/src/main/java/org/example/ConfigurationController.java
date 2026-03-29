package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ObjectInputFilter;

@RestController
@RequestMapping("examen/adaugare")
@CrossOrigin(origins="http://localhost:5175")
public class ConfigurationController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @PostMapping
    public Configuration addConfiguration(@RequestBody Configuration configuration) {
        return configurationRepository.add(configuration).orElse(null);
    }
}
