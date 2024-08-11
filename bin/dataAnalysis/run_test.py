import subprocess
import sys
import os
"""
A python script to run multiple tests
"""

def run_test(start, end, interval, value):
    with open("./analyses/resultsSerial.csv", mode="w"):
        # to initialise the csv
        pass
    with open("./analyses/resultsParallel.csv", mode="w"):
        pass
    number_runs = (int(end) - int(start)) // int(interval)
    for i in range(number_runs + 1):
        dimension = int(start) + int(interval) * i
        # To generate the csv file
        csv_info = f"{dimension}x{dimension}_{value}.csv {dimension} {dimension} {value}"
        input_path = f"./input/{dimension}x{dimension}_{value}.csv"
        print(input_path)
        # For the parallel version
        subprocess.run(["make", "csv", f"CSV_INFO=./input/{csv_info}"])
            # To run the trial
        subprocess.run(["make", "run_parallel" ,f'ARGS={input_path} ./output/parallel/{dimension}x{dimension}_{value}.png'])
        
        # For the serial version
        subprocess.run(["make", "csv", f"CSV_INFO=./input/{csv_info}"])
            # To run the trial
        subprocess.run(["make", "run_serial" ,f'ARGS={input_path} ./output/serial/{dimension}x{dimension}_{value}.png'])
        
        # clean up the csv files generated for the trial
        os.remove(input_path)
        

if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])