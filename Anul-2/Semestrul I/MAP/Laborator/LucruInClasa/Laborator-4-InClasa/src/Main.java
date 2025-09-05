import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Movie> movies = new ArrayList<>();
        Movie a = new Movie("A", 202);
        Movie b = new Movie("B", 2022);
        HorrroMovie c = new HorrroMovie(10, "d", 22);
        HorrroMovie d = new HorrroMovie(1000, "ddd", 2222);
        ActionMovie e=new ActionMovie(true,"aa",333);
        movies.add(a);
        movies.add(b);
        movies.add(c);
        movies.add(d);
        movies.add(e);
        for(Movie m: movies){
            if(m instanceof HorrroMovie){
                System.out.println(m.toString());
            }
        }


    }
}