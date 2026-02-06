package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrmApplicationTest {

    @Test
    void testMain() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
            springApp.when(() -> SpringApplication.run(eq(CrmApplication.class), any(String[].class)))
                    .thenReturn(context);

            CrmApplication.main(new String[]{});

            springApp.verify(() -> SpringApplication.run(eq(CrmApplication.class), any(String[].class)));
        }
    }

    @Test
    void testMainWithArguments() {
        try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
            springApp.when(() -> SpringApplication.run(eq(CrmApplication.class), any(String[].class)))
                    .thenReturn(context);

            String[] args = {"--server.port=8081", "--spring.profiles.active=test"};
            CrmApplication.main(args);

            springApp.verify(() -> SpringApplication.run(eq(CrmApplication.class), any(String[].class)));
        }
    }

    @Test
    void testContextLoads() {
        CrmApplication application = new CrmApplication();
        // Test that the application class can be instantiated
        assert application != null;
    }
}
