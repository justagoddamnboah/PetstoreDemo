package petstore.dto;

import petstore.models.enums.Breed;
import petstore.models.enums.Gender;
import petstore.models.enums.Species;

public class AddPetDto {
    private String name;
    private Species species;
    private Breed breed;
    private int ageMonths;
    private Gender gender;
    private double price;

    public AddPetDto() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public int getAgeMonths() {
        return ageMonths;
    }
    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}