package org.example;

import java.util.Optional;

public interface PlayerRepository  extends Repository<Integer,Player>{
    Optional<Player> login(String user);
}
