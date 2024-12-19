package itu.auth.mg.util;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class Utilitaire {
    public static String generatePin() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String encodePin(String pin) {
        return Base64.getEncoder().encodeToString(pin.getBytes());
    }

    public static String decodePin(String encodedPin) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPin);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }
}
