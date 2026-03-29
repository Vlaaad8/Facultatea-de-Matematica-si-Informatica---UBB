package org.example;

import java.util.List;

public interface GameRepository extends Repository<Integer,Game>{
    List<Game> findAllFinished(String name);
}
