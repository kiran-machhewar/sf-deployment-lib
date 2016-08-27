package util;

import java.io.File;

import com.sforce.soap.metadata.*;
/**
 * Sample that logs in and shows a menu of retrieve and deploy metadata options.
 */
public class DeploymentUtil {
	
	
	    // one second in milliseconds
	    private static final long ONE_SECOND = 1000;
	
	    // maximum number of attempts to deploy the zip file
	    private static final int MAX_NUM_POLL_REQUESTS = 50;
	

		public void deployFromZipFile(File zipFilePath, DeployOptions deployOptions, MetadataConnection targetConnection) throws Exception{
			byte[] zipBytes = FileUtil.getBytesFromFile(zipFilePath);
			
			AsyncResult asyncResult = targetConnection.deploy(zipBytes, deployOptions);
	        DeployResult result = waitForDeployCompletion(asyncResult.getId(),targetConnection);
	        if (!result.isSuccess()) {
	            printErrors(result, "Final list of failures:\n");
	            throw new Exception("The files were not successfully deployed");
	        }
		}
		
		private DeployResult waitForDeployCompletion(String asyncResultId, MetadataConnection targetConnection) throws Exception {
	        int poll = 0;
	        long waitTimeMilliSecs = ONE_SECOND;
	        DeployResult deployResult;
	        boolean fetchDetails;
	        do {
	            Thread.sleep(waitTimeMilliSecs);
	            // double the wait time for the next iteration

	            waitTimeMilliSecs *= 2;
	            if (poll++ > MAX_NUM_POLL_REQUESTS) {
	                throw new Exception(
	                    "Request timed out. If this is a large set of metadata components, " +
	                    "ensure that MAX_NUM_POLL_REQUESTS is sufficient.");
	            }
	            // Fetch in-progress details once for every 3 polls
	            fetchDetails = (poll % 3 == 0);

	            deployResult = targetConnection.checkDeployStatus(asyncResultId, fetchDetails);
	            System.out.println("Status is: " + deployResult.getStatus());
	            if (!deployResult.isDone() && fetchDetails) {
	                printErrors(deployResult, "Failures for deployment in progress:\n");
	            }
	        }
	        while (!deployResult.isDone());

	        if (!deployResult.isSuccess() && deployResult.getErrorStatusCode() != null) {
	            throw new Exception(deployResult.getErrorStatusCode() + " msg: " +
	                    deployResult.getErrorMessage());
	        }
	        
	        if (!fetchDetails) {
	            // Get the final result with details if we didn't do it in the last attempt.
	            deployResult = targetConnection.checkDeployStatus(asyncResultId, true);
	        }
	        
	        return deployResult;
	    }
		
	    /*
	    * Print out any errors, if any, related to the deploy.
	    * @param result - DeployResult
	    */
	    private void printErrors(DeployResult result, String messageHeader) {
	        DeployDetails details = result.getDetails();
	        StringBuilder stringBuilder = new StringBuilder();
	        if (details != null) {
	            DeployMessage[] componentFailures = details.getComponentFailures();
	            for (DeployMessage failure : componentFailures) {
	                String loc = "(" + failure.getLineNumber() + ", " + failure.getColumnNumber();
	                if (loc.length() == 0 && !failure.getFileName().equals(failure.getFullName()))
	                {
	                    loc = "(" + failure.getFullName() + ")";
	                }
	                stringBuilder.append(failure.getFileName() + loc + ":" 
	                    + failure.getProblem()).append('\n');
	            }
	            RunTestsResult rtr = details.getRunTestResult();
	            if (rtr.getFailures() != null) {
	                for (RunTestFailure failure : rtr.getFailures()) {
	                    String n = (failure.getNamespace() == null ? "" :
	                        (failure.getNamespace() + ".")) + failure.getName();
	                    stringBuilder.append("Test failure, method: " + n + "." +
	                            failure.getMethodName() + " -- " + failure.getMessage() + 
	                            " stack " + failure.getStackTrace() + "\n\n");
	                }
	            }
	            if (rtr.getCodeCoverageWarnings() != null) {
	                for (CodeCoverageWarning ccw : rtr.getCodeCoverageWarnings()) {
	                    stringBuilder.append("Code coverage issue");
	                    if (ccw.getName() != null) {
	                        String n = (ccw.getNamespace() == null ? "" :
	                        (ccw.getNamespace() + ".")) + ccw.getName();
	                        stringBuilder.append(", class: " + n);
	                    }
	                    stringBuilder.append(" -- " + ccw.getMessage() + "\n");
	                }
	            }
	        }
	        if (stringBuilder.length() > 0) {
	            stringBuilder.insert(0, messageHeader);            
	            System.out.println(stringBuilder.toString());
	        }
	    }
		
    
}