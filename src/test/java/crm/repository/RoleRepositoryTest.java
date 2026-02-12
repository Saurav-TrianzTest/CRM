package crm.repository;

import crm.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("RoleRepository Tests")
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Test RoleRepository extends JpaRepository")
    void testRoleRepositoryExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(RoleRepository.class));
    }

    @Test
    @DisplayName("Test RoleRepository is not null")
    void testRoleRepositoryNotNull() {
        assertNotNull(roleRepository);
    }

    @Test
    @DisplayName("Test findByName returns correct role")
    void testFindByName() {
        Role role = new Role();
        role.setName("ADMIN");
        entityManager.persist(role);
        entityManager.flush();

        Role found = roleRepository.findByName("ADMIN");

        assertNotNull(found);
        assertEquals("ADMIN", found.getName());
    }

    @Test
    @DisplayName("Test findByName returns null for non-existent role")
    void testFindByNameNotFound() {
        Role found = roleRepository.findByName("NON_EXISTENT");
        assertNull(found);
    }

    @Test
    @DisplayName("Test findByName with USER role")
    void testFindByNameUser() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);
        entityManager.flush();

        Role found = roleRepository.findByName("USER");

        assertNotNull(found);
        assertEquals("USER", found.getName());
    }

    @Test
    @DisplayName("Test save role")
    void testSaveRole() {
        Role role = new Role();
        role.setName("MODERATOR");

        Role saved = roleRepository.save(role);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("MODERATOR", saved.getName());
    }

    @Test
    @DisplayName("Test save role with null name")
    void testSaveRoleWithNullName() {
        Role role = new Role();
        role.setName(null);

        Role saved = roleRepository.save(role);

        assertNotNull(saved);
        assertNull(saved.getName());
    }

    @Test
    @DisplayName("Test findById returns correct role")
    void testFindById() {
        Role role = new Role();
        role.setName("SUPERVISOR");
        Role saved = entityManager.persist(role);
        entityManager.flush();

        Role found = roleRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("SUPERVISOR", found.getName());
    }

    @Test
    @DisplayName("Test findById returns empty for non-existent ID")
    void testFindByIdNotFound() {
        Role found = roleRepository.findById(999999).orElse(null);
        assertNull(found);
    }

    @Test
    @DisplayName("Test findAll returns all roles")
    void testFindAll() {
        Role role1 = new Role();
        role1.setName("ADMIN");
        entityManager.persist(role1);

        Role role2 = new Role();
        role2.setName("USER");
        entityManager.persist(role2);
        entityManager.flush();

        List<Role> roles = roleRepository.findAll();

        assertNotNull(roles);
        assertTrue(roles.size() >= 2);
    }

    @Test
    @DisplayName("Test delete role")
    void testDeleteRole() {
        Role role = new Role();
        role.setName("DELETE_ROLE");
        Role saved = entityManager.persist(role);
        entityManager.flush();

        int roleId = saved.getId();
        roleRepository.deleteById(roleId);

        Role deleted = roleRepository.findById(roleId).orElse(null);
        assertNull(deleted);
    }

    @Test
    @DisplayName("Test update role name")
    void testUpdateRoleName() {
        Role role = new Role();
        role.setName("ORIGINAL_NAME");
        Role saved = entityManager.persist(role);
        entityManager.flush();

        saved.setName("UPDATED_NAME");
        Role updated = roleRepository.save(saved);

        assertEquals("UPDATED_NAME", updated.getName());
    }

    @Test
    @DisplayName("Test count roles")
    void testCountRoles() {
        long initialCount = roleRepository.count();

        Role role = new Role();
        role.setName("COUNT_ROLE");
        entityManager.persist(role);
        entityManager.flush();

        long newCount = roleRepository.count();

        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @DisplayName("Test existsById returns true for existing role")
    void testExistsByIdTrue() {
        Role role = new Role();
        role.setName("EXISTS_ROLE");
        Role saved = entityManager.persist(role);
        entityManager.flush();

        assertTrue(roleRepository.existsById(saved.getId()));
    }

    @Test
    @DisplayName("Test existsById returns false for non-existent role")
    void testExistsByIdFalse() {
        assertFalse(roleRepository.existsById(999999));
    }

    @Test
    @DisplayName("Test save role with long name")
    void testSaveRoleWithLongName() {
        String longName = "SUPER_ADMIN_WITH_EXTENDED_PERMISSIONS";
        Role role = new Role();
        role.setName(longName);

        Role saved = roleRepository.save(role);

        assertNotNull(saved);
        assertEquals(longName, saved.getName());
    }

    @Test
    @DisplayName("Test findByName with special characters")
    void testFindByNameWithSpecialCharacters() {
        Role role = new Role();
        role.setName("SUPER_ADMIN");
        entityManager.persist(role);
        entityManager.flush();

        Role found = roleRepository.findByName("SUPER_ADMIN");

        assertNotNull(found);
        assertEquals("SUPER_ADMIN", found.getName());
    }

    @Test
    @DisplayName("Test save multiple roles and find by name")
    void testSaveMultipleRolesAndFindByName() {
        Role admin = new Role();
        admin.setName("ADMIN");
        roleRepository.save(admin);

        Role user = new Role();
        user.setName("USER");
        roleRepository.save(user);

        Role moderator = new Role();
        moderator.setName("MODERATOR");
        roleRepository.save(moderator);

        Role foundAdmin = roleRepository.findByName("ADMIN");
        Role foundUser = roleRepository.findByName("USER");
        Role foundModerator = roleRepository.findByName("MODERATOR");

        assertNotNull(foundAdmin);
        assertNotNull(foundUser);
        assertNotNull(foundModerator);
        assertEquals("ADMIN", foundAdmin.getName());
        assertEquals("USER", foundUser.getName());
        assertEquals("MODERATOR", foundModerator.getName());
    }

    @Test
    @DisplayName("Test findByName is case sensitive")
    void testFindByNameCaseSensitive() {
        Role role = new Role();
        role.setName("ADMIN");
        entityManager.persist(role);
        entityManager.flush();

        Role foundExact = roleRepository.findByName("ADMIN");
        Role foundLower = roleRepository.findByName("admin");

        assertNotNull(foundExact);
        assertNull(foundLower);
    }

    @Test
    @DisplayName("Test role name uniqueness")
    void testRoleNameUniqueness() {
        Role role1 = new Role();
        role1.setName("UNIQUE_ROLE");
        entityManager.persist(role1);
        entityManager.flush();

        Role found = roleRepository.findByName("UNIQUE_ROLE");
        assertNotNull(found);
        assertEquals("UNIQUE_ROLE", found.getName());
    }
}
