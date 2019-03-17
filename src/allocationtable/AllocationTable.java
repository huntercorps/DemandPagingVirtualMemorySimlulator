package allocationtable;

import java.util.List;
import simulation.Simulation.Page;

/**
 * The type Allocation table.
 */
public class AllocationTable implements PrintableFlipTable {
  private int rowSize;
  private int columnSize;
  private String[] tableHeader;
  private String[][] allocTable;

  /**
   * Instantiates a new Allocation table.
   *
   * @param memoryCapacity the memory capacity. sets table's row size
   * @param referenceString the reference string set's table's column size
   */
  public AllocationTable(int memoryCapacity, List referenceString) {
    columnSize = referenceString.size();
    rowSize = memoryCapacity;

    header(referenceString);
    tableContents();
  }

  /**
   * Add fault.
   *
   * @param col the col to add a fault too
   */
  public void addFault( int col) {
    allocTable[rowSize][col+1] = "F";
  }

  /**
   * Add victim to table.
   *
   * @param col the col to add victim too
   * @param victim the victim to add to table
   */
  public void addVictimToTable(int col, String victim) {
    allocTable[rowSize+1][col+1] = victim;
  }

  private void header(List referenceString) {
    tableHeader = new String[columnSize+1];
    tableHeader[0] = "Reference String";
    for (int i = 1; i < columnSize+1; i++) {
      tableHeader[i] = String.valueOf(referenceString.get(i-1));
    }
  }

  /**
   * creates and assigns the allocation tables content
   */
  private void tableContents() {
    allocTable = new String[rowSize+2][columnSize+1];

    for (int i = 0; i < rowSize+2; i++) {
      allocTable[i][0] = "Physical frame " + i;
    }
    allocTable[rowSize][0] = "Page faults";
    allocTable[rowSize+1][0] = "Victim frames";

    for (int i = 0; i < rowSize+2; i++) {
      for (int j = 1; j < columnSize+1 ; j++) {
        allocTable[i][j] = " ";
      }
    }

  }

  /**
   * Sets column with current allocated pages
   *
   * @param col the col to set
   * @param allocatedPages the allocated pages to add
   */
  public void setColumn(int col,List<Page> allocatedPages) {
    for (int i = 0; i < allocatedPages.size(); i++) {
      allocTable[i][col] = allocatedPages.get(i).getValue();
    }
  }

  /**
   * Print allocation table.
   */
  public void printAllocationTable() {
    System.out.print(FlipTable.of(tableHeader, allocTable));
  }

}

