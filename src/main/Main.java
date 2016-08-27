package main;

import java.io.File;

import util.DeploymentUtil;
import util.FileUtil;
import util.MetadataLoginUtil;

import com.sforce.soap.metadata.DeployOptions;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.TestLevel;
import com.sforce.ws.ConnectionException;


public class Main {
	
	
	public static void main(String[] args) {
		try {
			//testZipFileDeployment();
			testGithubToSFDeploy();
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
	

}
