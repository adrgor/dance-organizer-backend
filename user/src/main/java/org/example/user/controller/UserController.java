package org.example.user.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.example.user.exception.IntegrityException;
import org.example.user.exception.UserNotFoundException;
import org.example.user.jwt.JwtTokenUtil;
import org.example.user.messaging.Sender;
import org.example.user.model.User;
import org.example.user.model.UserDTO;
import org.example.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final String EMAIL_TAKEN = "Specified email is already in use";
    private static final String USERNAME_TAKEN = "Specified username is already in use";

    private static final String USER_ACTIVATION_URL = "http://localhost:8080/api/user/activate?token=";


    final UserRepository userRepository;
    final JwtTokenUtil jwtTokenUtil;
    final Sender activateUserMessageSender;

    @GetMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) throws UserNotFoundException {
        Optional<User> userEntity = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        return jwtTokenUtil.generateToken(userEntity.orElseThrow(UserNotFoundException::new));
    }

    @PostMapping("/register")
    public void addUser(@RequestBody @Valid UserDTO user) {
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .ifPresent(optionalUser -> {
                    if(optionalUser.getEmail().equals(user.getEmail()))
                        throw new IntegrityException(EMAIL_TAKEN);
                    else
                        throw new IntegrityException(USERNAME_TAKEN);
                });

        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());

        userRepository.save(userEntity);

        user.setId(userEntity.getId());
        user.setActivationUrl(USER_ACTIVATION_URL + jwtTokenUtil.generateActivationToken(user));
        activateUserMessageSender.send(user);
    }

    @GetMapping("/activate")
    public ResponseEntity<Void> activateUser(@RequestParam("token") String token) throws UserNotFoundException {
        if(jwtTokenUtil.isTokenValid(token)) {
            User user = userRepository
                    .findByUsername(jwtTokenUtil.getUsernameFromToken(token))
                    .orElseThrow(UserNotFoundException::new);

            user.setActivated(true);
            userRepository.save(user);
        }
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorizationHeader.substring(7);
        if(!jwtTokenUtil.isTokenValid(jwt) || jwtTokenUtil.getUserIdFromToken(jwt) != id) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody String jwt) {
        if(jwtTokenUtil.isTokenValid(jwt) && userRepository.findById(jwtTokenUtil.getUserIdFromToken(jwt)).isPresent()){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
