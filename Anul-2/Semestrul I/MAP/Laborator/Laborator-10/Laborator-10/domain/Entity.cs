namespace Laborator_10.domain;

public class Entity<ID>
{
    private ID _id;
    
    public ID GetId() => _id;
    public void SetId(ID id) => _id = id;
}