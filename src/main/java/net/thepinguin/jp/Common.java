package net.thepinguin.jp;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class Common {
	public static final File JP_HOME = new File(System.getProperty("user.home"), ".jp");
	public static final File M3_HOME = new File(System.getenv("M3_HOME"), "../");
}
