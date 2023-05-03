import cTools.KernelWrapper;

import java.io.File;
import java.io.FilenameFilter;
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
            String[] input = inputLine();
            if (input[0].equals("close") || input[0].equals("exit")) {
                close = false;
            }
            if (close) {
                String filePath = checkInput(input);

                if (filePath != null) {
                    System.out.println("Full path: " + filePath);
                    processing(input, filePath);
                } else {
                    System.out.println("Error. No such Path");
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

    // Checks first word in input for data
    public String checkInput(String[] input) {

        File file = new File(input[0]);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        for (int i = 0; i < PATH.length; i++) {
            file = new File(PATH[i] + "/" + input[0]);
            if (file.exists()) {
                return file.getAbsolutePath();
            } else {
                continue;
            }
        }

        // Actual directory
        String startingdir = System.getProperty("user.dir");

        String pattern = input[0];
        
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
