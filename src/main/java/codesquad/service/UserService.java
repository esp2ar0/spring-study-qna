package codesquad.service;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public void update(User user, User modifiedUser) {
        user.update(modifiedUser);
        userRepository.save(user);
    }
}
