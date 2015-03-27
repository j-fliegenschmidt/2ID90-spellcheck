import java.util.HashMap;
import static java.lang.Integer.min;
import java.util.Arrays;
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

        for (String word : words) {
            if (this.cr.inVocabulary(word)) {
                finalSuggestion += word + " ";
            } else {
                HashMap<String, Double> candidates = new HashMap<>();
                this.getCandidateWords(word).forEach(candidate -> 
                        candidates.put(candidate, calculateChannelModelProbability(candidate, word)));
                
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
        }

        return finalSuggestion.trim();
    }

    public double calculateChannelModelProbability(String suggested, String incorrect) {
        double result = this.cr.getSmoothedCount(suggested);
        
        String missspelling = this.findSpellingError(suggested, incorrect);
        
        return result;
    }

    //Janis, I'm not sure how to implement it if DamerauLevenshtein is in different class
    public Stream<String> getCandidateWords(String word) {
        //DamerauLevenshtein instance;
        //instance = new DamerauLevenshtein(entry, word);
        return this.cr.getVocabularyStream()
                .filter(entry -> damerauLevenshtein(entry, word, 26) == 1);
    }
    
    public static int damerauLevenshtein(String a, String b, int alphabetlength) {
        
        int foo[][];
        int cost;
        int i;
        int j;
        int len1 = a.length();
        int len2 = b.length();
        final int combined = len1 + len2;
        
        foo = new int[len1+2][len2+2];
        
        foo[0][0] = combined;
        
        for (i = 0; i <= len1; i ++) {
            foo[i+1][1] = i;
            foo[i+1][0] = combined;
        }
        for (j = 0; j <= len2; j ++) {
            foo[1][j+1] = j;
            foo[0][j+1] = combined;
        }
        
        int[] DA = new int[alphabetlength];
        Arrays.fill(DA, 0);
        
        for(i = 1; i<=a.length(); i++) {
            int DB = 0;
            
            for(j = 1; j<=b.length(); j++) {
                
                int i1 = DA[b.charAt(j-1)];
                int j1 = DB;
                int d = (a.charAt(i-1)==b.charAt(j-1))? 0:1;
                
                if (d==0) { 
                    DB = j;
                }
                
                foo[i+1][j+1] =
                  min(min(min(foo[i][j]+d,
                      foo[i+1][j] + 1),
                      foo[i][j+1]+1), 
                      foo[i1][j1] + (i-i1-1) + 1 + (j-j1-1));
              }
              DA[a.charAt(i-1)] = i;
            }
            return foo[a.length()+1][b.length()+1];
          }
    
}
