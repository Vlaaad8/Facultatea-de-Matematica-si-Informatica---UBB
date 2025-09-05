// See https://aka.ms/new-console-template for more information

using Laborator_10.domain;
using Laborator_10.repository;
using Laborator_10.service;
using Laborator_10.ui;

Console.WriteLine("Hello, World!");
var connectionString = "Host=localhost;Port=5432;Username=postgres;Password=1mai1984;Database=postgres";
RepositoryStudent repositoryDataBase=new RepositoryStudent(connectionString);
RepositoryActivePlayer repositoryDataBaseActivePlayer=new RepositoryActivePlayer(connectionString);
RepositoryMatch repositoryDataBaseMatch=new RepositoryMatch(connectionString);
RepositoryPlayer repositoryDataBasePlayer=new RepositoryPlayer(connectionString);
RepositoryTeam repositoryDataBaseTeam=new RepositoryTeam(connectionString);

ServiceActivePlayers serviceActivePlayers =
    new ServiceActivePlayers(repositoryDataBaseActivePlayer,repositoryDataBasePlayer,repositoryDataBaseMatch, new ValidatorActivePlayer());
ServiceMatch serviceMatch = new ServiceMatch(repositoryDataBaseTeam,repositoryDataBaseMatch);
ServicePlayer servicePlayer=new ServicePlayer(repositoryDataBasePlayer,repositoryDataBase,repositoryDataBaseTeam);
ServiceStudent serviceStudent=new ServiceStudent(repositoryDataBase,new ValidatorStudent());
ServiceTeam serviceTeam=new ServiceTeam(repositoryDataBaseTeam,new ValidatorTeam());

Controller controller=new Controller(serviceStudent,serviceActivePlayers,serviceMatch,serviceTeam,servicePlayer);
controller.main();