package net.thepinguin.jp;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Root;

public class Test {
	public static void main(String[] args){		
		
		try {
			String filename = "/Users/dennisb/Programming/github/jp/JPacker";
			
			Root root = ParseJP.parseFromFile(filename);
			System.out.println(root);
			if (root == null)
				System.exit(1);
			
			System.out.println(root.toString());
			root.resolveDependencies();
			
			
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
