package simulation;

import java.util.List;

/**
 * The type Opt algorithm.
 */
public class OPT_Algorithm extends Simulation {

  /**
   * Instantiates a new Opt algorithm.
   *
   * @param memoryCapacity the memory capacity
   * @param referenceString the reference string
   */
  public OPT_Algorithm(int memoryCapacity, List<String> referenceString) {
    super(memoryCapacity, referenceString);
  }

  /**
   * sets the method to determine victims to replace
   * This method returns the index of the last occurrence of the specified element in this list
   * It will return '-1' if the list does not contain the element.
   */
  @Override
  protected void setVictimPriorities(){
    for (Page page : getReferenceList()) {
      page.setReplacementPriority(getReferenceList()
          .subList(currentIdx + 1, getReferenceList().size())
          .lastIndexOf(page));

    }
  }

  /**
   * Method to add algorithms unique text to default intro text.
   * @return String of algorithms unique intro text.
   */
  @Override
  protected String algoSpecificIntro() {
    return "the optimal page-replacement algorithm is an idealized algorithm in which the victim "
        + "frame is the one that will not be accessed for the longest period of time\n";
  }

  /**
   * Method to add algorithms unique text to default text when page miss occurs.
   * @param victim the victim page
   * @return String of algorithms unique missed page text.
   */
  @Override
  protected String algoMissText( Page victim) {
    int occursAt = getReferenceList()
        .subList(currentIdx + 1, getReferenceList().size())
        .lastIndexOf(victim);
    return (occursAt == -1) ?
        "Which doesn't occur again in the reference string" :
        "which occurs again at index"+occursAt+"in the reference string";
  }

}

