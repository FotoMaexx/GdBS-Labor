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

class MiniShell {

    // Variables
    public String prompt = "Eingabe: ";
    public final String[] PATH = System.getenv("PATH").split(":");
    public Boolean close = true;

    public MiniShell() {
        while (close) {
            // Input into String
            String filePath;
            String[] input = inputLine();
            if (input[0].equals("close") || input[0].equals("exit")) {
                close = false;
            }
            if (close) {
                if(input[0].equals("cat")) {
                    if(input.length > 1) {
                        for(int i=1; i<input.length; i++){
                            if(input[i].equals("<") || input[i].equals("-")) {
                                stdin(input, i);
                            }
                            else if(input[i].equals(">")) {     
                                stdout(input, i);
                                i = input.length - 1;
                            }
                            else {
                                if(i < input.length - 1) {
                                    if(input[i+1].equals(">")) {

                                    }
                                    else {
                                        if(input[i-1].equals("cat")) {
                                            filePath = checkFile(input, i);

                                            if (filePath != null) {
                                                System.out.println("Full path: " + filePath);
                                                processing(input, filePath);
                                            } else {
                                                System.out.println("Error. No such Path");
                                            }
                                        }
                                    }
                                }
                                else {
                                    if(input[i-1].equals("cat")) {
                                        filePath = checkFile(input, i);

                                        if (filePath != null) {
                                            System.out.println("Full path: " + filePath);
                                            processing(input, filePath);
                                        } else {
                                            System.out.println("Error. No such Path");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    filePath = checkFile(input, 0);

                    if (filePath != null) {
                        System.out.println("Full path: " + filePath);
                        processing(input, filePath);
                    } else {
                    System.out.println("Error. No such Path");
                    }
                }
            }
        }
    }

    // Input and split
    public String[] inputLine() {
        System.out.println(prompt);

        Scanner input = new Scanner(System.in);
        return input.nextLine().split(" ");
    }

    // Checks input
    public String checkFile(String[] input, int field) {

        File file = new File(input[field]);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        for (int i = 0; i < PATH.length; i++) {
            file = new File(PATH[i] + "/" + input[field]);
            if (file.exists()) {
                return file.getAbsolutePath();
            } else {
                continue;
            }
        }

        // Actual directory
        String startingdir = System.getProperty("user.dir");

        String pattern = input[field];
        
        // replacing * with .* for regex
        if (pattern.contains("*")) {
            String[] parts = pattern.split("(?=\\*)");
            String part1 = parts[0];
            String part2 = parts[1];
            part1 = part1 + ".";

            pattern = part1 + part2;
        }

        // replacing ? with . for regex
        if (pattern.contains("?")) {
            String p = pattern.replace('?', '.');
            pattern = p;
        }

        String filenameprefix = pattern;
        File startingDirFile = new File(startingdir);
        final File[] listFiles = startingDirFile.listFiles(new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                return arg1.matches(filenameprefix);
            }
        });

        if(listFiles.length == 1) {
            return listFiles[0].getAbsolutePath();
        }

        if (listFiles.length > 0) {
            System.out.println("More than 1 File: " +Arrays.toString(listFiles));
        }

        return null;
    }
    

    // Standard-Input
    public void stdin(String[] in, int pos) {
        if(pos < in.length - 2) {
            if(in[pos+2].equals(">")) {
                return;
            }
        }
        if(pos - 2 > 0) {
            if((in[pos-2].equals(">"))) {
                return;
            }
        }
        if(pos < in.length - 1 && !(in[pos+1].equals("-")) && !(in[pos+1].equals(">")) && !(in[pos+1].equals("<"))) {
            String filePath = checkFile(in, pos+1);
        
            if (filePath != null) {
                System.out.println("Full path: " + filePath);
                processing(in, filePath);
            } else {
                System.out.println("Error. No such Path");
            }
        }
    }

    // Standard-Output
    public void stdout(String[] in, int pos) {
        
        if(pos < in.length - 1 && !(in[pos+1].equals("-")) && !(in[pos+1].equals(">")) && !(in[pos+1].equals("<"))) {
            
            if( pos+3 < in.length ) {
                if(in[pos+2].equals("<")) {
                    String filePath = checkFile(in, pos+3);
                    String[] parts = filePath.split("/");
                    String newFilePath = "";
            
                    for(int i=0; i<parts.length-1;i++) {
                        newFilePath = newFilePath + parts[i] + "/";
                    }
                    newFilePath = newFilePath + in[pos+1];
                    copy(filePath, newFilePath);
                    System.out.println("Copied " + filePath + " to " + newFilePath);
                    return;
                }
            }
            
            if(!(in[pos-1].equals("cat"))) {
                String filePath = checkFile(in, pos-1);
                String[] parts = filePath.split("/");
                String newFilePath = "";
            
                for(int i=0; i<parts.length-1;i++) {
                    newFilePath = newFilePath + parts[i] + "/";
                }
                newFilePath = newFilePath + in[pos+1];
                copy(filePath, newFilePath);
                System.out.println("Copied " + filePath + " to " + newFilePath);
            } 
        }
    }

    // Copy
    public static void copy(String oP, String nP) {
        File from = new File(oP);
        File to = new File(nP);
 
        try {
            copyFile(from, to);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private static void copyFile(File src, File dest) throws IOException {
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    // Routine
    public void processing(String[] commands, String filePath) {
        int childPID = fork();

        if (childPID == 0) {
            System.out.print("Running execv for PID: " + childPID + "  ");
            execv(filePath, commands);
            exit(0);
        } else if (childPID > 0) {
            int[] status = new int[1];
            System.out.println("Running waitpid for PID: " + childPID + "  ");
            waitpid(childPID, status, 0);

        } else {
            System.out.println("Fork didn't work.");

        }
    }

    public static void main(String[] args) {

        MiniShell s = new MiniShell();

        System.out.println("Leaving MiniShell.");
    }
}
