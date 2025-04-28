package com.ecommerce.services.otp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.TempUserDTO;

@Service
public class OtpService {
	private final Map<String, Integer> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, TempUserDTO> tempUserStorage = new ConcurrentHashMap<>();
    private final long OTP_EXPIRATION_MINUTES = 5;

    public int generateOtp(String email, TempUserDTO tempUser){
        String otp1 = String.format("%06d", new Random().nextInt(999999));
        int otp= Integer.parseInt(otp1);
        otpStorage.put(email, otp);
        tempUserStorage.put(email, tempUser);
        return otp;
    }

    public boolean verifyOtp(String email, int otp){
        if (!otpStorage.containsKey(email) ||!(otp ==(otpStorage.get(email))) || isOtpExpired(email)) {
            return false;
        }
        otpStorage.remove(email);
        return true;
    }

    private boolean isOtpExpired(String email){
    	TempUserDTO tempUser = tempUserStorage.get(email);
        return tempUser.getOtpGeneratedTime().plusMinutes(OTP_EXPIRATION_MINUTES).isBefore(LocalDateTime.now());
    }

    public TempUserDTO getTempUserDetails(String email) {
        return tempUserStorage.remove(email);
        
    }
}
