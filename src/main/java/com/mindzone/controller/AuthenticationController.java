package com.mindzone.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static com.mindzone.constants.Constants.*;
import static org.springframework.http.HttpMethod.POST;

@RestController
@RequestMapping(V1 + "/auth")
public class AuthenticationController {

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody AuthCodeRequest authCodeRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                authCodeRequest.getCode(), CLIENT_ID, CLIENT_SECRET, "your-redirect-uri");

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, POST, request, String.class);


        return ResponseEntity.ok(response.getBody());
    }

    @Getter
    @AllArgsConstructor
    public static class AuthCodeRequest {
        private String code;
    }
}