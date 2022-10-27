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

@RequestMapping("/auth/connect")
@RestController
public class ConnectController {

    private LoginService loginService = new LoginService();

    @Operation(summary = "Connect with Google", responses = {
            @ApiResponse(responseCode = "200", description = "Success Connect"),
            @ApiResponse(responseCode = "400", description = "Failure Connect"),
    })
    @PostMapping(value = "/google", headers = { "content-type=application/x-www-form-urlencoded" })
    public ResponseEntity<Map<String, Object>> googleConnect(@RequestBody String authorizationCode) {
        System.out.println(authorizationCode);
        Map<String, Object> rslt = loginService.googleLogin(authorizationCode);
        boolean success = (boolean) rslt.get("success");

        return new ResponseEntity<>(
                (Map<String, Object>) (success ? Map.of("token", rslt.get("data"))
                        : Map.of("error_msg", rslt.get("errorMsg"))),
                success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
