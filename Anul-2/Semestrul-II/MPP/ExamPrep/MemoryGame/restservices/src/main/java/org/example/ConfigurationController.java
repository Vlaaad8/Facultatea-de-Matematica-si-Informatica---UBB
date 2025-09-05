package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:5174")
@RequestMapping("examen/adaugare")
public class ConfigurationController {
    @Autowired
    private ConfigurationRepository configurationRepository;

    @PostMapping
    private Configuration addConfiguration(@RequestBody Configuration configuration) {
        return configurationRepository.add(configuration).orElse(null);
    }
}
