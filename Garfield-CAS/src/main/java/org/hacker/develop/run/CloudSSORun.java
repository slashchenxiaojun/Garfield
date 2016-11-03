package org.hacker.develop.run;

import com.jfinal.core.JFinal;

/**
 * XXX is your app name, choose if you want
 * */
public class CloudSSORun {
	
	public static void main(String[] args){
		JFinal.start("webapp", 8080, "/", 1);
	}
}
