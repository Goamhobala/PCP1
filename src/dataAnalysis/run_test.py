import subprocess
import sys
"""
A python script to run multiple tests
"""

def run_test(start, end, interval, value):
    with open("../../analyses/resultsSerial.csv", mode="w"):
        # to initialise the csv
        pass
    with open("../../analyses/resultsParallel.csv", mode="w"):
        pass
    number_runs = (end - start) // 2
    for i in range(number_runs):
        dimension = start + interval * i
        # To generate the csv file
        csv_info = "{dimension}x{dimension}_{value}.csv {dimension} {dimension} {value}"
        # For the parallel version
        subprocess.run(["make", "csv", f"CSV_INFO=..\parallelAbeliansandpile\input\{csv_info}"])
            # To run the trial
        subprocess.run(["make", "runParallel" ,"ARGS='..\parallelAbeliansandpile\input\{dimension}x{dimension}_{value}.csv ..\parallelAbeliansandpile\output\{dimension}x{dimension}_{value}.png'"])
        
        # For the serial version
        subprocess.run(["make", "csv", f"CSV_INFO=..\serialAbeliansandpile\input\{csv_info}"])
            # To run the trial
        subprocess.run(["make", "runSerial" ,"ARGS='..\serialAbeliansandpile\input\{dimension}x{dimension}_{value}.csv ..\serialAbeliansandpile\output\{dimension}x{dimension}_{value}.png'"])
        
        

if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])