package com.hmsapp.service;

import com.hmsapp.entity.User;
import com.hmsapp.payload.JwtToken;
import com.hmsapp.payload.LoginDto;
import com.hmsapp.payload.ProfileDto;
import com.hmsapp.payload.UserDto;
import com.hmsapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private JWTService jwtService;
    private OTPService otpService;

    public AuthService(UserRepository userRepository, ModelMapper modelMapper, JWTService jwtService, OTPService otpService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }




    UserDto mapToDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);
        return dto;
    }

    User mapToEntity(UserDto dto) {
        User user = modelMapper.map(dto, User.class);
        return user;
    }


    public ResponseEntity<?> createUser(@Valid UserDto dto) {

     User user = mapToEntity(dto);


        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if (opUsername.isPresent()) {
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }

        Optional<User> opEmail = userRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            return new ResponseEntity<>("Email already exist", HttpStatus.BAD_REQUEST);
        }

        Optional<User> opMobile = userRepository.findByMobile(user.getMobile());
        if (opMobile.isPresent()) {
            return new ResponseEntity<>("Mobile number already exist", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        user.setRole("ROLE_USER");
        User saved = userRepository.save(user);
        UserDto userDto = mapToDto(saved);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }



    public ResponseEntity<?> verifyLogin(LoginDto loginDto) throws UnsupportedEncodingException {
        Optional<User> opUser = userRepository.findByUsername(loginDto.getUsername());
        if(opUser.isPresent()) {
            User user = opUser.get();
            if(BCrypt.checkpw(loginDto.getPassword(), user.getPassword())){
                String token = jwtService.generateToken(user.getUsername());

                return new ResponseEntity<>(token,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Invalid Credentials!",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> createPropertyOwnerAccount(@Valid UserDto dto) {
        User user = mapToEntity(dto);


        Optional<User> opUsername = userRepository.findByUsername(user.getUsername());
        if (opUsername.isPresent()) {
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }

        Optional<User> opEmail = userRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            return new ResponseEntity<>("Email already exist", HttpStatus.BAD_REQUEST);
        }

        Optional<User> opMobile = userRepository.findByMobile(user.getMobile());
        if (opMobile.isPresent()) {
            return new ResponseEntity<>("Mobile number already exist", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        user.setRole("ROLE_OWNER");
        User saved = userRepository.save(user);
        UserDto userDto = mapToDto(saved);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

//    public ResponseEntity<?> createBlogManagerAccount(
//             UserDto dto
//    ){
//        //and all this code in service layer
//        Optional<User> opUsername = userRepository.findByUsername(dto.getUsername());
//        if (opUsername.isPresent()) {
//            return new ResponseEntity("Username already exists.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        Optional<User> opEmail = userRepository.findByEmail(dto.getEmail());
//        if (opEmail.isPresent()) {
//            return new ResponseEntity("Email already exists.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        Optional<User> opMobile = userRepository.findByMobile(dto.getMobile());
//        if (opMobile.isPresent()) {
//            return new ResponseEntity("Mobile already exists.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        dto.setPassword(BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt(10)));
//        dto.setRole("ROLE_BLOGMANAGER");
//        User user = mapToEntity(dto);
//        User savedUser = userRepository.save(user);
//        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//    }

    public static ResponseEntity<ProfileDto> getUserProfile(User user) {
        ProfileDto dto = new ProfileDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setUsername(user.getUsername());
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    public ResponseEntity<?> verifyOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(mobileNumber, otp);

        if (isValid) {
            // Normalize incoming mobile number by removing the +91 prefix if it exists
            String normalizedMobileNumber = normalizeMobileNumber(mobileNumber);
            // Find user by mobile number
            Optional<User> userOpt = userRepository.findByMobile(normalizedMobileNumber);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String token = jwtService.generateToken(user.getUsername()); // Generate JWT token

                JwtToken jwtToken = new JwtToken();
                jwtToken.setToken(token);
                jwtToken.setType("JWT");
                return new ResponseEntity<>(jwtToken, HttpStatus.OK); // Return JWT token on successful login
            } else {
                return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Invalid or expired OTP. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }
    private String normalizeMobileNumber(String mobileNumber) {
        // If mobile number starts with +91, remove it
        if (mobileNumber.startsWith("+91")) {
            return mobileNumber.substring(3); // Remove +91
        }
        return mobileNumber; // Return as is if it doesn't start with +91
    }
}
