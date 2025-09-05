package org.example.restaurantnou.domain;

public class MenuItem extends Entity {
    private String category;
    private String item;
    private float price;
    private String currency;

    public MenuItem(String category, String item, float price, String currency) {
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
