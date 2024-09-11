package unittest.driver;

import unittest.annotations.Order;
import unittest.annotations.Ordered;
import unittest.assertions.AssertionException;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.TestRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDriver {

    /**
     * Execute the specified test classes, returning the results of running
     * each test class, in the order given.
     */
    public static List<TestClassResult> runTests(String[] testclasses) {
        // TODO: complete this method
        List<TestClassResult> result = new ArrayList<TestClassResult>();
        for(int i =0;i<testclasses.length;i++){
            try {
                String fullString = testclasses[i];
                String[] parts = fullString.split("#");
                String className = parts[0];
                Class t = Class.forName(className);

                if(testclasses[i].contains("#")){
                    String[] half = testclasses[i].split("#");
                    String[] use = half[1].split(",");
                    List<String> list = new ArrayList<>();
                    Collections.addAll(list, use);
                    FilteredTestRunner temp3 = new FilteredTestRunner(t,list);
                    result.add(temp3.run());
                }
                else if(t.isAnnotationPresent(Ordered.class)){
                        OrderedTestRunner temp2 = new OrderedTestRunner(t);
                        result.add(temp2.run());
                }
                else{
                    TestRunner temp = new TestRunner(t);
                    result.add(temp.run());
                }
            }
            catch(ClassNotFoundException e){
                System.err.println("Class not found");
            }
        }
        int counter=0;
        if (result.isEmpty()){
            return result;
        }
        for(TestClassResult t: result){
            for(TestMethodResult t2: t.getTestMethodResults()) {
                if (t2.isPass()) {
                    System.out.println(t.getTestClassName() + "." + t2.getName() + " : PASS");
                } else {
                    System.out.println(t.getTestClassName() + "." + t2.getName() + " : FAIL");
                }
                counter++;
            }
        }
        System.out.println("==========");
        System.out.println("FAILURES:");
        int failcount =0;
        for(TestClassResult t:result){
            for(TestMethodResult m:t.getTestMethodResults()){
                if(!m.isPass()) {
                    System.out.println(t.getTestClassName() + "." + m.getName() + ":");
                    AssertionException use = m.getException();
                    use.printStackTrace();
                    failcount++;
                }
            }
        }
        System.out.println("==========");
        System.out.println("Tests run: "+ counter +", Failures: "+ failcount);
        return result;
        // We will call this method from our JUnit test cases.
    }

    public static void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        String[] use = {"sampletest.TestB","sampletest.TestC#test4,test6"};
        runTests(use);
    }
}
