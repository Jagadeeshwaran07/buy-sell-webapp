package com.labweek.menumate.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private final Map<String, Integer> otpStorage = new ConcurrentHashMap<>();

    // Store OTP for the given email
    public void saveOtp(String email, int otp) {
        otpStorage.put(email, otp);
    }

    // Validate OTP for the given email
    public boolean validateOtp(String email, int otp) {
        Integer storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp == otp;
    }

    // Clear OTP after successful verification
    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}