package ru.atomicscience.restapp.debug;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.atomicscience.restapp.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RandomUserGeneratorTest {
    @Autowired
    private RandomUserGenerator generator;

    @Test
    void Test_RequestWithZeroReturnsEmptyList() {
        List<User> returns = generator.generateTestUsers(0);

        assertEquals(0, returns.size());
    }

    @Test
    void Test_OutOfBoundsValueInvokeExceptions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateTestUsers(-1)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateTestUsers(1001)
        );
    }

    @Test
    void Test_RequestReturnsCorrectAmountOfObjects() {
        List<User> users = generator.generateTestUsers(100);

        assertEquals(100, users.size());
    }
}