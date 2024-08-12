# validate_results.py
import csv
import pandas as pd
import sys

def validate_results(csv1, csv2, output_path):
   csv1 = pd.read_csv(csv1)
   csv2 = pd.read_csv(csv2)
   diff = csv1.compare(csv2)
   with open(output_path, mode="w") as o:
       if len(diff) ==0:
           o.write("No difference!")
       else:
           o.write(f"""The differences between the two grids are as followed:\n
{diff}""")

if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])