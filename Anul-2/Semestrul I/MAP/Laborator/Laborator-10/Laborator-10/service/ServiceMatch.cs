using System.Runtime.InteropServices.JavaScript;
using Laborator_10.domain;
using Laborator_10.repository;

namespace Laborator_10.service;

public class ServiceMatch:Service<Match>
{
    private RepositoryTeam _repositoryTeam;
    private RepositoryMatch _repositoryMatch;

    public ServiceMatch(RepositoryTeam repositoryTeam, RepositoryMatch repositoryMatch)
    {
        _repositoryTeam = repositoryTeam;
        _repositoryMatch = repositoryMatch;
    }

    public void Add(int idTeam, int idTeamTwo)
    {
        Team teamOne=_repositoryTeam.FindOne(idTeam);
        Team teamTwo = _repositoryTeam.FindOne(idTeamTwo);
        Match match = new Match(teamOne, teamTwo,DateTime.Now);
        _repositoryMatch.Save(match);
    }

    public void Delete(int id)
    {
        Match match = _repositoryMatch.FindOne(id);
        _repositoryMatch.Delete(match);
    }

    public IEnumerable<Match> FindAll()
    {
        return _repositoryMatch.GetAll();
    }

    public Match FindOne(int id)
    {
        return _repositoryMatch.FindOne(id);
    }

    public IEnumerable<Match> FindTime(DateTime startDate, DateTime endDate)
    {
        
        var matches= FindAll().Where(m=> m.StartDate>=startDate&&m.StartDate<=endDate).ToList();

        return matches;
    }
    
}