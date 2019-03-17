package simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import allocationtable.AllocationTable;

/**
 * The type Simulation.
 */
public abstract class Simulation {
  private final List<Page> referenceList = new LinkedList<>();
  private List<Page> allocatedPages = new LinkedList<>();
  private final int memoryCapacity;
  private int pageFaults = 0;
  private String result = "";
  private AllocationTable allocationTable;

  /**
   * The Current idx.
   * used to determine column to set in allocation table
   */
  int currentIdx = 0;


  /**
   * Instantiates a new Simulation.
   *
   * @param memoryCapacity the memory capacity
   * @param referenceString the reference string
   */
  public Simulation(int memoryCapacity, List<String> referenceString) {
    this.memoryCapacity = memoryCapacity;
    setReferenceList(referenceString);
    allocationTable = new AllocationTable(memoryCapacity,referenceList);
    runSimulation();
  }

  /**
   * Sets victim priorities using algorithm.
   */
  protected abstract void setVictimPriorities();

  /**
   * Algo specific intro string.
   *
   * @return the string
   */
  protected abstract String algoSpecificIntro();

  /**
   * Algo miss text string.
   *
   * @param victim the victim
   * @return the string
   */
  protected abstract String algoMissText(Page victim);

  /**
   * Run simulation.
   */
  public void runSimulation() {
    initSim();
    for (Page pageToAllocate : referenceList) {

      if (isAlreadyAllocated(pageToAllocate)) {
        pageHitOccurred(pageToAllocate);

      } else {
        pageMissOccurred();

        if (!isUnallocatedFramesAvailable()) {
          allocatePage(pageToAllocate);
        } else {
          allocatePage(pageToAllocate,getVictim());

        }
      }
      updateSimulation();
    }
    ShowSummary();
  }

  /**
   * Prints the result of the simulation, page fault total
   */
  private void ShowSummary() {
    allocationTable.printAllocationTable();
    System.out.printf("End the end, a total of %d page faults were generated\n",pageFaults);
    promptToContinue();
  }

  /**
   * Method displayed at simulation start introducing algorithm
   * and displaying initial allocation table
   */
  private void initSim() {
    allocationTable.printAllocationTable();
    System.out.println(introText(memoryCapacity,referenceList));
    promptToContinue();
  }

  /**
   * Adds test description of simulations step
   * @param page that was hit
   */
  private void pageHitOccurred(Page page) {
    result += pageHitText(page);
    }

  /**
   * increments fault total and places an F in the table
   */
  private void pageMissOccurred() {
    pageFaults++;
    allocationTable.addFault(currentIdx);
  }

  /**
   * Helper method to check if current page is allocated already
   * @param pageToAllocate page to check if allocated
   * @return true if already allocated
   */
  private boolean isAlreadyAllocated(Page pageToAllocate) {
    return  allocatedPages.contains(pageToAllocate);
  }

  private boolean isUnallocatedFramesAvailable() {
    return allocatedPages.size() == memoryCapacity;
  }

  /**
   * Gets the victim to be replaced
   * @return
   */
  private Page getVictim() {
    setVictimPriorities();
    Page victim = getAllocatedPages().stream()
        .reduce((x, y) -> x.getReplacementPriority() <= y.getReplacementPriority() ? x : y)
        .get();
    victim.setReplacementPriority(0);
    return victim;
  }

  /**
   * Get allocated pages list.
   *
   * @return the list
   */
  public List<Page> getAllocatedPages(){return allocatedPages;}

  /**
   * Gets reference list.
   *
   * @return the reference list
   */
  public List<Page> getReferenceList() {return referenceList;}

  /**
   * Sets reference string list
   * @param referenceString
   */
  private void setReferenceList(List<String> referenceString) {
    IntStream.range(0, referenceString.size())
        .forEach(i -> getReferenceList().add(Page.createPage(referenceString.get(i))));
  }

  /**
   * Allocates the inputed page and assigns page miss text to
   * result
   * @param pageToAllocate
   */
  private void allocatePage(Page pageToAllocate) {
    allocatedPages.add(pageToAllocate);
    result += pageMissTextNoVictim(pageToAllocate);

  }

  /**
   * Overloaded method to Allocate/replace the inputed page with victim
   * and assigns page miss w/victim text to result
   * @param pageToAllocate
   * @param victim
   */
  private void allocatePage(Page pageToAllocate, Page victim) {
    allocatedPages.set(allocatedPages.indexOf(victim),pageToAllocate);
    allocationTable.addVictimToTable(currentIdx,victim.value);
    result += pageMissTextWithVictim(pageToAllocate,victim);
  }

  /**
   * Helper method to prepare simulation for the next step.
   * prints allocation table and
   * displays results of current step.
   */
  private void updateSimulation() {
    currentIdx++;

    allocationTable.setColumn(currentIdx,allocatedPages);
    allocationTable.printAllocationTable();
    System.out.println(result);
    result="";
    promptToContinue();
  }

  /**
   * Helper method to prompt user to continue
   */
  private void promptToContinue() {
    try {
      System.out.print("Press Enter to Continue.");
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to create intro text with specif algo text added.
   * @param memoryCapacity
   * @param referenceList
   * @return intro text string
   */
  private String introText(int memoryCapacity, List<Page> referenceList) {
    return String.format("%sThis simulation assumes a hypothetical computer having %d physical frames "
        + "numbered 0 to %d. It assumes that a single process that is running has a "
        + "virtual memory of %d frames.\nThe reference string is %s"
        ,algoSpecificIntro()
        ,memoryCapacity,memoryCapacity-1
        , referenceList.stream().distinct().count()
        , Arrays.toString(referenceList.toArray()));
  }

  /**
   * Method to create page hit text
   * @param pageToAllocate
   * @return page hit text string
   */
  private String pageHitText(Page pageToAllocate) {
    return String.format(
        "Virtual frame %s is referenced.\nBecause virtual frame %s is already present in "
            + "physical memory, in physical frame %d, nothing else needs to be done.\n"
            + "No page fault is generated.\nWe have no victim frame"
        ,pageToAllocate.getValue()
        ,pageToAllocate.getValue()
        ,allocatedPages.indexOf(pageToAllocate));
  }

  /**
   * Method to create page miss text with no victim
   * @param pageToAllocate
   * @return page miss text string
   */
  private String pageMissTextNoVictim(Page pageToAllocate) {
    return String.format(
        "Virtual frame %s is referenced.\nBecause virtual frame %s is not present in physical"
            + " memory, a page fault is generated.\nVirtual frame %s is loaded into physical "
            + "frame %d.\nBecause there was room in physical memory, we have no victim frame."
        ,pageToAllocate.getValue()
        ,pageToAllocate.getValue()
        ,pageToAllocate.getValue()
        ,allocatedPages.indexOf(pageToAllocate));
  }

  /**
   * Method to create page miss text with no victim and add
   * algorithm specific text
   * @param pageToAllocate
   * @param victim
   * @return page miss text with victim
   */
  private String pageMissTextWithVictim(Page pageToAllocate, Page victim) {
    return String.format("Virtual frame %s is referenced.\nBecause virtual frame %s is not present "
            + "in physical memory, a page fault is generated.\nBecause there is no more room in "
            + "the physical memory, a frame must be replaced.\nThe victim frame is virtual frame "
            + "%s, %s \nVirtual frame %s is swapped out, and virtual frame %s is swapped in."
        , pageToAllocate.getValue()
        , pageToAllocate.getValue()
        , victim.getValue()
        , algoMissText(victim)
        , victim.getValue()
        , pageToAllocate.getValue());
  }

  /**
   * The type Page. Used to represent pages in algorithm
   */
  public static class Page {

    /**
     * The constant issued.
     */
    public static ArrayList<Page> issued = new ArrayList<>();
    private final String value;
    private int replacementPriority = 0;

    private Page(String value) {
      this.value = value;
      issued.add(this);
    }

    /**
     * Create page page.
     *
     * @param value the value
     * @return the page
     */
    public static Page createPage(String value) {
      for (Page p : issued) {
        if (p.value.equals(value)) {
          return p;
        }
      }
      return new Page(value);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
      return value;
    }

    /**
     * Sets replacement priority.
     *
     * @param replacementPriority the replacement priority
     */
    void setReplacementPriority(int replacementPriority) {
      this.replacementPriority = replacementPriority;
    }

    /**
     * Gets replacement priority.
     *
     * @return the replacement priority
     */
    int getReplacementPriority() {
      return replacementPriority;
    }

    @Override
    public String toString() {
      return value;
    }
  }

}
