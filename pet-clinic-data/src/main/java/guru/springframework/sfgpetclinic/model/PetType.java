package guru.springframework.sfgpetclinic.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Pet types cannot be too much and
 * I decided to use Integer type for id to create better architecture
 * because id can be of any type (Long, Int, String, etc.).
 * */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "types")
public class PetType extends BaseEntity<Integer>{
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
