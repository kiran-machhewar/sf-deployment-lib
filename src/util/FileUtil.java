package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

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
}
