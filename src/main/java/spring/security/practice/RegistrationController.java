package spring.security.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.security.practice.model.MyUser;
import spring.security.practice.model.MyUserRepo;

@RestController
public class RegistrationController {

    @Autowired
    private MyUserRepo myUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public MyUser register(@RequestBody MyUser user) {
        // Mengenkripsi password sebelum menyimpan
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepo.save(user); // Menyimpan user ke database
    }
}
