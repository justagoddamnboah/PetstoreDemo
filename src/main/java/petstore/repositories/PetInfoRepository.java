package petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petstore.models.entities.PetInfo;

@Repository
public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {
    PetInfo findByPetId(Long petId);
}