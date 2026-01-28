package claude.mouseCDiff;

import java.util.HashMap;
import java.util.Map;

/**
 * From chatGPT
 */
public class TaxonCleaners {

    /** 16S label cleanup map: maps “as-seen” 16S labels to canonical genus/family bucket. */
    public static final Map<String, String> CLEAN_16S = new HashMap<>();

    /** WGS label cleanup map: maps WGS-derived genus tokens (or full names) to canonical genus bucket. */
    public static final Map<String, String> CLEAN_WGS = new HashMap<>();

    static {
        /* =========================
         * 16S CLEANUP (explicit)
         * ========================= */

        // Collapsed bracket genera
        CLEAN_16S.put("[Eubacterium]_hallii_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_coprostanoligenes_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_fissicatena_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_eligens_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_nodatum_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_ventriosum_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_ruminantium_group", "Eubacterium");
        CLEAN_16S.put("[Eubacterium]_brachy_group", "Eubacterium");

        CLEAN_16S.put("[Ruminococcus]_gnavus_group", "Ruminococcus");
        CLEAN_16S.put("[Ruminococcus]_gauvreauii_group", "Ruminococcus");
        CLEAN_16S.put("[Ruminococcus]_torques_group", "Ruminococcus");

        CLEAN_16S.put("[Clostridium]_innocuum_group", "Clostridium");
        CLEAN_16S.put("[Clostridium]_methylpentosum_group", "Clostridium");

        // sensu stricto bins -> base genus
        CLEAN_16S.put("Clostridium_sensu_stricto_1", "Clostridium");
        CLEAN_16S.put("Clostridium_sensu_stricto_13", "Clostridium");

        // Common merged 16S bins
        CLEAN_16S.put("Escherichia-Shigella", "Escherichia");
        CLEAN_16S.put("Burkholderia-Caballeronia-Paraburkholderia", "Burkholderia");

        // Family-level / bin-level labels: keep in family bucket for stable join
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

        // Candidatus: map known one explicitly (and let your generic cleaner handle the rest)
        CLEAN_16S.put("Candidatus_Soleaferrea", "Candidatus_Soleaferrea"); // leave as-is unless you prefer "Soleaferrea"
        CLEAN_16S.put("Candidatus_Stoquefichus", "Candidatus_Stoquefichus"); // leave as-is unless you prefer "Stoquefichus"

        // Pass-through placeholders you probably want to preserve
        CLEAN_16S.put("uncultured", "uncultured");
        CLEAN_16S.put("Incertae_Sedis", "Incertae_Sedis");
        CLEAN_16S.put("Chloroplast", "Chloroplast");
        CLEAN_16S.put("GCA-900066755", "GCA-900066755");
        CLEAN_16S.put("GCA-900066575", "GCA-900066575");
        CLEAN_16S.put("DTU089", "DTU089");
        CLEAN_16S.put("DTU014", "DTU014");
        CLEAN_16S.put("CAG-56", "CAG-56");
        CLEAN_16S.put("CAG-352", "CAG-352");
        CLEAN_16S.put("CAG-352", "CAG-352");
        CLEAN_16S.put("CHKCI001", "CHKCI001");
        CLEAN_16S.put("UC5-1-2E3", "UC5-1-2E3");
        CLEAN_16S.put("TM7x", "TM7x");

        /* =========================
         * WGS CLEANUP (explicit)
         * ========================= */

        // Shigella genomes: collapse to Escherichia to match 16S "Escherichia-Shigella" handling
        CLEAN_WGS.put("Shigella", "Escherichia");
        CLEAN_WGS.put("Shigella_boydii", "Escherichia");
        CLEAN_WGS.put("Shigella_flexneri", "Escherichia");
        CLEAN_WGS.put("Shigella_dysenteriae", "Escherichia");
        CLEAN_WGS.put("Shigella_sonnei", "Escherichia");

        // If you want to collapse these common reclassifications to match typical 16S genus labels, enable:
        // (Comment out if you prefer to keep the newer genera distinct.)
        CLEAN_WGS.put("Phocaeicola", "Bacteroides");      // Phocaeicola_vulgatus/dorei
        CLEAN_WGS.put("Phocaeicola_vulgatus", "Bacteroides");
        CLEAN_WGS.put("Phocaeicola_dorei", "Bacteroides");

        CLEAN_WGS.put("Segatella", "Prevotella");         // Segatella_copri/bryantii
        CLEAN_WGS.put("Segatella_copri", "Prevotella");
        CLEAN_WGS.put("Segatella_bryantii", "Prevotella");

        // Candidatus genus: for joining at genus level, map to Akkermansia
        CLEAN_WGS.put("Candidatus_Akkermansia_timonensis", "Akkermansia");
        CLEAN_WGS.put("Candidatus_Akkermansia", "Akkermansia"); // optional catch-all token

        // If you see “family bacterium” style WGS names, keep family bucket stable
        CLEAN_WGS.put("Lachnospiraceae_bacterium", "Lachnospiraceae");
        CLEAN_WGS.put("Christensenellaceae_bacterium", "Christensenellaceae");
        CLEAN_WGS.put("Bacteroidaceae_bacterium", "Bacteroidaceae");
        CLEAN_WGS.put("Clostridiales_bacterium", "Clostridiales");
        CLEAN_WGS.put("Oscillospiraceae_bacterium", "Oscillospiraceae");
        CLEAN_WGS.put("Vallitaleaceae_bacterium", "Vallitaleaceae");
    }

    /**
     * Apply the 16S cleaning map (exact-match) and otherwise return the original.
     * You can optionally call a generic normalizer before/after this.
     */
    public static String clean16SLabel(String raw) {
        if (raw == null) return null;
        String key = raw.trim();
        return CLEAN_16S.getOrDefault(key, key);
    }

    /**
     * For WGS you usually first derive a genus token (split on '_'), then clean.
     * This method accepts either a full WGS name or just a genus token.
     */
    public static String cleanWgsNameOrGenus(String raw) {
        if (raw == null) return null;
        String s = raw.trim();

        // If it looks like a binomial/label, take the first token as the genus.
        String genusToken = s.contains("_") ? s.substring(0, s.indexOf('_')) : s;

        // First try the full string mapping (covers explicit species exceptions)
        if (CLEAN_WGS.containsKey(s)) return CLEAN_WGS.get(s);

        // Then try genus-token mapping
        return CLEAN_WGS.getOrDefault(genusToken, genusToken);
    }
}
