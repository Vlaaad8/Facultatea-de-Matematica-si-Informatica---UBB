package org.example.restaurantnou.service;

import javafx.scene.control.Menu;
import org.example.restaurantnou.domain.MenuItem;
import org.example.restaurantnou.repository.RepositoryMenuItem;

import java.util.*;

public class ServiceMenuItem {
    private RepositoryMenuItem repositoryMenuItem;
    public ServiceMenuItem(RepositoryMenuItem repositoryMenuItem) {
        this.repositoryMenuItem = repositoryMenuItem;
    }
    public Iterable<MenuItem> findAll(){
        return this.repositoryMenuItem.findAll();
    }

    public Set<String> getCategories(){
        Set<String> categories = new HashSet<>();
        for(MenuItem menuItem : this.findAll()){
            categories.add(menuItem.getCategory());
        }
        return categories;
    }

    public Map<String, List<MenuItem>> sortByCategory(){
        Map<String, List<MenuItem>> sortByCategory = new HashMap<>();
        for(String category : this.getCategories()){
            List<MenuItem> items=new ArrayList<>();
            for(MenuItem menuItem : this.findAll()){
                if(menuItem.getCategory().equals(category)){
                    items.add(menuItem);
                }
            }
            sortByCategory.put(category, items);
        }
        return sortByCategory;
    }
}
