package net.thepinguin.jp.json.jpacker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.thepinguin.jp.Common;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;

import com.google.gson.annotations.SerializedName;

public class Dependency {
	@SerializedName("name")
	public String name = "";
	
	@SerializedName("file")
	public String file = "";
	
	@SerializedName("github")
	public String github = "";
	
	@SerializedName("version")
	public String version = "";
	
	@SerializedName("commit")
	public String commit = "";
	
	public String getArtifactId(){
		return name.split("#")[1];
	}
	
	public String getGroupId(){
		return name.split("#")[0];
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		sb.append("artifactId: `").append(this.getArtifactId()).append("`, ");
		sb.append("groupId: `").append(this.getGroupId()).append("`, ");
		sb.append("file: `").append(file).append("`, ");
		sb.append("github: `").append(github).append("`, ");
		sb.append("version: `").append(version).append("`");
		sb.append(" ]");
		return sb.toString();
	}
	
	public String getCannonicalName(){
		if(this.isFile()){
			return this.getArtifactId() + "(.jar)";
		} else if(this.isGithub()){
			return this.getArtifactId() + "(" + github + ")";
 		}
		return "";
	}

	public boolean resolve() {
		if(this.isGithub()){
			try{
				this.cloneRespository();
			} catch (Exception e){
				return false;
			}
			return true;
		} else if(this.isFile()){
			return true;
		}
		return false;
	}
	
	public boolean isGithub(){
		boolean https = github.startsWith("http");
		boolean empty = file.isEmpty();
//		if(empty && !https) System.out.println("can only resolve github http(s)");
		return empty && https;
	}
	
	public boolean isFile(){
		return !file.isEmpty();
	}
	
	private void cloneRespository() throws Exception{
		String ref = commit;
		if(ref.equals(""))
			ref = "HEAD";
		if(github.startsWith("git"))
			throw new Exception("cannot clone git via ssh");
		String groupId = this.getGroupId();
		String artifactId = this.getArtifactId();
		String uuid = UUID.randomUUID().toString();
		groupId = "deps/" + groupId.replace('.', '/') + "/" + artifactId + "/" + version + "/";
		File deps = new File(Common.JP_HOME, groupId);
		if(!deps.isDirectory() && !deps.mkdirs())
			throw new Exception("cannot create home: `" + deps + "`");
		// set tmp clone directory
		File tmpClone = new File(deps, uuid);
		// clone repository
		Git git = Git.cloneRepository().setURI(github).setDirectory(tmpClone).call();
		Repository repo = git.getRepository();
		repo.getRef(ref);
		// get last commit, and rename folder
		String name = repo.resolve(ref).name();
		
		// TODO: determine what version to checkout
		
		File newClone = new File(deps, name);
		if(!newClone.exists()){
			// rename to commit ref
			tmpClone.renameTo(newClone);
		}else{
			// remove, already 
			FileUtils.deleteDirectory(tmpClone);
		}
		git.close();
	}

	public Boolean isValid() {
		if(this.isFile() || this.isGithub()){
			if(!name.isEmpty() && !version.isEmpty())
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
}
