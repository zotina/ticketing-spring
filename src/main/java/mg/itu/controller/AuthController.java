package mg.itu.controller;

import mg.itu.security.JwtTokenProvider;
import mg.itu.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HttpSession session;

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        session.removeAttribute("jwtToken"); 
        return "views/auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        try {
            Authentication auth = authService.authenticateAdmin(email, password);
            return "redirect:/reservations"; 
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "views/auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken != null) {
            jwtTokenProvider.invalidateToken(jwtToken); 
        }
        session.removeAttribute("jwtToken"); 
        SecurityContextHolder.clearContext(); 
        return "redirect:/auth/login"; 
    }
}