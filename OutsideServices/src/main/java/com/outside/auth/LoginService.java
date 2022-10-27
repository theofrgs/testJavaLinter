package com.outside.auth;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.outside.api.GoogleService;
import com.outside.user.AuthType;
import com.outside.user.User;
import com.outside.EncryptService;
import com.outside.JWTService;
import com.outside.Service;

@org.springframework.stereotype.Service
public class LoginService extends Service {
    private EncryptService encryptService = new EncryptService();
    private JWTService jwtService = new JWTService();
    private GoogleService googleService = new GoogleService();
    private SignUpService signUpService = new SignUpService();

    public LoginService() {
        this.initLogger(LoginService.class);
    }

    private String checkArgs(String email, String password) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (email == null)
            return "Email cant be null";
        if (!pat.matcher(email).matches())
            return "Email is not an email";
        return null;
    }

    public Map<String, Object> nativeLogin(String email, String password) {
        User user = null;
        List<User> users = null;
        String error = checkArgs(email, password);
        String decryptPassword = null;

        if (error != null) {
            return Map.of(
                    "success", false,
                    "errorMsg", error);
        }
        user = new User();
        users = user.db().find(User.class).where().eq("email", email).findList();
        if (users.size() < 1)
            return Map.of(
                    "success", false,
                    "errorMsg", "User not found");
        decryptPassword = encryptService.decryptStr(users.get(0).getPassword());
        if (decryptPassword == null || decryptPassword.equals(password) == false) {
            return Map.of(
                    "success", false,
                    "errorMsg", "Password incorrect");
        }
        return Map.of(
                "success", true,
                "data", jwtService.createJWTToken(Map.of("user_id", users.get(0).getUci().toString())));
    }

    public String formatAuthorizatioCode(String authorizatioCode) {
        return authorizatioCode.replace("%2F", "/");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> googleLogin(String authorizatioCode) {
        User user = null;
        String email = null;
        Map<String, Object> profile = null;
        List<User> users = null;
        Map<String, Object> tokens = googleService.getRefreshToken(formatAuthorizatioCode(authorizatioCode));
        boolean success = ((boolean) tokens.get("success"));

        if (!success) {
            return Map.of(
                    "success", success,
                    "errorMsg", tokens.get("errorMsg"));
        }
        profile = googleService.getProfilInfo((String) ((Map<String, Object>) tokens.get("data")).get("access_token"));
        if (((boolean) profile.get("success")) == false) {
            return Map.of(
                    "success", success,
                    "errorMsg", profile.get("errorMsg"));
        }
        email = (String) ((Map<String, Object>) profile.get("profile")).get("email");
        user = new User();
        users = user.db().find(User.class).where()
                .eq("email", email).findList();
        if (users.size() < 1) {
            signUpService.registerGoogleDatabase(user, email, tokens);
            signUpService.registerNames(user);
            return Map.of(
                    "success", true,
                    "data", jwtService.createJWTToken(Map.of("user_id", user.getUci().toString())));
        } else {
            if (users.get(0).getAuthType() != AuthType.GOOGLE) {
                return Map.of(
                        "success", false,
                        "errorMsg", "User with this adress mail register with nativ account");
            }
            return Map.of(
                    "success", true,
                    "data", jwtService.createJWTToken(Map.of("user_id", users.get(0).getUci().toString())));
        }
    }
}
