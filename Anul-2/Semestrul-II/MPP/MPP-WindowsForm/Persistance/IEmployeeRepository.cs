using MPP_CSharpProject.domain;

namespace DefaultNamespace;

public interface IEmployeeRepository:IRepository<Employee>
{
    Employee? FindUserByPassword(string user, string password);
}