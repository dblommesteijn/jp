package net.thepinguin.jp.internal;

import java.io.File;
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
    	+ "{\"name\": \"net.thepinguin.hatta#hatta#0.1\","
    	+ "\"file\": \"/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar\""
    	+ "},"
    	+ "{\"name\": \"net.thepinguin.hatta#hatta#0.1\","
    	+ "\"git\": \"https://github.com/dblommesteijn/hatta\","
    	+ "\"target\": \"target/hatta-1.0-SNAPSHOT.jar\","
    	+ "\"goal\": \"assembly:assembly -DdescriptorId=jar-with-dependencies -DskipTests=true\""
    	+ "} ]}";
    
    public void setUp(){
    }
    
    public void tearDown(){
    }
    
    public void testParseValidJsonFile(){
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("valid_jpacker.json").getFile());
		Root root = ParseJP.parseFromFile(file.toString());
    	Assert.assertNotNull(root);
    }
    
    public void testParseInvalidJsonFile(){
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("invalid_jpacker.json").getFile());
		Root root = ParseJP.parseFromFile(file.toString());
    	Assert.assertNull(root);
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
    	Assert.assertEquals(hatta.name, "net.thepinguin.hatta#hatta#0.1");
		Assert.assertEquals(hatta.file, "/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar");
		Assert.assertTrue(hatta.isFile());
		Dependency hatta_github = deps.get(1);
		Assert.assertEquals(hatta_github.name, "net.thepinguin.hatta#hatta#0.1");
		Assert.assertEquals(hatta_github.git, "https://github.com/dblommesteijn/hatta");
		Assert.assertTrue(hatta_github.isGithub());
    }
    
    public void testParseMissingDependencies(){
    	String input = "{\"\": ["
            	+ "{\"name\": \"net.thepinguin.hatta#hatta\","
            	+ "\"file\": \"/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar\","
            	+ "\"version\": \"0.1\"} ]}";
    	Root root = ParseJP.parseFromString(input);
    	Assert.assertNotNull(root);
    	Assert.assertFalse(root.isValid());
    }
    
    public void testParseMissingFile(){
    	String input = "{\"dependencies\": ["
            	+ "{\"name\": \"net.thepinguin.hatta#hatta#0.1\"}"
            	+ "]}";
    	Root root = ParseJP.parseFromString(input);
    	Assert.assertNotNull(root);
    	Assert.assertTrue(root.isValid());
    	// NOTE: cannot determine if it's a dependency that is already resolved or missing `file` key
    }
    
    public void testParseMissingName(){
    	String input = "{\"dependencies\": ["
            	+ "{"
            	+ "\"file\": \"/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar\""
            	+ "} ]}";
    	Root root = ParseJP.parseFromString(input);
    	Assert.assertNotNull(root);
    	Assert.assertFalse(root.isValid());
    }
    
    public void testParseMissingVersion(){
    	String input = "{\"dependencies\": ["
    			+ "{\"name\": \"net.thepinguin.hatta#hatta\","
            	+ "\"file\": \"/Users/dennisb/Programming/github/hatta/target/hatta-0.1.jar\"}"
            	+ " ]}";
    	Root root = ParseJP.parseFromString(input);
    	Assert.assertNotNull(root);
    	Assert.assertFalse(root.isValid());
    }
    
    public void testParseBuildIn(){
    	String input = "{\"dependencies\": ["
    			+ "{\"name\": \"junit#junit#3.8.1\","
    			+ "\"scope\": \"test\""
            	+ "} ]}";
    	Root root = ParseJP.parseFromString(input);
    	Assert.assertNotNull(root);
    	Assert.assertTrue(root.isValid());
    	Assert.assertTrue(root.dependencies.get(0).isBuildIn());
    }
    
    public void testResolveFile(){
    	
    }
}
