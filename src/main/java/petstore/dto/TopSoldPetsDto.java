package petstore.dto;

import petstore.models.enums.Species;

public class TopSoldPetsDto {
    private Species species;
    private Long orderCount;

    // Конструктор для enum Species
    public TopSoldPetsDto(Species species, Long orderCount) {
        this.species = species;
        this.orderCount = orderCount;
    }

    public TopSoldPetsDto() {}

    // Геттеры и сеттеры
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    // Для удобного отображения
    public String getSpeciesName() {
        return species != null ? species.toString() : "";
    }
}