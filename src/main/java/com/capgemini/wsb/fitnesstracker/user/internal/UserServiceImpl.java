package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long userId){
        return userRepository.findById(userId);
    }

    @Override
    public  void removeUser(final User user){
        log.info("User {} removed", user.getId());
        if(user.getId() == null){
            throw new IllegalArgumentException("This user doesn't exist!");
        }
        userRepository.delete(user);
    }

    @Override
    public User addUser(final User user){
        log.info("Adding user :{}", user);
        User thisUser = userRepository.findById(user.getId()).get();
        thisUser.setEmail(user.getEmail());
        thisUser.setFirstName(user.getFirstName());
        thisUser.setLastName(user.getLastName());
        userRepository.save(thisUser);
        return  user;

    }
    @Override
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<User> findUserOlderThanX(Integer age) {
        return userRepository.findUserOlderThanX(age);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This user not found!"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setBirthdate(user.getBirthdate());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }
}