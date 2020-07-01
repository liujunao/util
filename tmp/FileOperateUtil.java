package com.wonders.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileOperateUtil {
	private static final String UPLOADDIR = "upload\\file\\";

	public static void download(HttpServletRequest request,
			HttpServletResponse response, String storeName, String contentType,
			String realName) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		String ctxPath = request.getSession().getServletContext()
				.getRealPath("/")
				+ FileOperateUtil.UPLOADDIR;
		String downLoadPath = ctxPath + storeName;
        File file = new File(ctxPath);
		long fileLength = new File(downLoadPath).length();

		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("GB2312"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}
	
	// 保存文件 1,文件 2，保存路径 3，文件名称
		public static void SaveFileFromInputStream(InputStream stream, String path,
				String filename) throws IOException {
			FileOutputStream fs = new FileOutputStream(path + "/" + filename);
			byte[] buffer = new byte[1024 * 1024];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
			fs.close();
			stream.close();
		}
		//删除文件
		public static  boolean DeleteFile(String sPath) {
			//sPath 要删除文件的位置
			boolean flag = false;
			File f = new File(sPath);  
	          if(f.exists()){
	        	 f.delete(); 
	        	 flag=true;
	        	 return flag;
	          }else {
	        	  return false;
	          }
		}

}
