package vn.iotstar.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                           CategoryRepository categoryRepository, ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        User admin = userRepository.findByUsername("admin").orElseGet(() -> {
            User u = new User();
            u.setUsername("admin");
            u.setEmail("trunghspkt@gmail.com");
            u.setPassword(passwordEncoder.encode("123456"));
            u.setFullName("Administrator");
            u.setRole(adminRole);
            u.setEnabled(true);
            return userRepository.save(u);
        });

        User user = userRepository.findByUsername("trungnh").orElseGet(() -> {
            User u = new User();
            u.setUsername("trungnh");
            u.setEmail("trungnh@hcmute.edu.vn");
            u.setPassword(passwordEncoder.encode("123456"));
            u.setFullName("Nguyễn Hữu Trung");
            u.setRole(userRole);
            u.setEnabled(true);
            return userRepository.save(u);
        });

        Category cVot = categoryRepository.findByCategoryName("Vợt Cầu Lông").orElseGet(() -> {
            return categoryRepository.save(new Category("Vợt Cầu Lông", "vot_cau_long.png"));
        });
        Category cGiay = categoryRepository.findByCategoryName("Giày Cầu Lông").orElseGet(() -> {
            return categoryRepository.save(new Category("Giày Cầu Lông", "giay_cau_long.jpg"));
        });
        Category cPhuKien = categoryRepository.findByCategoryName("Phụ Kiện Cầu Lông").orElseGet(() -> {
            return categoryRepository.save(new Category("Phụ Kiện Cầu Lông", "phu_kien.jpg"));
        });

        createProductIfNotFound("Yonex Astrox 100 ZZ", "Yonex", "4500000", 10, "/uploads/products/astrox100zz.jpg", "Vợt cầu lông cao cấp Yonex Astrox 100 ZZ.", cVot, user);
        createProductIfNotFound("Yonex Astrox 88D Pro", "Yonex", "4200000", 12, "/uploads/products/astrox88dpro.jpg", "Vợt cầu lông Yonex Astrox 88D Pro.", cVot, user);
        createProductIfNotFound("Yonex Astrox 88S Pro", "Yonex", "4100000", 10, "/uploads/products/astrox88spro.jpg", "Vợt cầu lông Yonex Astrox 88S Pro.", cVot, user);
        createProductIfNotFound("Yonex Nanoflare 1000 Z", "Yonex", "4600000", 8, "/uploads/products/nanoflare1000z.jpg", "Vợt cầu lông tốc độ cao Yonex Nanoflare 1000 Z.", cVot, user);
        createProductIfNotFound("Yonex Power Cushion 65 Z3", "Yonex", "3200000", 10, "/uploads/products/65z3.jpg", "Giày cầu lông Yonex Power Cushion 65 Z3.", cGiay, user);
        createProductIfNotFound("Yonex AC102EX Power Cushion Grip", "Yonex", "80000", 50, "/uploads/products/ac102ex.jpg", "Quấn cán vợt Yonex AC102EX.", cPhuKien, user);
    }

    private void createProductIfNotFound(String name, String brand, String price, Integer quantity,
                                           String imageName, String description, Category category, User user) {
        if (productRepository.findByName(name).isPresent()) return;
        Product p = new Product();
        p.setName(name);
        p.setBrand(brand);
        p.setPrice(new BigDecimal(price));
        p.setQuantity(quantity);
        p.setImageUrl(imageName);
        p.setDescription(description);
        p.setCategory(category);
        p.setUser(user);
        productRepository.save(p);
    }
}
