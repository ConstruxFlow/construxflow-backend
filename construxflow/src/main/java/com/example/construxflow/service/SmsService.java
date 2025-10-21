package com.example.construxflow.service;

import com.twilio.exception.AuthenticationException;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    public String sendSms(String to, String body) {
        try {
            // Validate phone number format
            if (to == null || to.trim().isEmpty()) {
                throw new IllegalArgumentException("Recipient phone number cannot be empty");
            }

            // Ensure phone number starts with country code
            String formattedTo = formatPhoneNumber(to);

            // Validate message body
            if (body == null || body.trim().isEmpty()) {
                body = "Hello from ConstruxFlow!"; // Default message
            }

            System.out.println("Sending SMS to: " + formattedTo + " from: " + twilioPhoneNumber);

            Message message = Message.creator(
                    new PhoneNumber(formattedTo),
                    new PhoneNumber(twilioPhoneNumber),
                    body
            ).create();

            System.out.println("SMS sent successfully with SID: " + message.getSid());
            return message.getSid();

        } catch (AuthenticationException e) {
            System.err.println("Twilio authentication failed: " + e.getMessage());
            throw new RuntimeException("Authentication failed: Please check your Twilio credentials (Account SID and Auth Token)", e);
        } catch (TwilioException e) {
            System.err.println("Twilio error: " + e.getMessage());
            throw new RuntimeException("Failed to send SMS: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Failed to send SMS: " + e.getMessage(), e);
        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Remove any spaces, dashes, or parentheses
        String cleaned = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");

        // If it doesn't start with +, determine the country code
        if (!cleaned.startsWith("+")) {
            if (cleaned.startsWith("071") || cleaned.startsWith("072") || cleaned.startsWith("075") ||
                cleaned.startsWith("076") || cleaned.startsWith("077") || cleaned.startsWith("078")) {
                // Sri Lankan mobile numbers starting with 07X
                cleaned = "+94" + cleaned.substring(1); // Remove leading 0 and add +94
            } else if (cleaned.startsWith("07")) {
                // Other Sri Lankan mobile numbers
                cleaned = "+94" + cleaned.substring(1);
            } else if (cleaned.startsWith("01")) {
                // Sri Lankan landline numbers
                cleaned = "+94" + cleaned.substring(1);
            } else if (cleaned.length() == 10 && cleaned.matches("\\d{10}")) {
                // US number (10 digits)
                cleaned = "+1" + cleaned;
            } else if (cleaned.length() == 11 && cleaned.startsWith("1")) {
                // US number with leading 1
                cleaned = "+" + cleaned;
            } else if (cleaned.startsWith("0") && cleaned.length() >= 9) {
                // Assume Sri Lankan number if starts with 0
                cleaned = "+94" + cleaned.substring(1);
            } else {
                // For other countries, add + if not present
                cleaned = "+" + cleaned;
            }
        }

        System.out.println("Formatted phone number: " + phoneNumber + " -> " + cleaned);
        return cleaned;
    }
}
