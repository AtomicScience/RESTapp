package ru.atomicscience.restapp.debug;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.atomicscience.restapp.models.User;
import ru.atomicscience.restapp.util.RandomSequenceGenerator;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A component for generating a random amount of test User objects,
 * ready to be added to the system.
 *
 * Is used for debug/test purposes only, thus assumes that all the passed
 * data is valid
 */
@Component
public class RandomUserGenerator {
    private final UserFromJsonCreator userFromJsonCreator;
    private final RandomSequenceGenerator sequenceGenerator;
    private final JsonNode supernode;
    private final int maxAmountOfRandomUsers;

    @SneakyThrows // We can safely assume that all the data is valid
    public RandomUserGenerator(UserFromJsonCreator userFromJsonCreator) {
        this.userFromJsonCreator = userFromJsonCreator;

        ObjectMapper mapper = new ObjectMapper();
        this.supernode = mapper.readTree(new File("src/main/resources/exampleUsers.json"));

        this.maxAmountOfRandomUsers = this.supernode.size();
        this.sequenceGenerator = new RandomSequenceGenerator(maxAmountOfRandomUsers);
    }

    public List<User> generateTestUsers(int count) {
        if(count < 0 || count > maxAmountOfRandomUsers)
            throw new IllegalArgumentException
                    ("Illegal users count: it cannot be negative or exceed " + maxAmountOfRandomUsers);

        List<JsonNode> randomJsonObjects = getRandomJsonSubnodes(count);

        return userFromJsonCreator.constructUsersFromJsonObjects(randomJsonObjects);
    }


    private List<JsonNode> getRandomJsonSubnodes(int count) {
        List<Integer> randomNaturalNumbers = sequenceGenerator.generateRandomSequence(count);

        return randomNaturalNumbers.stream()
                .map(supernode::get)
                .collect(Collectors.toList());
    }
}
