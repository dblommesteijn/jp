package net.thepinguin.jp.json.jpacker;

import java.io.File;
import java.io.IOException;
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
	
	public String getArtifactId(){
		return name.split("#")[1];
	}
	
	public String getGroupId(){
		return name.split("#")[0];
	}
	
	public void cloneRespository(String ref) throws Exception{
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
	
}
