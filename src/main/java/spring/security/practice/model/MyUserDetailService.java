package spring.security.practice.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private MyUserRepo myUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Mencari user berdasarkan username
        Optional<MyUser> user = myUserRepo.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername()) // Mengatur username
                    .password(userObj.getPassword()) // Mengatur password
                    .roles(getRoles(userObj)) // Mengatur role user
                    .build();
        } else {
            throw new UsernameNotFoundException(username); // Jika user tidak ditemukan
        }
    }

    private String[] getRoles(MyUser user) {
        // Mengembalikan array role, default "USER" jika role tidak di-set
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
