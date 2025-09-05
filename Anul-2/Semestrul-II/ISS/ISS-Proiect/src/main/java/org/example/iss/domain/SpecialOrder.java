package org.example.iss.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="specialorders")
public class SpecialOrder extends org.example.iss.domain.Entity {
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date date;

    public SpecialOrder(String order) {
        this.description = order;
        date = new Date();
    }
}
