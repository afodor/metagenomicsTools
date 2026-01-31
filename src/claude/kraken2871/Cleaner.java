package claude.kraken2871;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Cleaner {

    // If you prefer null instead of "unclassified", flip this.
    private static final boolean RETURN_NULL_WHEN_UNKNOWN = false;
    private static final String UNKNOWN = "unclassified";

    // Extract genus from tax strings like:
    // d__Bacteria;p__Actinobacteriota;...;g__Actinomyces
    private static final Pattern TAXONOMY_GENUS = Pattern.compile("(^|;)\\s*g__([^;\\s]+)");

    // First "word" token, optionally bracketed: [Clostridium] -> Clostridium
    private static final Pattern LEADING_BRACKETED = Pattern.compile("^\\s*\\[([^\\]]+)]\\s*(.*)$");

    // Words that usually mean "not a genus label"
    private static final Set<String> BAD_HEAD_WORDS = Set.of(
            "uncultured", "unidentified", "unclassified", "cellular", "organisms", "bacteria",
            "archaea", "eukaryota", "virus", "viruses", "unknown"
    );

    // Phrases that indicate the remainder is not a clean binomial; we still often
    // want the genus at the beginning (e.g., "Blochmannia endosymbiont of ...").
    // For other cases like "ant, tsetse..." we bail out.
    private static final Pattern COMMA_LIST = Pattern.compile(".*,.*");

    // If name ends with these (or contains them after genus), take the first token.
    private static final Set<String> GENUS_FOLLOWS_WITH = Set.of(
            "sp.", "sp", "strain", "str.", "subsp.", "subsp", "group", "complex",
            "genomosp.", "genomosp", "pv.", "serovar", "biovar", "isolate"
    );

    // “Candidatus Foo bar” -> genus = Foo
    private static final String CANDIDATUS = "candidatus";

    private Cleaner() {}

    /** Main entry: map an arbitrary taxon label to a genus (or UNKNOWN/null). */
    public static String toGenus(String raw) {
        if (raw == null) return unknown();
        String s = normalize(raw);
        if (s.isEmpty()) return unknown();

        // Skip common header-ish junk
        if (s.equalsIgnoreCase("full name")) return unknown();

        // Kraken/GTDB-style taxonomy string containing g__
        String genusFromTax = genusFromTaxonomyString(s);
        if (genusFromTax != null) return genusFromTax;

        // If it's a comma-separated list like "ant, tsetse, mealybug..."
        // treat as unknown bin.
        if (COMMA_LIST.matcher(s).matches()) return unknown();

        // Bracketed genus: "[Clostridium] innocuum"
        s = stripLeadingBracketedGenus(s);

        // Handle "unclassified Something" or "uncultured Something"
        // If it starts with those, we generally treat as unknown,
        // unless it clearly contains a genus token right after (you can tweak).
        String lowered = s.toLowerCase(Locale.ROOT);
        for (String bad : BAD_HEAD_WORDS) {
            if (lowered.equals(bad) || lowered.startsWith(bad + " ")) {
                // Special case: "unclassified Candidatus Pelagibacter"
                // we'll try to salvage the genus from the remainder.
                String remainder = s.substring(Math.min(s.length(), bad.length())).trim();
                if (!remainder.isEmpty()) {
                    String salvaged = salvageGenusFromPhrase(remainder);
                    if (salvaged != null) return salvaged;
                }
                return unknown();
            }
        }

        // If it's clearly a higher-rank label (family/order/phylum/class etc.), treat as unknown.
        if (looksLikeHigherRank(s)) return unknown();

        // General case: get first token as genus, with special handling for "Candidatus"
        String genus = salvageGenusFromPhrase(s);
        if (genus == null) return unknown();

        // Final cleanup: remove stray punctuation around genus
        genus = genus.replaceAll("^[^A-Za-z]+|[^A-Za-z]+$", "");
        if (genus.isEmpty()) return unknown();

        // Genus usually starts uppercase in these files; we won't force it,
        // but you can normalize capitalization if desired.
        return genus;
    }

    // -----------------------
    // Helpers
    // -----------------------

    private static String genusFromTaxonomyString(String s) {
        Matcher m = TAXONOMY_GENUS.matcher(s);
        if (m.find()) {
            String g = m.group(2);
            g = stripWeirdWrapping(g);
            if (!g.isBlank() && !looksLikeHigherRank(g)) return g;
        }
        return null;
    }

    private static String stripLeadingBracketedGenus(String s) {
        Matcher m = LEADING_BRACKETED.matcher(s);
        if (m.matches()) {
            String inside = m.group(1).trim();
            String after = m.group(2).trim();
            // Recompose as "Clostridium <rest>"
            if (!inside.isEmpty()) return (inside + (after.isEmpty() ? "" : " " + after)).trim();
        }
        return s;
    }

    private static String salvageGenusFromPhrase(String s) {
        String[] toks = s.split("\\s+");
        if (toks.length == 0) return null;

        // "Candidatus X y" -> genus is token after Candidatus
        if (toks[0].equalsIgnoreCase(CANDIDATUS)) {
            if (toks.length >= 2) return stripWeirdWrapping(toks[1]);
            return null;
        }

        // Otherwise genus is first token, but handle some common leading junk
        String first = stripWeirdWrapping(toks[0]);
        if (first.isEmpty()) return null;

        // If first token is itself a stopword, bail
        if (BAD_HEAD_WORDS.contains(first.toLowerCase(Locale.ROOT))) return null;

        // If the phrase is like "Bacillus subtilis group" or "Enterobacter cloacae complex"
        // first token is genus, good.
        // If the phrase is like "Acidovorax sp." first token is genus, good.
        // If it's like "Blochmannia endosymbiont of ..." first token is genus, good.
        // But if it's like "canis group" first token is not genus; bail.
        if (first.equalsIgnoreCase("canis")) return null;

        // If second token signals it's not a genus (rare), bail; otherwise keep first.
        if (toks.length >= 2) {
            String second = toks[1].toLowerCase(Locale.ROOT);
            if (GENUS_FOLLOWS_WITH.contains(second)) {
                return first;
            }
        }

        return first;
    }

    private static boolean looksLikeHigherRank(String s) {
        // Very lightweight heuristics for your list:
        // - Families often end with -aceae
        // - Orders often end with -ales
        // - Many phyla end with -ota or -phyta; classes with -ia (but genus can also end with -ia)
        // We only hard-reject strong signals to avoid false positives.
        String x = s.trim();
        if (x.isEmpty()) return true;

        // Single-token rank names: "Bacteria", "Bacilli", "Clostridia", etc. are common in your list.
        // Many genera are also single-token, so we only reject if it matches a strong suffix or known bin.
        String lower = x.toLowerCase(Locale.ROOT);

        if (lower.equals("bacteria") || lower.equals("archaea") || lower.equals("eukaryota")) return true;

        // If it contains a slash and the word "group", it's almost certainly not genus.
        if (lower.contains("/") && lower.contains("group")) return true;

        // Strong suffixes
        if (lower.endsWith("aceae")) return true;
        if (lower.endsWith("ales")) return true;
        if (lower.endsWith("ota")) return true;

        // Some explicit group labels
        if (lower.endsWith(" group") || lower.endsWith(" complex") || lower.endsWith(" incertae sedis")) {
            // Could still start with a genus (e.g. "Bacillus subtilis group"), which we handle earlier,
            // but if the whole string is ONLY "X group" (two tokens, lowercase-ish), likely not genus.
            // We'll let salvageGenusFromPhrase handle the genus in multi-token cases.
            return false;
        }

        return false;
    }

    private static String stripWeirdWrapping(String token) {
        if (token == null) return "";
        String t = token.trim();

        // Remove surrounding quotes if present
        if ((t.startsWith("\"") && t.endsWith("\"")) || (t.startsWith("'") && t.endsWith("'"))) {
            t = t.substring(1, t.length() - 1).trim();
        }

        // Remove leading/trailing punctuation (keep letters/numbers/_- inside)
        t = t.replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");

        // Normalize weird bracket artifacts like "[Clostridium]_symbiosum"
        t = t.replace("[", "").replace("]", "");

        // Some of your names have underscores joining genus/species; keep genus before underscore.
        int underscore = t.indexOf('_');
        if (underscore > 0) t = t.substring(0, underscore);

        return t;
    }

    private static String normalize(String raw) {
        // Collapse whitespace, trim, and remove invisible control chars
        String s = raw.replaceAll("\\p{Cntrl}", " ").trim();
        s = s.replaceAll("\\s+", " ");
        return s;
    }

    private static String unknown() {
        return RETURN_NULL_WHEN_UNKNOWN ? null : UNKNOWN;
    }

    // -----------------------
    // Tiny demo / sanity check
    // -----------------------
    public static void main(String[] args) {
        List<String> examples = List.of(
                "d__Bacteria;p__Actinobacteriota;c__Actinobacteria;o__Actinomycetales;f__Actinomycetaceae;g__Actinomyces",
                "[Clostridium] innocuum",
                "Acidovorax sp.",
                "Bacillus subtilis group",
                "Blochmannia endosymbiont of Polyrhachis (Hedomyrma) turneri",
                "unclassified Candidatus Pelagibacter",
                "Acetobacter aceti NBRC 14818",
                "Acetobacteraceae",
                "ant, tsetse, mealybug, aphid, etc. endosymbionts"
        );

        for (String e : examples) {
            System.out.printf("%-90s -> %s%n", e, toGenus(e));
        }
    }
}
