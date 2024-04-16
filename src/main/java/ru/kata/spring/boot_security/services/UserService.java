package ru.kata.spring.boot_security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.repositories.UserRepository;
import ru.kata.spring.boot_security.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

   private final UserRepository userRepository;

   @Autowired
   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public User findByUsername(String username) {
      return userRepository.findByUsername(username);
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = findByUsername(username);

      if (user == null) {
         throw new UsernameNotFoundException(String.format("User '%s' not found", username));
      }

      return new org.springframework.security.core.userdetails.User(
              user.getUsername(), user.getPassword(), user.getAuthorities());
   }

   @Transactional
   public User getUserById(Long id) {
      User user = null;
      Optional<User> optional = userRepository.findById(id);

      if (optional.isPresent()) {
         user = optional.get();
      } else {
         System.out.println("User not found. id = " + id);
      }

      return user;
   }

   @Transactional
   public List<User> getAllUsers() {
      return userRepository.findAll();
   }

   @Transactional
   public void saveUser(User user) {
      userRepository.save(user);
   }

   @Transactional
   public void deleteUser(Long id) {
      userRepository.deleteById(id);
   }
}