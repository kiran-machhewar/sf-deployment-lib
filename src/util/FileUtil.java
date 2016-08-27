package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	public static void main(String[] args) {
		try {
			createZipFileFromAFolderOfZipFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void createZipFileFromAFolderOfZipFile() throws Exception{
		
    	unZipIt("D:\\Returns\\SFChangeSetTool-master.zip","OT");
	}
	
	   /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public static void unZipIt(String zipFile, String outputFolder){

     byte[] buffer = new byte[1024];

     try{

    	//create output directory is not exists
    	File folder = new File("output.zip");
    	if(!folder.exists()){
    		folder.mkdir();
    	}

    	//get the zip file content
    	ZipInputStream zis =
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();

    	while(ze!=null){
    		System.out.println("ze name-->"+ze.getName()+"---"+ze.isDirectory());
    		
    		if(ze.getName().contains("/src/") && ze.isDirectory()){
    			break;
    		}
    		ze = zis.getNextEntry();
    	}
    	while(ze!=null){

    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);

           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
    	}

        zis.closeEntry();
    	zis.close();

    	System.out.println("Done");

    }catch(IOException ex){
       ex.printStackTrace();
    }
   }
}
	
