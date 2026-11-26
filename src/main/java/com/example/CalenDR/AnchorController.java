package com.example.CalenDR;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class AnchorController {

    Model signedIn(HttpServletRequest request, Model model){
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    @GetMapping("/")
    String home(HttpServletRequest request, Model model) throws SQLException{
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "index";
    }

    @GetMapping("about")
    String about(HttpServletRequest request, Model model){
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "about";
    }

    @GetMapping("about-david")
    String aboutDavid(HttpServletRequest request, Model model){
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "about-david";
    }

    @GetMapping("contact")
    String contact(HttpServletRequest request, Model model){
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "contact";
    }

    @GetMapping("services")
    String services(HttpServletRequest request, Model model){
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "services";
    }

    @GetMapping("login")
    String login(Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException, InterruptedException{
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    @GetMapping("login/fail")
    String loginFail(Model model){
        return "login/fail";
    }

    @GetMapping("signup")
    String signup(Model model){
        return "signup";
    }

    @GetMapping("signupfail")
    String signupfail(Model model){
        return "signupfail";
    }

    @GetMapping("FAQ")
    String faq(HttpServletRequest request, Model model){
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "FAQ";
    }

    @GetMapping("tos")
    String tos(HttpServletRequest request, Model model){
        model = signedIn(request, model);
        if(model.containsAttribute("error")) return "/error";

        return "tos";
    }

    @GetMapping("password-reset")
    String resetpw(HttpServletRequest request, Model model){
        return "password-reset";
    }

    @GetMapping("password-reset-fail")
    String resetpwfail(HttpServletRequest request, Model model){
        return "password-reset-fail";
    }

    @GetMapping("verification-sent")
    String verificationsent(HttpServletRequest request, Model model){
        return "verification-sent";
    }

    @GetMapping("verification")
    String verification(@RequestParam int type, @RequestParam String token, @RequestParam String email, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes){
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }
}
