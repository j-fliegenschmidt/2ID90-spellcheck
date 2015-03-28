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
public class DamerauLevenshtein2Test {
    
    private void checkExecute(String a, String b, int expResult) {
        System.out.println("Execute similarity testing");
        DamerauLevenshtein2 instance;
        instance = new DamerauLevenshtein2(a,b);
        int result = instance.execute();
        assertEquals("The result " + result + " is equal to " + expResult, 
                expResult, result);
    }
    
    //Testing for insertion
    @Test
    public void testExecuteDHS0() {
        checkExecute("hello", "hell", 1);
    }
    
    //Testing for same
    @Test
    public void testExecuteDHS1() {
        checkExecute("hello", "hello", 0);
    }
    
    //Testing for swap
    @Test
    public void testExecuteDHS2() {
        checkExecute("helol", "hello", 1);
    }
    
    //Testing for substitution
    @Test
    public void testExecuteDHS3() {
        checkExecute("hello", "hekko", 2);
    }
    
    //Testing for deletion
    @Test
    public void testExecuteDHS4() {
        checkExecute("hello", "helllo", 1);
    }
    
    //Testing for all incorrect
    @Test
    public void testExecuteDHS5() {
        checkExecute("xyz", "bc", 3);
    }
    
    //Testing for different sized words
    @Test
    public void testExecuteDHS6() {
        checkExecute("bahhhhhhhhh", "whered", 10);
    }
    
    //Testing for different sized words
    @Test
    public void testExecuteDHS7() {
        checkExecute("word", "bahhhhhhhhh", 11);
    }

    //Test case that broke the program
    @Test
    public void testProblemCandidates() {
        checkExecute("hme", "seriousandwidespreadweaknessespersistat", 38);
    }
    
    @Test
    public void testProblemFail() {
        checkExecute("hme", "has", 2);
    }
    
    @Test
    public void testProblemFail2() {
        checkExecute("xyz", "bc", 3);
    }
    
    @Test
    public void testPotentialFail3() {
        checkExecute("hoome", "home", 1);
    }
}
