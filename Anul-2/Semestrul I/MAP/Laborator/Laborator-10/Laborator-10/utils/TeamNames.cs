using System.ComponentModel.DataAnnotations;
using ValidationException = Laborator_10.domain.ValidationException;

namespace Laborator_10.utils;

public class TeamNames
{
    private List<string> _teamNames = new List<string>();

    public TeamNames()
    {
        _teamNames.Add("Houston Rockets");
        _teamNames.Add("Portland TrailBlazers");
        _teamNames.Add("Golden State Warriors");
    }

    public bool ValidateTeamNames(string teamName)
    {   return _teamNames.Contains(teamName);
    }
}