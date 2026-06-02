package com.gym.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",
        chatMemoryProvider = "gymAssistantChatMemoryProvider",
        tools = "gymAssistantTools", //tools配置
        contentRetriever = "gymAssistantContentRetrieverPincone" //配置向量存储
)
public interface GymAssistant {
    @SystemMessage(fromResource = "gym-prompt-template.txt")
    Flux<String> chat(@MemoryId String memoryId, @V(value = "会员账号") int memberAccount, @UserMessage String userMessage);
}
