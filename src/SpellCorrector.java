
import java.util.HashMap;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.HashSet;
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

        for (String word : words) {
            if (this.cr.inVocabulary(word)) {
                finalSuggestion += word + " ";
            } else {
                HashMap<String, Double> candidates = new HashMap<>();
                this.getCandidateWords(word).forEach(candidate -> 
                        candidates.put(candidate, calculateChannelModelProbability(candidate, word)));
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

    public Stream<String> getCandidateWords(String word) {
        return this.cr.getVocabularyStream()
                .filter(entry -> damerauLevenshtein(entry, word) == 1);
    }
    
    private static int damerauLevenshtein(String a, String b) {
        
        int foo[][];
        int cost;
        int i;
        int j;
        int len1 = a.length();
        int len2 = b.length();
        
        foo = new int[len1][len2];
        
        for (i = 0; i < len1; i ++) {
            foo[i][0] = i;
        }
        for (j = 0; j < len2; j ++) {
            foo[0][j] = j;
        }
        
        for (i = 0; i < len1; i ++) {
            for (j = 0; j < len2; j ++) {
                
                int char1 = a.charAt(i);
                int char2 = b.charAt(j);
                
                if (char1 == char2) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                
                //deletion
                int w = foo[i-1][j] + 1;
                //insertion
                int x = foo[i][j-1] + 1;
                //substitution
                int y = foo[i-1][j-1] + cost;
                
                foo[i][j] = min(w, min(x,y));
                
                if (i > 1 && j > 1 && char1 == b.charAt(j-1) && a.charAt(i-1) == char2) {
                    int z = foo[i-2][j-2] + cost;
                    foo[i][j] = min(foo[i][j], z);
                }
            }
        }
        
        return foo[len1][len2];
    }
    
}
