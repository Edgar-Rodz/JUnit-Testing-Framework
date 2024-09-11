package unittest.runners;

import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.listeners.GUITestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FilteredTestRunner extends TestRunner {

    private List<String> testMethods;

    public FilteredTestRunner(Class testClass, List<String> testMethods) {
        super(testClass);
        this.testMethods=testMethods;
        // TODO: complete this constructor
    }
    public List<String> getList(){
        return this.testMethods;
    }
    public TestClassResult run(){
            // Creates a new instance of the test runner
            Method[] methods = getTestClass().getDeclaredMethods();
            List<Method> use = new ArrayList<>();
            for(int j = 0; j < testMethods.size(); j++){
                for (Method method : methods) {
                    String check = testMethods.get(j);
                    if (method.getName().equals(check)){
                        use.add(method);
                    }
                }
            }

            TestClassResult result = new TestClassResult(getTestClass().getName());

            for(int i =0; i < use.size(); i++) {
                try {
                    Object testInstance = getTestClass().getDeclaredConstructor().newInstance();
                    use.get(i).invoke(testInstance);
                    TestMethodResult testResult = new TestMethodResult(use.get(i).getName(), true, null);
                    result.addTestMethodResult(testResult);
                } catch (InvocationTargetException e) {
                    AssertionException assertionException = (AssertionException) e.getTargetException();
                    TestMethodResult testResult = new TestMethodResult(use.get(i).getName(), false, assertionException);
                    result.addTestMethodResult(testResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
    }

    public TestClassResult guirun(){
        // Creates a new instance of the test runner
        Method[] methods = getTestClass().getDeclaredMethods();
        List<Method> use = new ArrayList<>();
        for(int j = 0; j < testMethods.size(); j++){
            for (Method method : methods) {
                String check = testMethods.get(j);
                if (method.getName().equals(check)){
                    use.add(method);
                }
            }
        }

        TestClassResult result = new TestClassResult(getTestClass().getName());

        for(int i =0; i < use.size(); i++) {
            getEAR().testStarted(use.get(i).getName());
            try {
                Object testInstance = getTestClass().getDeclaredConstructor().newInstance();
                use.get(i).invoke(testInstance);
                TestMethodResult testResult = new TestMethodResult(use.get(i).getName(), true, null);
                getEAR().testSucceeded(testResult);
                result.addTestMethodResult(testResult);
            } catch (InvocationTargetException e) {
                AssertionException assertionException = (AssertionException) e.getTargetException();
                TestMethodResult testResult = new TestMethodResult(use.get(i).getName(), false, assertionException);
                getEAR().testFailed(testResult);
                result.addTestMethodResult(testResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // TODO: Finish implementing this class
}
