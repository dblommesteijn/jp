package net.thepinguin.jp;

import java.util.List;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Dependency;
import net.thepinguin.jp.json.jpacker.Root;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class JPackerTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JPackerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( JPackerTest.class );
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
    
    public void testParseValidJson(){
    	Root root = ParseJP.parseFromString(__VALID_JSON);
    	Assert.assertNotNull(root);
    }
    
    public void testParseInvalidJson(){
    	Root root = ParseJP.parseFromString(__INVALID_JSON);
    	Assert.assertNull(root);
    }
    
    public void testParseValidJPackerJson(){
    	Root root = ParseJP.parseFromString(__VALID_LARGE_JSON);
    	Assert.assertNotNull(root);
    	Assert.assertFalse(root.dependencies.isEmpty());
    	List<Dependency> deps = root.dependencies;
    	Dependency hatta = deps.get(0);
    	Assert.assertEquals(hatta.name, "net.thepinguin.hatta#hatta");
		Assert.assertEquals(hatta.file, "/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar");
		Assert.assertEquals(hatta.version, "0.1");
		Dependency hatta_github = deps.get(1);
		Assert.assertEquals(hatta_github.name, "net.thepinguin.hatta#hatta");
		Assert.assertEquals(hatta_github.github, "https://github.com/dblommesteijn/hatta");
		Assert.assertEquals(hatta_github.version, "0.1");
    }
}
