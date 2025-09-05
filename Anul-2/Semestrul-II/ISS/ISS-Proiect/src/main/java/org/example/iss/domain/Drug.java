package org.example.iss.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@jakarta.persistence.Entity
@NoArgsConstructor
@Table(name="drugs")
public class Drug extends Entity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    private String observations;
    @Column(nullable = false)
    private int availableUnits;
}
