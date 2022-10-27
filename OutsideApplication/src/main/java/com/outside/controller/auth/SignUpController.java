package com.outside.controller.auth;

import com.outside.auth.SignUpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequestMapping("/auth/signup")
@RestController
public class SignUpController {

    private SignUpService signUpService = new SignUpService();

    @Operation(summary = "Register with our app", responses = {
            @ApiResponse(responseCode = "200", description = "Success register"),
            @ApiResponse(responseCode = "400", description = "Failure register"),
    })
    @PostMapping(value = "/native", headers = { "content-type=application/x-www-form-urlencoded" })
    public ResponseEntity<Map<String, Object>> nativeSignUp(@RequestBody String email,
            @RequestBody String password,
            @RequestBody String confirmPassword) {
        Map<String, Object> rslt = signUpService.nativeSignUp(email, password, confirmPassword);
        boolean success = (boolean) rslt.get("success");

        return new ResponseEntity<>(
                (Map<String, Object>) (success ? Map.of("token", rslt.get("data"))
                        : Map.of("error_msg", rslt.get("errorMsg"))),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
