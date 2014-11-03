package net.thepinguin.jp;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Root;

public class Test {
	public static void main(String[] args){		
		
		try {
			String filename = "/Users/dennisb/Programming/github/jp/JPacker";
			
			Root root = ParseJP.parseFromFile(filename);
			if (root == null)
				System.exit(1);
			
			System.out.println(root.toString());
			root.dependencies.get(1).cloneRespository();
			
			
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
