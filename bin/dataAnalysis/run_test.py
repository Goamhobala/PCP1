import subprocess
import sys
"""
A python script to run multiple tests
"""

def run_test(start, end, interval, value):
    number_runs = (end - start) / 2
    for i in range(number_runs):
        dimension = start + interval * i
        # To generate the csv file
        subprocess.run(["make", "csv", f"CSV_INF={dimension}x{dimension}_{value}.csv {dimension} {dimension} {value}"])
        # To run the trial
        subprocess.run(["make", "run" ,"ARGS='input\{dimension}x{dimension}_{value}.csv output\{dimension}x{dimension}_{value}.png'"])
        

if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])