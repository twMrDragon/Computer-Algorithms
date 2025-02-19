JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin

# 找出 HW1.java 和所有 Test*.java 檔案
HW1_SOURCE = $(SRC_DIR)/HW1.java
TEST_SOURCES = $(wildcard $(SRC_DIR)/Test*.java)
TEST_NAMES = $(basename $(notdir $(TEST_SOURCES)))

all: compile_hw1 compile_tests run_tests

compile_hw1: $(HW1_SOURCE)
	$(JAVAC) -d $(BIN_DIR) $(HW1_SOURCE)

compile_tests: $(TEST_SOURCES)
	$(JAVAC) -d $(BIN_DIR) -cp $(BIN_DIR) $(TEST_SOURCES)

# 逐個執行所有 Test 類別
run_tests: $(TEST_CLASSES)
	@for %%t in ($(TEST_NAMES)) do ( \
		echo Running %%t... && \
		$(JAVA) -ea -cp $(BIN_DIR) %%t \
	)

clean:
	del $(BIN_DIR)\*.class