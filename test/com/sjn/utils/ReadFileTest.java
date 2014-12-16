package com.sjn.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import junit.framework.TestCase;

public class ReadFileTest {

	@Test
	public void testRead() {
		try {
//			String a = FileUtils.readFileToString(new File("f:\\226个安全色-bootcss.html"));
			List<String> list = FileUtils.readLines(new File("f:\\226个安全色-bootcss.html"),"UTF-8");
			
			String a = ArrayUtils.join(list.toArray(),"','");
			
			System.out.println(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
