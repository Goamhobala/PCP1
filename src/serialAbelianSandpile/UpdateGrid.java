package serialAbelianSandpile;
import java.util.concurrent.RecursiveTask;
import java.lang.Runtime;

public class UpdateGrid extends RecursiveTask<int[][]>{
	private Grid gridContainer;
	private int head;
	private int tail;
	private int cutoff;
	private int [][] localUpdatedGrid;
	
	public UpdateGrid(Grid gridContainer, int head, int tail, int cutoff) {
		this.gridContainer = gridContainer;
		this.head = head;
		this.tail = tail ; 
		this.cutoff = cutoff;
		// plus the sink

		
//		this.cutoff = ;
//		System.out.println("CUTOFF " + cutoff + "head " + head + "tail "  + tail);
		
	}
	
	public UpdateGrid(Grid gridContainer) {
		this(gridContainer, 1, gridContainer.getRows(), gridContainer.getRows() / Runtime.getRuntime().availableProcessors());
	}
	
	public Grid getGrid() {
		return gridContainer;
	}
	
	public void init() {
		/*
		 * Reinitialise the UpdateGrid
		 * */
		
		
	}
	@Override
	public int[][] compute() {
		if (tail - head < cutoff) {
			this.localUpdatedGrid = new int[gridContainer.getRows() + 2][gridContainer.getColumns()+ 2];
//			int counter = 0;
			gridContainer.update(head, tail, localUpdatedGrid);
//			while(gridContainer.update(head, tail, localUpdatedGrid)) {
//				System.out.println(localUpdatedGrid[13][9]);
//				System.out.println("Updating: "+ head + " " + tail);
//				counter++;
//				System.out.println("Count: " + counter);
//			};
//			System.out.println("next step");
//			gridContainer.nextTimeStep(gridContainer.convertStart(head), gridContainer.convertEnd(tail), localUpdatedGrid);
			return localUpdatedGrid;
		}
		else {
			int[][] mergedGrid = new int[gridContainer.getRows() + 2][gridContainer.getColumns()+ 2];
			int mid = (tail + head)/2;
			UpdateGrid left = new UpdateGrid(gridContainer,head, mid, cutoff);
			UpdateGrid right = new UpdateGrid(gridContainer,mid,  tail, cutoff);
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
