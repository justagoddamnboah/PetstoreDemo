package petstore.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import petstore.dto.AddCartDto;
import petstore.models.entities.Pet;
import petstore.models.entities.PetInfo;
import petstore.models.entities.User;
import petstore.repositories.PetRepository;
import petstore.services.AddCartService;
import petstore.services.AuthService;
import petstore.services.FilterPetService;
import petstore.repositories.PetInfoRepository;
import petstore.services.RemovePetService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/catalogue/pet-card")
public class PetCardController {
    private final FilterPetService petService;
    private final PetInfoRepository petInfoRepository;
    private final PetRepository petRepository;
    private final RemovePetService removePetService;
    private final AuthService authService;
    private final AddCartService addCartService;


    public PetCardController(FilterPetService filterPetService, PetInfoRepository petInfoRepository, PetRepository petRepository, RemovePetService removePetService, AuthService authService, AddCartService addCartService) {
        this.petService = filterPetService;
        this.petInfoRepository = petInfoRepository;
        this.petRepository = petRepository;
        this.removePetService = removePetService;
        this.authService = authService;
        this.addCartService = addCartService;
    }

    @GetMapping("/{id}")
    public String petCard(@PathVariable("id") Long id, Model model) {
        System.out.println("=== DEBUG: ID получен: " + id + " ===");

        Optional<Pet> petOptional = petService.findPetById(id);

        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();

            PetInfo petInfo = petInfoRepository.findByPetId(id);

            // ДЛЯ ОТЛАДКИ - вывод в консоль
            System.out.println("=================================");
            System.out.println("Pet ID: " + pet.getId());
            System.out.println("Pet name: " + pet.getName());
            System.out.println("Is in orders: " + addCartService.isPetInOrders(pet));

            if (petInfo != null) {
                System.out.println("=== DEBUG: PetInfo найден: photoName=" +
                    petInfo.getPhotoName() + ", description=" + petInfo.getDescription());
            } else {
                System.out.println("=== DEBUG: PetInfo не найден для petId=" + id);
            }

            model.addAttribute("pet", pet);
            model.addAttribute("petInfo", petInfo); // Может быть null
            model.addAttribute("isPetInOrders", addCartService.isPetInOrders(pet));
            model.addAttribute("pageTitle", pet.getName() + " - Карточка питомца");
            return "pet-card";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable Long id, Principal principal,
                            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Для добавления в корзину необходимо войти в систему");
            return "redirect:/users/login";
        }
        String username = principal.getName();
        Optional<Pet> petOptional = petService.findPetById(id);

        if (petOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Питомец не найден");
            return "redirect:/catalogue";
        }
        Pet pet = petOptional.get();
        User user = authService.getUser(username);

        try {
            AddCartDto addCartDto = new AddCartDto();
            addCartDto.setUserId(user.getId());
            addCartDto.setPetId(pet.getId());
            addCartDto.setCreatedAt(LocalDateTime.now());

            log.info("Добавление в корзину: User ID = {}, Pet ID = {}", user.getId(), pet.getId());

            addCartService.addCart(addCartDto);

            redirectAttributes.addFlashAttribute("successMessage",
                "Питомец " + pet.getName() + " добавлен в корзину!");
            log.info("Питомец {} добавлен в корзину пользователю {}", pet.getName(), username);

        } catch (Exception e) {
            log.error("Ошибка при добавлении питомца в корзину: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                "Не удалось добавить питомца в корзину: " + e.getMessage());
        }
        return "redirect:/users/profile";
    }

    @PostMapping("/remove-pet/{id}")
    public String removePet(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {
        try {
            Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Питомец с ID " + id + " не найден"));

            removePetService.removePet(pet.getSpecies(), pet.getName());

            redirectAttributes.addFlashAttribute("successMessage",
                "Питомец '" + pet.getName() + "' (" + pet.getSpecies() + ") успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Ошибка при удалении питомца: " + e.getMessage());
        }
        return "redirect:/";
    }
}