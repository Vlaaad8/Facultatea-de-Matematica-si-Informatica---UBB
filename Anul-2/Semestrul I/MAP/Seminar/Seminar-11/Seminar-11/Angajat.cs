namespace Seminar_11;

public class Angajat : Entity<string> 
{
    public Angajat(string  nume, double venitPeOra, KnowledgeLevel nivel)
    {   ID=RandomID();
        Nume = nume;
        VenitPeOra = venitPeOra;
        Nivel = nivel;
    }
    public String Nume { get; set; }
    public double VenitPeOra { get; set; }
    public KnowledgeLevel Nivel { get; set; }
 
       
 
    public override string ToString()
    {
        return ID+" "+Nume+" "+VenitPeOra+" "+Nivel;
    }

    public string RandomID()
    {
        Random random = new Random();
        return random.Next().ToString();
    }
}