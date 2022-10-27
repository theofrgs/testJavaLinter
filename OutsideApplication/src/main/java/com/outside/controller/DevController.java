package com.outside.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.Claim;
import com.outside.EncryptService;
import com.outside.JWTService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RequestMapping("/dev")
@RestController
@ConditionalOnProperty(value = "com.outside.prod", havingValue = "false")
public class DevController {

    private JWTService jwtService = new JWTService();
    private EncryptService encryptService = new EncryptService();

    @PostMapping("/check-jwt")
    public ResponseEntity<Map<String, Object>> checkJwt(@RequestParam String jwt) {
        boolean success = (boolean) jwtService.checkToken(jwt);

        return new ResponseEntity<>(
                Map.of("success", success),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/jwt-content")
    public ResponseEntity<Map<String, Object>> getJwtContent(@RequestParam String jwt) {
        Map<String, Claim> claims = jwtService.getTokenContent(jwt);
        boolean success = claims != null;

        return new ResponseEntity<>(
                Map.of("success", success, success ? "data" : "error",
                        success ? claims.get("data").asMap() : "Error with token"),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/encrypt")
    public ResponseEntity<Map<String, Object>> getStringEncrypted(@RequestParam String str) {
        String encryptString = encryptService.encryptStr(str);
        boolean success = encryptString != null;

        return new ResponseEntity<>(
                Map.of("success", success, "data", success ? encryptString : "Error during the encryption"),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<Map<String, Object>> getStringDecrypted(@RequestParam String str) {
        String encryptString = encryptService.decryptStr(str);
        boolean success = encryptString != null;

        return new ResponseEntity<>(
                Map.of("success", success, "data", success ? encryptString : "Error during the decryption"),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
