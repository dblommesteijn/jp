package net.thepinguin.jp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DependencyBuildTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DependencyBuildTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DependencyBuildTest.class );
    }
    
    public void setUp(){
    }
    
    public void tearDown(){
    }
    
    public void testTestBuildMvn(){
    	
    }
}
