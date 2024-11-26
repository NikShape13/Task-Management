package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name) {
        if (roleRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Роль с таким именем уже существует");
        }
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Роль с именем " + name + " не найдена"));
    }
}