package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.User;
import fr.cda.cdafinalprojectbackend.exception.UserAlreadyExistsException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "user")
@AllArgsConstructor
public class UserController {
    UserServiceImpl userService;

    @GetMapping(value = "test")
    public String getTest() {
        return "Test successful";
    }

    @GetMapping()
    public List<UserDTO> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping(value = "{id}")
    public UserDTO getUserById(@PathVariable UUID id) throws UserNotFoundException {
        return this.userService.getUserById(id);
    }

    @PostMapping()
    public User createUser(@RequestBody UserCreateDTO userCreateDTO) throws UserAlreadyExistsException {
        return this.userService.createUser(userCreateDTO);
    }

    @PutMapping(value = "{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException {
        return this.userService.updateUser(id, userUpdateDTO);
    }

    @DeleteMapping(value = "{id}")
    public void deleteUser(@PathVariable UUID id) throws UserNotFoundException {
        this.userService.deleteUser(id);
    }
}