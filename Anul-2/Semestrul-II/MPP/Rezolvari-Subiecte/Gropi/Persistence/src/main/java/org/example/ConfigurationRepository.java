package org.example;

import java.util.List;

public interface ConfigurationRepository extends Repository<Integer,Configuration>{
    List<Configuration> findByColumn(int column,int configurationID);
}
