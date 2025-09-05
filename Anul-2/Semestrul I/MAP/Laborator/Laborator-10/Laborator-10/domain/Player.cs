namespace Laborator_10.domain;

public class Player : Student
{
    private Team _team;


    public Player(string name, string schoolName, Team team) : base(name, schoolName)
    {
        _team = team;
    }

    public Team Teams
    {
        get { return _team; }
    }

    public override string ToString()
    {
        return $"{base.ToString()}";
    }
}
