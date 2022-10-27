package com.outside.controller.auth;

import com.outside.auth.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequestMapping("/auth/login")
@RestController
public class LoginController {

    private LoginService loginService = new LoginService();

    @Operation(summary = "Login with our app", responses = {
            @ApiResponse(responseCode = "200", description = "Success login"),
            @ApiResponse(responseCode = "400", description = "Failure login"),
    })
    @PostMapping(value = "/native", headers = { "content-type=application/x-www-form-urlencoded" })
    public ResponseEntity<Map<String, Object>> nativeLogin(@RequestBody String email, @RequestBody String password) {
        Map<String, Object> rslt = loginService.nativeLogin(email, password);
        boolean success = (boolean) rslt.get("success");

        return new ResponseEntity<>(
                (Map<String, Object>) (success ? Map.of("token", rslt.get("data"))
                        : Map.of("error_msg", rslt.get("errorMsg"))),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
