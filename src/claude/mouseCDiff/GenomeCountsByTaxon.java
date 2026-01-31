package claude.mouseCDiff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Loads genome_counts_by_taxon.tsv into memory and allows lookup by taxaID (col 1).
 *
 * Expected header:
 * taxaID  numberOfGenomes  numberOfPlasmidsInGenomeFiles  MBGenomes  MBPlasmid  totalMB
 * 
 * ChatGPT auto coded
 */
public final class GenomeCountsByTaxon {

    public static final class TaxonGenomeCounts {
        private final String taxaID;
        private final int numberOfGenomes;
        private final int numberOfPlasmidsInGenomeFiles;
        private final double mbGenomes;
        private final double mbPlasmid;
        private final double totalMB;

        public TaxonGenomeCounts(
                String taxaID,
                int numberOfGenomes,
                int numberOfPlasmidsInGenomeFiles,
                double mbGenomes,
                double mbPlasmid,
                double totalMB
        ) {
            this.taxaID = Objects.requireNonNull(taxaID, "taxaID");
            this.numberOfGenomes = numberOfGenomes;
            this.numberOfPlasmidsInGenomeFiles = numberOfPlasmidsInGenomeFiles;
            this.mbGenomes = mbGenomes;
            this.mbPlasmid = mbPlasmid;
            this.totalMB = totalMB;
        }

        public String getTaxaID() { return taxaID; }
        public int getNumberOfGenomes() { return numberOfGenomes; }
        public int getNumberOfPlasmidsInGenomeFiles() { return numberOfPlasmidsInGenomeFiles; }
        public double getMbGenomes() { return mbGenomes; }
        public double getMbPlasmid() { return mbPlasmid; }
        public double getTotalMB() { return totalMB; }

        @Override
        public String toString() {
            return "TaxonGenomeCounts{" +
                    "taxaID='" + taxaID + '\'' +
                    ", numberOfGenomes=" + numberOfGenomes +
                    ", numberOfPlasmidsInGenomeFiles=" + numberOfPlasmidsInGenomeFiles +
                    ", mbGenomes=" + mbGenomes +
                    ", mbPlasmid=" + mbPlasmid +
                    ", totalMB=" + totalMB +
                    '}';
        }
    }

    private final Map<String, TaxonGenomeCounts> byId;

    private GenomeCountsByTaxon(Map<String, TaxonGenomeCounts> byId) {
        this.byId = Collections.unmodifiableMap(new LinkedHashMap<>(byId));
    }

    /** Load from a TSV file path. */
    public static GenomeCountsByTaxon load(Path tsvPath) throws IOException {
        Map<String, TaxonGenomeCounts> map = new LinkedHashMap<>();

        try (BufferedReader br = Files.newBufferedReader(tsvPath, StandardCharsets.UTF_8)) {
            String header = br.readLine();
            if (header == null) {
                throw new IOException("Empty file: " + tsvPath);
            }

            String line;
            int lineNo = 1; // header is line 1
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // TSV split; keep it simple (your file is clean tab-delimited)
                String[] f = line.split("\t", -1);
                if (f.length != 6) {
                    throw new IOException("Expected 6 columns at line " + lineNo + " but got " + f.length +
                            " | line=" + line);
                }

                String taxaID = f[0];

                int numberOfGenomes = parseInt(f[1], "numberOfGenomes", lineNo);
                int numberOfPlasmids = parseInt(f[2], "numberOfPlasmidsInGenomeFiles", lineNo);
                double mbGenomes = parseDouble(f[3], "MBGenomes", lineNo);
                double mbPlasmid = parseDouble(f[4], "MBPlasmid", lineNo);
                double totalMB = parseDouble(f[5], "totalMB", lineNo);

                TaxonGenomeCounts rec = new TaxonGenomeCounts(
                        taxaID, numberOfGenomes, numberOfPlasmids, mbGenomes, mbPlasmid, totalMB
                );

                // If you ever have duplicates, fail loudly (or change to "last wins" if preferred)
                if (map.putIfAbsent(taxaID, rec) != null) {
                    throw new IOException("Duplicate taxaID '" + taxaID + "' at line " + lineNo);
                }
            }
        }

        return new GenomeCountsByTaxon(map);
    }

    /** Convenience loader that wraps IOException as unchecked. */
    public static GenomeCountsByTaxon loadUnchecked(Path tsvPath) {
        try {
            return load(tsvPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Get a record by taxaID, or null if not present. */
    public TaxonGenomeCounts get(String taxaID) {
        return byId.get(taxaID);
    }

    /** Get a record by taxaID, or throw if missing. */
    public TaxonGenomeCounts require(String taxaID) {
        TaxonGenomeCounts rec = byId.get(taxaID);
        if (rec == null) throw new NoSuchElementException("No taxaID found: " + taxaID);
        return rec;
    }

    public boolean contains(String taxaID) {
        return byId.containsKey(taxaID);
    }

    public Set<String> ids() {
        return byId.keySet();
    }

    public Collection<TaxonGenomeCounts> records() {
        return byId.values();
    }

    public int size() {
        return byId.size();
    }

    private static int parseInt(String s, String fieldName, int lineNo) throws IOException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IOException("Bad int for " + fieldName + " at line " + lineNo + ": '" + s + "'", e);
        }
    }

    private static double parseDouble(String s, String fieldName, int lineNo) throws IOException {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IOException("Bad double for " + fieldName + " at line " + lineNo + ": '" + s + "'", e);
        }
    }

    // Tiny demo
    public static void main(String[] args) throws Exception {
        Path p = Path.of("genome_counts_by_taxon.tsv");
        GenomeCountsByTaxon db = GenomeCountsByTaxon.load(p);

        TaxonGenomeCounts a = db.get("Acinetobacter_baumannii");
        System.out.println(a);

        // Or fail-fast:
        TaxonGenomeCounts b = db.require("Bacillus_subtilis");
        System.out.println("B. subtilis totalMB = " + b.getTotalMB());
    }
}
