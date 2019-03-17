package commandline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import simulation.*;

/**
 * The type Command line runner.
 */
public class CommandLineRunner implements Runner {

  private Integer memoryCapacity = 4;
  private List<String> referenceList = new ArrayList<>();

  /**
   * Instantiates a new Command line runner.
   *
   * @param args the args if valid memory capacity is set to arg value
   */
  public CommandLineRunner(String[] args) {
    if (args.length > 0) {
      try {
        int input = parseAsInt(args[0]);
        if (input < 10 && input > 0) {
          this.memoryCapacity = input;
        }
        else {
          printErrorText("Physical frames set to default '4' frames.");
        }
      } catch (InvalidInputException e) {
        e.printStackTrace();
      }
      
    } else {
      printErrorText("Physical frames set to default '4' frames.");
    }
  }

  /**
   * The main program loop
   */
  @Override
  public void run() {
    while (true) {
      showMainMenu();
      switch (getUserInput()) {
        case "0":
          //Will exit the program
          System.out.print("Program exited");
          System.exit(0);
        case "1":
          readReferenceString();
          break;
        case "2":
          generateReferenceString();
          break;
        case "3":
          if (isReferenceStringSet()) {
            displayCurrentReferenceString();
          }
          break;
        case "4":
          if (isReferenceStringSet()) {
            new FIFO_Algorithm(memoryCapacity,referenceList);
          }
          break;
        case "5":
          if (isReferenceStringSet()) {
            new OPT_Algorithm(memoryCapacity,referenceList);
          }
          break;
        case "6":
          if (isReferenceStringSet()) {
            new LRU_Algorithm(memoryCapacity,referenceList);
          }
          break;
        case "7":
          if (isReferenceStringSet()) {
            new LFU_Algorithm(memoryCapacity,referenceList);
          }
          break;
        default:
          printErrorText("Command not recognized");
          break;
      }
    }
  }

  /**
   * Helper method to display main menu
   */
  @Override
  public void showMainMenu() {
    //Display menu
    System.out.print(
        "\nMAIN MENU:\n" +
        "\t0) Exit\n" +
        "\t1) Read reference string\n" +
        "\t2) Generate reference string\n" +
        "\t3) Display current reference string\n" +
        "\t4) Simulate FIFO\n" +
        "\t5) Simulate OPT\n" +
        "\t6) Simulate LRU\n" +
        "\t7) Simulate LFU\n" +
        
        "Enter your selection: ");

  }

  /**
   * reads reference string from user
   */
  @Override
  public void readReferenceString() {
    try {
      System.out.println("Enter reference string. Integers 0 thru 9 only allowed(ex. 0 3 4...).");
      String[] input = getUserInput().replaceAll("[,\\s]", "").split("");
      for (String s : input) {
        if (s.matches("[\\D]")|| s.length()<=0) {
          throw new InvalidInputException();
        }
        referenceList.add(s);
      }
    }catch (InvalidInputException ex){
      printErrorText(ex.getMessage());
      readReferenceString();
    }
  }

  /**
   * random generates a reference string of inputted length
   */
  @Override
  public void generateReferenceString() {
    System.out.println("Enter length of reference string.");
    
    try {
      String str = getUserInput();
      if (!str.matches("[\\d]+")) {
        throw new InvalidInputException();
      }
      new Random().ints(Integer.parseInt(str), 0, 9)
          .forEachOrdered(i -> referenceList.add(String.valueOf(i)));
      
    } catch (InvalidInputException ex) {
      printErrorText(ex.getMessage());
      generateReferenceString();
    }
  }

  /**
   * displays current reference string if set.
   */
  @Override
  public void displayCurrentReferenceString() {
    System.out.println("Reference string: " + Arrays.toString(referenceList.toArray()));
  }

  /**
   * Helper method to check if reference string is set.
   * @return if reference is set
   */
  @Override
  public boolean isReferenceStringSet() {
    if (referenceList.isEmpty()) {
      printErrorText("No reference string is set.");
      return false;
    }
      return true;
  }

}
