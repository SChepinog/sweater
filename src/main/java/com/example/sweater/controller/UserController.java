package com.example.sweater.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {

    final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping("{user}")
    public String userSave(
        @RequestParam("userId") User user,
        @RequestParam Map<String, String> form,
        @RequestParam String username
    ) {
        user.setUsername(username);
        user.getRoles().clear();

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        for (String role : form.keySet()) {
            if (roles.contains(role)) {
                user.getRoles().add(Role.valueOf(role));
            }
        }

        userRepo.save(user);
        return "redirect:";
    }
}
