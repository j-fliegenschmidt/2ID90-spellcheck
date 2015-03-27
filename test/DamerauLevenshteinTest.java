/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alexau
 */
public class DamerauLevenshteinTest {
    
    /**
     * Test of getDHSimilarity method, of class DamerauLevenshtein.
     */
    
    private void checkExecuteDHS(String a, String b, int expResult) {
        System.out.println("executeDHS");
        DamerauLevenshtein instance;
        instance = new DamerauLevenshtein(a,b);
        int result = instance.executeDHS();
        assertEquals("The result " + result + " is equal to " + expResult, 
                expResult, result);
    }
    
    //Testing for insertion
    @Test
    public void testExecuteDHS0() {
        checkExecuteDHS("hello", "hell", 1);
    }
    
    //Testing for same
    @Test
    public void testExecuteDHS1() {
        checkExecuteDHS("hello", "hello", 0);
    }
    
    //Testing for swap
    @Test
    public void testExecuteDHS2() {
        checkExecuteDHS("helol", "hello", 1);
    }
    
    //Testing for substitution
    @Test
    public void testExecuteDHS3() {
        checkExecuteDHS("hello", "hekko", 2);
    }
    
    //Testing for deletion
    @Test
    public void testExecuteDHS4() {
        checkExecuteDHS("hello", "helllo", 1);
    }
    
    //Testing for all incorrect
    @Test
    public void testExecuteDHS5() {
        checkExecuteDHS("xyz", "bc", 2);
    }
    
    //Testing for different sized words
    @Test
    public void testExecuteDHS6() {
        checkExecuteDHS("bahhhhhhhhh", "whered", 9);
    }
    
    //Testing for different sized words
    @Test
    public void testExecuteDHS7() {
        checkExecuteDHS("word", "bahhhhhhhhh", 10);
    }
    
    //Test case that broke the program
    @Test
    public void testProblemCandidates() {
        checkExecuteDHS("hme", "seriousandwidespreadweaknessespersistat", 38);
    }
}
