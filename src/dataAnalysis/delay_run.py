import subprocess
import time
import sys
""" 
To delay the run of a make command by a set time in seconds
"""

def delay_run(command, delayed_time):
    time.sleep(int(delayed_time))
    subprocess.run(["make", command])
    
if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])