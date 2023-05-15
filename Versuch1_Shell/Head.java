import cTools.KernelWrapper;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static cTools.KernelWrapper.*;

class Head {

    public String prompt = "Head: type 'head --help' for help";
    public final String[] PATH = System.getenv("PATH").split(":");
    public Boolean close = true;
    public byte[] buffer = new byte[50];

    public Head() {
        while (close) {
            // Input into String
            String filePath;
            String[] input = inputLine();
            if (input[0].equals("close")) {
                close = false;
            }
            else {
                if (input[0].equals("close")) {
                    close = false;
                }
                if(input[0].equals("head")) {
                    int fd = 0;
                    int rd = 0;
                    int cl = 0;
                }
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
                                    System.out.println("-c, --bytes NUM: print the first NUM bytes of each file");
                                    System.out.println("-n, --lines NUM: print the first NUM lines instead of the first 10");
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
        }   
    }

    // Input and split words in array
    // -------------------------------------------------------------------------------------------------------
    public String[] inputLine() {
        System.out.println();
        System.out.println(prompt);

        Scanner inputScanner = new Scanner(System.in);
        return inputScanner.nextLine().split(" ");
    }
    // -------------------------------------------------------------------------------------------------------

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
            System.out.println("ERROR: More than 1 File fitting the name.");
        }

        return null;
    }

    // ----------------------------------------------------------------------------
    public static void main(String[] args) {

        Head h = new Head();
    
        System.out.println("Leaving Head.");
        System.exit(0);
    }
    // ----------------------------------------------------------------------------
}