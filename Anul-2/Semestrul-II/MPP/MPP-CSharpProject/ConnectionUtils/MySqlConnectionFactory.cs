using System;
using System.Data;
using System.Collections.Generic;
using MySql.Data.MySqlClient;

namespace ConnectionUtils
{
	
	public class MySqlConnectionFactory:ConnectionFactory
	{
		public override IDbConnection createConnection(IDictionary<string,string> props)
		{
			//MySqlConnection
			//String connectionString = "Database=main;" +
										//"Data Source=localhost;" +
										//"User id=root;" +
										//"Password=parola;";
			String connectionString = props["ConnectionString"];
			Console.WriteLine("MySql ---se deschide o conexiune la  ... {0}", connectionString);
			
			return new MySqlConnection(connectionString);

	
		}
	}

}
