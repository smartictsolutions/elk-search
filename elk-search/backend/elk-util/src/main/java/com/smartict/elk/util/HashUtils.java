/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public HashUtils() {}

    private static String hexEncode(byte[] input) {
        StringBuilder result = new StringBuilder();
        char[] digits = new char[] {
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        };
        byte[] var3 = input;
        int var4 = input.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            result.append(digits[(b & 240) >> 4]);
            result.append(digits[b & 15]);
        }

        return result.toString();
    }

    public static String sha256(String input) throws NoSuchAlgorithmException {
        byte[] requestId = input.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(requestId);
        return hexEncode(md.digest());
    }
}
