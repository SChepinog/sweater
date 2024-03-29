package com.example.sweater.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;

@Controller
public class MainController {
    private final MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Map<String, Object> model) {
        model.put("user", user);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
        @AuthenticationPrincipal User user,
        Map<String, Object> model,
        @RequestParam String text,
        @RequestParam String tag
    ) {
        Message message = new Message(text, tag, user);
        messageRepo.save(message);
        model.put("messages", messageRepo.findAll());
        return "main";
    }
}
