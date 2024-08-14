//Copyright M.M.Kuttel 2024 CSC2002S, UCT
// Adapted by Jing Yeh
package parallelAbelianSandpile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
/* Parallel  program to simulate an Abelian Sandpile cellular automaton
 * Further adapeted by Jing Yeh YHXJIN001
 * Adapted from "Abelian Sandpile Simulation" by Michelle Kuttel 2024, University of Cape Town
 * Peachy Parallel Assignments (EduPar 2022)"  
 * developed by Bu\:cker, Casanova and Da Silva  (∗Institute for Computer Science, Friedrich Schiller University Jena, Jena, Germany)
 */

class AutomatonSimulation{
	static final boolean DEBUG=false;//for debugging output, off
	
	static long startTime = 0;
	static long endTime = 0;

	//timers - note milliseconds
	private static void tick(){ //start timing
		startTime = System.currentTimeMillis();
	}
	private static void tock(){ //end timing
		endTime=System.currentTimeMillis(); 
	}
	

/**
 * The writeCSV function writes data to a CSV file with the specified number of rows, columns, time,
 * and steps.
 * 
 * @param filePath The `filePath` parameter is the path to the CSV file where you want to write the
 * data. It should include the file name and extension (e.g., "data.csv").
 * @param rows The `rows` parameter in the `writeCSV` method represents the number of rows in the CSV
 * file that will be written. It indicates how many rows of data will be included in the file.
 * @param columns The `columns` parameter in the `writeCSV` method represents the number of columns in
 * the CSV file that will be written. It specifies the horizontal arrangement of data in each row of
 * the CSV file.
 * @param time The `time` parameter in the `writeCSV` method represents the time taken for a specific
 * operation or process. It is typically measured in milliseconds or seconds, depending on the context
 * of the operation being performed. This value is written to the CSV file along with other information
 * such as the number of rows
 * @param steps The `steps` parameter in the `writeCSV` method represents the number of steps taken in
 * a process or algorithm. It is being written to a CSV file along with other information such as the
 * number of rows, columns, and the time taken.
 */
	public static void writeCSV(String filePath, int rows, int columns, int time, int steps) {
		String delimiter = ",";
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
			writer.write(rows + delimiter + columns + delimiter + time + delimiter + steps + "\n");
			
		}catch (IOException e){
			e.printStackTrace();	
		}
	}
	
	//input is via a CSV file
/**
 * The function `readArrayFromCSV` reads a 2D array of integers from a CSV file specified by the
 * filePath parameter.
 * 
 * @param filePath The path to the file
 * @return The method `readArrayFromCSV` is returning a 2D integer array that is read from a CSV file
 * specified by the `filePath` parameter.
 */
	 public static int [][] readArrayFromCSV(String filePath) {
		 int [][] array = null;
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line = br.readLine();
	            if (line != null) {
	                String[] dimensions = line.split(",");
	                int width = Integer.parseInt(dimensions[0]);
	                int height = Integer.parseInt(dimensions[1]);
	               	System.out.printf("Rows: %d, Columns: %d\n", width, height); //Do NOT CHANGE  - you must ouput this

	                array = new int[height][width];
	                int rowIndex = 0;

	                while ((line = br.readLine()) != null && rowIndex < height) {
	                    String[] values = line.split(",");
	                    for (int colIndex = 0; colIndex < width; colIndex++) {
	                        array[rowIndex][colIndex] = Integer.parseInt(values[colIndex]);
	                    }
	                    rowIndex++;
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return array;
	    }
	 
    public static void main(String[] args) throws IOException  {

    	ParalleliseGrid simulationGrid;  //the cellular automaton grid
    	  	
    	if (args.length!=2) {   //input is the name of the input and output files
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}
    	/* Read argument values */
  		String inputFileName = args[0];  //input file name
		String outputFileName=args[1]; // output file name
    
 
    	// This formula effectively increases cutoff for smaller grid
		int cutoff =  2400/ (Runtime.getRuntime().availableProcessors());
	   	// Read from input .csv file
    	simulationGrid = new ParalleliseGrid(new Grid(readArrayFromCSV(inputFileName)),cutoff);
    	Grid grid = simulationGrid.getGrid();
    	int rows = grid.getRows();

    	ForkJoinPool pool = ForkJoinPool.commonPool();

    	//for debugging - hardcoded re-initialisation options
    	//simulationGrid.set(rows/2,columns/2,rows*columns*2);
    	//simulationGrid.set(rows/2,columns/2,55000);
    	//simulationGrid.setAll(4);
    	//simulationGrid.setAll(8);
   	
    	int counter=0;
    	tick(); //start timer
    	if(DEBUG) {
    		System.out.printf("starting config: %d \n",counter);
    		simulationGrid.getGrid().printGrid();
    	}
    	

    	int [][] mergedGrid = pool.invoke(simulationGrid);
    	boolean nextStep = grid.nextTimeStep(1, rows+1, mergedGrid);
		// While there's a change, advance to next time step
    	while (nextStep) {
    		counter++;
    		simulationGrid.reinitialize();
    		mergedGrid = pool.invoke(simulationGrid);
    		nextStep = grid.nextTimeStep(1, rows+1, mergedGrid);	
    	}
    	pool.shutdown();
   		tock(); //end timer
   		long duration = endTime - startTime;
        System.out.println("Simulation complete, writing image...");
    	grid.gridToImage(outputFileName); //write grid as an image - you must do this.
    	//Do NOT CHANGE below!
    	System.out.printf("\t Rows: %d, Columns: %d\n", grid.getRows(), grid.getColumns());
		System.out.printf("Number of steps to stable state: %d \n",counter);
		System.out.printf("Time: %d ms\n",endTime - startTime );			/*  Total computation time */		
		writeCSV("./analyses/resultsParallel.csv", rows, grid.getColumns(), (int) duration, counter);
		grid.gridToCSV(String.format("./output/parallel/%dx%d.csv", rows, grid.getColumns()));
    }
}