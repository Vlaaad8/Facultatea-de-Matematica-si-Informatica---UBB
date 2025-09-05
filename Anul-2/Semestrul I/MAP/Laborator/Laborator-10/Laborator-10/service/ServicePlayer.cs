using Laborator_10.domain;
using Laborator_10.repository;

namespace Laborator_10.service;

public class ServicePlayer:Service<Player>
{
    private RepositoryPlayer _repositoryPlayer;
    private RepositoryStudent _repositoryStudent;
    private RepositoryTeam _repositoryTeam;

    public ServicePlayer(RepositoryPlayer repositoryPlayer, RepositoryStudent repositoryStudent, RepositoryTeam repositoryTeam)
    {
        _repositoryPlayer = repositoryPlayer;
        _repositoryStudent = repositoryStudent;
        _repositoryTeam = repositoryTeam;
        
    }

    public void Save(int idStudent, int idTeam)
    {
        Student student=_repositoryStudent.FindOne(idStudent);
        Team team = _repositoryTeam.FindOne(idTeam);
        Player player = new Player(student.GetName(),student.GetSchoolName(), team);
        _repositoryPlayer.Save(player);
    }

    public void Delete(int id)
    {
        
    }

    public Player FindOne(int idPlayer)
    {
        return _repositoryPlayer.FindOne(idPlayer);
    }

    public IEnumerable<Player> FindAll()
    {
        return _repositoryPlayer.GetAll();
    }

    public IEnumerable<Player> FindAllByTeam(int idTeam)
    {
        var player = FindAll().Where(p => p.Teams.GetId() == idTeam);
        

        return player;
    }
}