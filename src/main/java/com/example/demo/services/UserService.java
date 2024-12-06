package com.example.demo.services;

import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.TokenRepository;
import com.example.demo.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
    }

    public User createUser(SignUpRequest userDto) throws Exception {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("User with this email allready exists");
        }

        User user = new User();
        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.addRole(role);
        user = userRepository.save(user);
        if(user == null) {
        	throw new Exception("Failed to create user");
        }
        return user;
    }
    
    public User createAdmin(SignUpRequest userDto) throws Exception {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("User with this email allready exists");
        }

        User user = new User();
        Role role = roleRepository.findByName("ROLE_ADMIN").get();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        user.addRole(role);
        user.addRole(userRole);
        
        user = userRepository.save(user);
        if(user == null) {
        	throw new Exception("Failed to create admin");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) throws Exception {
        User user = userRepository.getById(id);
        if(user == null) {
        	throw new Exception();
        }
        return user;
    }

    public void deleteUser(Long id) {
        User user;
		try {
			user = getUserById(id);
			userRepository.delete(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    public User updateUser(Long id, UserDto userDto) throws Exception {
        User user = getUserById(id);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.addRole(userDto.getRole());
        return userRepository.save(user);
    }
    
    public User getByEmail(String email) {
        try {
			return userRepository.findByEmail(email)
			        .orElseThrow(() -> new Exception("Пользователь не найден"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    
    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    
    public User getCurrentUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(email);
    }


    
}
