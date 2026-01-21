package vibeSeqs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class KmerCumulativeCDF {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java KmerCumulativeCDF <k> <output.tsv> <path1> [<path2> ...]");
            System.exit(1);
        }

        int k = Integer.parseInt(args[0]);
        if (k < 1 || k > 31) {
            throw new IllegalArgumentException("k must be between 1 and 31.");
        }

        Path outTsv = Paths.get(args[1]);
        List<Path> roots = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            roots.add(Paths.get(args[i]));
        }

        Map<Long, Long> counts = new HashMap<>(1 << 20);
        long totalKmers = 0;

        for (Path root : roots) {
            if (!Files.exists(root)) continue;

            try (Stream<Path> paths = Files.walk(root)) {
                Iterator<Path> it = paths
                        .filter(Files::isRegularFile)
                        .filter(p -> looksLikeFasta(p.getFileName().toString()))
                        .iterator();

                while (it.hasNext()) {
                    Path fasta = it.next();
                    System.err.println("Reading: " + fasta);
                    totalKmers += countFileCanonicalKmers(fasta, k, counts);
                }
            }
        }

        List<Map.Entry<Long, Long>> entries = new ArrayList<>(counts.entrySet());
        entries.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        writeTsv(outTsv, entries, totalKmers, k, true);
        System.err.println("Wrote: " + outTsv.toAbsolutePath());
    }

    private static boolean looksLikeFasta(String name) {
        String l = name.toLowerCase(Locale.ROOT);
        return l.endsWith(".fa") || l.endsWith(".fasta") || l.endsWith(".fna") || l.endsWith(".fas");
    }

    private static long countFileCanonicalKmers(Path fasta, int k, Map<Long, Long> counts)
            throws IOException {

        long added = 0;
        try (BufferedReader br = Files.newBufferedReader(fasta, StandardCharsets.UTF_8)) {
            String line;
            StringBuilder seq = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (line.charAt(0) == '>') {
                    if (seq.length() > 0) {
                        added += addSeqCanonicalKmers(seq, k, counts);
                        seq.setLength(0);
                    }
                } else {
                    seq.append(line.trim());
                }
            }
            if (seq.length() > 0) {
                added += addSeqCanonicalKmers(seq, k, counts);
            }
        }
        return added;
    }

    private static long addSeqCanonicalKmers(CharSequence seq, int k, Map<Long, Long> counts) {
        final long mask = (1L << (2 * k)) - 1;
        long fwd = 0L, rev = 0L;
        int valid = 0;
        long added = 0;

        for (int i = 0; i < seq.length(); i++) {
            int b = baseToBits(seq.charAt(i));
            if (b < 0) {
                fwd = rev = 0;
                valid = 0;
                continue;
            }

            fwd = ((fwd << 2) | b) & mask;
            rev = (rev >>> 2) | ((long) complementBits(b) << (2 * (k - 1)));

            if (++valid >= k) {
                long canon = Math.min(fwd, rev);
                counts.merge(canon, 1L, Long::sum);
                added++;
            }
        }
        return added;
    }

    private static int baseToBits(char c) {
        switch (c) {
            case 'A': case 'a': return 0;
            case 'C': case 'c': return 1;
            case 'G': case 'g': return 2;
            case 'T': case 't': return 3;
            default: return -1;
        }
    }

    private static int complementBits(int b) {
        return 3 - b;
    }

    // ---------------- TSV OUTPUT ----------------
    private static void writeTsv(
            Path out,
            List<Map.Entry<Long, Long>> sorted,
            long total,
            int k,
            boolean includeKmer
    ) throws IOException {

        try (BufferedWriter bw = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {

            if (includeKmer) {
                bw.write("rank\tcount\tfraction_of_total\tcumulative_count\tcumulative_fraction\tkmer\n");
            } else {
                bw.write("rank\tcount\tfraction_of_total\tcumulative_count\tcumulative_fraction\n");
            }

            long cum = 0;
            long rank = 0;

            for (Map.Entry<Long, Long> e : sorted) {
                rank++;
                long count = e.getValue();
                cum += count;

                double frac = (double) count / total;
                double cumFrac = (double) cum / total;

                bw.write(rank + "\t" +
                         count + "\t" +
                         frac + "\t" +
                         cum + "\t" +
                         cumFrac);

                if (includeKmer) {
                    bw.write("\t" + decodeKmer(e.getKey(), k));
                }
                bw.write("\n");
            }
        }
    }

    private static String decodeKmer(long code, int k) {
        char[] out = new char[k];
        for (int i = k - 1; i >= 0; i--) {
            out[i] = bitsToBase((int) (code & 3));
            code >>>= 2;
        }
        return new String(out);
    }

    private static char bitsToBase(int b) {
        return "ACGT".charAt(b);
    }
}


