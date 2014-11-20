package net.thepinguin.jp;

import java.io.File;
import java.util.List;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Dependency;
import net.thepinguin.jp.json.jpacker.Root;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class RepositoryTest extends TestCase {
    
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RepositoryTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( RepositoryTest.class );
    }
  
    public void setUp(){
    }
    
    public void tearDown(){
    }
    
    public void testCloneGithubCommit(){
    	String jpacker = "{\"dependencies\": ["
            	+ "{\"name\": \"net.thepinguin.jp#jp#0.1\","
            	+ "\"github\": \"https://github.com/java-packer/jp\","
            	+ "\"target\": \"target/jp-0.1-jar-with-dependencies.jar\","
            	+ "\"commit\": \"558a7b5e52edeb4165d32c306ee145b71df5b0a7\""
            	+ "} ]}";
    	Root root = ParseJP.parseFromString(jpacker);
    	Assert.assertNotNull(root);
    	Assert.assertTrue(root.isValid());
    	Assert.assertFalse(root.dependencies.isEmpty());
    	List<Dependency> deps = root.dependencies;
    	Dependency jp = deps.get(0);
    	Assert.assertFalse(jp.isFile());
    	Assert.assertTrue(jp.isGithub());
    	try {
			File repo = jp.cloneRepository();
			Assert.assertEquals(repo.getName(), "558a7b5e52edeb4165d32c306ee145b71df5b0a7");
			Assert.assertTrue(repo.isDirectory());
			Assert.assertTrue(repo.exists());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    }
    
    public void testCloneGithubSSH(){
    	String jpacker = "{\"dependencies\": ["
            	+ "{\"name\": \"net.thepinguin.jp#jp\","
            	+ "\"github\": \"git@github.com:java-packer/jp.git\","
            	+ "\"target\": \"target/jp-0.1-jar-with-dependencies.jar\","
            	+ "\"commit\": \"6cd474bcbfadf10f2b336b2674d4d1bf4a84e2ca\","
            	+ "\"version\": \"0.1\"} ]}";
    	Root root = ParseJP.parseFromString(jpacker);
    	Assert.assertNotNull(root);
    	Assert.assertFalse(root.isValid());
    	Assert.assertEquals(root.getErrorMessages().get(0), "github ssh not supported (use https)");
    }
    
    public void testCloneGithubBranch(){
    	// TODO: stub    	
    }
    
    public void testCloneGithubTag(){
    	// TODO: stub
    }
    
    public void testLookupLocal(){
    	String jpacker = "{\"dependencies\": ["
            	+ "{\"name\": \"net.thepinguin.jp#jp#0.1\","
            	+ "\"file\": \"target/jp-0.1-jar-with-dependencies.jar\""
            	+ "} ]}";
    	Root root = ParseJP.parseFromString(jpacker);
    	Assert.assertNotNull(root);
    	Assert.assertTrue(root.isValid());
    	Assert.assertFalse(root.dependencies.isEmpty());
    	List<Dependency> deps = root.dependencies;
    	Dependency jp = deps.get(0);
    	Assert.assertTrue(jp.isFile());
    	Assert.assertFalse(jp.isGithub());

    	// TODO: lookup local jar and add to classpath!
    	
    }
    
    public void testResolveDependencies(){
    	// TODO: test root.resolveDependencies()
    }
    
}
