using System.Text.Json.Serialization.Metadata;
using Laborator_10.utils;

namespace Laborator_10.domain;

public class ActivePlayer:Entity<int>
{
    private int _matchID;
    private int _totalPointsScored;
    private PlayerType _type;

    public ActivePlayer(int id,int matchId, int totalPointsScored, PlayerType type)
    {
        _matchID = matchId;
        this._totalPointsScored = totalPointsScored;
        this._type = type;
        this.SetId(id);
    }

    public PlayerType Type {get{return _type;}}
    public int MatchID {get{return _matchID;}}
    public int TotalPointsScored {get{return _totalPointsScored;}}

    public override string ToString()
    {
        return $"MatchID: {_matchID}, TotalPointsScored: {_totalPointsScored}";
    }
    
}