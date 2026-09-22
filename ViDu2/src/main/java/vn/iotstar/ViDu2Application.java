package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class ViDu2Application {

    public static void main(String[] args) {
        SpringApplication.run(ViDu2Application.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            if (userRepository.findByUsername("user01").isEmpty()) {
                User user = new User(
                        null,
                        "user01",
                        "user01@gmail.com",
                        passwordEncoder.encode("123456"),
                        "Nguyễn Hữu Trung",
                        "/images/user.png",
                        true,
                        userRole
                );
                userRepository.save(user);
            }
        };
    }
}
