package org.example;

import java.util.List;

public interface ConfigurationRepository extends Repository<Integer,Configuration>{
    Configuration update(Configuration configuration);
}
