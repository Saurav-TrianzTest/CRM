package crm.repository;

import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test UserRepository extends JpaRepository")
    void testUserRepositoryExtendsJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(UserRepository.class));
    }

    @Test
    @DisplayName("Test UserRepository is not null")
    void testUserRepositoryNotNull() {
        assertNotNull(userRepository);
    }

    @Test
    @DisplayName("Test findByUsername returns correct user")
    void testFindByUsername() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findByUsername("testuser");

        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    @DisplayName("Test findByUsername returns null for non-existent user")
    void testFindByUsernameNotFound() {
        User found = userRepository.findByUsername("nonexistent");
        assertNull(found);
    }

    @Test
    @DisplayName("Test findAllByEnabled returns only enabled users")
    void testFindAllByEnabled() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User enabledUser1 = User.builder()
                .username("enabled1")
                .email("enabled1@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(enabledUser1);

        User enabledUser2 = User.builder()
                .username("enabled2")
                .email("enabled2@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(enabledUser2);

        User disabledUser = User.builder()
                .username("disabled")
                .email("disabled@example.com")
                .password("pass")
                .enabled(0)
                .role(role)
                .build();
        entityManager.persist(disabledUser);
        entityManager.flush();

        Iterable<User> enabledUsers = userRepository.findAllByEnabled(1);
        List<User> userList = new ArrayList<>();
        enabledUsers.forEach(userList::add);

        assertEquals(2, userList.size());
        assertTrue(userList.stream().allMatch(u -> u.getEnabled() == 1));
    }

    @Test
    @DisplayName("Test findAllByEnabled returns only disabled users")
    void testFindAllByDisabled() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User disabledUser = User.builder()
                .username("disabled")
                .email("disabled@example.com")
                .password("pass")
                .enabled(0)
                .role(role)
                .build();
        entityManager.persist(disabledUser);
        entityManager.flush();

        Iterable<User> disabledUsers = userRepository.findAllByEnabled(0);
        List<User> userList = new ArrayList<>();
        disabledUsers.forEach(userList::add);

        assertFalse(userList.isEmpty());
        assertTrue(userList.stream().allMatch(u -> u.getEnabled() == 0));
    }

    @Test
    @DisplayName("Test findAllByEnabled returns empty for no matches")
    void testFindAllByEnabledEmpty() {
        Iterable<User> users = userRepository.findAllByEnabled(1);
        List<User> userList = new ArrayList<>();
        users.forEach(userList::add);

        assertTrue(userList.isEmpty());
    }

    @Test
    @DisplayName("Test save user")
    void testSaveUser() {
        Role role = new Role();
        role.setName("ADMIN");
        entityManager.persist(role);

        User user = User.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("newpass")
                .firstName("New")
                .lastName("User")
                .enabled(1)
                .role(role)
                .build();

        User saved = userRepository.save(user);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("newuser", saved.getUsername());
    }

    @Test
    @DisplayName("Test findById returns correct user")
    void testFindById() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
        User saved = entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("testuser", found.getUsername());
    }

    @Test
    @DisplayName("Test findAll returns all users")
    void testFindAll() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(user1);

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(user2);
        entityManager.flush();

        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    @DisplayName("Test delete user")
    void testDeleteUser() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User user = User.builder()
                .username("deleteuser")
                .email("delete@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        User saved = entityManager.persist(user);
        entityManager.flush();

        Long userId = saved.getId();
        userRepository.deleteById(userId);

        User deleted = userRepository.findById(userId).orElse(null);
        assertNull(deleted);
    }

    @Test
    @DisplayName("Test count users")
    void testCountUsers() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        long initialCount = userRepository.count();

        User user = User.builder()
                .username("countuser")
                .email("count@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        entityManager.persist(user);
        entityManager.flush();

        long newCount = userRepository.count();

        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @DisplayName("Test existsById returns true for existing user")
    void testExistsByIdTrue() {
        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);

        User user = User.builder()
                .username("existuser")
                .email("exist@example.com")
                .password("pass")
                .enabled(1)
                .role(role)
                .build();
        User saved = entityManager.persist(user);
        entityManager.flush();

        assertTrue(userRepository.existsById(saved.getId()));
    }

    @Test
    @DisplayName("Test existsById returns false for non-existent user")
    void testExistsByIdFalse() {
        assertFalse(userRepository.existsById(999999L));
    }
}
