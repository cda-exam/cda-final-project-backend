package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("/register")
    public void register(@RequestBody UserCreateDTO userCreateDTO) {
        log.info("Inscription");
        this.userService.createUser(userCreateDTO);
    }

    @PostMapping("/activation")
    public void activation(@RequestBody Map<String, String> activation) {
        log.info("Activation");
        this.userService.activateUser(activation);
    }
}
