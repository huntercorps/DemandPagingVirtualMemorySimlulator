package simulation;


import java.util.List;

/**
 * The type Lru algorithm.
 */
public class LRU_Algorithm extends Simulation {

  /**
   * Instantiates a new Lru algorithm.
   *
   * @param memoryCapacity the memory capacity
   * @param referenceString the reference string
   */
  public LRU_Algorithm(int memoryCapacity, List<String> referenceString) {
    super(memoryCapacity, referenceString);
  }

  /**
   * sets the method to determine victims to replace
   */
  protected void setVictimPriorities(){
    for (Page page : getReferenceList()) {
      page.setReplacementPriority(getReferenceList()
          .subList(0, currentIdx)
          .lastIndexOf(page));
    }
  }

  /**
   * Method to add algorithms unique text to default intro text.
   * @return String of algorithms unique intro text.
   */
  @Override
  protected String algoSpecificIntro() {
    return "The LRU page-replacement algorithm is an approximation of the optimal page-replacement"
        + " algorithm.\nthe victim frame is the one that has not been accessed for the"
        + " longest period of time.\n";
  }

  /**
   * Method to add algorithms unique text to default text when page miss occurs.
   * @param victim the victim page
   * @return String of algorithms unique missed page text.
   */
  @Override
  protected String algoMissText(Page victim) {
    return "which was not referenced for the longest period of time.";
  }

}
