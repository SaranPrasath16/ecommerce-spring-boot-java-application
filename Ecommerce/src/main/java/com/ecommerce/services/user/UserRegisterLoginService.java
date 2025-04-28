package com.ecommerce.services.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.LoginRequestDTO;
import com.ecommerce.dto.LoginResponseDTO;
import com.ecommerce.dto.OtpRequestDTO;
import com.ecommerce.dto.TempUserDTO;
import com.ecommerce.exceptionhandler.EntityAlreadyExistsException;
import com.ecommerce.exceptionhandler.InvalidInputException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.exceptionhandler.UnauthorizedException;
import com.ecommerce.model.User;
import com.ecommerce.repo.UserRepo;
import com.ecommerce.services.cart.CartService;
import com.ecommerce.services.email.EmailService;
import com.ecommerce.services.otp.OtpService;
import com.ecommerce.util.EmailValidator;
import com.ecommerce.util.JwtUtil;

@Service
public class UserRegisterLoginService {
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final OtpService otpService;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final CartService cartService;
    private final EmailService emailService;

	public UserRegisterLoginService(PasswordEncoder passwordEncoder, EmailValidator emailValidator,
			OtpService otpService, UserRepo userRepo, JwtUtil jwtUtil, CartService cartService,
			EmailService emailService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.emailValidator = emailValidator;
		this.otpService = otpService;
		this.userRepo = userRepo;
		this.jwtUtil = jwtUtil;
		this.cartService = cartService;
		this.emailService = emailService;
	}

	public String registerUser(User userRegisterRequest){
        String email = userRegisterRequest.getEmail();

        if(!emailValidator.isValidEmail(email)){
            throw new InvalidInputException("Invalid email input.");
        }

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());

        TempUserDTO tempUser = new TempUserDTO(email, encodedPassword,
                userRegisterRequest.getUserName(), userRegisterRequest.getMobile(),userRegisterRequest.getAddress(),
                LocalDateTime.now());
        
        int otp = otpService.generateOtp(email, tempUser);
        emailService.sendOtpByEmail(email, otp,userRegisterRequest.getUserName());

        return "OTP sent to email. Please verify to complete registration.";
    }

    public String verifyUserOtp(OtpRequestDTO otpRequest)  {
        String email = otpRequest.getEmail();
        int otp = otpRequest.getOtp();
        

        if (!otpService.verifyOtp(email, otp)){
            throw new UnauthorizedException("Invalid or expired OTP.");
        }

        TempUserDTO tempUser = otpService.getTempUserDetails(email);
        
        boolean emailExists = userRepo.findByEmail(email) != null;
        if (emailExists) {
            throw new EntityAlreadyExistsException("An account with this email already exists");
        }

        else {
            User user = new User(tempUser.getUserName(),
                    email, tempUser.getEncodedPassword(),tempUser.getMobile(),tempUser.getAddress(),false,false,false);
           
            userRepo.save(user);
            cartService.addCartId(user.getUserId());
            emailService.sendAccountCreatedEmail(email, tempUser.getUserName());
            List<User> superAdmins = userRepo.findSuperAdmins();
            List<String> superAdminEmails = superAdmins.stream()
                                                       .map(User::getEmail)
                                                       .collect(Collectors.toList());
            emailService.intimateSuperAdmins(superAdminEmails,user);
            return "User added Successfully";
        }
    }
    
    public LoginResponseDTO validateUser(LoginRequestDTO loginRequestDTO){
        User user = userRepo.findByEmail(loginRequestDTO.getEmail());
        
        if (user == null) {
            throw new ResourceNotFoundException("Buyer with provided credentials not found");
        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidInputException("Wrong Credentials");
        }

        String role;
        if (user.isMainAdmin()) {
            role = "ROLE_MAIN_ADMIN";
        } else if (user.isProductAdmin()) {
            role = "ROLE_PRODUCT_ADMIN";
        } else {
            role = "ROLE_USER";
        }
        String token = jwtUtil.generateToken(user.getEmail(), role, user.getUserId());
        return new LoginResponseDTO(token, "Login successful and Token generated");
    }


}
