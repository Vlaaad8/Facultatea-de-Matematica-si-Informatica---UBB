package org.example.restaurant.domain;

public class MenuItem extends Entity<Long>{
    private String category;
    private String item;
    private float price;
    private String currency;

    public MenuItem(Long aLong,String category, String item, float price,String currency) {
        super(aLong);
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public String getItem() {
        return item;
    }

    public float getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
