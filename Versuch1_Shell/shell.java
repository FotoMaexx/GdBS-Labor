import cTools.KernelWrapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardCopyOption.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import static cTools.KernelWrapper.*;

class Shell {

    public String prompt = "Eingabe: ";
    public final String[] PATH = System.getenv("PATH").split(":");
    public Boolean close = true;

    // ----------------------------------------------------------------------------
    // Main Shell Function
    // ----------------------------------------------------------------------------
    public Shell() {
        while (close) {
            // Input into String
            String[] input = inputLine();
            if (input[0].equals("close") || input[0].equals("exit")) {
                close = false;
            }
            else {
                String filePath = checkInput(input[0]);
                if(filePath != null) {
                    ArrayList<String> leftCommands = pipes(input);
                    System.err.println(leftCommands.toString());

                    String stdout = leftCommands.remove(leftCommands.size()-1);
                    String stdin = leftCommands.remove(leftCommands.size()-1);

                    input = leftCommands.toArray(new String[0]);

                    processing(input, filePath, stdin, stdout);
                }
                else {
                    System.out.println("ERROR: No such Path!");
                }
            }
        }
    }
    // ----------------------------------------------------------------------------
    
    // ----------------------------------------------------------------------------
    // Pipes Arraylist for ">" and "<" (Aufgabe 2)
    // ----------------------------------------------------------------------------
    public ArrayList<String> pipes(String[] input) {
        String stdIn = null;
        String stdOut = null;
        ArrayList<String> paramsLeft = new ArrayList<String>(input.length);

        for (int i = 0; i < input.length; i++) {
            if(input[i].contains("<")) {
                if (i + 1 < input.length) {
                    stdIn = input[i + 1];
                    i++;
                }
            } else if(input[i].contains(">")) {
                if(i + 1 < input.length) {
                    stdOut = input[i+1];
                    i++;
                }
            }
            else {
                paramsLeft.add(input[i]);
            }


        }

        paramsLeft.add(stdIn);
        paramsLeft.add(stdOut);
        return paramsLeft;
    }
    // ----------------------------------------------------------------------------

    // ----------------------------------------------------------------------------
    // Input Line String 
    // ----------------------------------------------------------------------------
    public String[] inputLine() {
        System.out.println();
        System.out.println(prompt);

        Scanner input = new Scanner(System.in);
        return input.nextLine().split(" ");
    }
    // ----------------------------------------------------------------------------

    // ----------------------------------------------------------------------------
    // Check Input Path
    // ----------------------------------------------------------------------------
    public String checkInput(String filePath) {
    
        File file = new File(filePath);
    
        if(file.exists()) {
            return file.getAbsolutePath();
        }
    
    
        for(int i = 0; i < PATH.length; i++) {
            file = new File(PATH[i] + "/" + filePath);
            if(file.exists()) {
                return file.getAbsolutePath();
            }
            else {
                continue;
            }}
        return null;
    }
    // ----------------------------------------------------------------------------

    // ----------------------------------------------------------------------------
    // Processing Routine
    // ----------------------------------------------------------------------------
    public void processing(String[] commands, String filePath, String stdin, String stdout) {
        //PID of Child
        int childPID = fork();
        //Child-Process
        if(childPID == 0) {
            if(stdin != null) {
                System.err.println("STDIN");
                close(STDIN_FILENO);
                open(stdin, O_RDONLY);
            }
            if(stdout != null) {
                System.err.println("STDOUT");
                close(STDOUT_FILENO);
                open(stdout, O_RDWR  | O_CREAT);
            }
            execv(filePath, commands);
        }
        else if(childPID > 0){
            //Parent-Process
            int[] status = new int[1];
            waitpid(childPID, status, 0);
        }
        else {
            System.err.println("ERROR: Fork failed!");
        }
    }
    // ----------------------------------------------------------------------------

    public static void main(String[] args) {

        Shell s = new Shell();

        System.out.println("Leaving Shell.");
    }
}