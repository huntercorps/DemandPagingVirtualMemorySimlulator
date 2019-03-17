import commandline.CommandLineRunner;
import commandline.Runner;

/**
 * The type App.
 */
public class App {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    Runner runner = new CommandLineRunner(args);
    runner.run();
  }
  
}
