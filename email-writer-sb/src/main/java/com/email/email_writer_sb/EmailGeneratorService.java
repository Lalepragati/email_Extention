package com.email.email_writer_sb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service   // help us calling api call
public class EmailGeneratorService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiapiKey;

    // constructor
    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    public String GenerateEmailReply(EmailRequest emailRequest) {
        // 1. Build Prompt which goes to backend as i/p
        String prompt = buildPrompt(emailRequest);

        // 2. craft req
        Map<String , Object> reqBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", prompt)
                        })
                }
        );

        // 3. do req and get res need api key but not going to do
        /* api calling using webclient enables handle asynchronous user request
            >add reactive web dependency */
        String response = webClient.post()
                .uri(geminiApiUrl + geminiapiKey)
                // Fixed header name - no trailing space, use constant for safety
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(reqBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // 4. extract res & return
        /* extract the content and return  res  to api */
        return extractResponseContent(response);
    }

    public String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email reply, please don't generate subject line  \n");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Use a").append(emailRequest.getTone()).append("Tone");
        }
        prompt.append("\n Original email :\n").append(emailRequest.getEmailcontent());
        return prompt.toString();
    }

    public String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // tool from json lib r, w 7 convert data from json->java obj vice versa
            JsonNode rootNode = mapper.readTree(response);
            // readTree(response) it method turn json res to tree like structure which is rep by treenode node :- easy navigate data
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch(Exception e) {
            return("Error Processing request :"  + e.getMessage());
        }
    }
}
