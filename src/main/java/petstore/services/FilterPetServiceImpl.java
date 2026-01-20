package petstore.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petstore.dto.SearchPetDto;
import petstore.dto.TopSoldPetsDto;
import petstore.models.entities.Pet;
import petstore.models.entities.PetInfo;
import petstore.models.enums.SortBy;
import petstore.repositories.CartRepository;
import petstore.repositories.PetInfoRepository;
import petstore.repositories.PetRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class FilterPetServiceImpl implements FilterPetService {

    private final PetRepository petRepository;
    private final PetInfoRepository petInfoRepository;
    private final CartRepository cartRepository;

    public FilterPetServiceImpl(PetRepository petRepository, PetInfoRepository petInfoRepository, CartRepository cartRepository) {
        this.petRepository = petRepository;
        this.petInfoRepository = petInfoRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Pet> findAllPets() {
        log.debug("Получение всех питомцев");
        return petRepository.findAll();
    }

    public PetInfo getPetInfoByPetId(Long petId) {
        log.debug("Получение информации о питомце по ID: {}", petId);
        return petInfoRepository.findByPetId(petId);
    }

    @Override
    public List<Pet> searchPets(SearchPetDto searchDto) {
        log.debug("Поиск питомцев с параметрами: {}", searchDto);

        List<Pet> allPets = petRepository.findAll();

        // Применяем фильтры
        List<Pet> filteredPets = allPets.stream()
            .filter(pet -> applySpeciesFilter(pet, searchDto))
            .filter(pet -> applyBreedFilter(pet, searchDto))
            .filter(pet -> applyGenderFilter(pet, searchDto))
            .filter(pet -> applyAgeFilter(pet, searchDto))
            .filter(pet -> applyPriceFilter(pet, searchDto))
            .collect(Collectors.toList());

        // Применяем сортировки
        filteredPets = applySorting(filteredPets, searchDto);

        log.info("Найдено питомцев после фильтрации: {}", filteredPets.size());
        return filteredPets;
    }

    @Override
    public Optional<Pet> findPetById(Long id) {
        log.debug("Поиск питомца по ID: {}", id);
        return petRepository.findById(id);
    }

    private boolean applySpeciesFilter(Pet pet, SearchPetDto searchDto) {
        if (searchDto.getSpecies() == null) {
            return true;
        }
        return searchDto.getSpecies().equals(pet.getSpecies());
    }

    private boolean applyBreedFilter(Pet pet, SearchPetDto searchDto) {
        if (searchDto.getBreed() == null) {
            return true;
        }
        return searchDto.getBreed().equals(pet.getBreed());
    }

    private boolean applyGenderFilter(Pet pet, SearchPetDto searchDto) {
        if (searchDto.getGender() == null) {
            return true;
        }
        return searchDto.getGender().equals(pet.getGender());
    }

    private boolean applyAgeFilter(Pet pet, SearchPetDto searchDto) {
        BigDecimal petAge = pet.getAgeMonths();
        if (petAge == null) {
            return false;
        }

        int age = petAge.intValue();

        if (searchDto.getMinAgeMonths() > 0 && age < searchDto.getMinAgeMonths()) {
            return false;
        }

        if (searchDto.getMaxAgeMonths() > 0 && age > searchDto.getMaxAgeMonths()) {
            return false;
        }

        return true;
    }

    private boolean applyPriceFilter(Pet pet, SearchPetDto searchDto) {
        BigDecimal petPrice = pet.getPrice();
        if (petPrice == null) {
            return false;
        }

        if (searchDto.getMinPrice() != null && searchDto.getMinPrice() > 0) {
            if (petPrice.compareTo(BigDecimal.valueOf(searchDto.getMinPrice())) < 0) {
                return false;
            }
        }

        if (searchDto.getMaxPrice() != null && searchDto.getMaxPrice() > 0) {
            if (petPrice.compareTo(BigDecimal.valueOf(searchDto.getMaxPrice())) > 0) {
                return false;
            }
        }

        return true;
    }

    private List<Pet> applySorting(List<Pet> pets, SearchPetDto searchDto) {
        Comparator<Pet> comparator = null;

        // Приоритет: сначала сортировка по цене, потом по возрасту
        if (searchDto.getSortByPrice() != null) {
            comparator = createPriceComparator(searchDto.getSortByPrice());

            if (searchDto.getSortByAge() != null) {
                comparator = comparator.thenComparing(createAgeComparator(searchDto.getSortByAge()));
            }
        } else if (searchDto.getSortByAge() != null) {
            comparator = createAgeComparator(searchDto.getSortByAge());
        }

        if (comparator != null) {
            return pets.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        }
        return pets;
    }

    private Comparator<Pet> createAgeComparator(SortBy sortByAge) {
        Comparator<Pet> comparator = Comparator.comparing(
            pet -> pet.getAgeMonths() != null ? pet.getAgeMonths() : BigDecimal.ZERO,
            Comparator.nullsFirst(Comparator.naturalOrder())
        );

        // Инвертируем если нужно сортировать по убыванию
        if (sortByAge == SortBy.DESCENDING) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private Comparator<Pet> createPriceComparator(SortBy sortByPrice) {
        Comparator<Pet> comparator = Comparator.comparing(
            pet -> pet.getPrice() != null ? pet.getPrice() : BigDecimal.ZERO,
            Comparator.nullsFirst(Comparator.naturalOrder())
        );

        if (sortByPrice == SortBy.DESCENDING) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    public List<TopSoldPetsDto> getTop5SoldPets() {
        return cartRepository.findTop5SoldPets();
    }

    private void logFilterApplied(String filterName, Object value) {
        if (value != null) {
            log.debug("Применен фильтр {}: {}", filterName, value);
        }
    }
}