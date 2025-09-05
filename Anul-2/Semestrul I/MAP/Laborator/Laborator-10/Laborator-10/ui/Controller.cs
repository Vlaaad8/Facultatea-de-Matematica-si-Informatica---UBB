using Laborator_10.domain;
using Laborator_10.service;
using Laborator_10.utils;

namespace Laborator_10.ui;

public class Controller
{
    private ServiceStudent _serviceStudent;
    private ServiceActivePlayers _serviceActivePlayers;
    private ServiceMatch _serviceMatch;
    private ServiceTeam _serviceTeam;
    private ServicePlayer _servicePlayer;


    public Controller(ServiceStudent serviceStudent, ServiceActivePlayers serviceActivePlayers, ServiceMatch serviceMatch, ServiceTeam serviceTeam, ServicePlayer servicePlayer)
    {
        _serviceStudent = serviceStudent;
        _serviceActivePlayers = serviceActivePlayers;
        _serviceMatch = serviceMatch;
        _serviceTeam = serviceTeam;
        _servicePlayer = servicePlayer;
    }

    public void showPlayersByTeam()
    {
        Console.WriteLine("Enter team's ID: ");
        int teamName = Convert.ToInt32(Console.ReadLine());
        List<Player> players = _servicePlayer.FindAllByTeam(teamName).ToList();
        Console.WriteLine(_serviceTeam.FindOne(teamName).Name+"'s players:");
        players.ForEach(player=>Console.WriteLine(player.ToString()));
    }

    public void showActivePlayers()
    {
        Console.WriteLine("Enter match's ID: ");
        int matchId = Convert.ToInt32(Console.ReadLine());
        Console.WriteLine("Enter team's ID: ");
        int teamName = Convert.ToInt32(Console.ReadLine());
        List<ActivePlayer> activePlayers=_serviceActivePlayers.GetActivePlayers(matchId, teamName).ToList();
        activePlayers.ForEach(player => Console.WriteLine(_serviceStudent.FindOne(player.GetId()).GetName() + player));
    }

    public void showMatches()
    {
        Console.WriteLine("Enter a start date: ");
        DateTime startDate = Convert.ToDateTime(Console.ReadLine());
        Console.WriteLine("Enter a end date: ");
        DateTime endDate = Convert.ToDateTime(Console.ReadLine());
        List<Match> all = _serviceMatch.FindTime(startDate, endDate).ToList();
        all.ForEach(match => Console.WriteLine(_serviceTeam.FindOne(match.FirstTeam.GetId()).Name + " vs " +
                                               _serviceTeam.FindOne(match.SecondTeam.GetId()).Name + "from " +
                                               match.StartDate));
    }

    public void showScore()
    {
        Console.WriteLine("Enter a match ID: ");
        int matchId = Convert.ToInt32(Console.ReadLine());
        Console.WriteLine(_serviceActivePlayers.CalculateScore(matchId));
    }
    //TODO: 4th option - show a match's score, started implementing smth in ServiceActivePlayers
    public void menu()
    {
        Console.WriteLine("1. Show players by team");
        Console.WriteLine("2. Show active players from a team specific match");
        Console.WriteLine("3. Show matches from a period of time");
        Console.WriteLine("4. Show a match's score");
    }

    public void main()
    {
        while (true)
        {
            menu();
            Console.WriteLine("Choose an action:");
            var x=Convert.ToInt32(Console.ReadLine());
            switch (x)
            {case 1:
                showPlayersByTeam();
                break;
            case 2:
                showActivePlayers();
                break;
            case 3:
                showMatches();
                break;
            case 4:
                showScore();
                break;
            default:
                Console.WriteLine("Please enter a valid action");
                break;
            
                
            }
        }
    }

    public void addData()
    {
        for (int i = 42; i < 44; i++)
        {
            Random rnd = new Random();
            int points = rnd.Next(1, 10);
            _serviceActivePlayers.Save(i, 1, points, PlayerType.Rezerva);
        }
    }
}