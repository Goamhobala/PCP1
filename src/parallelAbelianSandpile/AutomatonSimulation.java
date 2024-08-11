//Copyright M.M.Kuttel 2024 CSC2002S, UCT
package parallelAbelianSandpile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
/* Serial  program to simulate an Abelian Sandpile cellular automaton
 * This is the reference sequential version (Do not modify this code)
 * Michelle Kuttel 2024, University of Cape Town
 * Adapted from "Abelian Sandpile Simulation"
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
	
	public static void writeCSV(String filePath, int rows, int columns, int time, int steps) {
		String delimiter = ",";
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
			writer.write(rows + delimiter + columns + delimiter + time + delimiter + steps + "\n");
			
		}catch (IOException e){
			e.printStackTrace();	
		}
	}
	
	//input is via a CSV file
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
    
    	// Read from input .csv file
    	simulationGrid = new ParalleliseGrid(new Grid(readArrayFromCSV(inputFileName)));
    	Grid grid = simulationGrid.getGrid();
    	int rows = grid.getRows();
    	int cutoff = rows / (Runtime.getRuntime().availableProcessors() - 2);
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
    	
//		while(simulationGrid.compute()) {//run until no change
//	    		if(DEBUG) simulationGrid.getGrid().printGrid();
//	    		counter++;
//	    	}
// While there's a change, advance to next time step
    	int [][] mergedGrid = pool.invoke(simulationGrid);
    	boolean nextStep = grid.nextTimeStep(1, rows+1, mergedGrid);
    	while (nextStep) {
    		counter++;
    		simulationGrid = new ParalleliseGrid(grid, 1, rows, cutoff);
    		mergedGrid = pool.invoke(simulationGrid);
    		nextStep = grid.nextTimeStep(1, rows+1, mergedGrid);	
    	}
    	pool.shutdown();
   		tock(); //end timer
   		long duration = endTime - startTime;
        System.out.println("Simulation complete, writing image...");
    	grid.gridToImage(outputFileName); //write grid as an image - you must do this.
    	//Do NOT CHANGE below!
    	//simulation details - you must keep these lines at the end of the output in the parallel versions      	System.out.printf("\t Rows: %d, Columns: %d\n", simulationGrid.getRows(), simulationGrid.getColumns());
		System.out.printf("Number of steps to stable state: %d \n",counter);
		System.out.printf("Time: %d ms\n",endTime - startTime );			/*  Total computation time */		
		writeCSV("./analyses/resultsParallel.csv", rows, grid.getColumns(), (int) duration, counter);
    }
}