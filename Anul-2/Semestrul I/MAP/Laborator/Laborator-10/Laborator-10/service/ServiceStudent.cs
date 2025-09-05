using System.Net.Sockets;
using Laborator_10.domain;
using Laborator_10.repository;

namespace Laborator_10.service;

public class ServiceStudent:Service<Student>
{
    private RepositoryStudent _repositoryStudent;
    private ValidatorStudent _validatorStudent;

    public ServiceStudent(RepositoryStudent repositoryStudent, ValidatorStudent validatorStudent)
    {
        _repositoryStudent = repositoryStudent;
        _validatorStudent = validatorStudent;
    }

    public void Save(string name, string schoolName)
    {
        Student student = new Student(name, schoolName);
        _validatorStudent.Validate(student);
        _repositoryStudent.Save(student);
        
    }

    public void Delete(int id)
    {
        Student student = _repositoryStudent.FindOne(id);
        _repositoryStudent.Delete(student);
    }

    public IEnumerable<Student> FindAll()
    {
        return _repositoryStudent.GetAll();
    }

    public Student FindOne(int id)
    {
        return _repositoryStudent.FindOne(id);
    }
}