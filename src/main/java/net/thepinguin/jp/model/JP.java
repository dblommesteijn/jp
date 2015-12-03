package net.thepinguin.jp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.Mvn;
import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;

public class JP {
	
	public static String META_FILE = "JPacker";
	public static String META_M3 = "pom.xml";
	
	private File _cwd;
	private File _jpacker;
	private File _pom;
	private String _groupId;
	private String _artifactId;
	private String _version;
	
	
	public JP(File cwd) {
		_cwd = cwd.getAbsoluteFile();
		_jpacker = new File(_cwd, JP.META_FILE);
		_pom = new File(_cwd, JP.META_M3);
		// parse project info
		_groupId = this.parseGroupId();
		_artifactId = this.parseArtifactId();
		_version = this.parseVersion();
		// verbose output
		if(App.isVerbose()){
			System.out.println(" ... cwd: " + _cwd.getAbsolutePath());
			System.out.println(" ... jpacker: " + _jpacker.getAbsolutePath());
			System.out.println(" ... pom: " + _pom.getAbsolutePath());
			System.out.println(" ... groupId: " + _groupId);
			System.out.println(" ... artifactId: " + _artifactId);
			System.out.println(" ... version: " + _version);
		}
	}
	
	private static Element parsePomProject(File pom) {
		if(!pom.exists())
			return null;
		Walker walker = Walker.parseFromFile(pom);
		Document doc = walker.getDocument();
		// find project element
		List<Element> pss = doc.findElement("project");
		if (pss.isEmpty()) {
			// invalid pom
			return null;
		}
		// append dependencies
		Element project = pss.get(0);
		return project;
	}
	
	private String parseGroupId() {
		Element project = JP.parsePomProject(_pom);
		if(project == null)
			return "";
		List<Element> gid = project.findElement("groupId");
		if (gid.isEmpty()) {
			return "";
		}
		return gid.get(0).getValue();
	}
	
	private String parseArtifactId() {
		Element project = JP.parsePomProject(_pom);
		if(project == null)
			return "";
		List<Element> gid = project.findElement("artifactId");
		if (gid.isEmpty()) {
			return "";
		}
		return gid.get(0).getValue();
	}
	
	private String parseVersion() {
		Element project = JP.parsePomProject(_pom);
		if(project == null)
			return "";
		List<Element> gid = project.findElement("version");
		if (gid.isEmpty()) {
			return "";
		}
		return gid.get(0).getValue();
	}

	public File getJPacker() {
		return _jpacker;
	}

	public File getCwd() {
		return _cwd;
	}
	
	public File getPom() {
		return _pom;
	}

	public void setGroupId(String groupId) {
		_groupId = groupId;
	}

	public void setArtifactId(String artifactId) {
		_artifactId = artifactId;
	}
	
	public JP factory(File cwd) {
		JP jp = new JP(cwd);
		jp.setGroupId(this.getGroupId());
		jp.setArtifactId(this.getArtifactId());
		return jp;
	}

	public String[] getGroupDomain() {
		return _groupId.split("[.]");
	}

	public String getGroupId() {
		return _groupId;
	}

	public String getArtifactId() {
		return _artifactId;
	}
	
	public boolean isRepository() {
		return _jpacker.exists();
	}

	public boolean createNewProject() {
		return Mvn.newProject(_cwd, _groupId, _artifactId);
	}

	/**
	 * Create JPacker file
	 * @param file location of the project to create JPacker file in
	 * @throws Exception
	 */
	public void createJPacker() throws Exception {
		// build basic dependency (default)
		StringBuilder sb = new StringBuilder();
		sb.append("{\"dependencies\": [");
		sb.append(App.EOL);
		// add default test dependency
		sb.append("  { \"name\": \"junit#junit#4.12\", \"scope\": \"test\" }");
		sb.append(App.EOL);
		sb.append("]}");
		try {
			// write to JPacker file
			PrintWriter wrt = new PrintWriter(_jpacker);
			wrt.println(sb);
			wrt.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("unable to create JPacker file");
		}
	}

	public void isValidOrThrowException() throws Exception {
		if(!_cwd.exists() || _cwd.isFile())
			throw new Exception("invalid path");
		if(!_pom.isFile())
			throw new Exception("pom.xml not found");
	}

	public String getStatus() {
		StringBuilder sb = new StringBuilder();
		if(this.isRepository()) {
			sb.append("jp: project: `").append(_artifactId);
			sb.append("` (").append(_version).append(")").append(App.EOL);
			sb.append(" ... groupId: ").append(_groupId);
			//TODO: add collection status!
		} else {
			sb.append("jp: not a repository");
		}
		return sb.toString();
	}

}

