package net.thepinguin.jp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Root;
import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;
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
    
    public void testParseValidXml(){
    	Walker walker = Walker.parseFromString(__VALID_POM);
    	this.assertNotNull(walker);
    }
    
    public void testParseInvalidXml(){
    	Walker walker = Walker.parseFromString(__INVALID_POM);
    	this.assertNull(walker);
    }
    
    public void testParseValidPom(){
    	Walker walker = Walker.parseFromString(__VALID_LARGE_POM);
    	this.assertNotNull(walker);
    	Document doc = walker.getDocument();
    	this.assertNotNull(doc);
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
    
    @SuppressWarnings("static-access")
    private void elementHelper(Document doc, String elementName){
    	List<Element> e = doc.findElement(elementName);
    	this.assertFalse(e.isEmpty());
    }
    
    @SuppressWarnings("static-access")
	public void testParseWalkPom(){
    	Walker walker = Walker.parseFromString(__VALID_LARGE_POM);
    	Document doc = walker.getDocument();
    	this.assertNotNull(doc);
    	// find project element
    	List<Element> project = doc.findElement("project");
    	this.assertTrue(project.size() == 1);
    	List<Element> es = project.get(0).getElements();
    	// test nested elements in project
    	this.assertTrue(es.get(0).getName().equals("modelVersion"));
    	this.assertTrue(es.get(1).getName().equals("groupId"));
    	this.assertTrue(es.get(2).getName().equals("artifactId"));
    	this.assertTrue(es.get(3).getName().equals("packaging"));
    	this.assertTrue(es.get(4).getName().equals("version"));
    	this.assertTrue(es.get(5).getName().equals("name"));
    	this.assertTrue(es.get(6).getName().equals("url"));
    	this.assertTrue(es.get(7).getName().equals("dependencies"));
    	this.assertTrue(es.get(8).getName().equals("build"));
    	// test nested element in project > dependencies
    	List<Element> ds = es.get(7).getElements();
    	List<String> groupIds = Arrays.asList(new String[] {"junit", "gnu.getopt", 
    			"com.google.code.gson", "org.eclipse.jgit", "org.apache.commons"});
    	List<String> artifactIds = Arrays.asList(new String[] {"junit", "java-getopt", 
    			"gson", "org.eclipse.jgit", "commons-io"});
    	List<String> versions = Arrays.asList(new String[] {"3.8.1", "1.0.13", 
    			"2.3", "3.5.1.201410131835-r", "1.3.2"}); 
    	for(Element q : ds){
    		this.assertEquals(q.getName(), "dependency");
    		Element groupId = q.getElementByName("groupId");
    		Element artifactId = q.getElementByName("artifactId");
    		Element version = q.getElementByName("version");
    		this.assertNotNull(groupId);
    		this.assertNotNull(artifactId);
    		this.assertNotNull(version);
    		this.assertTrue(groupIds.contains(groupId.getValue()));
    		this.assertTrue(artifactIds.contains(artifactId.getValue()));
    		this.assertTrue(versions.contains(version.getValue()));
    	}
    	// test nested build > plugins
    	Element plugins = es.get(8).getElementByName("plugins");
    	this.assertNotNull(plugins);
    	Element plugin = plugins.getElementByName("plugin");
    	this.assertNotNull(plugin);
    	Element artifactId = plugin.getElementByName("artifactId");
    	this.assertNotNull(artifactId);
    	this.assertEquals(artifactId.getValue(), "maven-assembly-plugin");
    	Element executions = plugin.getElementByName("executions");
    	this.assertNotNull(executions);
    	Element execution = executions.getElementByName("execution");
    	this.assertNotNull(execution);
    	Element phase = execution.getElementByName("phase");
    	this.assertNotNull(phase);
    	this.assertEquals(phase.getValue(), "package");
    	Element goals = execution.getElementByName("goals");
    	this.assertNotNull(goals);
    	Element goal = goals.getElementByName("goal");
    	this.assertNotNull(goal);
    	this.assertEquals(goal.getValue(), "single");
    	Element configuration = plugin.getElementByName("configuration");
    	this.assertNotNull(configuration);
    	Element descriptorRefs = configuration.getElementByName("descriptorRefs");
    	this.assertNotNull(descriptorRefs);
    	Element descriptorRef = descriptorRefs.getElementByName("descriptorRef");
    	this.assertNotNull(descriptorRef);
    	this.assertEquals(descriptorRef.getValue(), "jar-with-dependencies");
    }
    

    
}
