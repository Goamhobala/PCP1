package serialAbelianSandpile;

import java.util.concurrent.RecursiveTask;

public class UpdateGrid extends RecursiveTask<Boolean>{
	private Grid gridContainer;
	private int head;
	private int tail;
	// Number of columns per thread
	int CUTOFF = 16;
	public UpdateGrid(Grid gridContainer) {
		this.gridContainer = gridContainer;
		this.head = 1;
		this.tail = gridContainer.getColumns() ; 
	}
	
	public Grid getGrid() {
		return gridContainer;
	}

	@Override
	public Boolean compute() {
		if (tail - head < CUTOFF) {
			return gridContainer.update();
			
		}
		else {

			int mid = (tail + head)/2;
			UpdateGrid left = new UpdateGrid(new Grid(gridContainer, head, mid));
			System.out.println("Switching to right");
			UpdateGrid right = new UpdateGrid(new Grid(gridContainer, mid, tail + 1));
			left.fork();
			return (right.compute() && left.join());
			
		}
	}

}
