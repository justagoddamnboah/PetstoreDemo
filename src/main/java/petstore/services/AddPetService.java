package petstore.services;

import petstore.dto.AddPetDto;

public interface AddPetService {
    void addPet(AddPetDto petDto);
}
