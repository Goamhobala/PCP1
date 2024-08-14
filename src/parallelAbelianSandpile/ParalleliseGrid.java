// Jing Yeh YHXJIN001
// This wrapper class parallelise the Grid object

package parallelAbelianSandpile;
import java.util.concurrent.RecursiveTask;

public class ParalleliseGrid extends RecursiveTask<int[][]>{
	private Grid gridContainer;
	private int head;
	private int tail;
	private int cutoff;
	private int [][] localUpdatedGrid;

	

	public ParalleliseGrid(Grid gridContainer, int head, int tail, int cutoff) {
		this.gridContainer = gridContainer;
		this.head = head;
		this.tail = tail ; 
		this.cutoff = cutoff;
		
	}
	

	public ParalleliseGrid(Grid gridContainer, int cutoff) {
		this(gridContainer, 1, gridContainer.getRows(), cutoff); // call constructor above
	}
	
/**
 * The getGrid() function returns the grid stored in the gridContainer.
 * 
 * @return The `gridContainer` is being returned.
 */
	public Grid getGrid() {
		return gridContainer;
	}
	
/**
 * The compute method creates a local copy of the updated grid and merges the grid updated by each
 * thread based on a cutoff value.
 * 
 * @return The `compute()` method returns a 2D integer array representing the updated grid. If the size
 * of the grid being processed by the current thread is less than the cutoff value, a local copy of the
 * updated grid is created and returned. Otherwise, the grid is split into two parts, processed by
 * separate threads, and then the results are merged before returning the final updated grid.
 */
	@Override
	public int[][] compute() {
		if (tail - head < cutoff) {
			this.localUpdatedGrid = new int[gridContainer.getRows() + 2][gridContainer.getColumns()+ 2];
			gridContainer.update(head, tail, localUpdatedGrid);
			return localUpdatedGrid;
		}
		else {
			int[][] mergedGrid = new int[gridContainer.getRows() + 2][gridContainer.getColumns()+ 2];
			int mid = (tail + head)/2;
			ParalleliseGrid left = new ParalleliseGrid(gridContainer,head, mid, cutoff);
			ParalleliseGrid right = new ParalleliseGrid(gridContainer,mid,  tail, cutoff);
			left.fork();
			int[][] updatedRight = right.compute();
			int[][] updatedLeft = left.join();
			// The grids have been updated once, ready to be merged
			for (int i = head; i <= tail; i++) {
				for (int j = 1 ; j <= gridContainer.getColumns() + 1 ; j++) {
					if (i <= mid) {
						mergedGrid[i][j] = updatedLeft[i][j];
					}
					else {
						mergedGrid[i][j] = updatedRight[i][j];
					}
				}
			}
			return mergedGrid;

			
		}
	}

}
