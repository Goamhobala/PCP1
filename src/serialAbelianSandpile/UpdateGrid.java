package serialAbelianSandpile;
import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.lang.Runtime;

public class UpdateGrid extends RecursiveAction{
	private Grid gridContainer;
	private int head;
	private int tail;
	private int cutoff;
	private int [][] localUpdatedGrid;
	protected static ArrayList<int[][]> updatedGrids;
	
	public UpdateGrid(Grid gridContainer, int head, int tail) {
		this.gridContainer = gridContainer;
		this.head = head;
		this.tail = tail ; 
		this.cutoff = gridContainer.getRows() / Runtime.getRuntime().availableProcessors();
		// plus the sink
		this.localUpdatedGrid = new int[gridContainer.getRows() + 2][gridContainer.getColumns()+ 2];
		
		this.cutoff = 16;
//		System.out.println("CUTOFF " + cutoff + "head " + head + "tail "  + tail);
		
	}
	
	public UpdateGrid(Grid gridContainer) {
		this(gridContainer, 1, gridContainer.getRows() -1);
	}
	
	public Grid getGrid() {
		return gridContainer;
	}
	
	public void setGrid(Grid newGrid) {
		this.gridContainer = newGrid;
	}

	@Override
	public void compute() {
		if (tail - head < cutoff) {
			while(!gridContainer.update(head, tail, localUpdatedGrid)) {
//				System.out.println("Updating: "+ head + " " + tail);
				continue;
			};
//			System.out.println("next step");
			gridContainer.nextTimeStep(gridContainer.convertStart(head), gridContainer.convertEnd(tail), localUpdatedGrid);
		}
		else {
// TODO: Problem, stops after going through each position once
			int mid = (tail + head)/2;
			UpdateGrid left = new UpdateGrid(gridContainer,head, mid);
			UpdateGrid right = new UpdateGrid(gridContainer,mid - 1,  tail);
			left.fork();
			right.compute();
			left.join();
//			if (right.compute() && left.join()) {
//				return true;
//			}
//			else {
//				return false;
//			}

			
		}
	}

}
