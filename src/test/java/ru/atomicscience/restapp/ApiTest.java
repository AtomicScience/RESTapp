package ru.atomicscience.restapp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import ru.atomicscience.restapp.config.UserJpaConfig;

@SpringBootTest
@ContextConfiguration(
        classes = { UserJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class ApiTest {
}
