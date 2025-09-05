public class Mail {
    private String name;
    private String manager;
    private Integer year;
    private double profit;

    public Mail(String name, String manager, Integer year, double profit) {
        this.name = name;
        this.manager = manager;
        this.year = year;
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "name='" + name + '\'' +
                ", manager='" + manager + '\'' +
                ", year=" + year +
                ", profit=" + profit +
                '}';
    }
}
