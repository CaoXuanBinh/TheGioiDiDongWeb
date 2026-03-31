package com.hutech.CAOXUANBINH.service;

import com.hutech.CAOXUANBINH.model.Role;
import com.hutech.CAOXUANBINH.model.User;
import com.hutech.CAOXUANBINH.repository.RoleRepository;
import com.hutech.CAOXUANBINH.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .build();
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Assign default ROLE_USER if no roles set
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            userRole.ifPresent(role -> user.getRoles().add(role));
        }
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalStateException("User with id " + id + " not found.");
        }
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (id == null) {
            throw new IllegalArgumentException("User id is required for update.");
        }
        User existing = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found: " + id));

        if (user.getUsername() != null) existing.setUsername(user.getUsername());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getPhone() != null) existing.setPhone(user.getPhone());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existing.setRoles(user.getRoles());
        }

        return userRepository.save(existing);
    }
}
