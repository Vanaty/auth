package itu.auth.mg.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Hash a raw password.
     * @param rawPassword The plain-text password to hash.
     * @return The hashed password.
     */
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verify if a raw password matches the hashed password.
     * @param rawPassword The plain-text password.
     * @param hashedPassword The hashed password stored in the database.
     * @return True if the passwords match, false otherwise.
     */
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}

