import csv
import sys

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

        for i in range(int(rows)):
            columns_list = []
            for j in range(int(columns)):
                columns_list.append(value)
            writer.writerow(columns_list)
            

if __name__ == "__main__":
    args = sys.argv
    globals()[args[1]](*args[2:])
        