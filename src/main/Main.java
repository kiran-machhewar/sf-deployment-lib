package main;

import java.io.File;

import util.DeploymentUtil;
import util.FileUtil;
import util.MailUtil;
import util.MetadataLoginUtil;

import com.sforce.soap.metadata.DeployOptions;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.TestLevel;


public class Main {
	
	
	public static void main(String[] args) {
		try {
			//testZipFileDeployment();
			//testGithubToSFDeploy();
			testMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void testZipFileDeployment() throws Exception{
		DeployOptions deployOptions = new DeployOptions();
        deployOptions.setPerformRetrieve(false);
        deployOptions.setRollbackOnError(true);
        deployOptions.setCheckOnly(false);          
        deployOptions.setTestLevel(TestLevel.NoTestRun);
        MetadataConnection targetConnection = MetadataLoginUtil.getMetadataConnection("kiran_machhewar@psl.com", "", "Production");
        DeploymentUtil deploymentUtil = new DeploymentUtil();
        deploymentUtil.deployFromZipFile(new File("D:\\Returns\\SFChangeSetTool-master.zip"), deployOptions, targetConnection);
	}
	
	public static void testGithubToSFDeploy() throws Exception{
		DeployOptions deployOptions = new DeployOptions();
        deployOptions.setPerformRetrieve(false);
        deployOptions.setRollbackOnError(true);
        deployOptions.setCheckOnly(false);          
        deployOptions.setTestLevel(TestLevel.NoTestRun);
        MetadataConnection targetConnection = MetadataLoginUtil.getMetadataConnection("kiran_machhewar@psl.com", "", "Production");
        DeploymentUtil deploymentUtil = new DeploymentUtil();
        byte []zipData = FileUtil.downloadFile("https://github.com/kiran-machhewar/SFChangeSetTool/archive/master.zip");
        FileUtil.createFileFromByteArray(zipData,new File("Download.zip"));
        zipData = FileUtil.processZipToKeepSrcFolderOnly(zipData);
        FileUtil.createFileFromByteArray(zipData,new File("Clean.zip"));
        deploymentUtil.deployFromZipByteArrayData(zipData, deployOptions, targetConnection);
	}
	
	public static void testMail() {
		System.out.println(System.getenv("GMAIL_USERNAME"));
		System.out.println(System.getenv("GMAIL_PASSWORD"));
		MailUtil mailUtil = new MailUtil(System.getenv("GMAIL_USERNAME"), System.getenv("GMAIL_PASSWORD"));
		String[] to = { "smachhewar@gmail.com" }; // list of recipient email addresses
		mailUtil.sendFromGMail(to,"Test Mail Subject","BOdy of the email");
		System.out.println("Mail si Sent");
	}
	

}
