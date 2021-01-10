package com.servbyte.ecommerce.service.serviceImpl;

import com.servbyte.ecommerce.dtos.ApplicationUserDto;
import com.servbyte.ecommerce.dtos.ApplicationUserLoginDto;
import com.servbyte.ecommerce.dtos.response.ApplicationUserResponse;
import com.servbyte.ecommerce.dtos.response.JwtResponse;
import com.servbyte.ecommerce.entities.ApplicationUser;
import com.servbyte.ecommerce.enums.ApiErrorCodes;
import com.servbyte.ecommerce.exceptions.BadRequestException;
import com.servbyte.ecommerce.repository.ApplicationUserRepository;
import com.servbyte.ecommerce.security.JwtTokenService;
import com.servbyte.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private ApplicationUserRepository userRepository;

    private PasswordEncoder bCryptPasswordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtTokenService jwtTokenService;

    private final List<String> roleList = Arrays.asList("USER", "RESTAURANT", "LOGISTICS");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserServiceImpl(ApplicationUserRepository userRepository, PasswordEncoder bCryptPasswordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void registerUser(ApplicationUserDto userDTO) {
        ApplicationUser user = new ApplicationUser();
        BeanUtils.copyProperties(userDTO, user);
        if(!roleList.stream().anyMatch(role -> user.getRole().equals(role))){
            throw new BadRequestException(ApiErrorCodes.INVALID_REQUEST.getKey(), "Role not found");
        }
        String password = bCryptPasswordEncoder.encode(userDTO.getPassword());
        user.setPassword(password);
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
    }


    @Override
    public List<ApplicationUserResponse> findAllUsers(){
        List<ApplicationUser> listOfUsers = userRepository.findAll();
        if(listOfUsers == null) throw new EntityNotFoundException("No user found");
        List<ApplicationUserResponse> userResponses = listOfUsers.stream().map(user -> {
            ApplicationUserResponse userResponse = new ApplicationUserResponse();
            BeanUtils.copyProperties(user, userResponse);
            return userResponse;
        }).collect(Collectors.toList());
        return userResponses;
    }

    @Override
    public ApplicationUser findUserByEmail(String email) {
        if(email != null) return userRepository.findByEmail(email);
        else throw new BadRequestException(ApiErrorCodes.INVALID_REQUEST.getKey(), "Email connot be empty");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()));
        return user;
    }

    @Override
    public JwtResponse generateToken(ApplicationUserLoginDto loginDTO) {
        JwtResponse jwtResponse = new JwtResponse();
        final Authentication auth = authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        ApplicationUser user = (ApplicationUser) loadUserByUsername(loginDTO.getEmail());
        BeanUtils.copyProperties(user, jwtResponse);
        jwtResponse.setAccessToken(jwtTokenService.generateToken(loginDTO.getEmail(), user.getRole()));

        return jwtResponse;
    }

    private Authentication authenticate(String username, String password)  {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e){
            e.printStackTrace();
            throw new DisabledException("Disabled ", e);
        }catch (BadCredentialsException e){
            e.printStackTrace();
            throw new BadCredentialsException("Incorrect credentials ", e);
        }
    }

}

