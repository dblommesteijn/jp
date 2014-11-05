package net.thepinguin.jp;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Root;
import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParsePomTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParsePomTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ParsePomTest.class );
    }
    
    private static final String __VALID_XML = "<xml><project></project></xml>";
    private static final String __INVALID_XML = "<xml><project>project></xml>";
    
    public void setUp(){
    }
    
    public void tearDown(){
    }
    
    public void testValidXml(){
    	Walker walker = Walker.parseFromString(__VALID_XML);
    	this.assertNotNull(walker);
//		Document doc = walker.getDocument();
    }
    
    public void testInvalidXml(){
    	Walker walker = Walker.parseFromString(__INVALID_XML);
    	this.assertNull(walker);
//		Document doc = walker.getDocument();
    }
}
