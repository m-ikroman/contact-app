package project_skripsi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import project_skripsi.rest.model.UpdateUserRequest;
import project_skripsi.rest.repository.UserRepository;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.RegisterUserRequest;
import project_skripsi.rest.model.UserResponse;
import project_skripsi.rest.security.BCrypt;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void register(RegisterUserRequest request){

        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);

    }

    public UserResponse get(User user){

        return UserResponse
                .builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();

    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request){

        validationService.validate(request);

        if (Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())){
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        return UserResponse.builder().username(user.getUsername()).name(user.getName()).build();

    }

}
