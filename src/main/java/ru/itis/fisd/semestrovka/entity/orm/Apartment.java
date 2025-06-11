package ru.itis.fisd.semestrovka.entity.orm;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private BigDecimal area;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private String status;

    @ManyToMany(mappedBy = "favoriteApartments")
    private Set<User> favoriteBy = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment that = (Apartment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
