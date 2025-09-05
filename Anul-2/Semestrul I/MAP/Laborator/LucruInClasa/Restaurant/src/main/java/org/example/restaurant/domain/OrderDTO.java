package org.example.restaurant.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderDTO {
    private Long tableID;
    private String orderedItems;
    private LocalDateTime date;

    public OrderDTO(Long tableID, String orderedItems, LocalDateTime date) {
        this.tableID = tableID;
        this.orderedItems = orderedItems;
        this.date = date;
    }

}
