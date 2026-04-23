import java.math.BigInteger;

public class DiffieHellman {
    public static void main(String[] args) {
        // Public parameters (p must be prime, g is a primitive root)
        BigInteger p = new BigInteger("23");
        BigInteger g = new BigInteger("5");

        // Alice chooses private key 'a' and computes public key 'A'
        BigInteger a = new BigInteger("6");
        BigInteger A = g.modPow(a, p); // A = g^a mod p

        // Bob chooses private key 'b' and computes public key 'B'
        BigInteger b = new BigInteger("15");
        BigInteger B = g.modPow(b, p); // B = g^b mod p

        // They exchange A and B, then calculate the shared secret
        BigInteger secretAlice = B.modPow(a, p); // S = B^a mod p
        BigInteger secretBob = A.modPow(b, p);   // S = A^b mod p

        System.out.println("Alice's Secret: " + secretAlice);
        System.out.println("Bob's Secret: " + secretBob);
    }
}
