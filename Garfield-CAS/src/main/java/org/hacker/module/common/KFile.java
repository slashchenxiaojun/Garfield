package org.hacker.module.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.hacker.exception.ApiException;

/**
 * A File helper tools
 * 
 * @author Mr.J.
 * 
 * @since 2014-12-05
 **/
public class KFile {
  
  /**
   * 利用递归的方法深度遍历文件夹下的所有文件
   * 
   * @param src 目标目录
   * @param out 输出list
   * @param fileEndName 文件名后缀
   * 
   * @throws FileNotFoundException
   */
	public static void findAllByName(File src, List<File> out, String fileEndName) throws FileNotFoundException {
		validateFile(src);
		if(src.isFile()) throw new ApiException("Oop~ src is file.");
		File[] tmpfiles = src.listFiles();
		if (tmpfiles == null)
			return;
		for(int i = 0; i < tmpfiles.length; i++) {
			if(tmpfiles[i].isDirectory()) {
				findAllByName(tmpfiles[i], out, fileEndName);
			} else {
				if(tmpfiles[i].getName().endsWith("." + fileEndName)) {
				  if(out == null) out = new ArrayList<>();
				  out.add(tmpfiles[i]);
				}
			}
		}
	}

	/**
	 * 根据uri创建，文件/文件夹
	 * 
	 * @param uri
	 *            文件/文件夹 路径
	 * 
	 * @return file 引用，如果为null则表示创建失败
	 */
	public static File createFile(String uri) {
		File file = new File(uri);
		if(file.exists()) {
			return file;
		}
		if(uri.endsWith(File.separator)) {
			return null;
		}
		// 判断目标文件所在的目录是否存在
		if(!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			if(!file.getParentFile().mkdirs()) {
				return null;
			}
		}
		// 创建目标文件
		try {
			if(file.createNewFile()) {
				return file;
			}else{
				return null;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串写入指定文件
	 * 
	 * @param str
	 * @param f
	 * @throws FileNotFoundException
	 */
	public static void write(String str[], File f) throws FileNotFoundException {
		validateFile(f);
		if (f.canWrite()) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			try {
				for (String s : str) {
					bw.write(s);
				}
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bw != null)
						bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将字符串写入指定文件
	 * 
	 * @param str
	 * @param f
	 * @throws FileNotFoundException
	 */
	public static void write(String str, File f) throws FileNotFoundException {
		write(new String[] { str }, f);
	}
	
	public static String read(File f) throws FileNotFoundException{
		validateFile(f);
		if(f.canRead()){
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			StringBuffer sb = new StringBuffer();
			try {
				while(true){
					String str = br.readLine();
					if(str == null) break;
					sb.append(str).append(System.getProperty("line.separator"));
				}
			} catch (Exception e) {
			  e.printStackTrace();
			}finally{
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * 验证File参数的正确性
	 * 
	 * @param f
	 * @throws FileNotFoundException
	 */
	private static boolean validateFile(File f) throws FileNotFoundException {
		if (!f.exists()) {
			throw new FileNotFoundException("Oop~ Please check if your File exists.");
		} else {
		  return true;
		}
	}
}
