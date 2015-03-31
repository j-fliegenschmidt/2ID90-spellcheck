
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SpellCorrector {

    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;

    final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz'".toCharArray();

    public SpellCorrector(CorpusReader cr, ConfusionMatrixReader cmr) {
        this.cr = cr;
        this.cmr = cmr;
    }

    public String correctPhrase(String phrase) {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException("phrase must be non-empty.");
        }

        String[] words = phrase.split(" ");
        String finalSuggestion = "";

        // Fix spelling errors:
        String lastWord = "";
        for (String word : words) {
            if (this.cr.inVocabulary(word)) { // in vocabulary => valid word      
                finalSuggestion += word + " ";
            } else { // not in vocabulary => misspelled
                String _lastWord = lastWord;
                HashMap<String, Double> candidates = new HashMap<>();
                this.getCandidateWords(word).forEach(candidate
                        -> candidates.put(candidate, calculateChannelModelProbability(candidate, word, _lastWord)));

                double prob = Integer.MIN_VALUE;
                String corrWord = "<empty>";
                for (Map.Entry<String, Double> entry : candidates.entrySet()) {
                    if (entry.getValue() > prob) {
                        prob = entry.getValue();
                        corrWord = entry.getKey();
                    }
                }

                finalSuggestion += corrWord + " ";
            }

            lastWord = word;
        }

        return finalSuggestion.trim();
    }

    public double calculateChannelModelProbability(String suggested, String incorrect, String preceding) {
        // Influence 1: Is the suggestion a common word?
        double result = this.cr.getSmoothedCount(suggested);
        
        // Influence 2: Is the suggested word commonly following the last word?
        // Rated higher since context is most important
        result += 10 * this.cr.getSmoothedCount(preceding + " " + suggested);

        // Influence 3: Is it plausible to assume the spelling error happened?
        String[] spellingError = SpellCorrector.findSpellingError(suggested, incorrect);
        result *= (this.cmr.getConfusionCount(spellingError[0], spellingError[1]) + 1);
        
        return result;
    }

    public Stream<String> getCandidateWords(String word) {
        return this.cr.getVocabularyStream()
                .filter(entry -> {
                    DamerauLevenshtein2 dlInstance = new DamerauLevenshtein2(entry, word);
                    return dlInstance.execute() == 1;
                });
    }

    /**
     * Edit distance 1 only.
     *
     * @param correct
     * @param incorrect
     * @return { correct, misspelling }
     */
    public static String[] findSpellingError(String correct, String incorrect) {
        String result[] = {"", ""};
        int lengthDiff = correct.length() - incorrect.length();

        switch (lengthDiff) {
            case 0: // transposition
                for (int i = 0; i < correct.length(); i++) {
                    if (correct.charAt(i) != incorrect.charAt(i)) {
                        result[0] = String.valueOf(correct.charAt(i));
                        result[1] = String.valueOf(incorrect.charAt(i));
                        break;
                    }
                }

                break;

            case 1: // deletion
                for (int i = 0; i < incorrect.length(); i++) {
                    if (incorrect.charAt(i) != correct.charAt(i)) {
                        if (i == 0) {
                            result[0] = correct.substring(0, 2);
                            result[1] = String.valueOf(incorrect.charAt(0));
                        } else {
                            result[0] = correct.substring(i - 1, i + 1);
                            result[1] = String.valueOf(incorrect.charAt(i - 1));
                        }
                        
                        break;
                    }
                }

                break;

            case -1: // insertion
                for (int i = 0; i < correct.length(); i++) {
                    if (correct.charAt(i) != incorrect.charAt(i)) {
                        if (i == 0) {
                            result[0] = "";
                            result[1] = String.valueOf(incorrect.charAt(0));
                        } else {
                            result[0] = String.valueOf(correct.charAt(i - 1));
                            result[1] = incorrect.substring(i - 1, i + 1);
                        }
                        
                        break;
                    }
                }
                
                break;

            default:
                // Not needed, not implemented.
                break;
        }

        return result;
    }
}
