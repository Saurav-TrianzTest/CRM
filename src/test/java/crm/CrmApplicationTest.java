package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrmApplicationTest {

    @Test
    void testConstructor() {
        // When
        CrmApplication application = new CrmApplication();

        // Then
        assertNotNull(application);
    }

    @Test
    void testMainMethod() {
        // Given
        String[] args = {};
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testMainMethodWithNullArgs() {
        // Given
        String[] args = null;
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testMainMethodWithEmptyArgs() {
        // Given
        String[] args = {};
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testMainMethodWithMultipleArgs() {
        // Given
        String[] args = {"--server.port=8080", "--spring.profiles.active=dev", "--debug"};
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testMainMethodWithSingleArg() {
        // Given
        String[] args = {"--server.port=9090"};
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testClassHasSpringBootApplicationAnnotation() {
        // When
        boolean hasAnnotation = CrmApplication.class.isAnnotationPresent(SpringBootApplication.class);

        // Then
        assertTrue(hasAnnotation, "CrmApplication should have @SpringBootApplication annotation");
    }

    @Test
    void testSpringBootApplicationAnnotationAttributes() {
        // When
        SpringBootApplication annotation = CrmApplication.class.getAnnotation(SpringBootApplication.class);

        // Then
        assertNotNull(annotation);
        // Verify default annotation attributes
        assertEquals(0, annotation.scanBasePackageClasses().length);
        assertEquals(0, annotation.exclude().length);
    }

    @Test
    void testMainMethodIsStatic() throws Exception {
        // When
        int modifiers = CrmApplication.class.getMethod("main", String[].class).getModifiers();

        // Then
        assertTrue(java.lang.reflect.Modifier.isStatic(modifiers), "main method should be static");
    }

    @Test
    void testMainMethodIsPublic() throws Exception {
        // When
        int modifiers = CrmApplication.class.getMethod("main", String[].class).getModifiers();

        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers), "main method should be public");
    }

    @Test
    void testMainMethodIsVoid() throws Exception {
        // When
        Class<?> returnType = CrmApplication.class.getMethod("main", String[].class).getReturnType();

        // Then
        assertEquals(void.class, returnType, "main method should return void");
    }

    @Test
    void testMainMethodHasCorrectParameterType() throws Exception {
        // When
        Class<?>[] parameterTypes = CrmApplication.class.getMethod("main", String[].class).getParameterTypes();

        // Then
        assertEquals(1, parameterTypes.length, "main method should have one parameter");
        assertEquals(String[].class, parameterTypes[0], "main method parameter should be String[]");
    }

    @Test
    void testClassIsPublic() {
        // When
        int modifiers = CrmApplication.class.getModifiers();

        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers), "CrmApplication class should be public");
    }

    @Test
    void testClassPackage() {
        // When
        String packageName = CrmApplication.class.getPackage().getName();

        // Then
        assertEquals("crm", packageName, "CrmApplication should be in 'crm' package");
    }

    @Test
    void testMainMethodExistsAndIsAccessible() throws Exception {
        // When/Then
        assertDoesNotThrow(() -> {
            CrmApplication.class.getMethod("main", String[].class);
        }, "main method should exist and be accessible");
    }

    @Test
    void testApplicationCanBeInstantiated() {
        // When/Then
        assertDoesNotThrow(() -> {
            CrmApplication app = new CrmApplication();
            assertNotNull(app);
        }, "CrmApplication should be instantiable");
    }

    @Test
    void testMultipleInstancesCanBeCreated() {
        // When
        CrmApplication app1 = new CrmApplication();
        CrmApplication app2 = new CrmApplication();

        // Then
        assertNotNull(app1);
        assertNotNull(app2);
        assertNotSame(app1, app2, "Multiple instances should be different objects");
    }

    @Test
    void testMainMethodCallsSpringApplicationRun() {
        // Given
        String[] args = {"--test=true"};
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(eq(CrmApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testMainMethodWithProfileArgument() {
        // Given
        String[] args = {"--spring.profiles.active=production"};
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testMainMethodWithDatabaseArguments() {
        // Given
        String[] args = {
                "--spring.datasource.url=jdbc:mysql://localhost:3306/crm",
                "--spring.datasource.username=admin",
                "--spring.datasource.password=secret"
        };
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(CrmApplication.class, args))
                    .thenReturn(mockContext);

            CrmApplication.main(args);

            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(CrmApplication.class, args), times(1));
        }
    }

    @Test
    void testClassSimpleName() {
        // When
        String simpleName = CrmApplication.class.getSimpleName();

        // Then
        assertEquals("CrmApplication", simpleName);
    }

    @Test
    void testClassCanonicalName() {
        // When
        String canonicalName = CrmApplication.class.getCanonicalName();

        // Then
        assertEquals("crm.CrmApplication", canonicalName);
    }
}
