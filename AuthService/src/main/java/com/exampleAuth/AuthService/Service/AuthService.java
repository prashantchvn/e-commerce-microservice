package com.exampleAuth.AuthService.Service;

import com.exampleAuth.AuthService.Entity.Role;
import com.exampleAuth.AuthService.Entity.User;
import com.exampleAuth.AuthService.Repository.UserRepository;
import com.exampleAuth.AuthService.Utils.JWTUtils;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }
        if(user.getRole() == Role.USER){
            User newUser = User.builder()
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .isVerified(false)
                    .build();
            userRepository.save(newUser);
        }
        if(user.getRole() == Role.DEALER){
            User newUser = User.builder()
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .isVerified(false)
                    .buisnessAddress(user.getBuisnessAddress())
                    .businessId(user.getBusinessId())
                    .contactNumber(user.getContactNumber())
                    .build();
            userRepository.save(newUser);
        }
        return "Registration successful Please verify your account through otp before logging in";
    }

    public Map<String, String> login(String email, String password, String otp) {
        User user = userRepository.findByEmail(email).orElse(null);
        Map<String, String> response = new HashMap<>();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            response.put("error", "Invalid email or password");
            return response;
        }
        if(!user.getIsVerified()){
            if(StringUtils.isEmpty(otp)){
                //call email service with the email address
            }else{
                //check if redis have the otp for this user
                if(true){
                    //if found check the otp against the requested one
                }else{
                    //check in the DB against the OTP
                    if(true){
                        //check for the password matcher
                        if(true){
                            //sucessfull login
                            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                            response.put("token", jwtUtils.generateToken(userDetails));
                            user.setIsVerified(true);
                            userRepository.save(user);
                            //clear the redis otp for that user
                            return response;
                        }
                    }else{
                        // store the otp in redis server
                    }
                }
            }
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        response.put("token", jwtUtils.generateToken(userDetails));
        return response;
    }
}
