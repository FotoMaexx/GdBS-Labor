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
    public byte[] buffer = new byte[50];

    public MiniShell() {
        while (close) {
            // Input into String
            String filePath;
            String[] input = inputLine();
            if (input[0].equals("close") || input[0].equals("exit")) {
                close = false;
            }
            if (close) {
                // CAT - Case
                // -------------------------------------------------------------------------------------------------------
                if(input[0].equals("cat")) {
                    if(input.length > 1) {
                        for(int i=1; i<input.length; i++){
                            if(input[i].equals("<") || input[i].equals("-")) {
                                catProcessing(input, i);
                            }
                            else if(input[i].equals(">")) {     
                                catCopy(input, i);
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
                                                head(0, filePath, 10);
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
                                            head(0, filePath, 10);
                                        } else {
                                            System.out.println("Error. No such Path");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // -------------------------------------------------------------------------------------------------------

                // HEAD - Case
                // -------------------------------------------------------------------------------------------------------
                else if(input[0].equals("head")) {
                    int fd = 0;
                    int rd = 0;
                    int cl = 0;
                    if(input.length > 1) {
                        for(int i=1; i<input.length; i++){
                            if(i == 1) {
                                if (input[1].charAt(0) == '-') {
                                    if(input[1].equals("--help")) {
                                        System.out.println("SYNOPSIS:");
                                        System.out.println("head [OPTION]... [FILE]...");
                                        System.out.println("example: head -c --bytes 5 <filename>");
                                        System.out.println("");
                                        System.out.println("DESCRIPTION:");
                                        System.out.println("Print the first 10 lines of each FILE to standard output.  With more than one FILE, precede each with a header giving the file name.");
                                        System.out.println("");
                                        System.out.println("OPTIONS:");
                                        System.out.println("-c, --bytes NUM: print the first NUM bytes of each file; with the leading '-', print all but the last NUM bytes of each file");
                                        System.out.println("-n, --lines NUM: print the first NUM lines instead of the first 10; with the leading '-', print all but the last NUM lines of each file");
                                        i=input.length;
                                    }
                                    else {
                                        i++;
                                        i++;
                                    }
                                }
                                else {
                                    filePath = checkFile(input, i);
                                    head(0, filePath, 10);
                                }
                            }
                            else {
                                if (input[1].charAt(0) == '-' && i>3) {
                                    if(input[1].equals("-c")) {
                                        if(input[2].equals("--bytes")) {
                                            filePath = checkFile(input, i);
                                            head(1, filePath, Integer.parseInt(input[3]));
                                        }
                                    }
                                    else if(input[1].equals("-n")) {
                                        if(input[2].equals("--lines")) {
                                            filePath = checkFile(input, i);
                                            head(2, filePath, Integer.parseInt(input[3]));
                                        }
                                    }
                                    else{
                                        System.out.println("Syntax error.");
                                    }
                                }
                                else {
                                    filePath = checkFile(input, i);
                                    head(0, filePath, 10);
                                }
                            }
                        }
                    }
                }
                // -------------------------------------------------------------------------------------------------------

                // CASE: erstes Wort ist kein Command
                // -------------------------------------------------------------------------------------------------------
                else {
                    filePath = checkFile(input, 0);

                    if (filePath != null) {
                        System.out.println("Full path: " + filePath);
                        processing(input, filePath);
                    } else {
                    System.out.println("Error. No such Path");
                    }
                }
                // -------------------------------------------------------------------------------------------------------
            }
        }
    }

    // Head - Routine
    // -------------------------------------------------------------------------------------------------------
    public void head(int opt, String path, int val) {
        int fd = 0;
        int rd = 0;
        int cl = 0;
        int check = 0;
        byte a = (byte) 10;

        if(opt == 0) {
            fd = open(path, O_RDWR);
            rd = read(fd, buffer, buffer.length-1);
            cl = close(fd);
            for(int k=0; k<buffer.length && check<10; k++){
                if (buffer[k] == a) {
                    check++;
                }
                System.out.print((char)buffer[k]);
            }
        }
        else if(opt == 1) {
            fd = open(path, O_RDWR);
            rd = read(fd, buffer, buffer.length-1);
            cl = close(fd);
            for(int k=0; k<val; k++){
                System.out.print((char)buffer[k]);
            }
            System.out.println();
        }
        else if(opt == 2) {
            fd = open(path, O_RDWR);
            rd = read(fd, buffer, buffer.length-1);
            cl = close(fd);
            for(int k=0; k<buffer.length && check<val; k++){
                if (buffer[k] == a) {
                    check++;
                }
                System.out.print((char)buffer[k]);
            }
        }
    }
    // -------------------------------------------------------------------------------------------------------

    // Input and split words in array
    // -------------------------------------------------------------------------------------------------------
    public String[] inputLine() {
        System.out.println();
        System.out.println(prompt);

        Scanner input = new Scanner(System.in);
        return input.nextLine().split(" ");
    }
    // -------------------------------------------------------------------------------------------------------

    // Checks input
    // -------------------------------------------------------------------------------------------------------
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
        
        // Get actual directory
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
    // -------------------------------------------------------------------------------------------------------

    // Cat-Verarbeitung
    // -------------------------------------------------------------------------------------------------------
    public void catProcessing(String[] in, int pos) {
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
                head(0, filePath, 10);
            } else {
                System.out.println("Error. No such Path");
            }
        }
    }
    // -------------------------------------------------------------------------------------------------------

    // Cat ">"-Case 
    // -------------------------------------------------------------------------------------------------------
    public void catCopy(String[] in, int pos) {
        
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
    // -------------------------------------------------------------------------------------------------------

    // Copy File
    // -------------------------------------------------------------------------------------------------------
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
    // -------------------------------------------------------------------------------------------------------

    // Running File with execv/waitpid/fork
    // -------------------------------------------------------------------------------------------------------
    public void processing(String[] commands, String filePath) {
        int childPID = fork();

        if (childPID > 0) {
            int[] status = new int[1];
            System.out.println("Running waitpid for PID: " + childPID + "  ");
            waitpid(childPID, status, 0);
        } else if (childPID == 0) {
                System.out.print("Running execv for PID: " + childPID + "  ");
                execv(filePath, commands);
                exit(0);
        } else {
            System.out.println("Fork didn't work.");

        }
    }
    // -------------------------------------------------------------------------------------------------------
    
    // MAIN
    // -------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

        MiniShell s = new MiniShell();

        System.out.println("Leaving MiniShell.");
    }
    // -------------------------------------------------------------------------------------------------------
}