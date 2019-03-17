package simulation;

import java.util.Arrays;
import java.util.List;

/**
 * The type Lfu algorithm.
 */
public class LFU_Algorithm extends Simulation {

  /**
   * Instantiates a new Lfu algorithm.
   *
   * @param memoryCapacity the memory capacity
   * @param referenceString the reference string
   */
  public LFU_Algorithm(int memoryCapacity, List<String> referenceString) {
    super(memoryCapacity, referenceString);
  }

  /**
   * sets the method to determine victims to replace
   */
  @Override
  protected void setVictimPriorities(){
    for (Page page : getReferenceList()) {
      page.setReplacementPriority((int) getReferenceList()
          .subList(0, currentIdx)//+1
          .stream()
          .filter(pages -> pages.equals(page))
          .count());
    }
  }

  /**
   * Method to add algorithms unique text to default intro text.
   * @return String of algorithms unique intro text.
   */
  @Override
  protected String algoSpecificIntro() {
    return "In the LFU page-replacement algorithm, the victim frame is the one"
        + " that has the smallest number of references.\n";
  }

  /**
   * Method to add algorithms unique text to default text when page miss occurs.
   * @param victim the victim page
   * @return String of algorithms unique missed page text.
   */
  @Override
  protected String algoMissText(Page victim) {
    return String.format(
        "which was used %dx. We applied the policy to choose the first least frequently used\n"
        + "frame in top-down order. Therefore, frame %s was least frequently used and will be "
        + "replaced."
        ,getReferenceList().subList(0, currentIdx).stream().filter(page -> page.equals(victim)).count()
        ,victim.getValue());
  }

}
