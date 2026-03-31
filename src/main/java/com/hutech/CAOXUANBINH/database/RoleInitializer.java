package com.hutech.CAOXUANBINH.database;

import com.hutech.CAOXUANBINH.model.Role;
import com.hutech.CAOXUANBINH.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            ensureRole("ROLE_USER", "Nguoi dung thong thuong");
            ensureRole("ROLE_ADMIN", "Quan tri he thong");
            ensureRole("ROLE_MANAGER", "Quan ly san pham");
        };
    }

    private void ensureRole(String name, String description) {
        if (roleRepository.findByName(name).isPresent()) {
            return;
        }
        Role role = createRole(name, description);
        roleRepository.save(role);
    }

    private Role createRole(String name, String description) {
        try {
            Role role = new Role();
            Field nameField = Role.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(role, name);

            Field descField = Role.class.getDeclaredField("description");
            descField.setAccessible(true);
            descField.set(role, description);
            return role;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot initialize role: " + name, e);
        }
    }
}
