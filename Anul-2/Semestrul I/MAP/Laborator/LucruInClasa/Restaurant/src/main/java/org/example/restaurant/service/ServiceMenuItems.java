package org.example.restaurant.service;

import org.example.restaurant.domain.MenuItem;
import org.example.restaurant.repository.RepositoryMenuItem;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class ServiceMenuItems {
    private RepositoryMenuItem repositoryMenuItem;

    public ServiceMenuItems(RepositoryMenuItem repositoryMenuItem) {
        this.repositoryMenuItem = repositoryMenuItem;
    }

    public Iterable<MenuItem> findAll() throws SQLException {
        return repositoryMenuItem.findAll();
    }

    public Set<String> getCategories() throws SQLException {
        Set<String> categories = new HashSet<>();
        for (MenuItem menuItem : findAll()) {
            categories.add(menuItem.getCategory());
        }
        return categories;
    }

    public Map<String, List<MenuItem>> getItemsByCategory() throws SQLException {
        Set<String> categories = getCategories();
        Map<String, List<MenuItem>> itemsByCategory = new HashMap<>();
        for (String category : categories) {
            List<MenuItem> items = new ArrayList<>();
            for (MenuItem menuItem : findAll()) {
                if (menuItem.getCategory().equals(category)) {
                    items.add(menuItem);
                }

            }
            itemsByCategory.put(category, items);
        }
        return itemsByCategory;
    }
}

