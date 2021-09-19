package ru.atomicscience.restapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.atomicscience.restapp.dao.UsersCrudRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsersCrudRepository repository;

    @BeforeEach
    void populateDatabase() {

    }

    @Test
    void sampleTest() throws Exception {
        mockMvc
                .perform(get("/users")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
