package petstore.models.entities;

import jakarta.persistence.*;
import petstore.models.enums.Breed;
import petstore.models.enums.Gender;
import petstore.models.enums.Species;

import java.math.BigDecimal;

@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {
    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "age_months", precision = 4, scale = 1)
    private BigDecimal ageMonths;

    @Column(name = "gender", length = 7)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "species", length = 32)
    @Enumerated(EnumType.STRING)
    private Species species;

    @Column(name = "breed", length = 32)
    @Enumerated(EnumType.STRING)
    private Breed breed;

    @Column(name = "price", precision = 8, scale = 2)
    private BigDecimal price;
    public Pet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(BigDecimal ageMonths) {
        this.ageMonths = ageMonths;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}