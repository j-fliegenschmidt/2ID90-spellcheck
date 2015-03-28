/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alexau
 */
public class DamerauLevenshtein2 {
    private String wordOne;
    private String wordTwo;
    private int[][] foobar;
 
    public DamerauLevenshtein2(String a, String b)
    {
        if ((a.length() > 0 || !a.isEmpty())  || (b.length() > 0 || !b.isEmpty()))
        {
            wordOne = a;
            wordTwo = b;
        }        
    }
 
    public int execute() {
        int cost = -1;
        int del, sub, ins;
         
        foobar = new int[wordOne.length()+1][wordTwo.length()+1];
 
        for (int i = 0; i <= wordOne.length(); i++) {
            foobar[i][0] = i;
        }
 
        for (int i = 0; i <= wordTwo.length(); i++) {
            foobar[0][i] = i;
        }
 
        for (int i = 1; i <= wordOne.length(); i++) {
            
            for (int j = 1; j <= wordTwo.length(); j++) {
                
                if (wordOne.charAt(i-1) == wordTwo.charAt(j-1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
 
                del = foobar[i-1][j]+1;
                ins = foobar[i][j-1]+1;
                sub = foobar[i-1][j-1]+cost;
 
                foobar[i][j] = Math.min(del,Math.min(ins,sub));
 
                if ((i > 1) && (j > 1) && 
                        (wordOne.charAt(i-1) == wordTwo.charAt(j-2)) && 
                        (wordOne.charAt(i-2) == wordTwo.charAt(j-1))) {
                    
                    foobar[i][j] = Math.min(foobar[i][j], foobar[i-2][j-2]+cost);
                }
            }
        }
        
        return foobar[wordOne.length()][wordTwo.length()];
    }
     
}
