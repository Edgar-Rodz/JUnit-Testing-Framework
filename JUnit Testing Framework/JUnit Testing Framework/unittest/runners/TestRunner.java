package unittest.runners;

import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.listeners.GUITestListener;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestRunner {
    private final Class testClass;
    private TestListener EAR;
    public TestRunner(Class testClass) {
//        General Constructor
        this.testClass = testClass;
    }
    public Class getTestClass(){
        return testClass;
    }

    public TestListener getEAR(){
        return EAR;
    }

    public TestClassResult run() {
        // Creates a new instance of the test runner
        Method[] methods = testClass.getDeclaredMethods();
        for (int i = 0; i < methods.length - 1; i++) {
            for (int j = i + 1; j < methods.length; j++) {
                if (methods[i].getName().compareTo(methods[j].getName()) > 0) {
                    Method temp = methods[i];
                    methods[i] = methods[j];
                    methods[j] = temp;
                }
            }
        }
        TestClassResult result = new TestClassResult(testClass.getName());

        for(Method m : methods) {
            try {
                if (m.isAnnotationPresent(Test.class)) {
                    Object testInstance = testClass.getDeclaredConstructor().newInstance();
                    m.invoke(testInstance);
                    TestMethodResult testResult = new TestMethodResult(m.getName(), true, null);
                    result.addTestMethodResult(testResult);
                }
            } catch (InvocationTargetException e) {
                AssertionException assertionException = (AssertionException) e.getTargetException();
                TestMethodResult testResult = new TestMethodResult(m.getName(), false, assertionException);
                result.addTestMethodResult(testResult);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public TestClassResult guirun() {
        // Creates a new instance of the test runner
        Method[] methods = testClass.getDeclaredMethods();
        for (int i = 0; i < methods.length - 1; i++) {
            for (int j = i + 1; j < methods.length; j++) {
                if (methods[i].getName().compareTo(methods[j].getName()) > 0) {
                    Method temp = methods[i];
                    methods[i] = methods[j];
                    methods[j] = temp;
                }
            }
        }
        TestClassResult result = new TestClassResult(testClass.getName());

        for(Method m : methods) {
            getEAR().testStarted(m.getName());
            try {
                if (m.isAnnotationPresent(Test.class)) {
                    Object testInstance = testClass.getDeclaredConstructor().newInstance();
                    m.invoke(testInstance);
                    TestMethodResult testResult = new TestMethodResult(m.getName(), true, null);
                    getEAR().testSucceeded(testResult);
                    result.addTestMethodResult(testResult);
                }
            } catch (InvocationTargetException e) {
                AssertionException assertionException = (AssertionException) e.getTargetException();
                TestMethodResult testResult = new TestMethodResult(m.getName(), false, assertionException);
                getEAR().testFailed(testResult);
                result.addTestMethodResult(testResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void addListener(TestListener listener) {
        this.EAR = listener;

        // Do NOT implement this method for Assignment 4
        // You will implement this for Assignment 5
        // Do NOT remove this method

    }
}
