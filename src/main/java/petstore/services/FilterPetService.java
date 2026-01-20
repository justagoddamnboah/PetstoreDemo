package petstore.services;

import petstore.dto.SearchPetDto;
import petstore.dto.TopSoldPetsDto;
import petstore.models.entities.Pet;

import java.util.List;
import java.util.Optional;

public interface FilterPetService {
    List<Pet> findAllPets();
    List<Pet> searchPets(SearchPetDto searchDto);
    Optional<Pet> findPetById(Long id);
    List<TopSoldPetsDto> getTop5SoldPets();
}