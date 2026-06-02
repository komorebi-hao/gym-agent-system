package com.gym.controller;

import com.gym.assistant.GymAssistant;
import com.gym.bean.ChatForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class ApiChatController {

    @Autowired
    private GymAssistant gymAssistant;

    @PostMapping(value = "/query", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> query(@RequestBody ChatForm chatForm) {
        return gymAssistant.chat(chatForm.getMemoryId(), chatForm.getMemberAccount(), chatForm.getMessage())
                .onErrorResume(e -> Flux.just("错误：" + e.getMessage()));
    }

}