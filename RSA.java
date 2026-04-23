import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    private BigInteger n, d, e;

    public RSA(int bitlen) {
        SecureRandom r = new SecureRandom();
        // 1. Choose two large primes p and q
        BigInteger p = BigInteger.probablePrime(bitlen / 2, r);
        BigInteger q = BigInteger.probablePrime(bitlen / 2, r);

        // 2. Compute n = p * q
        n = p.multiply(q);

        // 3. Compute phi = (p-1)(q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // 4. Choose e (commonly 65537) such that gcd(e, phi) = 1
        e = BigInteger.valueOf(65537);

        // 5. Compute d (modular inverse of e mod phi)
        d = e.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n); // C = M^e mod n
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n); // M = C^d mod n
    }

    public static void main(String[] args) {
        RSA rsa = new RSA(1024);
        BigInteger message = new BigInteger("12345"); // Sample numerical message

        BigInteger encrypted = rsa.encrypt(message);
        BigInteger decrypted = rsa.decrypt(encrypted);

        System.out.println("Original: " + message);
        System.out.println("Decrypted: " + decrypted);
    }
}
