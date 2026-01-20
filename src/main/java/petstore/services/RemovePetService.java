package petstore.services;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import petstore.models.entities.Pet;
import petstore.models.enums.Species;

import java.util.Optional;

public interface RemovePetService {
    void removePet(Species species, String name);
}