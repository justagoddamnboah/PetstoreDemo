package petstore.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petstore.dto.AddPetDto;
import petstore.models.entities.Pet;
import petstore.repositories.PetRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AddPetServiceImpl implements AddPetService {
    private final PetRepository petRepository;
    private final ModelMapper mapper;

    public AddPetServiceImpl(PetRepository petRepository, ModelMapper mapper) {
        this.petRepository = petRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "pets", allEntries = true)
    public void addPet(AddPetDto petDTO) {
        log.debug("Добавление нового питомца: {}", petDTO.getName());
        Pet pet = mapper.map(petDTO, Pet.class);
        petRepository.save(pet);
        log.info("Питомец успешно добавлен: {} {}", pet.getSpecies(), pet.getName());
    }
}