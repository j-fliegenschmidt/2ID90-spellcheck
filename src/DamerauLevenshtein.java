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
    private int change;
 
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
    
    public int getChange() {
        return change;
    } 
    
    public int executeDHS() {
        
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
                
                int sub = foobar[i][j]+d;
                int del = foobar[i][j+1]+1;
                int ins = foobar[i+1][j]+1;
                int trans = foobar[i1][j1]+(i - i1-1)+1+(j-j1-1);

                foobar[i+1][j+1] = Math.min(sub, Math.min(del, Math.min(ins, trans)));
                
            }
            //helper[wordOne.indexOf(wordOne.charAt(i-1))] = i;
        }
        
        change = foobar[wordOne.length()][wordTwo.length()];
        return change;
    }
    
//    public String getMethod() throws IllegalStateException {
//        if (change > 1) {
//            throw new IllegalStateException("The DamerauLevenschtein score is not 1");
//        } else if (change < 1) {
//            throw new IllegalStateException("DamerauLevenschtein has not be executed yet");
//    }
//        
//        if (change == 1) {
//            for (int i = 0; )
//        }
//    }
}
