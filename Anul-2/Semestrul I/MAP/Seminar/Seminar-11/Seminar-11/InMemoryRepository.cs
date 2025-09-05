using System.Security.Principal;

namespace Seminar_11;

public class InMemoryRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
{

    private ID key;
    protected IDictionary<ID, E> entities = new Dictionary<ID, E>();
    protected IValidator<E> vali;

    public InMemoryRepository(IValidator<E> vali)
    {
        this.vali = vali;
    }
    public E FindOne(ID id)
    {
        if (entities.ContainsKey(id))
        {
            return entities[id];
        }
        return null;
    }

    public IEnumerable<E> FindAll()
    {
        return entities.Values;
    }

    public virtual E Save(E entity)
    {
        if (entity == null)
            throw new ArgumentNullException("Entity must not be null");
        if(entities.ContainsKey(entity.ID)){
            return entity;
        }
        entities[entity.ID] = entity;
        return default(E);
    }

    public E Delete(ID id)
    {
        if (entities.ContainsKey(id))
        {
            entities.Remove(id);
        }
        else
        {
            throw new KeyNotFoundException("Entity not found");
        }
        return default(E);
    }

    public E Update(E entity)
    {
        if (entities.ContainsKey(entity.ID))
        {
            return this.entities[entity.ID] = entity;
        }

        throw new KeyNotFoundException("Entity not found");
    }
}