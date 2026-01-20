package petstore.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import petstore.dto.UserRegistrationDto;
import petstore.models.entities.Pet;
import petstore.models.entities.Role;
import petstore.models.entities.User;
import petstore.services.AddCartService;
import petstore.services.AuthService;
import petstore.view_models.UserProfileView;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;
    private final AddCartService addCartService;

    public AuthController(AuthService authService, AddCartService addCartService) {
        this.authService = authService;
        this.addCartService = addCartService;
        log.info("AuthController инициализирован");
    }

    @ModelAttribute("userRegistrationDto")
    public UserRegistrationDto initForm() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public String register() {
        log.debug("Отображение страницы регистрации");
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegistrationDto userRegistrationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка регистрации пользователя: {}", userRegistrationDto.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при регистрации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/users/register";
        }

        this.authService.register(userRegistrationDto);
        log.info("Пользователь успешно зарегистрирован: {}", userRegistrationDto.getUsername());

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("Отображение страницы входа");
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
        @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
        String password, RedirectAttributes redirectAttributes) {

        log.warn("Неудачная попытка входа для пользователя: {}, Нужный пароль: {}", username, password);
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        System.out.println();
        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (principal == null) {
            log.debug("Пользователь не аутентифицирован, перенаправление на логин");
            return "redirect:/users/login";
        }

        String username = principal.getName();
        log.debug("Отображение профиля пользователя: {}", username);

        User user = authService.getUser(username);

        // Получаем питомцев в корзине
        List<Pet> petsInCart = addCartService.getPetsInCart(user);

        // Создаем View Model - используем user.getRoles()
        UserProfileView userProfileView = new UserProfileView(
            username,
            user.getEmail(),
            user.getAge(),
            user.getRoles(),
            petsInCart
        );

        model.addAttribute("user", userProfileView);
        model.addAttribute("hasRoles", userProfileView.getRoles() != null && !userProfileView.getRoles().isEmpty());
        model.addAttribute("pageTitle", "Профиль пользователя: " + username);
        model.addAttribute("petsInCart", petsInCart);

        return "profile";
    }
}