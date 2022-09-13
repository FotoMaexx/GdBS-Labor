import cTools.KernelWrapper;
import java.io.File;
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
    while(close) {
      // Input into String
      String[] input = inputLine();
      if(input[0].equals("close") || input[0].equals("exit")) {
        close = false;
      }
      if(close) {
        String filePath = checkInput(input[0]);

        if(filePath != null) {
          System.out.println("Full path: " + filePath);
          processing(input, filePath);
        }
        else {
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
  public String checkInput(String input) {

    File file = new File(input);

    if(file.exists()) {
      return file.getAbsolutePath();
    }

    for(int i = 0; i < PATH.length; i++) {
      file = new File(PATH[i] + "/" + input);
      if(file.exists()) {

        return file.getAbsolutePath();
      }
      else {
        continue;
      }
    }
    return null;
  }

  public void processing(String[] commands, String filePath) {
    int childPID = fork();

    if(childPID == 0) {
      System.out.print("Running execv for PID: " + childPID + "  ");
      execv(filePath, commands);
      exit(0);
    }
    else if(childPID > 0){
      int[] status = new int[1];
      System.out.println("Running waitpid for PID: " + childPID + "  ");
      waitpid(childPID, status, 0);

    }
    else {
      System.out.println("Fork didn't work.");

    }
  }

  public static void main(String[] args) {

    MiniShell s = new MiniShell();

    System.out.println("Leaving MiniShell.");
  }
}
