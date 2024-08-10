# Makefile for compiling and running serial program

# Directories
SRC_DIR = src/serialAbelianSandpile
BIN_DIR = bin/serialAbelianSandpile

# Source files
JAVA_FILES = $(wildcard $(SRC_DIR)/*.java)

# Compiled class files
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(JAVA_FILES))

# Compilation flags
JAVAC_FLAGS = -d bin -sourcepath src

# Main class
MAIN_CLASS = serialAbelianSandpile.AutomatonSimulation

# Default arguments (update these if needed)
#ARGS ?= input/65_by_65_all_4.csv output/2TEST65_by_65_all_4.png  # Replace 'default_arguments' with your specific default arguments, if any
ARGS ?= input/517_by_517_centre_534578.csv output/2TEST517_by_517_all_4.png 

CSV_INFO ?= input/1024x1024.csv 1024 1024 4
# Targets
.PHONY: all clean run directories

all: directories $(CLASS_FILES)

directories:
	@mkdir -p $(BIN_DIR)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	javac $(JAVAC_FLAGS) $<

clean:
	rm -rf bin/*

csv:
	python3 $(SRC_DIR)/csv_generator.py csv_generator $(CSV_INFO)

run: all
	java -classpath bin $(MAIN_CLASS) $(ARGS)

