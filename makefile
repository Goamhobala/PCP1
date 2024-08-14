
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
RUN_ARGS ?= input/64x64_6.csv output/64x64_6.png
VALIDATE_ARGS ?= output/parallel/64x64.csv output/serial/64x64.csv output/comparisons/64x64.txt
CSV_INFO ?= 64 64 6
TEST_INFO ?= 16 160 10 4
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


validate_results:
	python3 $(SRC_DIR_ANALYSIS)/validate_results.py validate_results $(VALIDATE_ARGS)$

delay_command:
	python3 $(SRC_DIR_ANALYSIS)/delay_run.py delay_run $(TIME_DELAY) $(DELAYED_COMMAND)

clean:
	rm -rf bin/*

csv:
	python3 $(SRC_DIR_ANALYSIS)/csv_generator.py csv_generator $(CSV_INFO)

run_serial: all
	java -classpath bin $(MAIN_CLASS_SERIAL) $(RUN_ARGS)

run_parallel: all
	java -classpath bin $(MAIN_CLASS_PARALLEL) $(RUN_ARGS)

run: run_parallel

run_test:
	python3 $(SRC_DIR_ANALYSIS)/run_test.py run_test $(TEST_INFO) $(TIME_DELAY)


