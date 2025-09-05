class Pizza {
    protected int id;
    public Pizza(int id) {this.id = id;}
    public Pizza() { }
    public boolean equals(Pizza obj) { return obj.id ==this.id;
    } }