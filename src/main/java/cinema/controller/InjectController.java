package cinema.controller;

import cinema.model.Role;
import cinema.model.User;
import cinema.service.AuthenticationService;
import cinema.service.RoleService;
import cinema.service.UserService;
import java.util.Set;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InjectController {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;

    public InjectController(UserService userService,
                            RoleService roleService,
                            AuthenticationService authenticationService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/inject")
    public String injectUsers() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleService.add(userRole);
        authenticationService.register("user@gmail.com", "12345");
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("12345");
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        roleService.add(adminRole);
        admin.setUserRoles(Set.of(adminRole));
        userService.add(admin);
        return "Injected";
    }
}
