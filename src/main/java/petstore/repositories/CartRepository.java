package petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import petstore.dto.TopSoldPetsDto;
import petstore.models.entities.Order;
import petstore.models.entities.Pet;
import petstore.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndPet(User user, Pet pet);
    List<Order> findByUser(User user);
    Optional<Order> findByPet(Pet pet);
    List<Order> findAllByUser(User user);
    boolean existsByUserAndPet(User user, Pet pet);

    @Query("SELECT new petstore.dto.TopSoldPetsDto(p.species, COUNT(o.id)) " +
        "FROM Order o " +
        "JOIN o.pet p " +
        "GROUP BY p.species " +
        "ORDER BY COUNT(o.id) DESC " +
        "LIMIT 5")
    List<TopSoldPetsDto> findTop5SoldPets();

    boolean existsByPet(Pet pet);
}