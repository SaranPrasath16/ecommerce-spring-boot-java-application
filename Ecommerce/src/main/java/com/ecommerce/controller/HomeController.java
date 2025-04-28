package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.dto.LoginRequestDTO;
import com.ecommerce.dto.LoginResponseDTO;
import com.ecommerce.dto.OtpRequestDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.model.User;
import com.ecommerce.services.user.UserImpl;
import com.ecommerce.services.user.UserRegisterLoginService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class HomeController {

	private final UserRegisterLoginService userRegisterLoginService;
	private final UserImpl userImpl;
	
    public HomeController(UserRegisterLoginService userRegisterLoginService, UserImpl userImpl) {
		super();
		this.userRegisterLoginService = userRegisterLoginService;
		this.userImpl = userImpl;
	}
	
	@GetMapping("/homepage")
    public ResponseEntity<List<ProductGetResponseDTO>> getHomePage(){
        List<ProductGetResponseDTO> productList = userImpl.getHomePage();
        return  ResponseEntity.ok(productList);
    }

	@PostMapping("/user/register")
	public ResponseEntity<String> registerUser(@RequestBody User userRequest){
	    String msg = userRegisterLoginService.registerUser(userRequest);
	    return ResponseEntity.ok(msg);
	}
	
    @PostMapping("/user/otp")
    public ResponseEntity<String> verifyUserOtp(@RequestBody OtpRequestDTO otpRequest){
        String msg = userRegisterLoginService.verifyUserOtp(otpRequest);
        return ResponseEntity.ok(msg);
    }
    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response){
        LoginResponseDTO loginResponseDTO = userRegisterLoginService.validateUser(loginRequestDTO);
        response.setHeader("Authorization", "Bearer " + loginResponseDTO.getToken());

        return ResponseEntity.ok(loginResponseDTO.getMessage());
    }
}


