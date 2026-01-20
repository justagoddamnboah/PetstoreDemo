package petstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import petstore.dto.SearchPetDto;
import petstore.dto.TopSoldPetsDto;
import petstore.models.entities.Pet;
import petstore.services.FilterPetService;

import java.util.List;

@Controller
public class HomeController {

    private final FilterPetService petService;

    public HomeController(FilterPetService filterPetService) {
        this.petService = filterPetService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Pet> allPets = petService.findAllPets();

        List<TopSoldPetsDto> topSoldPets = petService.getTop5SoldPets();

        System.out.println("=== DEBUG: Found " + allPets.size() + " pets ===");
        System.out.println("=== DEBUG: Top sold pets: " + topSoldPets.size() + " species ===");

        for (TopSoldPetsDto dto : topSoldPets) {
            System.out.println("Species: " + dto.getSpecies() + ", Count: " + dto.getOrderCount());
        }

        model.addAttribute("allPets", allPets);
        model.addAttribute("topSoldPets", topSoldPets);
        model.addAttribute("pageTitle", "Главная страница");
        model.addAttribute("searchDto", new SearchPetDto());

        return "home";
    }
}