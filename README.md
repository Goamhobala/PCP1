To run a single of the serial/parallel version of the program
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

To compare two different csv files
```makefile 
make validate_results VALIDATE_ARGS="csv1_path csv2_path outputTxt_path"```

