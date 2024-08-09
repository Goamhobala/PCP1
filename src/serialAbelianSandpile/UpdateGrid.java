package serialAbelianSandpile;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import java.lang.Runtime;

public class UpdateGrid extends RecursiveTask<Boolean>{
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
	public Boolean compute() {
		if (tail - head < cutoff) {
			while(gridContainer.update(head, tail)) {
//				System.out.println("Updating: "+ head + " " + tail);
				continue;
			};
			return true;
			
		}
		else {
// TODO: Problem, stops after going through each position once
			int mid = (tail + head)/2;
			UpdateGrid left = new UpdateGrid(gridContainer ,head, mid);
			UpdateGrid right = new UpdateGrid(gridContainer ,mid,  tail);
			left.fork();
			if (right.compute() && left.join()) {
				return true;
			}
			else {
				return false;
			}

			
		}
	}

}
