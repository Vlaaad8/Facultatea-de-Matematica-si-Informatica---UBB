
package org.example.iss.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orderitems")
@Getter
@Setter
public class OrderItem extends org.example.iss.domain.Entity {
    @ManyToOne
    private Order mainOrder;
    @ManyToOne
    private Drug drug;
}

