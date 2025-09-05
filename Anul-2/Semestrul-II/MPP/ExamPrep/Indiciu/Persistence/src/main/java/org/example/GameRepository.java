package org.example;

import java.util.List;

public interface GameRepository extends Repository<Integer,Game>{
    List<Game> getAllFinished(String name);
}
