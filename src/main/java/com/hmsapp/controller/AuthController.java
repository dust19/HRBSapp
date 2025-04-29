package com.hmsapp.controller;

import com.hmsapp.entity.User;
import com.hmsapp.payload.JwtToken;
import com.hmsapp.payload.LoginDto;
import com.hmsapp.payload.ProfileDto;
import com.hmsapp.payload.UserDto;
import com.hmsapp.repository.UserRepository;
import com.hmsapp.service.AuthService;
import com.hmsapp.service.OTPService;
import com.hmsapp.service.TwilioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private UserRepository userRepository;
    private OTPService otpService;
    private TwilioService twilioService;

    public AuthController(AuthService authService, UserRepository userRepository, OTPService otpService, TwilioService twilioService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.twilioService = twilioService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto dto){
        ResponseEntity<?> user = authService.createUser(dto);
        return user;
    }
    @PostMapping("/property/sign-up")
    public ResponseEntity<?> createPropertyOwnerAccount(@Valid @RequestBody UserDto dto){
        ResponseEntity<?> user = authService.createPropertyOwnerAccount(dto);
        return user;
    }

//    @PostMapping("/blog/sign-up")
//    public ResponseEntity<?> createBlogManagerAccount(@Valid @RequestBody UserDto dto){
//        ResponseEntity<?> user = authService.createBlogManagerAccount(dto);
//        return user;
//    }
    @PostMapping("/blog/sign-up")
    public ResponseEntity<?> createBlogManagerAccount(
        @RequestBody User user
    ){
        //and all this code in service layer
        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if (opUsername.isPresent()) {
            return new ResponseEntity("Username already exists.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opEmail = userRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            return new ResponseEntity("Email already exists.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opMobile = userRepository.findByMobile(user.getMobile());
        if (opMobile.isPresent()) {
            return new ResponseEntity("Mobile already exists.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        user.setRole("ROLE_BLOGMANAGER");
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws UnsupportedEncodingException {

        ResponseEntity<?> response = authService.verifyLogin(loginDto);

        //Logic to send the response on success in the form of JSON.
        if (response.getStatusCode() == HttpStatus.OK) {
            String token = (String) response.getBody(); // Extract the token from the body
            JwtToken jwtToken = new JwtToken();
            jwtToken.setToken(token);
            jwtToken.setType("JWT");
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        }

        return response; // Return the error response as it is
    }



    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getUserProfile(@AuthenticationPrincipal User user) {
        ResponseEntity<ProfileDto> userProfile = AuthService.getUserProfile(user);
        return userProfile;
    }
    //http://localhost:8080/api/auth/log-in/generate-otp?mobileNumber=%2B917899609771
    //Special characters in URL (+)
    //The + sign in +917899609771 might cause issues in some cases because URLs interpret + as a space.
    //Solution: Encode the + sign as %2B in the URL
    @PostMapping("/log-in/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestParam String mobileNumber) {
        String otp = otpService.generateOtp(mobileNumber);

        // Send OTP via Twilio
        twilioService.sendSms(mobileNumber, "Your OTP for login is: " + otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully to " + mobileNumber);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/api/auth/verify-otp?mobileNumber=%2B917899609771&otp=827163
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        return authService.verifyOtp(mobileNumber, otp);
    }


}
