package petstore.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import petstore.dto.AddPetDto;
import petstore.services.AddPetService;

@Slf4j
@Controller
@RequestMapping("/")
public class AddPetController {

    private final AddPetService addPetService;

    public AddPetController(AddPetService addPetService) {
        this.addPetService = addPetService;
        log.info("AddPetController инициализирован");
    }

    @GetMapping("/pet-add")
    public String addPet() {
        log.debug("Отображение формы добавления питомца");
        return "pet-add";
    }

    @ModelAttribute("petModel")
    public AddPetDto initPet() {
        return new AddPetDto();
    }

    @PostMapping("/pet-add")
    public String addPet(@Valid AddPetDto petModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка POST запроса на добавление питомца");

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при добавлении питомца: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("petModel", petModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.petModel",
                bindingResult);
            return "redirect:/";
        }

        addPetService.addPet(petModel);
        redirectAttributes.addFlashAttribute("successMessage",
            "Питомец '" + petModel.getSpecies() + petModel.getName() + "' успешно добавлен!");

        return "redirect:/";
    }
}