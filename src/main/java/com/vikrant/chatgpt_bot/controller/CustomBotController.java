package com.vikrant.chatgpt_bot.controller;

import com.vikrant.chatgpt_bot.dto.ChatGPTRequest;
import com.vikrant.chatgpt_bot.dto.ChatGptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);

        // Check if the response is null or empty
        if (chatGptResponse == null || chatGptResponse.getChoices() == null || chatGptResponse.getChoices().isEmpty()) {
            return "No response received from the API";
        }

        // Check if the first choice is null
        ChatGptResponse.Choice firstChoice = chatGptResponse.getChoices().get(0);
        if (firstChoice == null || firstChoice.getMessage() == null) {
            return "No valid response received from the API";
        }

        // Return the content of the first message
        return firstChoice.getMessage().getContent();
    }
}
