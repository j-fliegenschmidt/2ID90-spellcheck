
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CorpusReader {

    final static String CNTFILE_LOC = "samplecnt.txt";
    final static String VOCFILE_LOC = "samplevoc.txt";

    private HashMap<String, Integer> ngrams;
    private Set<String> vocabulary;

    public CorpusReader() throws IOException {
        readNGrams();
        readVocabulary();
    }

    /**
     * Returns the n-gram count of <NGram> in the file
     *
     *
     * @param nGram : space-separated list of words, e.g. "adopted by him"
     * @return 0 if <NGram> cannot be found, otherwise count of <NGram> in file
     */
    public int getNGramCount(String nGram) throws NumberFormatException {
        if (nGram == null || nGram.length() == 0) {
            throw new IllegalArgumentException("NGram must be non-empty.");
        }
        Integer value = ngrams.get(nGram);
        return value == null ? 0 : value;
    }

    private void readNGrams() throws
            FileNotFoundException, IOException, NumberFormatException {
        ngrams = new HashMap<>();

        FileInputStream fis;
        fis = new FileInputStream(CNTFILE_LOC);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        while (in.ready()) {
            String phrase = in.readLine().trim();
            String s1, s2;
            int j = phrase.indexOf(" ");

            s1 = phrase.substring(0, j);
            s2 = phrase.substring(j + 1, phrase.length());

            int count = 0;
            try {
                count = Integer.parseInt(s1);
                ngrams.put(s2, count);
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("NumberformatError: " + s1);
            }
        }
    }

    private void readVocabulary() throws FileNotFoundException, IOException {
        vocabulary = new HashSet<>();

        FileInputStream fis = new FileInputStream(VOCFILE_LOC);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        while (in.ready()) {
            String line = in.readLine();
            vocabulary.add(line);
        }
    }

    /**
     * Returns the size of the number of unique words in the dataset
     *
     * @return the size of the number of unique words in the dataset
     */
    public int getVocabularySize() {
        return vocabulary.size();
    }

    /**
     * Returns the subset of words in set that are in the vocabulary
     *
     * @param set
     * @return
     */
    public HashSet<String> inVocabulary(Set<String> set) {
        HashSet<String> h = new HashSet<>(set);
        h.retainAll(vocabulary);
        return h;
    }

    public boolean inVocabulary(String word) {
        return vocabulary.contains(word);
    }

    public double getSmoothedCount(String NGram) {
        if (NGram == null || NGram.length() == 0) {
            throw new IllegalArgumentException("NGram must be non-empty.");
        }

        double smoothedCount = 0.0;
        int totalPossibilities = 0;
        FrequencyTable frequencyTyble = new FrequencyTable();
        String nGramProposition = NGram.substring(0, NGram.lastIndexOf(" ") + 1);

        for (String word : this.vocabulary) {
            int wordFrequency = this.getNGramCount(nGramProposition + word);
            totalPossibilities += wordFrequency;

            frequencyTyble.incrementOccurence(wordFrequency);
        }

        int NGramCount = this.getNGramCount(NGram);
        if (NGramCount == 0) {
            smoothedCount = frequencyTyble.getInterpolatedFrequency(1) / totalPossibilities;
        } else {
            smoothedCount = ((NGramCount + 1) * frequencyTyble.getInterpolatedFrequency(NGramCount + 1))
                    / frequencyTyble.getInterpolatedFrequency(NGramCount);
        }

        return smoothedCount;
    }

    public Stream<String> getVocabularyStream() {
        return this.vocabulary.stream();
    }
    
    private class FrequencyTable {
        
        private int lowestFrequency;
        private int highestFrequency;
        private final HashMap<Integer, Integer> frequencies;
        
        public FrequencyTable() {
            this.frequencies = new HashMap<>();
            this.highestFrequency = Integer.MIN_VALUE;
            this.lowestFrequency = Integer.MAX_VALUE;
        }
        
        public void incrementOccurence(int frequency) {
            if (this.frequencies.containsKey(frequency)) {
                this.frequencies.put(frequency, this.frequencies.get(frequency));
            }
            else {
                this.frequencies.put(frequency, 1);
            }
            
            this.highestFrequency = this.highestFrequency < frequency ? frequency : this.highestFrequency;
            this.lowestFrequency = this.lowestFrequency > frequency ? frequency : this.lowestFrequency;
        }
        
        public int getRawFrequency(int frequency) {
            return this.frequencies.getOrDefault(frequency, 0);
        }
        
        public double getInterpolatedFrequency(int frequency) {
            int frequencyFrequency = this.getRawFrequency(frequency);
            if (frequencyFrequency == 0) {
                if (frequency > this.highestFrequency) {
                    for (int i = this.highestFrequency; i < frequency; i++) {
                        frequencyFrequency *= 0.66;
                    }
                }
                else if (frequency < this.lowestFrequency) {
                    for (int i = frequency; i < this.lowestFrequency; i++) {
                        frequencyFrequency *= 1.5;
                    }
                }
                else {
                    int y_diff = this.frequencies.get(this.highestFrequency) 
                            - this.frequencies.get(this.lowestFrequency);
                    int x_diff = this.frequencies.get(this.highestFrequency)
                            - this.frequencies.get(this.lowestFrequency);
                    
                    frequencyFrequency = x_diff != 0 ? y_diff / x_diff :
                            this.frequencies.get(this.highestFrequency);
                }
            }
            
            return frequencyFrequency;
        }
    }
}
