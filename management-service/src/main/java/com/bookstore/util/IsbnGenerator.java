package com.bookstore.util;

import java.util.Random;

public class IsbnGenerator {

    public static String generateIsbn() {
        Random random = new Random();
        StringBuilder isbn = new StringBuilder("978");

        while (isbn.length() < 12) {
            isbn.append(random.nextInt(10));
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += digit * (i % 2 == 0 ? 1 : 3);
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        isbn.append(checkDigit);

        return isbn.toString();
    }

}
