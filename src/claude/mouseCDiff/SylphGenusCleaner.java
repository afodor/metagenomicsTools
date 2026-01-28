package claude.mouseCDiff;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Robust genus extraction and cleaning for Sylph-style reference strings.
 *
 * Example input:
 *   "NZ_CP136422.1 Blautia coccoides strain DSM 935 chromosome, complete genome"
 * Output:
 *   "Blautia"
 *
 * Designed to tolerate:
 * - NCBI accessions with versions: NZ_CP136422.1, CP036170.1, AP018533.1, NC_000913.3
 * - Bracket genera: [Clostridium] scindens ...
 * - "MAG ... uncultured <Genus> sp."
 * - "Candidatus <Genus> <species>"
 * - Family/order buckets: Lachnospiraceae bacterium, Clostridiales bacterium
 * - Hyphen merged bins: Escherichia-Shigella, Burkholderia-Caballeronia-Paraburkholderia
 */
public class SylphGenusCleaner {

    /* =========================
     * 1) Explicit cleanup maps
     * ========================= */

    /** 16S label cleanup: maps exact 16S bins to canonical genus/family bucket. */
    public static final Map<String, String> CLEAN_16S = new HashMap<>();

    /** WGS cleanup: maps genus tokens (and a few full-name exceptions) to canonical genus bucket. */
    public static final Map<String, String> CLEAN_WGS = new HashMap<>();

    static {
        // ---- 16S buckets / bins
        CLEAN_16S.put("Escherichia-Shigella", "Escherichia");
        CLEAN_16S.put("Burkholderia-Caballeronia-Paraburkholderia", "Burkholderia");

        CLEAN_16S.put("[Eubacterium]_hallii_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_coprostanoligenes_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_fissicatena_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_eligens_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_nodatum_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_ventriosum_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_ruminantium_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_brachy_group", "Eubacterium");
        CLEAN_16S.put("Eubacterium", "Eubacterium");

        CLEAN_16S.put("[Ruminococcus]_gnavus_group", "Ruminococcus");
        CLEAN_16S.put("[Ruminococcus]_gauvreauii_group", "Ruminococcus");
        CLEAN_16S.put("[Ruminococcus]_torques_group", "Ruminococcus");

        CLEAN_16S.put("[Clostridium]_innocuum_group", "Clostridium");
        CLEAN_16S.put("[Clostridium]_methylpentosum_group", "Clostridium");
        CLEAN_16S.put("Clostridium_sensu_stricto_1", "Clostridium");
        CLEAN_16S.put("Clostridium_sensu_stricto_13", "Clostridium");

        CLEAN_16S.put("Lachnospiraceae_FCS020_group", "Lachnospiraceae");
        CLEAN_16S.put("Lachnospiraceae_ND3007_group", "Lachnospiraceae");
        CLEAN_16S.put("Lachnospiraceae_NK4A136_group", "Lachnospiraceae");
        CLEAN_16S.put("Lachnospiraceae_UCG-001", "Lachnospiraceae");
        CLEAN_16S.put("Lachnospiraceae_UCG-004", "Lachnospiraceae");
        CLEAN_16S.put("Lachnospiraceae_UCG-010", "Lachnospiraceae");
        CLEAN_16S.put("Lachnospiraceae", "Lachnospiraceae");

        CLEAN_16S.put("Erysipelotrichaceae_UCG-003", "Erysipelotrichaceae");
        CLEAN_16S.put("Erysipelotrichaceae", "Erysipelotrichaceae");

        CLEAN_16S.put("Christensenellaceae_R-7_group", "Christensenellaceae");

        CLEAN_16S.put("Family_XIII_UCG-001", "Family_XIII");
        CLEAN_16S.put("Family_XIII_AD3011_group", "Family_XIII");

        CLEAN_16S.put("uncultured", "uncultured");
        CLEAN_16S.put("Incertae_Sedis", "Incertae_Sedis");
        CLEAN_16S.put("Chloroplast", "Chloroplast");

        // ---- WGS overrides
        CLEAN_WGS.put("Shigella", "Escherichia");
        CLEAN_WGS.put("Escherichia-Shigella", "Escherichia"); // just in case

        // Optional collapsing of newer genera into older/16S-common genera:
        CLEAN_WGS.put("Phocaeicola", "Bacteroides");
        CLEAN_WGS.put("Segatella", "Prevotella");

        // Candidatus Akkermansia example you asked about:
        CLEAN_WGS.put("Candidatus_Akkermansia_timonensis", "Akkermansia");
        CLEAN_WGS.put("Candidatus_Akkermansia", "Akkermansia");

        // Family-level “bacterium” labels from WGS references
        CLEAN_WGS.put("Lachnospiraceae_bacterium", "Lachnospiraceae");
        CLEAN_WGS.put("Christensenellaceae_bacterium", "Christensenellaceae");
        CLEAN_WGS.put("Bacteroidaceae_bacterium", "Bacteroidaceae");
        CLEAN_WGS.put("Clostridiales_bacterium", "Clostridiales");
        CLEAN_WGS.put("Oscillospiraceae_bacterium", "Oscillospiraceae");
        CLEAN_WGS.put("Vallitaleaceae_bacterium", "Vallitaleaceae");
    }

    /* =========================
     * 2) Parsing / token helpers
     * ========================= */

    // Improved accession pattern: covers NZ_CP136422.1, NC_000913.3, CP036170.1, AP018533.1, etc.
    private static final Pattern ACCESSION_PREFIX =
            Pattern.compile("^[A-Z]{1,4}_[A-Z]{1,6}\\d+\\.\\d+$|^[A-Z]{2,6}\\d+\\.\\d+$");

    private static final Pattern BRACKET_GENUS = Pattern.compile("^\\[([A-Za-z][A-Za-z0-9_-]*)\\]$");

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "MAG", "TPA_asm", "MAG:", "TPA_asm:",
            "complete", "genome", "chromosome", "plasmid",
            "strain", "isolate", "assembly", "contig", "scaffold", "sequence",
            "whole", "shotgun", "project", "data", "WGS",
            "chromosome,", "genome,", "sequence,", "assembly,"
    ));

    private static boolean looksLikeAccession(String tok) {
        if (tok == null) return false;
        String t = stripPunct(tok);
        // must have version suffix like ".1"
        if (!t.matches(".*\\.\\d+$")) return false;
        // must contain at least one digit
        if (!t.matches(".*\\d.*")) return false;
        // usually uppercase/digits/underscore before the version
        return t.matches("^[A-Z0-9_]+\\.\\d+$");
    }

    private static String stripPunct(String token) {
        if (token == null) return "";
        return token.replaceAll("^[\\p{Punct}]+|[\\p{Punct}]+$", "");
    }

    private static String normalizeGenusToken(String token) {
        String t = stripPunct(token);

        // If "Genus_species" take genus part
        int us = t.indexOf('_');
        if (us > 0) t = t.substring(0, us);

        // Map merged 16S-style bin if it appears on WGS side
        if (t.equalsIgnoreCase("Escherichia-Shigella")) return "Escherichia";

        return t;
    }

    private static String applyWgsOverrides(String genusOrBucket) {
        if (genusOrBucket == null) return null;

        String g = genusOrBucket;

        // If passed as "Candidatus_<Genus>", strip prefix
        if (g.startsWith("Candidatus_")) {
            String after = g.substring("Candidatus_".length());
            if (!after.isEmpty()) g = after;
        }

        // Apply explicit map
        return CLEAN_WGS.getOrDefault(g, g);
    }

    /* =========================
     * 3) Public API
     * ========================= */

    /** Clean a 16S label (exact-match map first, otherwise return original). */
    public static String clean16SLabel(String raw16s) {
        if (raw16s == null) return null;
        String key = raw16s.trim();
        return CLEAN_16S.getOrDefault(key, key);
    }

    /**
     * Clean a WGS name or genus token.
     * If raw contains underscores, we take the first token as genus (typical binomial formatting),
     * then apply overrides.
     */
    public static String cleanWgsNameOrGenus(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;

        // If exact full string exception exists, use it
        if (CLEAN_WGS.containsKey(s)) return CLEAN_WGS.get(s);

        String genusToken = s.contains("_") ? s.substring(0, s.indexOf('_')) : s;
        genusToken = normalizeGenusToken(genusToken);
        return applyWgsOverrides(genusToken);
    }

    /**
     * Extract a genus (or stable bucket like Lachnospiraceae/Clostridiales) from a Sylph line.
     *
     * Example:
     *   "NZ_CP136422.1 Blautia coccoides strain DSM 935 chromosome, complete genome" -> "Blautia"
     */
    public static String genusFromSylphLine(String line) {
        if (line == null) return null;
        String s = line.trim();
        if (s.isEmpty()) return null;

        // Cut off after first comma to reduce trailing noise
        int comma = s.indexOf(',');
        if (comma > 0) s = s.substring(0, comma).trim();

        String[] toks = s.split("\\s+");
        if (toks.length == 0) return null;

        int i = 0;

        // 1) Skip accession if present
        String t0 = stripPunct(toks[0]);
        if (ACCESSION_PREFIX.matcher(t0).matches() || looksLikeAccession(t0)) {
            i = 1;
        }

        // 2) Skip MAG/TPA_asm boilerplate
        while (i < toks.length) {
            String t = stripPunct(toks[i]);
            if (t.isEmpty()) { i++; continue; }

            if (t.equalsIgnoreCase("MAG") || t.equalsIgnoreCase("MAG:") ||
                t.equalsIgnoreCase("TPA_asm") || t.equalsIgnoreCase("TPA_asm:")) {
                i++;
                continue;
            }
            break;
        }

        // 3) Candidatus <Genus> <species> => genus is next token
        if (i < toks.length && stripPunct(toks[i]).equalsIgnoreCase("Candidatus")) {
            i++;
            if (i < toks.length) {
                String g = normalizeGenusToken(toks[i]);
                return applyWgsOverrides(g);
            }
            return "Candidatus";
        }

        // 4) uncultured <Genus> sp. or uncultured <Family> bacterium
        if (i < toks.length && stripPunct(toks[i]).equalsIgnoreCase("uncultured")) {
            i++;
            if (i < toks.length) {
                String g = normalizeGenusToken(toks[i]);
                return applyWgsOverrides(g);
            }
            return "uncultured";
        }

        // 5) bracketed genus like [Clostridium]
        if (i < toks.length) {
            String raw = stripPunct(toks[i]);
            Matcher m = BRACKET_GENUS.matcher(raw);
            if (m.matches()) {
                String g = m.group(1);
                return applyWgsOverrides(g);
            }
        }

        // 6) Otherwise: find first plausible organism token
        while (i < toks.length) {
            String t = stripPunct(toks[i]);
            if (t.isEmpty()) { i++; continue; }

            // Skip stopwords
            if (STOPWORDS.contains(t) || STOPWORDS.contains(t.toLowerCase(Locale.ROOT))) {
                i++;
                continue;
            }

            // If it begins with a letter, treat it as genus/bucket
            if (Character.isLetter(t.charAt(0))) {
                String g = normalizeGenusToken(t);
                return applyWgsOverrides(g);
            }

            i++;
        }

        return null;
    }

    /* =========================
     * 4) Tiny demo
     * ========================= */

    public static void main(String[] args) {
        String ex = "NZ_CP136422.1 Blautia coccoides strain DSM 935 chromosome, complete genome";
        System.out.println(genusFromSylphLine(ex)); // should print: Blautia
    }
}
