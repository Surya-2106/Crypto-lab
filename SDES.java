public class SDES {
    // Permutation Tables
    static int[] P10 = {3,5,2,7,4,10,1,9,8,6};
    static int[] P8  = {6,3,7,4,8,5,10,9};
    static int[] IP  = {2,6,3,1,4,8,5,7};
    static int[] IP_INV = {4,1,3,5,7,2,8,6};
    static int[] EP  = {4,1,2,3,2,3,4,1};
    static int[] P4  = {2,4,3,1};

    // S-Boxes
    static int[][][] S = {
        {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,0,2}},
        {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}}
    };

    // Helper: Permutation
    static int permute(int input, int[] map, int bitLen) {
        int result = 0;
        for(int i=0; i<map.length; i++) {
            if(((input >> (bitLen - map[i])) & 1) == 1) {
                result |= (1 << (map.length - 1 - i));
            }
        }
        return result;
    }

    // Helper: fK function
    static int fk(int input, int key) {
        int left = (input >> 4) & 0xF;
        int right = input & 0xF;

        int ep = permute(right, EP, 4);
        int xored = ep ^ key;

        int l_bits = (xored >> 4) & 0xF;
        int r_bits = xored & 0xF;

        int row0 = ((l_bits & 8) >> 2) | (l_bits & 1);
        int col0 = (l_bits >> 1) & 3;

        int row1 = ((r_bits & 8) >> 2) | (r_bits & 1);
        int col1 = (r_bits >> 1) & 3;

        int s_val = (S[0][row0][col0] << 2) | S[1][row1][col1];
        int p4_val = permute(s_val, P4, 4);

        return ((left ^ p4_val) << 4) | right;
    }

    public static int encrypt(int plaintext, int key) {
        return run(plaintext, key, true);
    }

    public static int decrypt(int ciphertext, int key) {
        return run(ciphertext, key, false);
    }

    private static int run(int text, int key, boolean encrypt) {
        // Key Generation
        int k_perm = permute(key, P10, 10);
        int l = (k_perm >> 5) & 0x1F, r = k_perm & 0x1F;

        // LS-1
        l = ((l << 1) & 0x1F) | (l >> 4);
        r = ((r << 1) & 0x1F) | (r >> 4);
        int k1 = permute((l << 5) | r, P8, 10);

        // LS-2 (additional shift)
        l = ((l << 2) & 0x1F) | (l >> 3); // Total 3 shifts from original
        r = ((r << 2) & 0x1F) | (r >> 3);
        int k2 = permute((l << 5) | r, P8, 10);

        int key1 = encrypt ? k1 : k2;
        int key2 = encrypt ? k2 : k1;

        // Process
        int t = permute(text, IP, 8);
        t = fk(t, key1);

        // Swap halves
        t = ((t & 0xF) << 4) | ((t >> 4) & 0xF);

        t = fk(t, key2);

        return permute(t, IP_INV, 8);
    }

    public static void main(String[] args) {
        int plaintext = 0b10101010;
        int key = 0b1010000010;

        int cipher = encrypt(plaintext, key);
        System.out.println("Plaintext:  " + Integer.toBinaryString(plaintext));
        System.out.println("Key:        " + Integer.toBinaryString(key));
        System.out.println("Ciphertext: " + Integer.toBinaryString(cipher));

        int recovered = decrypt(cipher, key);
        System.out.println("Decrypted:  " + Integer.toBinaryString(recovered));
    }
}
