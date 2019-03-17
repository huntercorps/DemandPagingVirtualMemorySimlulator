package simulation;


import java.util.List;

/**
 * The type Fifo algorithm.
 */
public class FIFO_Algorithm extends Simulation {

  /**
   * Instantiates a new Fifo algorithm.
   *
   * @param memoryCapacity the memory capacity
   * @param referenceString the reference string
   */
  public FIFO_Algorithm(int memoryCapacity, List<String> referenceString) {
    super(memoryCapacity, referenceString);
  }

  /**
   * sets the method to determine victims to replace
   */
  protected void setVictimPriorities() {
    getAllocatedPages()
        .forEach(page -> page.setReplacementPriority(page.getReplacementPriority() - 1));
  }

  /**
   * Method to add algorithms unique text to default intro text.
   * @return String of algorithms unique intro text.
   */
  @Override
  protected String algoSpecificIntro() {
    return "In the FIFO page-replacement algorithm, the victim frame the oldest frame\n";
  }


  /**
   * Method to add algorithms unique text to default text when page miss occurs.
   * @param victim the victim page
   * @return String of algorithms unique missed page text.
   */
  protected String algoMissText(Page victim) {
    return "according to the FIFO strategy used in this algorithm. ";
  }

}
