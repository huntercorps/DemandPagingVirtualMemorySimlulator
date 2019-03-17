package commandline;

import java.io.IOException;
import java.util.Scanner;

/**
 * The interface Runner.
 */
public interface Runner {

  /**
   * Run.
   */
  void run();

  /**
   * Show main menu.
   */
  void showMainMenu();

  /**
   * Read reference string from user input.
   */
  void readReferenceString();

  /**
   * Generate reference string.
   */
  void generateReferenceString();

  /**
   * Displays current reference string
   */
  void displayCurrentReferenceString();

  /**
   * checks if reference string is set.
   * @return true if set.
   */
  boolean isReferenceStringSet();
  
  /*   ----Default Methods----- */

  /**
   * Helper method to parse ints from strings
   * @param str , the string to convert.
   * @return int value of string.
   * @throws InvalidInputException
   */
  default int parseAsInt(String str) throws InvalidInputException {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException ex) {
      throw new InvalidInputException("Invalid Input. Input must be an Integer");
    }
  }

  /**
   * Gets user input w/ scanner
   * @return
   */
  default String getUserInput() {
    Scanner scan = new Scanner(System.in);
    return scan.nextLine().trim();
  }

  /**
   * Prints string in red text. Uses ANSI Color.
   * Not supported by windows.
   * @param str to print
   */
  default void printErrorText(String str) {
    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
      System.out.println("Error: "+str);
    } else {
      System.out.println("\u001B[31m"+str+"\u001B[0m");
    }
  }

  /*   ----inner Class----- */

  /**
   * The type Invalid input exception.
   */
  class InvalidInputException extends IOException {
    public InvalidInputException() {
      super("Input not recognized");
    }

    public InvalidInputException(String message) {
    }
  }

}
