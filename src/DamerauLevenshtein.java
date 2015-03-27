/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alexau
 */
public class DamerauLevenshtein {
    private String wordOne;
    private String wordTwo;
    private int[][] foobar;
    private int alphabet;
 
    public DamerauLevenshtein(String a, String b)
    {
        if ((a.length() > 0 || !a.isEmpty())  || (b.length() > 0 || !b.isEmpty())) {
            
            if (a.length() >= b.length()) {
                wordOne = a;
                wordTwo = b;
            } else {
                wordOne = b;
                wordTwo = a;
            }
        }
        //The number of letters in the alphabet including apostrophes
        alphabet = 27;
    }
    
    public int executeDHS() {
        
        //int res = -1;
        int INF = wordOne.length() + wordOne.length();
 
        foobar = new int[wordOne.length()+1][wordOne.length()+1];
 
        for (int i = 0; i < wordOne.length(); i++)
        {
            foobar[i+1][1] = i;
            foobar[i+1][0] = INF;
        }
 
        for (int i = 0; i < wordTwo.length(); i++)
        {
            foobar[1][i+1] = i;
            foobar[0][i+1] = INF;
        }
 
        int[] helper = new int[alphabet];
        
        
        for (int i = 0; i < alphabet; i++) {
            helper[i] = 0;
        }
 
        for (int i = 1; i < wordOne.length(); i++){
            int db = 0;
            
            for (int j = 1; j < wordTwo.length(); j++) {
 
                int i1 = helper[wordTwo.indexOf(wordTwo.charAt(j-1))];
                int j1 = db;
                
                int d;
                if (wordOne.charAt(i-1)==wordTwo.charAt(j-1)) {
                    d = 0;
                } else {
                    d = 1;
                }
                
                if (d == 0) {
                    db = j;
                }
 
                foobar[i+1][j+1] = Math.min(
                        Math.min(foobar[i][j]+d, foobar[i+1][j]+1),
                        Math.min(foobar[i][j+1]+1,foobar[i1][j1]+(i - i1-1)+1+(j-j1-1)));
            }
            helper[wordOne.indexOf(wordOne.charAt(i-1))] = i;
        }
         
        return foobar[wordOne.length()][wordTwo.length()];
    }
}