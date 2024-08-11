import csv
import sys
"""
run like this in command line
python csv_generator.py csv_generator "../../input/256x256.csv" 256 256 4
to create a 256x256 grid for e.g.
"""

def csv_generator(file_path, rows, columns, value):
    """ To quickly generate csv files 
    with each cell filled by the same value

    Args:
        file 
        rows (int): _description_
        columns (int): _description_
        values (any): value to fill each cell with
    """
    with open(file_path, mode="w") as file:
        writer = csv.writer(file, delimiter=",")
        writer.writerow([rows, columns])
        for i in range(int(rows)):
            columns_list = []
            for j in range(int(columns)):
                columns_list.append(value)
            writer.writerow(columns_list)
            

if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])
        