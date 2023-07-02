package org.example.user.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.example.user.exception.*;
import org.example.user.jwt.JwtTokenUtil;
import org.example.user.messaging.Sender;
import org.example.user.model.User;
import org.example.user.model.UserDTO;
import org.example.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

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
    public String login(@RequestBody UserDTO user) throws UserNotFoundException,
                                                          IncorrectPasswordException,
                                                          UserNotActivatedException {
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername());
        User userEntity = optionalUser.orElseThrow(UserNotFoundException::new);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatching = passwordEncoder.matches(user.getPassword(), userEntity.getPassword());

        if(!isPasswordMatching) {
            throw new IncorrectPasswordException();
        }

        if(!userEntity.isActivated()) {
            throw new UserNotActivatedException();
        }

        return jwtTokenUtil.generateToken(userEntity);
    }

    @PostMapping("/register")
    public void addUser(@RequestBody @Valid UserDTO user) {
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .ifPresent(optionalUser -> {
                    if(optionalUser.getEmail().equals(user.getEmail()))
                        throw new IntegrityException(ErrorMessages.EMAIL_TAKEN, ErrorCodes.EMAIL_TAKEN_CODE);
                    else
                        throw new IntegrityException(ErrorMessages.USERNAME_TAKEN, ErrorCodes.USERNAME_TAKEN_CODE);
                });

        User userEntity = new User();
        userEntity.setUsername(user.getUsername());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        userEntity.setPassword(hashedPassword);
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
    public boolean validateToken(@RequestBody String jwt) {
        return jwtTokenUtil.isTokenValid(jwt) &&
               userRepository.findById(jwtTokenUtil.getUserIdFromToken(jwt)).isPresent();
    }
}
