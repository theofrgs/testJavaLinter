package com.outside.auth;

import com.outside.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.outside.user.AuthType;
import com.outside.EncryptService;
import com.outside.JWTService;
import com.outside.api.GoogleService;

@org.springframework.stereotype.Service
public class SignUpService extends com.outside.Service {
    private EncryptService encryptService = new EncryptService();
    private JWTService jwtService = new JWTService();
    private GoogleService googleService = new GoogleService();
    public static final Logger LOGGER = LogManager.getLogger(EncryptService.class);

    private String passwordStrengthCheck(String password) {
        String numberRegex = "^.*[0-9]+.*$";
        String upperRegex = "^.*[A-Z]+.*$";
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password);
        boolean isStringContainsSpecialCharacter = matcher.find();

        if (password.length() < 10)
            return "password must be at least 10 characters";
        if (!password.matches(numberRegex))
            return "password must have at least 1 number";
        if (!password.matches(upperRegex))
            return "password must have at least 1 upper case";
        if (!isStringContainsSpecialCharacter)
            return "password must have at least 1 special character (~!@#$%^&*()_+{}\\[\\]:;,.<>/?-)";
        return null;
    }

    private String checkArgs(String email, String password, String confirmPassword) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (email == null)
            return "Email cant be null";
        if (!pat.matcher(email).matches())
            return "Email is not an email";
        if (!password.equals(confirmPassword))
            return "password dont correspond to confirmPassword";
        return passwordStrengthCheck(password);
    }

    protected Map<String, Object> checkIfUserExists(String email) {
        User user = null;
        List<User> userList = null;

        user = new User();
        userList = user.db().find(User.class).where().eq("email", email).findList();
        if (userList.size() > 0)
            return Map.of(
                    "success", false,
                    "errorMsg", "User with this adress mail already exist");
        return Map.of("success", true, "user", user);
    }

    protected void registerUserNatifDatabase(User user, String email, String password) {
        user.setEmail(email);
        user.setPassword(encryptService.encryptStr(password));
        user.setVerified(true);
        user.setAuthType(AuthType.NATIVE);
        user.save();
    }

    @SuppressWarnings("unchecked")
    protected void registerGoogleDatabase(User user, String email, Map<String, Object> tokens) {
        user.setEmail(email);
        user.setRefreshToken(
                encryptService.encryptStr((String) ((Map<String, Object>) tokens.get("data")).get("refresh_token")));
        user.setAccessToken(
                encryptService.encryptStr((String) ((Map<String, Object>) tokens.get("data")).get("access_token")));
        user.setVerified(true);
        user.setAuthType(AuthType.GOOGLE);
        user.save();
    }

    protected void registerNames(User user) {
        user.setFirstname("#user" + user.getUci().toString());
        user.setLastname("#user" + user.getUci().toString());
        user.save();
    }

    public Map<String, Object> nativeSignUp(String email, String password, String confirmPassword) {
        User user = null;
        Map<String, Object> userExist = null;
        String error = checkArgs(email, password, confirmPassword);

        if (error != null) {
            return Map.of(
                    "success", false,
                    "errorMsg", error);
        }
        userExist = checkIfUserExists(email);
        if ((Boolean) userExist.get("success") == false) {
            return userExist;
        }
        user = (User) userExist.get("user");
        // sendEmail(email);
        registerUserNatifDatabase(user, email, password);
        registerNames(user);
        return Map.of(
                "success", true,
                "data", jwtService.createJWTToken(Map.of("user_id", user.getUci().toString())));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> googleSignUp(String authorizationCode) {
        User user = null;
        Map<String, Object> profile = null;
        Map<String, Object> userExist = null;
        String email = null;
        Map<String, Object> tokens = googleService.getRefreshToken(authorizationCode);
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
        userExist = checkIfUserExists(email);
        if ((Boolean) userExist.get("success") == false) {
            return userExist;
        }
        user = (User) userExist.get("user");
        registerGoogleDatabase(user, email, tokens);
        registerNames(user);
        return Map.of(
                "success", true,
                "data", jwtService.createJWTToken(Map.of("user_id", user.getUci().toString())));
    }
}
