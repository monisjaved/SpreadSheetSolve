import java.util.*;

public class SpreadsheetSolve{

    public static void main(String... args){
        String[] tokens = {"2", "1", "+", "3", "*"};
        String temp = "2 2 * 2 +";
        tokens = temp.split(" ");
        int value = evalRPN(tokens);
        System.out.println(value);
    }
    public static int evalRPN(String[] tokens) {
 
        int returnValue = 0;
 
        String operators = "+-*/";
 
        Stack<String> stack = new Stack<String>();
 
        for(String t : tokens){
            if(!operators.contains(t)){
                stack.push(t);
            }else{
                int a = Integer.valueOf(stack.pop());
                int b = Integer.valueOf(stack.pop());
                int index = operators.indexOf(t);
                switch(index){
                    case 0:
                        stack.push(String.valueOf(a+b));
                        break;
                    case 1:
                        stack.push(String.valueOf(b-a));
                        break;
                    case 2:
                        stack.push(String.valueOf(a*b));
                        break;
                    case 3:
                        stack.push(String.valueOf(b/a));
                        break;
                }
            }
        }
 
        returnValue = Integer.valueOf(stack.pop());
 
        return returnValue;
 
    }

}