package org.example.iss.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Collate;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends org.example.iss.domain.Entity {
    @ManyToOne
    private User user;
    @Column(nullable = false)
    private int quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public Order(User user, int quantity) {
        this.user = user;
        this.quantity = quantity;
        status=OrderStatus.Pending;
    }

}
