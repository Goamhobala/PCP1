//Copyright M.M.Kuttel 2024 CSC2002S, UCT
package serialAbelianSandpile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import javax.imageio.ImageIO;

//This class is for the grid for the Abelian Sandpile cellular automaton
public class Grid {
	private int rows, columns;
	private int [][] grid; //grid 
	int [][] updatedGrid;//grid for next time step
    
	public Grid(int w, int h) {
		rows = w+2; //for the "sink" border
		columns = h+2; //for the "sink" border
		grid = new int[this.rows][this.columns];
		updatedGrid=new int[this.rows][this.columns];
		/* grid  initialization */
		for(int i=0; i<this.rows; i++ ) {
			for( int j=0; j<this.columns; j++ ) {
				grid[i][j]=0;
				updatedGrid[i][j]=0;
			}
		}
	}

	public Grid(int[][] newGrid) {
		this(newGrid.length,newGrid[0].length); //call constructor above
		//don't copy over sink border
		for(int i=1; i<rows-1; i++ ) {
			for( int j=1; j<columns-1; j++ ) {
				this.grid[i][j]=newGrid[i-1][j-1];
			}
		}
		
	}
	
//	public Grid(Grid copyGrid, int head, int tail) {
////		Create partial copy of the grid
//		this(tail-head, copyGrid.columns - 2);
//		System.out.println(tail);
//		for (int i=0; i < tail - head ; i++) {
//			for (int j=1; j < copyGrid.columns - 1 ; j++ ) {
//				System.out.println("head:" + head + "tail" + tail);
//				System.out.println("Cell row:" + Integer.toString(i + head) + "column:" + Integer.toString(j) + "added" );
//				this.grid[i][j] = copyGrid.get(i, j);
//			}
//			
//		}
//	}
	
	public Grid(Grid copyGrid) {
		this(copyGrid.rows,copyGrid.columns); //call constructor above
		/* grid  initialization */
		for(int i=0; i<rows; i++ ) {
			for( int j=0; j<columns; j++ ) {
				this.grid[i][j]=copyGrid.get(i,j);
			}
		}
	}
	
	public int getRows() {
		return rows-2; //less the sink
	}

	public int getColumns() {
		return columns-2;//less the sink
	}


	int get(int i, int j) {
		return this.grid[i][j];
	}

	void setAll(int value) {
		//borders are always 0
		for( int i = 1; i<rows-1; i++ ) {
			for( int j = 1; j<columns-1; j++ ) 			
				grid[i][j]=value;
			}
	}
	
//	public void synchroniseGrid(int[][] newGrid) {
//		for(int i=1; i<rows; i++ ) {
//			for( int j=1; j<columns; j++ ) {
//				this.grid[i][j]=newGrid[i][j];
//			}
//		}
//	}
	//for the next timestep - copy updatedGrid into grid
	public boolean nextTimeStep(int start, int end, int [][] localUpdatedGrid) {
		boolean nextStep = false;
		for(int i=start; i < end; i++ ) {
			for( int j=1; j<=columns-1; j++ ) {
				if (grid[i][j]!=localUpdatedGrid[i][j]) {
					nextStep = true;
//					System.out.println("global: " + grid[i][j] + " local: " + localUpdatedGrid[i][j]);
//					System.out.println("At: " + i + " " + j);
					grid[i][j]=localUpdatedGrid[i][j];
//					System.out.println("After:  global: " + grid[i][j] + " local: " + localUpdatedGrid[i][j]);
				}
			}
		}
//		ForkJoinPool pool = new ForkJoinPool();
//		UpdateGrid updateGrid = new UpdateGrid(this);
//    	int[][] mergedGrid = pool.invoke(updateGrid);
//    	pool.shutdown();
//    	if (nextStep) {
//    		nextTimeStep(1, getRows() + 1, mergedGrid);
//    	}
//		System.out.println("One step done");
    	return nextStep;
	}
	
	//key method to calculate the next update grid
	boolean update(int head, int tail, int [][] localUpdatedGrid) {
//		int [][] localUpdatedGrid = new int[this.rows][this.columns];
		boolean change=false;
		//do not update border



		for( int i = head; i <= tail; i++ ) {
			for( int j = 1; j<columns-1; j++ ) {
//				System.out.println("Updating" +  i + " " + j);
				int previous = localUpdatedGrid[i][j];
				localUpdatedGrid[i][j] = (grid[i][j] % 4) + 
						(grid[i-1][j] / 4) +
						grid[i+1][j] / 4 +
						grid[i][j-1] / 4 + 
						grid[i][j+1] / 4;
//				System.out.println("From " +  grid [i][j]+ " to " + localUpdatedGrid[i][j]);
				if (previous !=localUpdatedGrid[i][j]) {  
//					System.out.println("Previous: " + previous);
//					System.out.println("Difference: " + "global: " + grid[i][j] + " local: " + localUpdatedGrid[i][j]);
//					System.out.println("At: " + i + " " + j);
					change=true;
				}
		}} //end nested for
//	if (change) { 
////		System.out.println("Next step");
//		nextTimeStep(start, end, localUpdatedGrid);
//		}
	return change;
	}
	
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
				if ( grid[i][j] > 0) 
					System.out.printf("%4d", grid[i][j] );
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

		for( int i=0; i<rows; i++ ) {
			for( int j=0; j<columns; j++ ) {
			     g=0;//green
			     b=0;//blue
			     r=0;//red

				switch (grid[i][j]) {
					case 0:
		                r=152;
		                g=55;
		                b=173;
		            case 1:
		            	r=231;
		            	g=230;
		            	b=230;
		                break;
		            case 2:
		            	r = 255;
		            	g = 192;
		            	b = 0;
		            	break;
		            case 3:
		            	r=14;
		                b=52;
		                g=91;
		                
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
