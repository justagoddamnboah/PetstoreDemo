package petstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import petstore.dto.AddCartDto;
import petstore.services.AddCartService;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class AddCartController {

    private final AddCartService addCartService;

    @PostMapping("/add")
    public String addToCart(@Valid AddCartDto addCartDto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        log.debug("Добавление питомца {} в корзину пользователя {}",
            addCartDto.getPetId(), addCartDto.getUserId());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/catalogue/pet/" + addCartDto.getPetId();
        }

        try {
            addCartService.addCart(addCartDto);
            redirectAttributes.addFlashAttribute("successMessage",
                "Питомец успешно добавлен в корзину!");
        } catch (Exception e) {
            log.error("Ошибка при добавлении в корзину: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                "Не удалось добавить питомца в корзину: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}