**Jing Yeh**
**YHXJIN001**

## Table of Contents
[[#1. Introduction]]
* [[#1.1 General Introduction]]
- [[#1.2 Parallelisation Approach]]
* [[#1.3 Validation of Algorithm]]
* [[#1.4 Benchmarking]]
[[#Discussion]]
[[#Conclusion]]
## 1. Introduction
####     1.1 General Introduction
This assignment deals with parallel computing. We are provided with the serial program that simulates Abelian sandpiles. Our task is to code a parallel version of the same program, and then report on the potential speed ups for different grid sizes.

We have included make commands to run both the serial and parallel versions, as well as a few commands to run some python scripts to help with the experiment. A summary is given below.

To run one trial of the serial/parallel version of the program
```makefile
make run_serial $(RUN_ARGS="input_path output_path")$
make run_parallel $(RUN_ARGS="input_path output_path")$
```

To run a series of trials of both the serial and parallel version of the programs **Note:** results are automatically written to *"analyses/resultsParallel.csv"* and
*"analyses/resultsSerial.csv"* respectively.
```
make run_test $(TEST_INFO="start end intervalSize(>=1) values")$
// For example:
make run_test TEST_INFO="500 1000 100 6"
// Will run 5 runs of both the serial and parallel versions // of the program on 500x500 600x600 ...... 1000x1000 grids // with each cell filled with 6 
```
A csv generator to quickly create grids filled by the same value is included for convenience
```
make csv $(CSV_INFO="rows columns value")$
// For example:
make csv CSV_INFO="517 517 8"
// Will genearte a csv file with 517 rows, 517 columns, with // each cell filled by 8
```
####     1.2 Parallelisation Approach
The parallelisation is done as followed:
- Calculate the cutoff value using the empirically determined formula
$$
\frac{rows\left( 1+\frac{500}{rows} \right)}{processors - 1}	
$$
Note that the -1 is to account for the main thread
* We will then recursively divide the grid into smaller sub-grids, using the *compute* method of a wrapper class *ParalleliseGrid*. The wrapper class extends from *RecursiveTask*, and parallelise the *Grid* object
* Once the sub-grid size goes below the cut-off value. We update the sub-grid by saving the updated value to a local grid, the *localUpdatedGrid*, each stored by an instance of *ParalleliseGrid*.
* After the updates are done, we then merge the local grids. The merged grid will then be passed to the 
* 
	

####     1.3 Validation of Algorithm
####     1.4 Benchmarking

## Discussion

## Conclusion

### Git log
The first and last 10 lines as specified is included here:
![[Pasted image 20240309230724.png]]
However, here's one that more clearly demonstrates usage of git.
![[Pasted image 20240309230825.png]]