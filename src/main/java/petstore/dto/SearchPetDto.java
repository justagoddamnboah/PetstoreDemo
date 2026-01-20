package petstore.dto;

import petstore.models.enums.Breed;
import petstore.models.enums.Gender;
import petstore.models.enums.SortBy;
import petstore.models.enums.Species;

public class SearchPetDto {
    private Species species;
    private Breed breed;
    private Gender gender;
    private int minAgeMonths;
    private int maxAgeMonths;
    private Double minPrice;
    private Double maxPrice;
    private SortBy sortByAge;
    private SortBy sortByPrice;

    public SearchPetDto() {

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

    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    public Double getMaxPrice() {
        return maxPrice;
    }

    public int getMinAgeMonths() {
        return minAgeMonths;
    }
    public void setMinAgeMonths(int minAgeMonths) {
        this.minAgeMonths = minAgeMonths;
    }

    public int getMaxAgeMonths() {
        return maxAgeMonths;
    }
    public void setMaxAgeMonths(int maxAgeMonths) {
        this.maxAgeMonths = maxAgeMonths;
    }

    public SortBy getSortByAge() {
        return sortByAge;
    }
    public void setSortByAge(SortBy sortByAge) {
        this.sortByAge = sortByAge;
    }

    public SortBy getSortByPrice() {
        return sortByPrice;
    }
    public void setSortByPrice(SortBy sortByPrice) {
        this.sortByPrice = sortByPrice;
    }
}