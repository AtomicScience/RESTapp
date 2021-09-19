package ru.atomicscience.restapp.debug;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.security.UserRole;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple class for creating User instances from the JsonNodes
 * Is used for debug/test purposes only, thus assumes that all the passed
 * data is valid
 */
@Component
public class UserFromJsonCreator {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final PasswordEncoder encoder;

    public UserFromJsonCreator(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public List<User> constructUsersFromJsonObjects(List<JsonNode> rawJsonObjects) {
        return rawJsonObjects.stream()
                .map(this::constructSingleUserFromJson)
                .collect(Collectors.toList());
    }

    public User constructSingleUserFromJson(JsonNode jsonNode) {
        User user = new User();
        
        user.setLogin(jsonNode.get("login").asText());
        user.setFirstName(jsonNode.get("firstName").asText());
        user.setLastName(jsonNode.get("lastName").asText());
        user.setBirthday(getBirthdayAsDate(jsonNode));
        user.setPassword(hashPassword(jsonNode));
        user.setAboutMe(jsonNode.get("aboutMe").asText());
        user.setAddress(jsonNode.get("address").asText());
        user.setRole(UserRole.valueOf(jsonNode.get("role").asText()));

        return user;
    }

    @SneakyThrows // We can safely assume that all the data is valid
    private Date getBirthdayAsDate(JsonNode jsonNode) {
        return DATE_FORMAT.parse(jsonNode.get("birthday").asText());
    }

    private String hashPassword(JsonNode jsonNode) {
        String rawPassword = jsonNode.get("password").asText();

        return encoder.encode(rawPassword);
    }
}
