import java.util.Scanner;

public class MathAlgorithms {

    // 1. Euclidean Algorithm (GCD)
    // Logic: Keep replacing 'a' with 'b' and 'b' with 'a % b' until b becomes 0.
    public static int getGCD(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // 2. Primality Test (Optimized Trial Division)
    // Logic: Skip even numbers and check only up to the square root of n.
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;

        // Eliminate multiples of 2 and 3 immediately
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // Example Usage
        System.out.println("GCD of 48 and 18: " + getGCD(48, 18)); // Output: 6

        int num = 29;
        System.out.println(num + " is prime? " + isPrime(num)); // Output: true
    }
}
