package petstore.services;

import org.springframework.stereotype.Service;
import petstore.dto.AddCartDto;
import petstore.models.entities.Pet;
import petstore.models.entities.User;

import java.util.List;

@Service
public interface AddCartService {
    void addCart(AddCartDto addCartDto);
    List<Pet> getPetsInCart(User user);
    boolean isPetInOrders(Pet pet);
}
