
package serialAbelianSandpile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

import javax.imageio.ImageIO;
	
//This class is for the grid for the Abelian Sandpile cellular automaton
public class GridParallel extends RecursiveTask{
	private int rows, columns;
	private List<int[]> grid; //grid 
	private List<int[]> updateGrid;//grid for next time step
    private int head;
    private int tail;
    private int cutoff;
	public GridParallel(int w, int h) {
		rows = w+2; //for the "sink" border
		columns = h+2; //for the "sink" border
		cutoff = h+2/16;
		head = 0;
		tail = h+2;
		grid = new ArrayList<int[]>(this.columns);
		updateGrid=new ArrayList<int[]>(this.columns);
		/* grid  initialization */
		for(int j=0; j<this.columns; j++ ) {
			grid.add(new int[rows]);
			Arrays.fill(grid.get(j), 0);
		}
	}

	public GridParallel(List<int[]> newGrid) {
		this(newGrid.size(), newGrid.get(0).length); //call constructor above
		//don't copy over sink border
		for(int j=1; j<columns-1; j++ ) {
			for( int i=1; i<rows-1; i++ ) {
				this.grid.get(j)[i]= newGrid.get(j-1)[i-1];
			}
		}
		
	}
	public GridParallel(int[][] newGrid) {
		this(newGrid.length,newGrid[0].length); //call constructor above
		//don't copy over sink border
		for(int i=1; i<rows-1; i++ ) {
			for( int j=1; j<columns-1; j++ ) {
				this.grid.get(j)[i]=newGrid[i-1][j-1];
			}
		}
		
		
	}
//	public GridParallel(GridParallel copyGrid) {
//		this(copyGrid.rows,copyGrid.columns); //call constructor above
//		/* grid  initialization */
//		for(int i=0; i<rows; i++ ) {
//			for( int j=0; j<columns; j++ ) {
//				this.grid[i][j]=copyGrid.get(i,j);
//			}
//		}
//	}
	
	public int getRows() {
		return rows-2; //less the sink
	}

	public int getColumns() {
		return columns-2;//less the sink
	}


	int get(int i, int j) {
		return this.grid.get(j)[i];
	}

	void setAll(int value) {
		//borders are always 0
		for( int j = 1; j<columns-1; j++ ) {
			for( int i = 1; i<rows-1; i++ ) 			
				grid.get(j)[i]=value;
			}
	}
	

	//for the next timestep - copy updateGrid into grid
	public void nextTimeStep() {
		for(int j=1; j<columns-1; j++ ) {
			for( int i=1; i<columns-1; i++ ) {
				this.grid.get(j)[i]=updateGrid.get(j)[i];
			}
		}
	}
	
	//key method to calculate the next update grod
	public Boolean compute() {
		Boolean changed = new Boolean(false);
		if (tail - head < cutoff) {
			for( int i = 1; i<rows-1; i++ ) {
				for( int j = 1; j<columns-1; j++ ) {
					updateGrid.get(j)[i] = (grid.get(j)[i] % 4) + 
							(grid.get(j)[i-1] / 4) +
							grid.get(j)[i+1] / 4 +
							grid.get(j-1)[i] / 4 + 
							grid.get(j+1)[i] / 4;
					changed = true;
				}
			}
			if (changed) {
				nextTimeStep();
			}

		}
		else {
			GridParallel left = new GridParallel(grid.subList(head, tail/2));
			GridParallel right = new GridParallel(grid.subList(tail/2, tail + 1));
			left.fork();
			right.compute();
			left.join();
		}
		return changed;
	}
	
//	boolean update() {
//		boolean change=false;
//		//do not update border
//		for( int i = 1; i<rows-1; i++ ) {
//			for( int j = 1; j<columns-1; j++ ) {
//				updateGrid[i][j] = (grid[i][j] % 4) + 
//						(grid[i-1][j] / 4) +
//						grid[i+1][j] / 4 +
//						grid[i][j-1] / 4 + 
//						grid[i][j+1] / 4;
//				if (grid[i][j]!=updateGrid[i][j]) {  
//					change=true;
//				}
//		}} //end nested for
//	if (change) { nextTimeStep();}
//	return change;
//	}
//	
	
	
	//display the grid in text format
	void printGrid( ) {
		int i,j;
		//not border is not printed
		System.out.printf("Grid:\n");
		System.out.printf("+");
		for( j=1; j<columns-1; j++ ) System.out.printf("  --");
		System.out.printf("+\n");
		for( i=1; i<rows-1; i++ ) {
			System.out.printf("|");
			for( j=1; j<columns-1; j++ ) {
				if ( grid.get(j)[i] > 0) 
					System.out.printf("%4d", grid.get(j)[i] );
				else
					System.out.printf("    ");
			}
			System.out.printf("|\n");
		}
		System.out.printf("+");
		for( j=1; j<columns-1; j++ ) System.out.printf("  --");
		System.out.printf("+\n\n");
	}
	
	//write grid out as an image
	void gridToImage(String fileName) throws IOException {
        BufferedImage dstImage =
                new BufferedImage(rows, columns, BufferedImage.TYPE_INT_ARGB);
        //integer values from 0 to 255.
        int a=0;
        int g=0;//green
        int b=0;//blue
        int r=0;//red

		for( int j=0; j<columns; j++ ) {
			for( int i=0; i<rows; i++ ) {
			     g=0;//green
			     b=0;//blue
			     r=0;//red

				switch (grid.get(j)[i]) {
					case 0:
		                break;
		            case 1:
		            	g=255;
		                break;
		            case 2:
		                b=255;
		                break;
		            case 3:
		                r = 255;
		                break;
		            default:
		                break;
				
				}
		                // Set destination pixel to mean
		                // Re-assemble destination pixel.
		              int dpixel = (0xff000000)
		                		| (a << 24)
		                        | (r << 16)
		                        | (g<< 8)
		                        | b; 
		              dstImage.setRGB(i, j, dpixel); //write it out

			
			}}
		
        File dstFile = new File(fileName);
        ImageIO.write(dstImage, "png", dstFile);
	}
	
	


}
