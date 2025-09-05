namespace Laborator_10.domain;

public class Match:Entity<int>
{
    private Team _firstTeam;
    private Team _secondTeam;
    private DateTime _startDate;

    public Match(Team firstTeam, Team secondTeam, DateTime startDate)
    {
        this._firstTeam = firstTeam;
        this._secondTeam = secondTeam;
        this._startDate = startDate;
    }
    

    public Team FirstTeam => _firstTeam;
    public Team SecondTeam => _secondTeam;
    public DateTime StartDate => _startDate;
}