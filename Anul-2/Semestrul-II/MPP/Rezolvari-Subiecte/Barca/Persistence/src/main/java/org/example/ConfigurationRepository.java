package org.example;

public interface ConfigurationRepository extends Repository<Integer,Configuration>{
    Configuration update(Configuration configuration);
}
