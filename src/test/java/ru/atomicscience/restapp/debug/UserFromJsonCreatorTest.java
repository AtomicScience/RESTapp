package ru.atomicscience.restapp.debug;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.atomicscience.restapp.models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserFromJsonCreatorTest {
    @Autowired
    private UserFromJsonCreator creator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void Test_SingleInput_ValidStringData() {
        JsonNode json = getSingleValidJsonNode();

        User createdUser = creator.constructSingleUserFromJson(json);

        assertEquals("EmlynBraden", createdUser.getLogin());
        assertEquals("Emlyn", createdUser.getFirstName());
        assertEquals("Braden", createdUser.getLastName());

        String aboutMe = "Hello! I live in Tajikistan, I like playing Monopoly, and my favorite movie is Terminator Salvation";
        assertEquals(aboutMe, createdUser.getAboutMe());
        assertEquals("Tajikistan", createdUser.getAddress());
        assertEquals("USER" , createdUser.getRole().toString());
    }

    @Test
    void Test_SingleInput_ValidPassword() {
        JsonNode json = getSingleValidJsonNode();

        User createdUser = creator.constructSingleUserFromJson(json);

        assertTrue(passwordEncoder.matches("EmlynBraden", createdUser.getPassword()));
    }

    @Test
    void Test_SingleInput_ValidDate() {
        JsonNode json = getSingleValidJsonNode();

        User createdUser = creator.constructSingleUserFromJson(json);

        Calendar cal = Calendar.getInstance();
        cal.setTime(createdUser.getBirthday());

        System.out.println(createdUser.getBirthday());

        assertEquals(1936, cal.get(Calendar.YEAR));
        // Months start at 0, thus 5th month is represented as 4 within the Calendar
        assertEquals(4, cal.get(Calendar.MONTH));
        assertEquals(19, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void Test_MultipleInput() {
        List<JsonNode> nodes = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            nodes.add(getSingleValidJsonNode());
        }

        List<User> users = creator.constructUsersFromJsonObjects(nodes);

        assertEquals(10, users.size());
    }

    @SneakyThrows
    private JsonNode getSingleValidJsonNode() {
        ObjectMapper mapper = new ObjectMapper();

        // Let's run our tests on the first user in the users list
        return mapper.readTree(new File("src/main/resources/exampleUsers.json")).get(0);
    }
}