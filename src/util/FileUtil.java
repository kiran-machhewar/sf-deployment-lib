package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {

	public static boolean localRun = false;
	public static byte [] getBytesFromFile(File file) throws IOException{
		byte bytes[] = null;
		try (FileInputStream fis = new FileInputStream(file)) {
		    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
		        byte[] buffer = new byte[1024];
		        int read = -1;
		        while ((read = fis.read(buffer)) != -1) {
		            baos.write(buffer, 0, read);
		        }
		        bytes = baos.toByteArray();
		    } 
		} catch (IOException exp) {
		    throw exp;
		}
		return bytes;
	}
	
	public static byte[] processZipToKeepSrcFolderOnly(byte zipInputData[]) throws IOException {
	    ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(zipInputData));		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte[]          buffer = new byte[512];
		ZipEntry entry = zin.getNextEntry();
		while (entry !=null) {		
			if(entry.getName().contains("/src/") && entry.isDirectory()){
				break;
			}
			entry = zin.getNextEntry();
		}
		while (entry !=null) {
	        // create a new empty ZipEntry			
	        ZipEntry newEntry = new ZipEntry("src"+entry.getName().split("/src")[1]);
	        zos.putNextEntry(newEntry);
	        int len = 0;
            while ((len = zin.read(buffer)) > 0)
            {
            	zos.write(buffer, 0, len);
            }
		    entry = zin.getNextEntry();
		}
		baos.close();
		zos.close();
		byte  [] byteArray = baos.toByteArray();
		return byteArray;
    }
	
	public static void createFileFromByteArray(byte [] byteArray,File file) throws Exception{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(byteArray);
		fos.close();
	}
	
	public static byte[] downloadFile(String file) throws Exception{
		URL obj = new URL(file);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

	
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL for file download: " + file);
		System.out.println("Response Code : " + responseCode);

		con.getInputStream();
		InputStream is = con.getInputStream();

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
	    byte[] buffer = new byte[4096];
	    int len;
	    while ((len = is.read(buffer)) > 0) {
	        baos.write(buffer, 0, len);
	    }
		byte [] byteArray = baos.toByteArray();
		baos.close();
		
		return byteArray;
	}
}
	
