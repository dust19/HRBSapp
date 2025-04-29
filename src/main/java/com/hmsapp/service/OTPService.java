package com.hmsapp.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    private final Map<String, OtpData> otpStore = new HashMap<>();
    private final Random random = new Random();

    private static final long OTP_VALIDITY_DURATION = 60 * 1000; // 1 minute (in milliseconds)

    public String generateOtp(String mobileNumber) {
        String otp = String.format("%06d", random.nextInt(999999)); // Generate 6-digit OTP
        long expiryTime = System.currentTimeMillis() + OTP_VALIDITY_DURATION;

        // Store OTP and expiry time in the map
        otpStore.put(mobileNumber, new OtpData(otp, expiryTime));

        return otp;
    }

    public boolean verifyOtp(String mobileNumber, String enteredOtp) {
        OtpData otpData = otpStore.get(mobileNumber);

        if (otpData == null) {
            return false; // No OTP found
        }

        // Check if OTP is expired
        if (System.currentTimeMillis() > otpData.getExpiryTime()) {
            otpStore.remove(mobileNumber); // Remove expired OTP
            return false;
        }

        // Verify OTP
        if (otpData.getOtp().equals(enteredOtp)) {
            otpStore.remove(mobileNumber); // Remove OTP after successful verification
            return true;
        }

        return false;
    }


    // Helper class to store OTP and expiry time
    private static class OtpData {
        private final String otp;
        private final long expiryTime;

        public OtpData(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }
}
