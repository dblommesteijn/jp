package net.thepinguin.jp.internal;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PomTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PomTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PomTest.class );
    }
    
    private static final String __VALID_POM = "<xml><project></project></xml>";
    private static final String __INVALID_POM = "<xml><project>project></xml>";
    private static final String __VALID_LARGE_POM = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" "
    		+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
			+ "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">"
			+ "<modelVersion>4.0.0</modelVersion>"
			+ "<groupId>net.thepinguin.jp</groupId>"
			+ "<artifactId>jp</artifactId>"
			+ "<packaging>jar</packaging>"
			+ "<version>0.1</version>"
			+ "<name>jp</name>"
			+ "<url>http://maven.apache.org</url>"
			+ "<dependencies>"
			+ "<dependency>"
			+ "<groupId>junit</groupId>"
			+ "<artifactId>junit</artifactId>"
			+ "<version>3.8.1</version>"
			+ "<scope>test</scope>"
			+ "</dependency>"
			+ "<dependency>"
			+ "<groupId>gnu.getopt</groupId>"
			+ "<artifactId>java-getopt</artifactId>"
			+ "<version>1.0.13</version>"
			+ "</dependency>"
			+ "<dependency>"
			+ "<groupId>com.google.code.gson</groupId>"
			+ "<artifactId>gson</artifactId>"
			+ "<version>2.3</version>"
			+ "</dependency>"
			+ "<dependency>"
			+ "<groupId>org.eclipse.jgit</groupId>"
			+ "<artifactId>org.eclipse.jgit</artifactId>"
			+ "<version>3.5.1.201410131835-r</version>"
			+ "</dependency>"
			+ "<dependency>"
			+ "<groupId>org.apache.commons</groupId>"
			+ "<artifactId>commons-io</artifactId>"
			+ "<version>1.3.2</version>"
			+ "</dependency>"
			+ "</dependencies>"
			+ "<build>"
			+ "<plugins>"
			+ "<plugin>"
			+ "<artifactId>maven-assembly-plugin</artifactId>"
			+ "<executions>"
			+ "<execution>"
			+ "<phase>package</phase>"
			+ "<goals>"
			+ "<goal>single</goal>"
			+ "</goals>"
			+ "</execution>"
			+ "</executions>"
			+ "<configuration>"
			+ "<descriptorRefs>"
			+ "<descriptorRef>jar-with-dependencies</descriptorRef>"
			+ "</descriptorRefs>"
			+ "</configuration>"
			+ "</plugin>"
			+ "</plugins>"
			+ "</build>"
			+ "</project>";
    
    public void setUp(){
    }
    
    public void tearDown(){
    }
    
	public void testParseValidXmlFile(){
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("valid_pom.xml").getFile());
		Walker walker = Walker.parseFromFile(file);
		Assert.assertNotNull(walker);
    	Document doc = walker.getDocument();
    	Assert.assertNotNull(doc);
	}
	
	public void testParseInvalidXmlFile(){
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("invalid_pom.xml").getFile());
		Walker walker = Walker.parseFromFile(file);
		Assert.assertNull(walker);
	}
	
    public void testParseValidXml(){
    	Walker walker = Walker.parseFromString(__VALID_POM);
    	Assert.assertNotNull(walker);
    }
    
    public void testParseInvalidXml(){
    	Walker walker = Walker.parseFromString(__INVALID_POM);
    	Assert.assertNull(walker);
    }
    
    public void testParseValidPom(){
    	Walker walker = Walker.parseFromString(__VALID_LARGE_POM);
    	Assert.assertNotNull(walker);
    	Document doc = walker.getDocument();
    	Assert.assertNotNull(doc);
    	// find unique elements in xml
    	elementHelper(doc, "modelVersion");
    	elementHelper(doc, "groupId");
    	elementHelper(doc, "artifactId");
    	elementHelper(doc, "packaging");
    	elementHelper(doc, "version");
    	elementHelper(doc, "name");
    	elementHelper(doc, "url");
    	elementHelper(doc, "dependencies");
    	elementHelper(doc, "dependency");
    	elementHelper(doc, "build");
    	elementHelper(doc, "plugins");
    	elementHelper(doc, "plugin");
    	elementHelper(doc, "executions");
    	elementHelper(doc, "execution");
    	elementHelper(doc, "phase");
    	elementHelper(doc, "goals");
    	elementHelper(doc, "goal");
    	elementHelper(doc, "configuration");
    	elementHelper(doc, "descriptorRefs");
    	elementHelper(doc, "descriptorRef");
    }
    
    private void elementHelper(Document doc, String elementName){
    	List<Element> e = doc.findElement(elementName);
    	Assert.assertFalse(e.isEmpty());
    }
    
	public void testParseWalkPom(){
    	Walker walker = Walker.parseFromString(__VALID_LARGE_POM);
    	Document doc = walker.getDocument();
    	Assert.assertNotNull(doc);
    	// find project element
    	List<Element> project = doc.findElement("project");
    	Assert.assertTrue(project.size() == 1);
    	List<Element> es = project.get(0).getElements();
    	// test nested elements in project
    	Assert.assertTrue(es.get(0).getName().equals("modelVersion"));
    	Assert.assertTrue(es.get(1).getName().equals("groupId"));
    	Assert.assertTrue(es.get(2).getName().equals("artifactId"));
    	Assert.assertTrue(es.get(3).getName().equals("packaging"));
    	Assert.assertTrue(es.get(4).getName().equals("version"));
    	Assert.assertTrue(es.get(5).getName().equals("name"));
    	Assert.assertTrue(es.get(6).getName().equals("url"));
    	Assert.assertTrue(es.get(7).getName().equals("dependencies"));
    	Assert.assertTrue(es.get(8).getName().equals("build"));
    	// test nested element in project > dependencies
    	List<Element> ds = es.get(7).getElements();
    	List<String> groupIds = Arrays.asList(new String[] {"junit", "gnu.getopt", 
    			"com.google.code.gson", "org.eclipse.jgit", "org.apache.commons"});
    	List<String> artifactIds = Arrays.asList(new String[] {"junit", "java-getopt", 
    			"gson", "org.eclipse.jgit", "commons-io"});
    	List<String> versions = Arrays.asList(new String[] {"3.8.1", "1.0.13", 
    			"2.3", "3.5.1.201410131835-r", "1.3.2"}); 
    	for(Element q : ds){
    		Assert.assertEquals(q.getName(), "dependency");
    		Element groupId = q.getElementByName("groupId");
    		Element artifactId = q.getElementByName("artifactId");
    		Element version = q.getElementByName("version");
    		Assert.assertNotNull(groupId);
    		Assert.assertNotNull(artifactId);
    		Assert.assertNotNull(version);
    		Assert.assertTrue(groupIds.contains(groupId.getValue()));
    		Assert.assertTrue(artifactIds.contains(artifactId.getValue()));
    		Assert.assertTrue(versions.contains(version.getValue()));
    	}
    	// test nested build > plugins
    	Element plugins = es.get(8).getElementByName("plugins");
    	Assert.assertNotNull(plugins);
    	Element plugin = plugins.getElementByName("plugin");
    	Assert.assertNotNull(plugin);
    	Element artifactId = plugin.getElementByName("artifactId");
    	Assert.assertNotNull(artifactId);
    	Assert.assertEquals(artifactId.getValue(), "maven-assembly-plugin");
    	Element executions = plugin.getElementByName("executions");
    	Assert.assertNotNull(executions);
    	Element execution = executions.getElementByName("execution");
    	Assert.assertNotNull(execution);
    	Element phase = execution.getElementByName("phase");
    	Assert.assertNotNull(phase);
    	Assert.assertEquals(phase.getValue(), "package");
    	Element goals = execution.getElementByName("goals");
    	Assert.assertNotNull(goals);
    	Element goal = goals.getElementByName("goal");
    	Assert.assertNotNull(goal);
    	Assert.assertEquals(goal.getValue(), "single");
    	Element configuration = plugin.getElementByName("configuration");
    	Assert.assertNotNull(configuration);
    	Element descriptorRefs = configuration.getElementByName("descriptorRefs");
    	Assert.assertNotNull(descriptorRefs);
    	Element descriptorRef = descriptorRefs.getElementByName("descriptorRef");
    	Assert.assertNotNull(descriptorRef);
    	Assert.assertEquals(descriptorRef.getValue(), "jar-with-dependencies");
    }
	
	public void testAppendRemoveRepositoriesPom(){
		// load valid pom from string
		Walker walker = Walker.parseFromString(__VALID_LARGE_POM);
    	Document doc = walker.getDocument();
    	Assert.assertNotNull(doc);
    	List<Element> project = doc.findElement("project");
    	Assert.assertTrue(project.size() == 1);
    	Element p = project.get(0);
    	Assert.assertNotNull(p);
    	// get repositories element
		List<Element> repos = p.findElement("repositories");
		Element repo;
		Element newRepos = new Element("repositories");
		if(repos.isEmpty())
			repo = p.addElement(newRepos);
		else
			repo = repos.get(0);
		// addElementSelf should return the passed element
		Assert.assertEquals(newRepos, repo);
		// repo should be an instance of Element
		Assert.assertEquals(repo.getClass(), Element.class);
		// append repository element
		String id = "project.local";
		String name = "project";
		String url = "file:${project.basedir}/repo";
		Element dep_id = new Element("id", id);
		Element dep_name = new Element("name", name);
		Element dep_url = new Element("url", url);
		Element newRepo = new Element("repository");
		Element ret = newRepo.addElementSelf(dep_id).addElementSelf(dep_name).addElementSelf(dep_url);
		// addElementSelf returns the called element (not the passed element)
		Assert.assertEquals(ret, newRepo);
		// append repository to repositories
		repo.addElement(newRepo);
		// find element by name
		Element newRepo2 = repo.getElementByName("repository");
		Assert.assertEquals(newRepo, newRepo2);
		// remove element by object
		repo.removeElement(newRepo);
		newRepo2 = repo.getElementByName("repository");
		Assert.assertNull(newRepo2);
		repo.addElement(newRepo);
		newRepo2 = repo.getElementByName("repository");
		Assert.assertNotNull(newRepo2);
		repo.removeElement(newRepo.getName());
		newRepo2 = repo.getElementByName("repository");
		Assert.assertNull(newRepo2);
	}
	


}
