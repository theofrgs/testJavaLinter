package com.outside.controller.auth;

import com.outside.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class VerifyEmailController {
    private EmailService emailService = new EmailService();

    @Operation(summary = "Send email of verification", responses = {
            @ApiResponse(responseCode = "200", description = "Code send in email"),
            @ApiResponse(responseCode = "400", description = "Email fail"),
    })
    @PostMapping(value = "/verify", headers = { "content-type=application/x-www-form-urlencoded" })
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody String email) {
        Map<String, Object> rslt = emailService.sendVerifEmail(email);
        boolean success = (boolean) rslt.get("success");

        return new ResponseEntity<>(
                (Map<String, Object>) (success ? Map.of("code", rslt.get("code"))
                        : Map.of("error_msg", rslt.get("errorMsg"))),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
