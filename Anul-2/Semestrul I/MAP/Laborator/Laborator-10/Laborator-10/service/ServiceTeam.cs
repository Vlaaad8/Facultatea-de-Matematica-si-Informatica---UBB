using System.ComponentModel.DataAnnotations;
using Laborator_10.domain;
using Laborator_10.repository;

namespace Laborator_10.service;

public class ServiceTeam:Service<Team>
{
    private RepositoryTeam _repository;
    private ValidatorTeam _validator;

    public ServiceTeam(RepositoryTeam repository, ValidatorTeam validator)
    {
        _repository = repository;
        _validator = validator;
    }

    public void Save(string name)
    {
        Team team = new Team(name);
        _validator.Validate(team);
        _repository.Save(team);
    }

    public void Delete(int id)
    {
        Team team = _repository.FindOne(id);
        _repository.Delete(team);
    }

    public IEnumerable<Team> FindAll()
    {
        return _repository.GetAll();
    }

    public Team FindOne(int id)
    {
        return _repository.FindOne(id);
    }
    
    
}