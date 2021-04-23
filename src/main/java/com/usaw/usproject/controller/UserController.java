package com.usaw.usproject.controller;

import com.usaw.usproject.model.*;
import com.usaw.usproject.payload.response.MessageResponse;
import com.usaw.usproject.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    PasswordEncoder encoder;

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable("id") long id, @RequestBody User user) {

        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent()) {
            User _user = userData.get();
            _user.setFirstName(user.getFirstName());
            _user.setLastName(user.getLastName());
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @PutMapping("/users/diet/{id}")
    public ResponseEntity<User> updateDietOfUser(@Valid @PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent()) {
            User _user = userData.get();
            _user.setCalories(user.getCalories());
            _user.setFat(user.getFat());
            _user.setCarbohydrates(user.getCarbohydrates());
            _user.setProteins(user.getProteins());
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @PutMapping("/users/reset")
    public ResponseEntity<User> updateResetPassword(@Valid @RequestBody Map<String, String> json) {
        String email = json.get("email");
        String url = json.get("url");
        Optional<User> userData = userRepository.findByEmail(email);
        if(userData.isPresent()) {
            User _user = userData.get();
            String token = RandomString.make(45);
            _user.setResetPasswordToken(token);
            String resetPasswordLink = url + "/token/" + token;
            try {
                sendEmail(email, resetPasswordLink);
            } catch (UnsupportedEncodingException | MessagingException e) {
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("support@przepisyapp.com", "PrzepisyApp Dział Pomocy");
        helper.setTo(email);
        String subject = "Reset hasła";
        String content = "<p>Witaj,</p>"
                + "<p>Otrzymaliśmy prośbę o zresetowanie twojego hasła.</p>"
                + "<p>Kliknij w link poniżej, aby zmienić twoje hasło!</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Zmień hasło</a></b></p>"
                + "<p>Zignoruj tego maila jeśli nie pamiętasz hasła albo nie wysłałeś prośby.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @PutMapping("/users/passwordToken")
    public ResponseEntity<User> getByPasswordToken(@Valid @RequestBody String token) {
        Optional<User> userData = userRepository.findByResetPasswordToken(token);
        if(userData.isPresent()) {
            User _user = userData.get();
            return new ResponseEntity<>(_user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @PutMapping("/users/password")
    public ResponseEntity<User> update(@Valid @RequestBody Map<String, String> json) {
        Long id = 0L;
        id = Long.valueOf(json.get("id"));
        String password = json.get("password");
        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent()) {
            User _user = userData.get();
            _user.setPassword(encoder.encode(password));
            _user.setResetPasswordToken(null);
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
