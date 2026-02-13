package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for RoleServiceImpl
 * Tests all public methods, constructors, edge cases, null checks, and boundary conditions
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role testRole;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_TEST");

        adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        userRole = new Role();
        userRole.setId(3);
        userRole.setName("ROLE_USER");
    }

    // Constructor Tests
    @Test
    void testConstructor_WithValidRepository() {
        RoleRepository repository = mock(RoleRepository.class);
        RoleServiceImpl service = new RoleServiceImpl(repository);
        assertNotNull(service);
    }

    @Test
    void testConstructor_WithNullRepository() {
        RoleServiceImpl service = new RoleServiceImpl(null);
        assertNotNull(service);
    }

    // listAllRoles Tests
    @Test
    void testListAllRoles_WithMultipleRoles() {
        // Arrange
        List<Role> roles = Arrays.asList(testRole, adminRole, userRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(3, ((List<Role>) result).size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithSingleRole() {
        // Arrange
        List<Role> roles = Collections.singletonList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals("ROLE_TEST", roleList.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithEmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithNullResult() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(null);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_VerifyRepositoryCalledOnce() {
        // Arrange
        List<Role> roles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        roleService.listAllRoles();

        // Assert
        verify(roleRepository, times(1)).findAll();
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void testListAllRoles_MultipleCalls() {
        // Arrange
        List<Role> roles = Arrays.asList(testRole, adminRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result1 = roleService.listAllRoles();
        Iterable<Role> result2 = roleService.listAllRoles();

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(2, ((List<Role>) result1).size());
        assertEquals(2, ((List<Role>) result2).size());
        verify(roleRepository, times(2)).findAll();
    }

    @Test
    void testListAllRoles_WithDifferentRoleTypes() {
        // Arrange
        Role moderatorRole = new Role();
        moderatorRole.setId(4);
        moderatorRole.setName("ROLE_MODERATOR");

        Role guestRole = new Role();
        guestRole.setId(5);
        guestRole.setName("ROLE_GUEST");

        List<Role> roles = Arrays.asList(adminRole, userRole, moderatorRole, guestRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(4, ((List<Role>) result).size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingSpecialCharacters() {
        // Arrange
        Role specialRole1 = new Role();
        specialRole1.setId(6);
        specialRole1.setName("ROLE_ADMIN@2024");

        Role specialRole2 = new Role();
        specialRole2.setId(7);
        specialRole2.setName("ROLE_USER_V2.0");

        List<Role> roles = Arrays.asList(specialRole1, specialRole2);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertTrue(roleList.stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN@2024")));
        assertTrue(roleList.stream().anyMatch(r -> r.getName().equals("ROLE_USER_V2.0")));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingNullNames() {
        // Arrange
        Role roleWithNullName = new Role();
        roleWithNullName.setId(8);
        roleWithNullName.setName(null);

        List<Role> roles = Arrays.asList(testRole, roleWithNullName);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertTrue(roleList.stream().anyMatch(r -> r.getName() == null));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingEmptyNames() {
        // Arrange
        Role roleWithEmptyName = new Role();
        roleWithEmptyName.setId(9);
        roleWithEmptyName.setName("");

        List<Role> roles = Arrays.asList(testRole, roleWithEmptyName);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertTrue(roleList.stream().anyMatch(r -> r.getName().isEmpty()));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithLargeNumberOfRoles() {
        // Arrange
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Role role = new Role();
            role.setId(i);
            role.setName("ROLE_" + i);
            roles.add(role);
        }
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(100, ((List<Role>) result).size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingWhitespace() {
        // Arrange
        Role roleWithWhitespace = new Role();
        roleWithWhitespace.setId(10);
        roleWithWhitespace.setName("   ROLE_ADMIN   ");

        List<Role> roles = Collections.singletonList(roleWithWhitespace);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals("   ROLE_ADMIN   ", roleList.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingVeryLongNames() {
        // Arrange
        Role roleWithLongName = new Role();
        roleWithLongName.setId(11);
        String longName = "ROLE_" + "A".repeat(250);
        roleWithLongName.setName(longName);

        List<Role> roles = Collections.singletonList(roleWithLongName);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals(longName, roleList.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingZeroIds() {
        // Arrange
        Role roleWithZeroId = new Role();
        roleWithZeroId.setId(0);
        roleWithZeroId.setName("ROLE_ZERO");

        List<Role> roles = Collections.singletonList(roleWithZeroId);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals(0, roleList.get(0).getId());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingNegativeIds() {
        // Arrange
        Role roleWithNegativeId = new Role();
        roleWithNegativeId.setId(-1);
        roleWithNegativeId.setName("ROLE_NEGATIVE");

        List<Role> roles = Collections.singletonList(roleWithNegativeId);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals(-1, roleList.get(0).getId());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithRolesContainingMaxIntegerId() {
        // Arrange
        Role roleWithMaxId = new Role();
        roleWithMaxId.setId(Integer.MAX_VALUE);
        roleWithMaxId.setName("ROLE_MAX");

        List<Role> roles = Collections.singletonList(roleWithMaxId);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals(Integer.MAX_VALUE, roleList.get(0).getId());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithDuplicateRoleIds() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_DUPLICATE_1");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_DUPLICATE_2");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Role>) result).size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithDuplicateRoleNames() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_DUPLICATE");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_DUPLICATE");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertTrue(roleList.stream().allMatch(r -> r.getName().equals("ROLE_DUPLICATE")));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithLowercaseRoleNames() {
        // Arrange
        Role lowercaseRole = new Role();
        lowercaseRole.setId(12);
        lowercaseRole.setName("role_lowercase");

        List<Role> roles = Collections.singletonList(lowercaseRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals("role_lowercase", roleList.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_WithMixedCaseRoleNames() {
        // Arrange
        Role mixedCaseRole = new Role();
        mixedCaseRole.setId(13);
        mixedCaseRole.setName("RoLe_MiXeD");

        List<Role> roles = Collections.singletonList(mixedCaseRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Role>) result).size());
        List<Role> roleList = (List<Role>) result;
        assertEquals("RoLe_MiXeD", roleList.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_VerifyNoOtherMethodsCalled() {
        // Arrange
        List<Role> roles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        roleService.listAllRoles();

        // Assert
        verify(roleRepository, only()).findAll();
    }

    @Test
    void testListAllRoles_ConsistencyAcrossMultipleCalls() {
        // Arrange
        List<Role> roles = Arrays.asList(testRole, adminRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result1 = roleService.listAllRoles();
        Iterable<Role> result2 = roleService.listAllRoles();
        Iterable<Role> result3 = roleService.listAllRoles();

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(2, ((List<Role>) result1).size());
        assertEquals(2, ((List<Role>) result2).size());
        assertEquals(2, ((List<Role>) result3).size());
        verify(roleRepository, times(3)).findAll();
    }
}
