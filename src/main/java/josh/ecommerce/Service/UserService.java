package josh.ecommerce.Service;

import josh.ecommerce.DTO.UserCreateDto;
import josh.ecommerce.DTO.UserDto;
import josh.ecommerce.DTO.UserUpdateDto;
import josh.ecommerce.Entity.Role;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Entity.UserStatus;
import josh.ecommerce.Repository.UserRepository;
import josh.ecommerce.Security.AdminConfigProperties;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminConfigProperties adminConfigProperties;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    private void initAdmin() throws Exception {
        if(userRepository.existsByUsername(adminConfigProperties.getUsername()) &&
                !userRepository.existsByRoleAndUsername(Role.ADMIN, adminConfigProperties.getUsername())) {
            throw new Exception("Invalid admin role");
        }

        if(userRepository.existsByRoleAndUsername(Role.ADMIN, adminConfigProperties.getUsername())) {
            return;
        }

        User admin = new User();
        admin.setUsername(adminConfigProperties.getUsername());
        admin.setPassword(passwordEncoder.encode(adminConfigProperties.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setUserStatus(UserStatus.ENABLED);

        userRepository.save(admin);
    }

    @Override
    @NullMarked
    public void run(String... args) throws Exception {
        initAdmin();
    }

    public boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsSeller(Integer id) {
        return userRepository.existsByRoleAndId(Role.SELLER, id);
    }

    public boolean existsSeller(String username) {
        return userRepository.existsByRoleAndUsername(Role.SELLER, username);
    }

    public UserDto addSeller(UserCreateDto sellerCreateDto) {
        if(!Objects.equals(Role.SELLER, sellerCreateDto.getRole()) ||
                userRepository.existsByUsername(sellerCreateDto.getUsername())) {
            return null;
        }

        User seller = new User();
        seller.setUsername(sellerCreateDto.getUsername());
        seller.setPassword(passwordEncoder.encode(sellerCreateDto.getPassword()));
        seller.setRole(sellerCreateDto.getRole());
        seller.setUserStatus(UserStatus.ENABLED);

        return new UserDto(userRepository.save(seller));
    }

    public List<UserDto> getSellers() {
        List<User> sellers = userRepository.findByRole(Role.SELLER);

        return sellers
                .stream()
                .map(UserDto::new)
                .toList();
    }

    public UserDto getSeller(Integer id) {
        User seller = userRepository.findByRoleAndId(Role.SELLER, id).orElse(null);
        return seller != null ? new UserDto(seller) : null;
    }

    public UserDto getSeller(String username) {
        User seller = userRepository.findByRoleAndUsername(Role.SELLER, username).orElse(null);
        return seller != null ? new UserDto(seller) : null;
    }

    public UserDto updateSeller(UserUpdateDto sellerUpdateDto) {
        if(!userRepository.existsByRoleAndId(Role.SELLER, sellerUpdateDto.getId())) {
            return null;
        }

        User anotherUser = userRepository.findByUsername(sellerUpdateDto.getUsername()).orElse(null);
        if(anotherUser != null && !Objects.equals(anotherUser.getId(), sellerUpdateDto.getId())) {
            return null;
        }

        User seller = new User();
        seller.setId(sellerUpdateDto.getId());
        seller.setUsername(sellerUpdateDto.getUsername());
        seller.setPassword(passwordEncoder.encode(sellerUpdateDto.getPassword()));
        seller.setRole(sellerUpdateDto.getRole());

        return new UserDto(userRepository.save(seller));
    }

    @Transactional
    public void disabledSeller(Integer id) {
        userRepository.disabledByRoleAndId(Role.SELLER, id);
    }

    public boolean existsCustomer(Integer id) {
        return userRepository.existsByRoleAndId(Role.CUSTOMER, id);
    }

    public boolean existsCustomer(String username) {
        return userRepository.existsByRoleAndUsername(Role.CUSTOMER, username);
    }

    public UserDto addCustomer(UserCreateDto customerCreateDto) {
        if(!customerCreateDto.getRole().equals(Role.CUSTOMER) ||
                userRepository.existsByUsername(customerCreateDto.getUsername())) {
            return null;
        }

        User customer = new User();
        customer.setUsername(customerCreateDto.getUsername());
        customer.setPassword(passwordEncoder.encode(customerCreateDto.getPassword()));
        customer.setRole(customerCreateDto.getRole());
        customer.setUserStatus(UserStatus.ENABLED);

        return new UserDto(userRepository.save(customer));
    }

    public List<UserDto> getCustomers() {
        List<User> customers = userRepository.findByRole(Role.CUSTOMER);

        return customers
                .stream()
                .map(UserDto::new)
                .toList();
    }

    public UserDto getCustomer(Integer id) {
        User customer = userRepository.findByRoleAndId(Role.CUSTOMER, id).orElse(null);
        return customer != null ? new UserDto(customer) : null;
    }

    public UserDto getCustomer(String username) {
        User customer = userRepository.findByRoleAndUsername(Role.CUSTOMER, username).orElse(null);
        return customer != null ? new UserDto(customer) : null;
    }

    public UserDto updateCustomer(UserUpdateDto customerUpdateDto) {
        if(!userRepository.existsByRoleAndId(Role.CUSTOMER, customerUpdateDto.getId())) {
            return null;
        }

        User anotherUser = userRepository.findByUsername(customerUpdateDto.getUsername()).orElse(null);
        if(anotherUser != null && !Objects.equals(anotherUser.getId(), customerUpdateDto.getId())) {
            return null;
        }

        User customer = new User();
        customer.setId(customerUpdateDto.getId());
        customer.setUsername(customerUpdateDto.getUsername());
        customer.setPassword(passwordEncoder.encode(customerUpdateDto.getPassword()));
        customer.setRole(customerUpdateDto.getRole());

        return new UserDto(userRepository.save(customer));
    }

    @Transactional
    public void disabledCustomer(Integer id) {
        userRepository.disabledByRoleAndId(Role.CUSTOMER, id);
    }

}
