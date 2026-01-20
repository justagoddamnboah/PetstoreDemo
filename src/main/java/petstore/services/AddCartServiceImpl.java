package petstore.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petstore.dto.AddCartDto;
import petstore.models.entities.Order;
import petstore.models.entities.Pet;
import petstore.models.entities.User;
import petstore.repositories.CartRepository;
import petstore.repositories.PetRepository;
import petstore.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddCartServiceImpl implements AddCartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public AddCartServiceImpl(CartRepository cartRepository, UserRepository userRepository, PetRepository petRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void addCart(AddCartDto addCartDto) {
        // Получаем пользователя по ID
        User user = userRepository.findById(addCartDto.getUserId())
            .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + addCartDto.getUserId()));

        // Получаем питомца по ID
        Pet pet = petRepository.findById(addCartDto.getPetId())
            .orElseThrow(() -> new RuntimeException("Питомец не найден с ID: " + addCartDto.getPetId()));

        // ПРОВЕРКА: Есть ли уже этот питомец в корзине пользователя?
        boolean alreadyInCart = cartRepository.existsByUserAndPet(user, pet);

        if (alreadyInCart) {
            throw new RuntimeException("Питомец '" + pet.getName() + "' уже в вашей корзине");
        }

        // Создаем заказ (корзину)
        Order order = new Order();
        order.setUser(user);
        order.setPet(pet);
        order.setCreatedAt(addCartDto.getCreatedAt() != null ? addCartDto.getCreatedAt() : LocalDateTime.now());

        cartRepository.save(order);
    }

    @Override
    //@Cacheable(value = "pets_in_cart", key = "#user.id")
    public List<Pet> getPetsInCart(User user) {
        List<Order> orders = cartRepository.findByUser(user);
        return orders.stream()
            .map(Order::getPet)
            .toList();
    }

    @Override
    public boolean isPetInOrders(Pet pet) {
        return cartRepository.existsByPet(pet);
    }
}