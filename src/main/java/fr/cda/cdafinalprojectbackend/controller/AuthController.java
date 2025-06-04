package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.user.LoginDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.configuration.security.JwtService;
import fr.cda.cdafinalprojectbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public void register(@RequestBody UserCreateDTO userCreateDTO) {
        log.info("Inscription");
        this.userService.createUser(userCreateDTO);
    }

    @PostMapping("/activate/{email}")
    public void activation(@RequestBody Map<String, String> activation, @PathVariable String email) {
        log.info("Activation");
        this.userService.activateUser(activation, email);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDTO) {
        final Authentication authenticate = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );

        if (authenticate.isAuthenticated()) {
            return this.jwtService.getJwtToken(loginDTO.username());
        }
        log.info("resultat {}", authenticate.isAuthenticated());

        return null;
    }
}
