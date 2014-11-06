package net.thepinguin.jp;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Root;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class ParseJPackerTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParseJPackerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ParseJPackerTest.class );
    }
    
    private static final String __VALID_JSON = "{\"dependencies\": []}";
    private static final String __INVALID_JSON = "{\" []}";
    private static final String __VALID_LARGE_JSON = "{\"dependencies\": ["
    	+ "{\"name\": \"net.thepinguin.hatta#hatta\","
    	+ "\"file\": \"/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar\","
    	+ "\"version\": \"0.1\"},"
    	+ "{\"name\": \"net.thepinguin.hatta#hatta\","
    	+ "\"github\": \"https://github.com/dblommesteijn/hatta\","
    	+ "\"commit\": \"\","
    	+ "\"version\": \"0.1\"} ]}";
    
    public void setUp(){
    }
    
    public void tearDown(){
    }
    
    public void testValidJson(){
    	Root root = ParseJP.parseFromString(__VALID_JSON);
		this.assertNotNull(root);
    }
    
    public void testInvalidJson(){
    	Root root = ParseJP.parseFromString(__INVALID_JSON);
		this.assertNull(root);
    }
    
    public void testParseValidJPackerJson(){
    	Root root = ParseJP.parseFromString(__VALID_LARGE_JSON);
    	this.assertNotNull(root);
    	// TODO: add JPacker specific tests here!!!
    	
    	
    }
    
    
}
