package qBitsAndCBits;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Classical collision-based search for Simon's problem.
 * Complexity ~ O(2^{n/2}) expected oracle queries.
 * 
 * This is from chatGPT
 */
public class SimonsProblemClassical {

    // ---------- Oracle interface ----------
    interface SimonOracleIfc {
        // Evaluate f(x) with x interpreted as an n-bit value (0..2^n-1).
        BigInteger f(BigInteger x);
        int n(); // number of bits
    }

    // ---------- A promise-respecting Simon oracle with hidden s ----------
    static class SimonOracle implements SimonOracleIfc {
        private final int n;
        private final BigInteger s;
        private final SecureRandom rnd = new SecureRandom();

        // Map each pair's canonical representative -> unique n-bit output
        private final Map<BigInteger, BigInteger> repToOutput = new HashMap<>();
        private final Set<BigInteger> usedOutputs = new HashSet<>();

        public SimonOracle(int n, BigInteger s) {
            if (n < 2) throw new IllegalArgumentException("n must be >= 2");
            if (s.equals(BigInteger.ZERO)) throw new IllegalArgumentException("s must be nonzero");
            if (s.bitLength() > n) throw new IllegalArgumentException("s must fit in n bits");
            this.n = n;
            this.s = s;
        }

        @Override
        public BigInteger f(BigInteger x) {
            x = maskNBits(x, n);
            BigInteger y = x.xor(s);
            BigInteger rep = x.min(y); // canonical representative of the pair {x, x^s}
            return repToOutput.computeIfAbsent(rep, k -> freshUniqueNBit());
        }

        @Override
        public int n() { return n; }

        // Generate a fresh random n-bit output not used before (to enforce 2-to-1 with unique images)
        private BigInteger freshUniqueNBit() {
            while (true) {
                BigInteger cand = randomNBits(n, rnd);
                if (usedOutputs.add(cand)) return cand;
            }
        }
    }

    // ---------- Classical search: find s by collision ----------
    static class ClassicalSimonFinder {
        private final SimonOracleIfc oracle;
        private final Random rnd = new SecureRandom();
        private long queries = 0;

        public ClassicalSimonFinder(SimonOracleIfc oracle) {
            this.oracle = oracle;
        }

        public Result findS(long maxQueries) {
            Map<BigInteger, BigInteger> outToInput = new HashMap<>();
            int n = oracle.n();

            while (queries < maxQueries) {
                BigInteger x = randomNBits(n, rnd);
                BigInteger fx = oracle.f(x); queries++;

                BigInteger prevX = outToInput.putIfAbsent(fx, x);
                if (prevX != null && !prevX.equals(x)) {
                    BigInteger s = prevX.xor(x);
                    return new Result(s, queries, true, prevX, x, fx);
                }
            }
            return new Result(null, queries, false, null, null, null);
        }

        public long queriesUsed() { return queries; }
    }

    static class Result {
        final BigInteger s;              // discovered secret
        final long queries;              // queries used
        final boolean found;             // was a collision found?
        final BigInteger x1, x2, fx;     // the colliding witnesses and the output

        Result(BigInteger s, long queries, boolean found, BigInteger x1, BigInteger x2, BigInteger fx) {
            this.s = s; this.queries = queries; this.found = found;
            this.x1 = x1; this.x2 = x2; this.fx = fx;
        }
    }

    // ---------- Utilities ----------
    static BigInteger randomNBits(int n, Random rnd) {
        // Uniform over {0,1}^n
        int byteLen = (n + 7) / 8;
        byte[] buf = new byte[byteLen];
        rnd.nextBytes(buf);
        BigInteger val = new BigInteger(1, buf);
        return maskNBits(val, n);
    }

    static BigInteger maskNBits(BigInteger x, int n) {
        if (n == 0) return BigInteger.ZERO;
        BigInteger mask = BigInteger.ONE.shiftLeft(n).subtract(BigInteger.ONE);
        return x.and(mask);
    }

    static String toBinary(BigInteger x, int n) {
        String b = x.toString(2);
        if (b.length() < n) {
            char[] pad = new char[n - b.length()];
            Arrays.fill(pad, '0');
            return new String(pad) + b;
        }
        if (b.length() > n) b = b.substring(b.length() - n);
        return b;
    }

    // ---------- Demo ----------
    public static void main(String[] args) {
        int n = 40; // try 20..30 for fun; runtime grows ~ 2^{n/2}
        SecureRandom rnd = new SecureRandom();

        // Choose a random nonzero secret s
        BigInteger s;
        do { s = randomNBits(n, rnd); } while (s.equals(BigInteger.ZERO));

        SimonOracle oracle = new SimonOracle(n, s);
        ClassicalSimonFinder finder = new ClassicalSimonFinder(oracle);

        // Set a generous cap on queries (a bit above birthday bound)
        long maxQueries = 3L * (1L << (n / 2)); // heuristic cap

        long t0 = System.currentTimeMillis();
        Result r = finder.findS(maxQueries);
        long t1 = System.currentTimeMillis();

        if (r.found) {
            System.out.println("Found collision:");
            System.out.println("  x1 = " + toBinary(r.x1, n));
            System.out.println("  x2 = " + toBinary(r.x2, n));
            System.out.println("  f(x1) = f(x2) = " + toBinary(r.fx, n));
            System.out.println();

            System.out.println("Discovered s = x1 XOR x2 = " + toBinary(r.s, n));
            System.out.println("True s                       " + toBinary(s, n));
            System.out.println("Match? " + r.s.equals(s));
            System.out.println("Oracle queries used: " + r.queries);
            System.out.println("Elapsed ms: " + (t1 - t0));
        } else {
            System.out.println("Failed to find a collision within the query budget.");
            System.out.println("Queries attempted: " + r.queries);
        }
    }
}
