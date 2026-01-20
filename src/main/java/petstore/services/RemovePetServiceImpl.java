package petstore.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petstore.models.enums.Species;
import petstore.repositories.PetRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RemovePetServiceImpl implements RemovePetService {
    private PetRepository petRepository;
    public RemovePetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }
    @Override
    @Transactional
    @CacheEvict(cacheNames = {"pets", "pet_info"}, allEntries = true)
    public void removePet(Species species, String name) {
        log.debug("Удаление питомца: {} {}", species, name);
        if (!petRepository.existsByName(name)) {
            log.warn("Попытка удалить несуществующего питомца: {} {}",species, name);
            //throw new PetNotFoundException("Питомец '" + species + name + "' не найден");
        }
        petRepository.deleteByName(name);
        log.info("Питомец успешно удален: {} {}", species, name);
    }
}