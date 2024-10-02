package com.thiago.abarros.ms.user.utils;

import java.security.SecureRandom;

/**
 * Generates random passwords of a specified length, ensuring they meet common password requirements.
 *
 * @see #generateRandomPassword(int)
 */
public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random password of the specified length.
     * <p>
     * The generated password will contain at least one uppercase letter, one lowercase letter, one digit, and one special character.
     *
     * @param length the desired length of the password
     * @return a random password of the specified length
     */
    public static String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);

        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        return shuffleString(password.toString());
    }

    /**
     * Shuffles the characters in the input string to add an extra layer of randomness.
     *
     * @param input the string to shuffle
     * @return the shuffled string
     */
    private static String shuffleString(String input) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = a[index];
            a[index] = a[i];
            a[i] = temp;
        }
        return new String(a);
    }
}
