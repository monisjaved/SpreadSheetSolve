import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;


public class SpreadsheetSolve{

	// store the whole spreadsheet
    public static HashMap<String,String> sheet = new HashMap<String,String>();
    // invalid value string
    public static String INVALID_VALUE = "#REF!";
    // regular expressions to match value types
    public static Pattern numeric = Pattern.compile("[0-9]+.?[0-9]*");
	public static Pattern oper = Pattern.compile("[+-/*^]");
	public static Pattern cell = Pattern.compile("[A-Z]+[0-9]+");
	
	// delimiters to split filestream line or cell values
	public static String DELIMITER = "\\s*,\\s*";
	public static String DELIMITER_TOKENS = "\\s";

	// handy function to convert number to character for column ref
    public static String getCharForNumber(Integer i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    // function to readCSV
    // params 
    // csvFile<String>: path of csvFile <extension doesnt matter as long as its csv
    // returns Integer array of sizes {row,col}
    public static Integer[] readCSV(String csvFile){
        Scanner scanner = null;
		try {
			scanner = new Scanner(new File(csvFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String line = scanner.nextLine();
        // get size from first line
        String[] size = line.split(DELIMITER);
        Integer numRows = Integer.parseInt(size[0]);
        Integer numCols = Integer.parseInt(size[1]);
        for(Integer i=0; i<numRows; i++){
        	line = scanner.nextLine();
        	// get data of one row
        	String[] data = line.split(DELIMITER);
        	for(Integer col=0; col<numCols; col++){
        		// split to get individual cell values
        		String cell = getCharForNumber(col+1) + String.valueOf(i+1);
        		/// store in hashmap
        		sheet.put(cell, data[col]);
        	}
        }
        scanner.close();
        Integer[] sizes = {numRows, numCols};
        return sizes;
    }
    
    
    // function to solve each cell of spreadsheet
    // params
    // sizes <Integer array>: 1x2 array of sizes {row,col}
    // returns void
    public static void solveSheet(Integer[] sizes){
    	Integer numRows = sizes[0];
    	Integer numCols = sizes[1];
    	// iterate through each cell in sheet
    	for(Integer i=1; i<=numRows; i++){
    		for(Integer j=1; j<=numCols; j++){
    			//get cellnumber for hashmap
    			new String();
				String cellNum = getCharForNumber(j) + String.valueOf(i);
				// get cell value from hashmap
				String[] tokens = sheet.get(cellNum).split(DELIMITER_TOKENS);
				// blank list of visited to be passed to evalCell
				List<String> visited = new ArrayList<String>();
				// get cellValue from evalCell
    			String cellValue = evalCell(cellNum, tokens, visited);
    			// output value
    			System.out.print(cellValue);
    			if(j < numCols){
    				System.out.print(", ");
    			}
    			else{
    				System.out.print("\n");
    			}
    		}
    	}    	
    }
    
    
    // function to evaluate cell Value
    // params
    // cellNum <String>: cellNumber [A1,B2,etc..]
    // tokens <String Array>: cell value for cellNum split by space
    // visited <List of String>: list of cell numbers already visited
    // return value of the cell 
	public static String evalCell(String cellNum, String[] tokens, List<String> visited) {
		
		// identify operators
		String operators = "+-*/";
		// create blank stack
		Stack<String> stack = new Stack<String>();
		
		if(tokens.length == 1){
			// if only one token then it can be either a number or another reference to cell
			if(numeric.matcher(tokens[0]).matches()){
				// return value if number
				return tokens[0];
			}
			if(cell.matcher(tokens[0]).matches()){
				// if already being processed then cyclic reference detected
				if(visited.contains(tokens[0])){
					// update the value of cell in hashmap for future reference
					sheet.put(tokens[0],INVALID_VALUE);
					return INVALID_VALUE;
				}
				// get value of the reference from sheet
				String t = sheet.get(tokens[0]);
				if(t == null){
					// if its not present then update in hashmap and return
					sheet.put(tokens[0], INVALID_VALUE);
					return INVALID_VALUE;
				}
				//add current cell being processed to visited list
				visited.add(cellNum);
				//evaluate reference value for usage
				String cellValue = evalCell(tokens[0], t.split(DELIMITER_TOKENS), visited);
				// remove current cell from visited list
				visited.remove(cellNum);
				// update reference value in hashmap for future reference
				sheet.put(tokens[0], cellValue);
				if(cellValue.equals(INVALID_VALUE)){
					return INVALID_VALUE;
				}
			}
		}
		else{
			// if more than one token then iterate
			for (String t : tokens) {
				if(numeric.matcher(t).matches()){
					// if number then add to stack
					stack.push(t);
				}
				if(cell.matcher(t).matches()){
					// if token is reference cell then evaluate it like above but 
					// add value to stack instead of returning it
					if(visited.contains(t)){
						sheet.put(t, INVALID_VALUE);
						return INVALID_VALUE;
					}
					String tokenValue = sheet.get(t);
					if(tokenValue == null){
						return INVALID_VALUE;
					}
					if(tokenValue.equals(INVALID_VALUE)){
						return INVALID_VALUE;
					}
					visited.add(cellNum);
					String cellValue = evalCell(t, tokenValue.split(DELIMITER_TOKENS), visited);
					visited.remove(cellNum);
					sheet.put(t, cellValue);
					if(cellValue.equals(INVALID_VALUE)){
						return INVALID_VALUE;
					}
					stack.push(cellValue);
				}
				if(oper.matcher(t).matches()){
					// if operator is found then process last two elements in stack
					int a = Integer.valueOf(stack.pop());
					int b = Integer.valueOf(stack.pop());
					switch (t) {
					case "+":
						stack.push(String.valueOf(a + b));
						break;
					case "-":
						stack.push(String.valueOf(b - a));
						break;
					case "*":
						stack.push(String.valueOf(a * b));
						break;
					case "/":
						stack.push(String.valueOf(b / a));
						break;
					}
				}
			}
			if(stack.size() == 1){
				// if stack size is 1 at the end then return the value
				String returnValue = stack.pop();
				return returnValue;
			}
			else{
				// if stack size is more than 1 then RPN is invalid
				return INVALID_VALUE;
			}
		}
		//if no condition is satisified then its invalid
		return INVALID_VALUE;
	}

    public static void main(String... args){
    	// get filename from argument
    	String fileName = args[0];
    	// get size of sheet 
    	Integer[] size = readCSV(fileName);
    	// print output of solved sheet
    	solveSheet(size);

    }

}