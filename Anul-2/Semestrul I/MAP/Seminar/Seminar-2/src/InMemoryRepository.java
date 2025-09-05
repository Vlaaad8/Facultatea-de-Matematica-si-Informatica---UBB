import java.util.HashMap;

public class InMemoryRepository<ID,E extends Entity<ID>> implements Repository<ID,E>{
    private HashMap<ID,E> entities;
    InMemoryRepository() {
        entities = new HashMap<>();
    }
    @Override
    public E findOne(ID id) {
        return null;
    }

    @Override
    public Iterable<E> findAll() {
        return null;
    }

    @Override
    public E save(E entity) {
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        return null;
    }

    @Override
    public E update(E entity) {
        return null;
    }
}
