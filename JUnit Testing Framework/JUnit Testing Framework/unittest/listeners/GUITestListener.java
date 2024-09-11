package unittest.listeners;

import javafx.scene.control.TextArea;
import unittest.assertions.AssertionException;
import unittest.results.TestMethodResult;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GUITestListener implements TestListener {

    public int totaltests = 0;
    public int sucesses =0;
    public int f =0;

    public void testResult(){
        outArea.appendText("Tests Run: "+ totaltests+" Tests Passed: "+sucesses+ " Tests Failed: "+f+"\n");
        totaltests=0;
        sucesses=0;
        f=0;
    }

    // Call this method right before the test method starts running

    static volatile TextArea outArea = new TextArea();
    @Override
    public void testStarted(String testMethod) {
        totaltests++;
        outArea.appendText("Running "+testMethod);
        outArea.appendText("\n");
        try {
            Thread.sleep(500);
        }
        catch(Exception e) {}
    }

    // Call this method right after the test method finished running successfully
    @Override
    public void testSucceeded(TestMethodResult testMethodResult) {
        if (testMethodResult.isPass()){
            sucesses++;
            outArea.appendText(testMethodResult.getName()+" Passed!");
            outArea.appendText("\n");
        }
        try {
            Thread.sleep(500);
        }
        catch(Exception e) {}
    }

    // Call this method right after the test method finished running and failed
    @Override
    public void testFailed(TestMethodResult testMethodResult) {
        if (!testMethodResult.isPass()){
            f++;
            outArea.appendText(testMethodResult.getName()+" Failed!");
            outArea.appendText("\n");
            AssertionException use=testMethodResult.getException();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            use.printStackTrace(pw);
            String exceptionText = sw.toString();
            outArea.appendText(exceptionText);
        }
        try {
            Thread.sleep(500);
        }
        catch(Exception e) {}
    }

    public static void getArea(TextArea t){
        outArea=t;
    }
}
