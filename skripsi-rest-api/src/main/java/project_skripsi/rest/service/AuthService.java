package project_skripsi.rest.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.server.ResponseStatusException;
import project_skripsi.rest.repository.UserRepository;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.LoginUserRequest;
import project_skripsi.rest.model.TokenResponse;
import project_skripsi.rest.security.BCrypt;

import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;

    @Transactional
    public TokenResponse login(LoginUserRequest request){

        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username of password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())){

            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Day());

            userRepository.save(user);

            return TokenResponse
                    .builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username of password wrong");
        }

    }

    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

    private Long next30Day(){
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }

}
