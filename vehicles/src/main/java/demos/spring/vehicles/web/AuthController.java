package demos.spring.vehicles.web;

import demos.spring.vehicles.model.Offer;
import demos.spring.vehicles.model.User;
import demos.spring.vehicles.service.AuthService;
import demos.spring.vehicles.service.BrandService;
import demos.spring.vehicles.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String getOfferForm(@ModelAttribute("user") User user) {
        return "auth-register";
    }

    @PostMapping("/register")
    public String createNewOffer(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if(result.hasErrors()) {
            log.error("Error registering user: {}", result.getAllErrors());
            return "auth-register";
        }
        try {
            User registeredUser = authService.register(user);
            return "redirect:login";
        } catch(Exception ex) {
            log.error("Error registering user", ex);
            return "auth-register";
        }
    }

    @GetMapping("/login")
    public String getLoginForm(Model model) {
        if(model.getAttribute("username") == null) {
            model.addAttribute("username", "");
        }
        if(model.getAttribute("password") == null) {
            model.addAttribute("password", "");
        }
        return "auth-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @ModelAttribute("redirectUrl") String redirectUrl,
                        RedirectAttributes redirectAttributes,
                        HttpSession session){
        User loggedUser = authService.login(username, password);
        if(loggedUser == null) {
            String errors = "Invalid username or password.";
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:login";
        } else {
            session.setAttribute("user", loggedUser);
            if(redirectUrl != null && redirectUrl.trim().length() > 0) {
                return "redirect:" + redirectUrl;
            } else {
                return "redirect:/";
            }
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
