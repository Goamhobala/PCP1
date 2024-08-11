# # Makefile for compiling and running serial program

# # Directories
# SRC_DIR_SERIAL = src/serialAbelianSandpile
# BIN_DIR_SERIAL = bin/serialAbelianSandpile
# SRC_DIR_PARALLEL = src/parallelAbelianSandpile
# BIN_DIR_PARALLEL = bin/parallelAbelianSandpile
# # Source files
# JAVA_FILES_SERIAL = $(wildcard $(SRC_DIR_SERIAL)/*.java)

# # Compiled class files
# CLASS_FILES = $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(JAVA_FILES))

# # Compilation flags
# JAVAC_FLAGS = -d bin -sourcepath src

# # Main class
# MAIN_CLASS = serialAbelianSandpile.AutomatonSimulation

# # Default arguments (update these if needed)
# #ARGS ?= input/65_by_65_all_4.csv output/2TEST65_by_65_all_4.png  # Replace 'default_arguments' with your specific default arguments, if any
# ARGS ?= input/517_by_517_centre_534578.csv output/2TEST517_by_517_all_4.png 

# CSV_INFO ?= input/1024x1024.csv 1024 1024 4
# # Targets
# .PHONY: all clean run directories

# all: directories $(CLASS_FILES)

# directories:
# 	@mkdir -p $(BIN_DIR)

# $(BIN_DIR)/%.class: $(SRC_DIR)/%.java
# 	javac $(JAVAC_FLAGS) $<

# clean:
# 	rm -rf bin/*

# csv:
# 	python3 $(SRC_DIR)/csv_generator.py csv_generator $(CSV_INFO)

# run: all
# 	java -classpath bin $(MAIN_CLASS) $(ARGS)

# Makefile for compiling and running both serial and parallel versions of the program

# Directories
SRC_DIR_SERIAL = src/serialAbelianSandpile
BIN_DIR_SERIAL = bin/serialAbelianSandpile
SRC_DIR_PARALLEL = src/parallelAbelianSandpile
BIN_DIR_PARALLEL = bin/parallelAbelianSandpile
SRC_DIR_ANALYSIS = src/dataAnalysis

# Source files
JAVA_FILES_SERIAL = $(wildcard $(SRC_DIR_SERIAL)/*.java)
JAVA_FILES_PARALLEL = $(wildcard $(SRC_DIR_PARALLEL)/*.java)

# Compiled class files
CLASS_FILES_SERIAL = $(patsubst $(SRC_DIR_SERIAL)/%.java, $(BIN_DIR_SERIAL)/%.class, $(JAVA_FILES_SERIAL))
CLASS_FILES_PARALLEL = $(patsubst $(SRC_DIR_PARALLEL)/%.java, $(BIN_DIR_PARALLEL)/%.class, $(JAVA_FILES_PARALLEL))

# Compilation flags
JAVAC_FLAGS = -d bin -sourcepath src

# Main classes
MAIN_CLASS_SERIAL = serialAbelianSandpile.AutomatonSimulation
MAIN_CLASS_PARALLEL = parallelAbelianSandpile.AutomatonSimulation

# Default arguments (update these if needed)
ARGS ?= input/128x128.csv output/128x128.png

CSV_INFO ?= input/1024x1024.csv 1024 1024 4
TEST_INFO ?= 16 156 10 4
TIME_DELAY ?= 0
DELAYED_COMMAND ?= run_parallel
# Targets
.PHONY: all clean run directories

all: directories_serial directories_parallel $(CLASS_FILES_SERIAL) $(CLASS_FILES_PARALLEL)

directories_serial:
	@mkdir -p $(BIN_DIR_SERIAL)

directories_parallel:
	@mkdir -p $(BIN_DIR_PARALLEL)

$(BIN_DIR_SERIAL)/%.class: $(SRC_DIR_SERIAL)/%.java
	javac $(JAVAC_FLAGS) $<

$(BIN_DIR_PARALLEL)/%.class: $(SRC_DIR_PARALLEL)/%.java
	javac $(JAVAC_FLAGS) $<



delay_command:
	python3 $(SRC_DIR_ANALYSIS)/delay_run.py delay_run $(DELAYED_COMMAND) $(TIME_DELAY)

clean:
	rm -rf bin/*

csv:
	python3 $(SRC_DIR_ANALYSIS)/csv_generator.py csv_generator $(CSV_INFO)

run_serial: all
	nohup java -classpath bin $(MAIN_CLASS_SERIAL) $(ARGS)

run_parallel: all
	nohup java -classpath bin $(MAIN_CLASS_PARALLEL) $(ARGS)

# run: run_serial run_parallel

run_test:
	python3 $(SRC_DIR_ANALYSIS)/run_test.py run_test $(TEST_INFO) $(TIME_DELAY)
