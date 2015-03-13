
import java.util.HashSet;

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

        for (String word : words) {
            if (this.cr.inVocabulary(word)) {
                finalSuggestion += word + " ";
            } else {
                HashSet<String> candidates = this.getCandidateWords(word);
            }
        }

        return finalSuggestion.trim();
    }

    public double calculateChannelModelProbability(String suggested, String incorrect) {
        /**
         * CODE TO BE ADDED *
         */

        return 0.0;
    }

    public HashSet<String> getCandidateWords(String word) {
        HashSet<String> listOfWords = new HashSet<>();
        
        this.cr.getVocabularyStream()
                .filter(entry -> damerauLevenshtein(entry, word) == 1)
                .forEach(listOfWords::add);
        
        return listOfWords;
    }
    
    private static int damerauLevenshtein(String a, String b) {
        // TODO
        return 0;
    }
}
