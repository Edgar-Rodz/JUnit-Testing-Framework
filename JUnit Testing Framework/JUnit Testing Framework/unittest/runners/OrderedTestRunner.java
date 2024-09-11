package unittest.runners;

import unittest.annotations.Order;
import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.listeners.GUITestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OrderedTestRunner extends TestRunner {

    public OrderedTestRunner(Class testClass) {
        super(testClass);
        // TODO: complete this constructor
    }
    public TestClassResult run() {

        Method[] methods = getTestClass().getDeclaredMethods();
        List<Method> om = new ArrayList<>();
        List<Method> nm = new ArrayList<>();

        for (int i = 0; i < methods.length - 1; i++) {//sorts all methods alphabetically first
            for (int j = i + 1; j < methods.length; j++) {
                if (methods[i].getName().compareTo(methods[j].getName()) > 0) {
                    Method temp = methods[i];
                    methods[i] = methods[j];
                    methods[j] = temp;
                }
            }
        }

        for (Method m : methods) {//puts methods into two ArrayLists if there is order or not.
            if (m.isAnnotationPresent(Order.class)) {
                om.add(m);
            } else {
                nm.add(m);
            }
        }
        for (int i = 0; i < om.size() - 1; i++) { //sorts by Order priority
            for (int j = i + 1; j < om.size(); j++) {
                int o1 = om.get(i).getAnnotation(Order.class).value();
                int o2 = om.get(j).getAnnotation(Order.class).value();
                if (o1 > o2) {
                    Method temp = om.get(i);
                    om.set(i, om.get(j));
                    om.set(j, temp);
                }
            }
        }
        List<Method> combined = new ArrayList<>(om); //combined om and nm
        combined.addAll(nm);
        Method[] organized = new Method[methods.length]; //organized has everything ordered
        organized = combined.toArray(organized);
        TestClassResult result = new TestClassResult(getTestClass().getName());

        for (Method m : organized) {
            try {
                Object testInstance = getTestClass().getDeclaredConstructor().newInstance();
                m.invoke(testInstance);
                TestMethodResult testResult = new TestMethodResult(m.getName(), true, null);
                result.addTestMethodResult(testResult);

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

        Method[] methods = getTestClass().getDeclaredMethods();
        List<Method> om = new ArrayList<>();
        List<Method> nm = new ArrayList<>();

        for (int i = 0; i < methods.length - 1; i++) {//sorts all methods alphabetically first
            for (int j = i + 1; j < methods.length; j++) {
                if (methods[i].getName().compareTo(methods[j].getName()) > 0) {
                    Method temp = methods[i];
                    methods[i] = methods[j];
                    methods[j] = temp;
                }
            }
        }

        for (Method m : methods) {//puts methods into two ArrayLists if there is order or not.
            if (m.isAnnotationPresent(Order.class)) {
                om.add(m);
            } else {
                nm.add(m);
            }
        }
        for (int i = 0; i < om.size() - 1; i++) { //sorts by Order priority
            for (int j = i + 1; j < om.size(); j++) {
                int o1 = om.get(i).getAnnotation(Order.class).value();
                int o2 = om.get(j).getAnnotation(Order.class).value();
                if (o1 > o2) {
                    Method temp = om.get(i);
                    om.set(i, om.get(j));
                    om.set(j, temp);
                }
            }
        }
        List<Method> combined = new ArrayList<>(om); //combined om and nm
        combined.addAll(nm);
        Method[] organized = new Method[methods.length]; //organized has everything ordered
        organized = combined.toArray(organized);
        TestClassResult result = new TestClassResult(getTestClass().getName());

        for (Method m : organized) {
            getEAR().testStarted(m.getName());
            try {
                Object testInstance = getTestClass().getDeclaredConstructor().newInstance();
                m.invoke(testInstance);
                TestMethodResult testResult = new TestMethodResult(m.getName(), true, null);
                getEAR().testSucceeded(testResult);
                result.addTestMethodResult(testResult);

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
}

    // TODO: Finish implementing this class
