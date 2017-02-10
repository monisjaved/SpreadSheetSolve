

## SpreadSheetSolve
Java program to input comma separated spreadsheet with referenced values and output solved sheet including INVALID value output for invalid references.

### Instructions
1. The input will be a simplified version of a CSV file. The program should take a file as a command argument.
2. Rows are indexed as numbers (1, 2, 3, ...) and columns are indexed as capital letters (A, B, C, ...).
3. The maximum number of rows and columns is 9.
4. Cells are referenced by the row and column they are in. (e.g. A1, B3, D9)
5. The first row of the input will specify the size of the sheet and will be represented
as: (number of rows, number of columns) in the CSV file.
6. Formulas are specified in  reverse polish notation  (RPN) and space is used as the
separator. One major advantage of RPN is that precedence is unambiguous and
there is no complex grouping of expressions using parentheses.
7. Formulas may have references to other cells.
8. All values in cells are positive integers.
9. Output the results of the spreadsheet to the terminal (do not include the first row
of the spreadsheet).
10. Check if there are circular dependencies (e.g. the formula in cell A1 refers to B1 and the formula in cell B1 refers to A1), invalid references, poorly specified formula - see example below in examples section. If so, jump out of the computation, and update values of all invalid cells to be “#REF!”.

### Compilation Instructions

```
javac SpreadsheetSolve.java
```

### Running Instructions

```
java SpreadsheetSolve <path to input file>
```
