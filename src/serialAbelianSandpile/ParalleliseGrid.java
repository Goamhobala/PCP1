package serialAbelianSandpile;
import java.util.concurrent.RecursiveTask;
import java.lang.Runtime;

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
	
	public ParalleliseGrid(Grid gridContainer) {
		this(gridContainer, 1, gridContainer.getRows(), gridContainer.getRows() / Runtime.getRuntime().availableProcessors());
	}
	
	public Grid getGrid() {
		/*
		 * Return the grid
		 * */
		return gridContainer;
	}
	
	@Override
	public int[][] compute() {
		/*
		 * Create local copy of the updated grid. Then merge the grid updated by each thread
		 * */
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
