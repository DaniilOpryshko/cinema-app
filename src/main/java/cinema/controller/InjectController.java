package cinema.controller;

import cinema.model.Role;
import cinema.model.User;
import cinema.service.RoleService;
import cinema.service.UserService;
import java.util.Set;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InjectController {
    private final UserService userService;
    private final RoleService roleService;

    public InjectController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/inject")
    public void injectUsers() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("12345");
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleService.add(userRole);
        user.setUserRoles(Set.of(userRole));
        userService.add(user);
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("12345");
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        roleService.add(adminRole);
        admin.setUserRoles(Set.of(adminRole));
        userService.add(admin);
    }
}
