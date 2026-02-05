package crm.repository;

import crm.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
public class RoleRepositoryTest {

    @Test
    public void testRoleRepositoryInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.repository.RoleRepository"));
    }

    @Test
    public void testRoleRepositoryHasFindByNameMethod() throws NoSuchMethodException {
        assertNotNull(RoleRepository.class.getMethod("findByName", String.class));
    }
}
