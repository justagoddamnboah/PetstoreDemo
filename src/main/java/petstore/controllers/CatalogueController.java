package petstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import petstore.dto.SearchPetDto;
import petstore.models.entities.Pet;
import petstore.services.FilterPetService;

import java.util.List;

@Controller
public class CatalogueController {
    private final FilterPetService petService;

    public CatalogueController(FilterPetService filterPetService) {
        this.petService = filterPetService;
    }
    @GetMapping("/catalogue")
    public String showCatalogue() {
        return "catalogue";
    }
    @PostMapping("/catalogue")
    public String searchPets(@ModelAttribute("searchDto") SearchPetDto searchDto, Model model) {
        List<Pet> filteredPets = petService.searchPets(searchDto);

        model.addAttribute("allPets", filteredPets);
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("pageTitle", "Результаты поиска");

        return "catalogue";
    }
}