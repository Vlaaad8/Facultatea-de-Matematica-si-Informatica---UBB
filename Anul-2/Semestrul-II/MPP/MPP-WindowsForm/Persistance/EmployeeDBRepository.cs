using System.Data;
using log4net;
using MPP_CSharpProject.domain;
using MPP_CSharpProject.utils;

namespace DefaultNamespace;

public class EmployeeDBRepository(IDictionary<string, string> dbConnection):IEmployeeRepository
{
    private static readonly ILog Log = LogManager.GetLogger("EmployeeDBRepository");
    public Employee FindOne(int  entity)
    {
        Log.Info($"Getting Employee with ID {entity}");
        using var connection=DBUtils.getConnection(dbConnection);
        if (connection.State != ConnectionState.Open)
        {
            connection.Open(); 
        }
        
        using var command = connection.CreateCommand();
        command.CommandText="Select * from employees where id = @id";
        var parameter = command.CreateParameter();
        parameter.ParameterName = "@id";
        parameter.Value = entity;
        command.Parameters.Add(parameter);
        using var reader = command.ExecuteReader();
        if (reader.Read())
        {
            var id=reader.GetInt32(0);
            var user=reader.GetString(1);
            var password=reader.GetString(2);
            var firstName=reader.GetString(3);
            var lastName=reader.GetString(4);
            Employee employee=new Employee(user,password,firstName,lastName);
            employee.SetId(id);
            Log.Info($"Getting Employee with ID {id}");
            return employee;
        }
        Log.Warn($"Employee with ID {entity} not found");
        return null;
    }

    public Employee Save(Employee entity)
    {
        Log.Info($"Saving Employee {entity}");
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText =
            "insert into employees(user,password,firstName,lastName) values(@user,@password,@firstName,@lastName)";

        var userParameter = command.CreateParameter();
        userParameter.ParameterName = "@user";
        userParameter.Value = entity.GetUser();
        command.Parameters.Add(userParameter);

        var passwordParameter = command.CreateParameter();
        passwordParameter.ParameterName = "@password";
        passwordParameter.Value = entity.GetPassword();
        command.Parameters.Add(passwordParameter);

        var firstNameParameter = command.CreateParameter();
        firstNameParameter.ParameterName = "@firstName";
        firstNameParameter.Value = entity.GetFirstName();
        command.Parameters.Add(firstNameParameter);

        var lastNameParameter = command.CreateParameter();
        lastNameParameter.ParameterName = "@lastName";
        lastNameParameter.Value = entity.GetLastName();
        command.Parameters.Add(lastNameParameter);
        try
        {   
            var result = command.ExecuteNonQuery();

        }
        catch (Exception ex)
        {   Log.Warn("Entity was not saved");
            System.Console.WriteLine(ex.Message);
        }
        Log.Info("Entity was saved");
        return entity;
    }

    
    public Employee Delete(Employee entity)
    {
        Log.Info($"Deleting Employee {entity}");
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText ="delete from employees where id = @id";
        var parameter = command.CreateParameter();
        parameter.ParameterName = "@id";
        parameter.Value = entity.GetId();
        command.Parameters.Add(parameter);

        int result=command.ExecuteNonQuery();
        if (result == 1)
        {   Log.Info("Employee was deleted");
            return entity;
        }
        Log.Warn($"Employee with ID {entity} not found");
        return null;
    }

    public Employee Update(Employee entity)
    {
        Log.Info($"Updating Employee {entity}");
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText ="update employees set user=@user,password=@password,firstName=@firstName,lastName=@lastName where id = @id";
        
        var userParameter = command.CreateParameter();
        userParameter.ParameterName = "@user";
        userParameter.Value = entity.GetUser();
        command.Parameters.Add(userParameter);

        var passwordParameter = command.CreateParameter();
        passwordParameter.ParameterName = "@password";
        passwordParameter.Value = entity.GetPassword();
        command.Parameters.Add(passwordParameter);

        var firstNameParameter = command.CreateParameter();
        firstNameParameter.ParameterName = "@firstName";
        firstNameParameter.Value = entity.GetFirstName();
        command.Parameters.Add(firstNameParameter);

        var lastNameParameter = command.CreateParameter();
        lastNameParameter.ParameterName = "@lastName";
        lastNameParameter.Value = entity.GetLastName();
        command.Parameters.Add(lastNameParameter);
        
        var idParameter = command.CreateParameter();
        idParameter.ParameterName = "@id";
        idParameter.Value = entity.GetId();
        command.Parameters.Add(idParameter);
        
        int result=command.ExecuteNonQuery();
        if (result == 1)
        {   Log.Info("Employee was updated");
            return entity;
        }
        Log.Warn($"Employee with ID {entity.GetId()} not found");
        return null;
    }

    public IEnumerable<Employee> GetAll()
    {   Log.Info("Getting all employees");
        var employees = new List<Employee>();
        
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText ="select * from employees";
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id=reader.GetInt32(0);
            var user=reader.GetString(1);
            var password=reader.GetString(2);
            var firstName=reader.GetString(3);
            var lastName=reader.GetString(4);
            Employee employee=new Employee(user,password,firstName,lastName);
            employee.SetId(id);
            employees.Add(employee);
        }
        return employees;
    }

    public Employee FindUserByPassword(string user, string password)
    {   Log.Info($"Getting Employee with user {user} and password {password}");
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText ="select * from employees where user=@user and password=@password";
        var parameter = command.CreateParameter();
        
        var userParameter = command.CreateParameter();
        userParameter.ParameterName = "@user";
        userParameter.Value = user;
        command.Parameters.Add(userParameter);

        var passwordParameter = command.CreateParameter();
        passwordParameter.ParameterName = "@password";
        passwordParameter.Value = password;
        command.Parameters.Add(passwordParameter);
        
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id=reader.GetInt32(0);
            var user1=reader.GetString(1);
            var password1=reader.GetString(2);
            var firstName=reader.GetString(3);
            var lastName=reader.GetString(4);
            Employee employee=new Employee(user,password,firstName,lastName);
            employee.SetId(id);
            Log.Info("Entity was found");
            return employee;
            
        }
        Log.Info("Entity was not found");
        return null;
    }
}