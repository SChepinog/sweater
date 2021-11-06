package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.repos.MessageRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    private final MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/")
    public String greeting(
            Map<String, Object> model
    ) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            Map<String, Object> model,
            @RequestParam String text,
            @RequestParam String tag
    ) {
        Message message = new Message(text, tag);
        messageRepo.save(message);
        model.put("messages", messageRepo.findAll());
        return "main";
    }

    @PostMapping("/filter")
    public String filter(
            Map<String, Object> model,
            @RequestParam String filter
    ) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
//        messageRepo.save(message);
        model.put("messages", messages);
        return "main";
    }
}