
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

        String lastWord = "";
        for (String word : words) {
            if (this.cr.inVocabulary(word)) {
                finalSuggestion += word + " ";
            } else {
                String _lastWord = lastWord;
                HashMap<String, Double> candidates = new HashMap<>();
                this.getCandidateWords(word).forEach(candidate
                        -> candidates.put(candidate, calculateChannelModelProbability(candidate, word, _lastWord)));

                double prob = 0;
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
        double result = this.cr.getSmoothedCount(suggested);

        //String[] spellingError = SpellCorrector.findSpellingError(suggested, incorrect).split("|");
        //result *= this.cmr.getConfusionCount(spellingError[0], spellingError[1]);
        
        return result;
    }

    public Stream<String> getCandidateWords(String word) {
        return this.cr.getVocabularyStream()
                .filter(entry -> {
                    DamerauLevenshtein dlInstance = new DamerauLevenshtein(entry, word);                    
                    return dlInstance.executeDHS() == 1;
                });
    }
    
    private static String findSpellingError(String correct, String incorrect) {
        String result = "";
        int lengthDiff = correct.length() - incorrect.length();
        lengthDiff = lengthDiff < 0 ? -1 * lengthDiff : lengthDiff;
        
        if (lengthDiff < 2) {
            
        }
        else {
            
        }
        
        return result;
    }
}
