package unittest.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import unittest.annotations.Ordered;
import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.listeners.GUITestListener;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.TestRunner;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import static unittest.listeners.GUITestListener.getArea;

public class TestGUI extends Application {

    @FXML
    Button enterButton;
    @FXML
    TextField classPathText;
    @FXML
    Pane testPane;
    @FXML
    Button resetButton;
    @FXML
    Button runButton;
    @FXML
    TextArea outputArea;

    ArrayList<String> tests= new ArrayList<>();
    @Override
    public void start(Stage applicationStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));// Set up the primary stage
        applicationStage.setTitle("JavaFX Application"); // Set the title
        applicationStage.setScene(new Scene(root, 600, 500)); // Set the scene
        applicationStage.show();
    }
    @FXML
    private void initialize(){
        enterButton.setOnAction(event -> {
            String className = classPathText.getText();
            List<String> testNames = getTestNames(className);
            displayTestNames(testNames);
            getArea(outputArea);
        });
    }
    @FXML
    private void clear(){
        resetButton.setOnAction((event -> {
            outputArea.setText("");
            tests.clear();
        }));
    }
    @FXML
    private void testSelect(){
        runButton.setOnAction(event -> {
            String [] use=tests.toArray(new String[tests.size()]);
            runTests(use);
        });

    }
    private void displayTestNames(List<String> testNames){
        testPane.getChildren().clear();
        double y = 10;
        Map<String, List<CheckBox>> classMethodMap = new HashMap<>();
        for (String functionName : testNames) {
            if(!functionName.contains("$")) {//class names
                CheckBox checkBox = new CheckBox(functionName);
                checkBox.setLayoutX(10); // Set x
                checkBox.setLayoutY(y); // Set y
                y += 25; // Increment y
                testPane.getChildren().add(checkBox); // Add checkbox to the Pane
                classMethodMap.put(functionName, new ArrayList<>());
                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {// Run a function when the checkbox is checked
                        for (CheckBox methodCheckBox : classMethodMap.get(functionName)) {
                            methodCheckBox.setSelected(true);
                            tests.add(methodCheckBox.getId());
                        }
                    }
                    else{
                        for (CheckBox methodCheckBox : classMethodMap.get(functionName)) {
                            methodCheckBox.setSelected(false);
                            tests.remove(methodCheckBox.getId());
                        }
                    }
                });
            }
            else{//method names
                String temp = functionName.replace("$", "");
                int index = temp.indexOf("."); // Find the first occurrence of dot
                index = temp.indexOf(".", index + 1); // Find the second occurrence of dot
                String replacedString;
                if (index != -1) { // If the second dot is found
                    replacedString = temp.substring(0, index) + "#" + temp.substring(index + 1);
                } else {
                    replacedString = temp;
                }
                CheckBox checkBox = new CheckBox(temp);
                checkBox.setLayoutX(25); // Set x-position
                checkBox.setLayoutY(y);// Set y-position
                checkBox.setId(replacedString);
                y += 25; // Increment y-position for the next checkbox
                testPane.getChildren().add(checkBox); // Add checkbox to the Pane
                for (Map.Entry<String, List<CheckBox>> entry : classMethodMap.entrySet()) {
                    if (functionName.startsWith(entry.getKey())) {
                        entry.getValue().add(checkBox);
                        break;
                    }
                }
                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {// Run a function when the checkbox is checked
                        if(!tests.contains(replacedString)){
                            tests.add(replacedString);
                        }
                    }
                    else{
                        tests.remove(replacedString);
                    }
                });
            }
        }
    }

private List<String> getTestNames(String directoryPath) {
        List<String> testNames = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".java")) {// Check if the file is a class file
                    try {
                        String[] parts2 = directory.getName().split("\\\\"); // Split the path at each backslash
                        String packageName = parts2[parts2.length - 1];
                        String fileName = file.getName();
                        String classNameWithoutExtension = fileName.replaceFirst("[.][^.]+$", ""); // Remove the extension
                        String[] parts = fileName.split("\\."); // Split the string at the period (.)
                        String nameWithoutExtension = parts[0];
                        testNames.add(packageName+"."+nameWithoutExtension);
                        String fullyQualifiedClassName = packageName + "." + classNameWithoutExtension;
                        Class<?> clazz = Class.forName(fullyQualifiedClassName);
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(Test.class)) {
                                testNames.add(fullyQualifiedClassName+"."+method.getName()+"$");
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        System.err.println("Error loading class: " + file.getName());
                    }
                }
            }
        }

        return testNames;
    }

public void runTests(String[] testclasses) {
    // Execute the tests on a separate thread
    Thread testThread = new Thread(() -> {
        List<TestClassResult> result = new ArrayList<>();
        TestListener t1 = new GUITestListener();
        for (int i = 0; i < testclasses.length; i++) {
            try {
                String fullString = testclasses[i];
                String[] parts = fullString.split("#");
                String className = parts[0];
                Class t = Class.forName(className);

                if (testclasses[i].contains("#")) {
                    String[] half = testclasses[i].split("#");
                    String[] use = half[1].split(",");
                    List<String> list = new ArrayList<>();
                    Collections.addAll(list, use);
                    FilteredTestRunner temp3 = new FilteredTestRunner(t, list);
                    temp3.addListener(t1);
                    result.add(temp3.guirun());
                } else if (t.isAnnotationPresent(Ordered.class)) {
                    OrderedTestRunner temp2 = new OrderedTestRunner(t);
                    temp2.addListener(t1);
                    result.add(temp2.guirun());
                } else {
                    TestRunner temp = new TestRunner(t);
                    temp.addListener(t1);
                    result.add(temp.guirun());
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found");
            }
        }
        ((GUITestListener)t1).testResult();
        Platform.runLater(() -> {
        });
    });
    testThread.start();
}



    public static void main(String[] args) {
        launch(args); // Launch application
    }
}
