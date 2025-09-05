using System.Collections;
using Laborator_10.domain;
using Laborator_10.repository;
using Laborator_10.utils;

namespace Laborator_10.service;

public class ServiceActivePlayers:Service<ActivePlayer>
{
    private RepositoryActivePlayer _repositoryActivePlayer;
    private RepositoryPlayer _repositoryPlayer;
    private RepositoryMatch _repositoryMatch;
    private ValidatorActivePlayer _validator;

    public ServiceActivePlayers(RepositoryActivePlayer repositoryActivePlayer, RepositoryPlayer repositoryStudent,RepositoryMatch repositoryMatch,ValidatorActivePlayer validator){
        _repositoryActivePlayer = repositoryActivePlayer;
        _repositoryMatch = repositoryMatch;
        _repositoryPlayer = repositoryStudent;
        _validator = validator;
    }

    public void Save(int idPlayer,int idMatch, int totalpointsScored, PlayerType player)
    {
        ActivePlayer activePlayer = new ActivePlayer(idPlayer,idMatch, totalpointsScored, player);
        _validator.Validate(activePlayer);
        
        _repositoryActivePlayer.Save(activePlayer);
    }

    public void Delete(int id)
    {
        
    }
    public IEnumerable<ActivePlayer> FindAll()
    {
        return _repositoryActivePlayer.GetAll();
    }

    public ActivePlayer FindOne(int id)
    {
        return _repositoryActivePlayer.FindOne(id);
    }

    public IEnumerable<ActivePlayer> GetActivePlayers(int matchId, int idTeam)
    {
        return FindAll()
            .Where(player => player.MatchID == matchId && 
                             _repositoryPlayer.FindOne(player.GetId()).Teams.GetId() == idTeam)
            .ToList();
        
    }


    public string CalculateScore(int idMatch)
    {
        Match match = _repositoryMatch.FindOne(idMatch);

        var activePlayers = _repositoryActivePlayer
            .GetAll()
            .Where(player => player.MatchID == idMatch)
            .ToList();

        int scoreTeamOne = activePlayers
            .Where(player => _repositoryPlayer.FindOne(player.GetId()).Teams.GetId() == match.FirstTeam.GetId())
            .Sum(player => player.TotalPointsScored);

        int scoreTeamTwo = activePlayers
            .Where(player => _repositoryPlayer.FindOne(player.GetId()).Teams.GetId() == match.SecondTeam.GetId())
            .Sum(player => player.TotalPointsScored);

        return $"{scoreTeamOne} : {scoreTeamTwo}";
    }

    
}