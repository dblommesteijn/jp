package net.thepinguin.jp.internal;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.model.JP;

public class ModelTest extends TestCase {
	
	private File _testRepos;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ModelTest( String testName )
    {
        super( testName );
        _testRepos = new File(App.JP_HOME, "test/repos/").getAbsoluteFile();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ModelTest.class );
    }
    
    public void testInitJP() {
    	File cwd = new File(_testRepos, "testinitjp");
    	JP jp = new JP(cwd);
    	Assert.assertEquals("", jp.getArtifactId());
    	Assert.assertEquals("", jp.getGroupId());
    	Assert.assertEquals(cwd, jp.getCwd());
    	Assert.assertEquals(new File(cwd, JP.META_FILE), jp.getJPacker());
    	Assert.assertEquals(new File(cwd, JP.META_M3), jp.getPom());
    }
    
    public void testSetArtifactId() {
    	File cwd = new File(_testRepos, "testsetartifactid");
    	JP jp = new JP(cwd);
    	Assert.assertEquals("", jp.getArtifactId());
    	jp.setArtifactId("artifact");
    	Assert.assertEquals("artifact", jp.getArtifactId());
    }
 
    public void testSetGroupId() {
    	File cwd = new File(_testRepos, "testsetgroupid");
    	JP jp = new JP(cwd);
    	Assert.assertEquals("", jp.getGroupId());
    	jp.setGroupId("net.thepinguin.jp");
    	Assert.assertEquals("net.thepinguin.jp", jp.getGroupId());
    }
    
    public void testGroupDomain() {
    	File cwd = new File(_testRepos, "testgroupdomain");
    	JP jp = new JP(cwd);
    	Assert.assertEquals("", jp.getGroupId());
    	jp.setGroupId("net.thepinguin.jp");
    	Assert.assertEquals("net.thepinguin.jp", jp.getGroupId());
    	String[] ds = jp.getGroupDomain();
    	Assert.assertEquals("net", ds[0]);
    	Assert.assertEquals("thepinguin", ds[1]);
    	Assert.assertEquals("jp", ds[2]);
    }
    
    public void testNewProject() {
    	File cwd = new File(_testRepos, "");
    	JP jp = new JP(cwd);
    	jp.setArtifactId("testnewproject");
    	jp.setGroupId("net.thepinguin.jp");
    	Assert.assertEquals("testnewproject", jp.getArtifactId());
    	Assert.assertEquals("net.thepinguin.jp", jp.getGroupId());
    }
    
}
