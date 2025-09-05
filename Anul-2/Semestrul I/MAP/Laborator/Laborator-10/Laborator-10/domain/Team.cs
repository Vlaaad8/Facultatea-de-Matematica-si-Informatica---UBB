namespace Laborator_10.domain;

public class Team:Entity<int>
{
    private string name;

    public Team(string name)
    {
        this.name = name;
    }
    public string Name{get{return name;}}
}