package gov.nist.hit.core.api.util;

import java.security.SecureRandom;
import java.util.Base64;

public class NonceGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateNonce() {
        byte[] nonceBytes = new byte[16];
        secureRandom.nextBytes(nonceBytes);
        return Base64.getEncoder().encodeToString(nonceBytes);
    }
}