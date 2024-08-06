package serialAbelianSandpile;

import java.util.concurrent.RecursiveTask;
import java.lang.Runtime;

public class UpdateGrid extends RecursiveTask<Boolean>{
	private Grid gridContainer;
	private int head;
	private int tail;
	// Number of columns per thread
	int cutoff;
	public UpdateGrid(Grid gridContainer, int head, int tail) {
		this.gridContainer = gridContainer;
		this.head = head;
		this.tail = tail ; 
//		this.cutoff = gridContainer.getRows() / Runtime.getRuntime().availableProcessors();
		this.cutoff = 16;
	}
	
	public UpdateGrid(Grid gridContainer) {
		this(gridContainer, 1, gridContainer.getRows());
	}
	
	public Grid getGrid() {
		return gridContainer;
	}

	@Override
	public Boolean compute() {
		if (tail - head < cutoff) {
			System.out.println("Updating" + head + " " + tail);
			return gridContainer.update(head, tail);
			
		}
		else {

			int mid = (tail + head)/2;
			UpdateGrid left = new UpdateGrid(gridContainer ,head, mid);
			UpdateGrid right = new UpdateGrid(gridContainer ,mid,  tail);
			left.fork();
			return (right.compute() && left.join());
			
		}
	}

}
