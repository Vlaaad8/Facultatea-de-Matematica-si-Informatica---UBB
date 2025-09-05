namespace DefaultNamespace;

public class Entity<ID> 
{
    private ID _id;

    public ID GetId()
    {
        return _id;
    }

    public void SetId(ID id)
    {
        _id = id;
    }
    
}