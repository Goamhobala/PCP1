//Copyright M.M.Kuttel 2024 CSC2002S, UCT
// Adapted by Jing Yeh YHXJIN001
package parallelAbelianSandpile;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;



//This class is for the grid for the Abelian Sandpile cellular automaton
public class Grid {
	private int rows, columns;
	private int [][] grid; //grid 
    
	public Grid(int w, int h) {
		rows = w+2; //for the "sink" border
		columns = h+2; //for the "sink" border
		grid = new int[this.rows][this.columns];
		/* grid  initialization */
		for(int i=0; i<this.rows; i++ ) {
			for( int j=0; j<this.columns; j++ ) {
				grid[i][j]=0;
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
	
	
	public Grid(Grid copyGrid) {
		this(copyGrid.rows,copyGrid.columns); //call constructor above
		/* grid  initialization */
		for(int i=0; i<rows; i++ ) {
			for( int j=0; j<columns; j++ ) {
				this.grid[i][j]=copyGrid.get(i,j);
			}
		}
	}
	
/**
 * The function `getRows()` returns the number of rows minus 2.
 * @return The method `getRows()` is returning the value of `rows` minus 2.
 * with 2 rows being subtracted to account for the sink.
 */
	public int getRows() {
		return rows-2; //less the sink
	}

/**
 * This Java function returns the number of columns minus 2.
 * 
 * @return The method `getColumns()` is returning the value of `rows` minus 2.
 * with 2 rows being subtracted to account for the sink.
 */
	public int getColumns() {
		return columns-2;//less the sink
	}


/**
 * The `get` function returns the value at position (i, j) in a 2D grid.
 * 
 * @param i The parameter `i` represents the row index in a 2D grid.
 * @param j The parameter `j` in the `get` method represents the column index in a 2D grid. It is used
 * to specify the column from which you want to retrieve a value.
 * @return The value at index (i, j) in the grid is being returned.
 */
	int get(int i, int j) {
		return this.grid[i][j];
	}
/**
 * The function sets all elements in the grid to a specified value, excluding the border elements.
 * 
 * @param value The `value` parameter in the `setAll` method represents the value that you want to set
 * for all elements in the `grid` array, except for the border elements. The method iterates through
 * the inner elements of the grid (excluding the border elements) and sets each element to the
 * specified
 */
	void setAll(int value) {
		//borders are always 0
		for( int i = 1; i<rows-1; i++ ) {
			for( int j = 1; j<columns-1; j++ ) 			
				grid[i][j]=value;
			}
	}
	

/**
 * This function updates a grid with values from another grid and returns true if any values were
 * changed.
 * 
 * @param start The `start` parameter in the `nextTimeStep` method represents the starting row index
 * from which the values in the `updatedGrid` will be copied into the main `grid`.
 * @param end The `end` parameter in the `nextTimeStep` method represents the ending row index up to
 * which the values from the `updatedGrid` will be copied into the `grid`. The method iterates over the
 * rows starting from the `start` index up to (but not including) the `
 * @param updatedGrid The `updatedGrid` parameter in the `nextTimeStep` method represents a 2D array
 * that contains updated values for a grid. The method iterates over the specified range of rows and
 * columns in the `updatedGrid` and copies the values into another grid called `grid`. If any value
 * @return The method `nextTimeStep` returns a boolean value indicating whether any values in the
 * `updatedGrid` were different from the corresponding values in the `grid`. If any differences were
 * found and updated in the `grid`, the method returns `true`, otherwise it returns `false`.
 */
	public boolean nextTimeStep(int start, int end, int [][] updatedGrid) {
		boolean nextStep = false;
		for(int i=start; i < end; i++ ) {
			for( int j=1; j<=columns-1; j++ ) {
				int localValue = updatedGrid[i][j];
				if (grid[i][j]!=localValue) {
					nextStep = true;
					grid[i][j]=localValue;
				}
			}
		}
    	return nextStep;
	}
	

/**
 * This function updates a local copy of a grid based on calculations involving neighboring grid
 * values.
 * 
 * @param head The `head` parameter in the `update` function represents the starting row index in the
 * grid where the update operation will begin. It indicates the topmost row that needs to be updated in
 * the grid.
 * @param tail The `tail` parameter in the `update` function represents the ending index of the rows
 * that need to be updated in the grid. It is used in the loop to iterate over the rows from the `head`
 * index to the `tail` index (inclusive) and update the values in the `
 * @param localUpdatedGrid The `localUpdatedGrid` parameter is a 2D array that represents a local copy
 * of the grid where the updated values will be stored. This method `update` takes the values from the
 * original `grid` array, performs some calculations, and updates the corresponding values in the
 * `localUpdatedGrid
 */
	void update(int head, int tail, int [][] localUpdatedGrid) {
		for( int i = head; i <= tail; i++ ) {
			for( int j = 1; j<columns-1; j++ ) {
				localUpdatedGrid[i][j] = (grid[i][j] % 4) + 
						(grid[i-1][j] / 4) +
						grid[i+1][j] / 4 +
						grid[i][j-1] / 4 + 
						grid[i][j+1] / 4;

		}} //end nested for

	}
	
/**
 * The printGrid function prints a grid with borders and values stored in a 2D array, excluding the
 * border elements.
 */
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
	
/**
 * The function `gridToCSV` writes the contents of a grid to a CSV file specified by the `path`
 * parameter.
 * 
 * @param path The `path` parameter in the `gridToCSV` method is the file path where the CSV file will
 * be written. This method writes the contents of a grid to a CSV file at the specified path.
 */
	void gridToCSV(String path) {
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write("New grid," + getRows() + "," + getColumns() + "\n");
		for( int i=1; i<rows-1; i++ ) {
			for( int j=1; j<columns-1; j++ ) {
				writer.write(grid[i][j] + ",");
				}
			writer.write("\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//write grid out as an image
/**
 * The `gridToImage` function converts a grid of integer values into an image with specific color
 * mappings and saves it as a PNG file.
 * 
 * @param fileName the path to the output file
 */
	void gridToImage(String fileName) throws IOException {
        BufferedImage dstImage = new BufferedImage(rows, columns, BufferedImage.TYPE_INT_ARGB);
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
		            	r=28;
		            	g=157;
		            	b=255;
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
