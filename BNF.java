/* Zach Barnhart
Input an expression following this BNF:
<expression> ::= <term> + <expression> | <term> - <expression> | <term>
<term> ::= <factor> * <term> | <factor> / <term> | <factor>
<factor> ::= <digit> | ( <expression> )
<digit> ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
 */

import java.io.*;

public class BNF  { //Driver program

    public static void main(String[] args) throws IOException
    {
        String ch = "yes"; // We will create a driver that allows us to try another expression after the first. This initiates to allow us to evaluate at least one expression
        BufferedReader keyboard = new BufferedReader( new InputStreamReader(System.in));
        while (ch.equals("yes")) { //this is the loop that allows us to try several expressions

            Tree tree = new Tree(); //we will make an expression tree for the input string
            
            System.out.println("Enter the expression to be evaluated:");
            String exp = keyboard.readLine();
            tree.analyze(exp); //this function turns the string input into an expression tree following the BNF rules
            
            System.out.println("Preorder Traversal of Expression Tree:");
            tree.showPreOrderTree(tree.root); // this method traverses the tree by using prefix notation rules
            System.out.println();
            
            System.out.println("Postorder Traversal of Expression Tree:");
            tree.showPostOrderTree(tree.root); // this method traverses the tree using postfix notation rules
            System.out.println();

            StringReorder test = new StringReorder(exp); //this turns the input string into a "StringReorder" object, which has some methods we can work with
            String str = test.reorder(); //this reorders the StringReorder object to be a string of the arguments and operators written in postfix notation
            int result = Stringanalyzer(str); //This evaluates the above string
            System.out.println("Following the rules of the BNF, the expression evaluates to " + result);
            System.out.println("Enter \"yes\" to evaluate another expression, \"no\" to quit");
            ch = keyboard.readLine(); //allows the user to type "yes" to enter a second expression
        }
    }

    public static int Stringanalyzer(String str) { //This method takes a string that is a series of arguments and postfix operators and evaluates them

        IntStack stack = new IntStack(str.length()); //we put all integers in a stack

        for(int i = 0; i<str.length(); i++) {

            char ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                stack.push(Character.getNumericValue(ch)); //if a character in a sting is an integer, put it on the stack
            } else {
                int result; //if an operator, pull two integers off the stack, perform the operation, and push the result back on the stack
                int int1 = stack.pop();
                int int2 = stack.pop();
                switch (ch)
                {
                    case '+':
                        result = int2 + int1;
                        stack.push(result);
                        break;
                    case '-':
                        result = int2 - int1;
                        stack.push(result);
                        break;
                    case '*':
                        result = int2 * int1;
                        stack.push(result);
                        break;
                    case '/':
                        result = int2 / int1;
                        stack.push(result);
                        break;
                }

            }
        }
        return stack.pop(); //after the above loop is complete, pop the last integer off the stack for the final result
    }
}

class Node {
    
    public char data; //binary trees need nodes. Nodes have their own data, and point to a left node and right node (possibly null)
    public Node left; // note that the data type for our Nodes is char, so we have to be careful later to not mistake the data type for int for certain nodes
    public Node right;

    public Node(char ch) { // constructor method takes a char and makes a node with that char as data
        data = ch;
    }
}

class Tree {

    public Node root; //trees must have a root node

    public Tree() {
        root = null; //though it may be null
    }

    public void analyze(String str) { //this method takes a string, reorders it to have postfix order, and

        StringReorder stt = new StringReorder(str);
        str = stt.reorder(); // make str have the right postfix order
        NodeStack stack = new NodeStack(str.length());
        str = str + "#"; //I basically just add this sign to make my while loop easier to define. # is the end of the string
        int i = 0;
        char symb = str.charAt(i);
        Node tempNode;
        while (symb != '#') { //for all characters before the end of string

            if (Character.isDigit(symb)) {
                tempNode = new Node(symb); //if character is a number, we make a node for it...
                stack.push(tempNode); //and push the node on the stack
            } else if (symb == '+' || symb == '-' || symb == '/' || symb == '*') { //if the character is an operator
                Node node1 = stack.pop(); //we pull two ints off the stack
                Node node2 = stack.pop();
                tempNode = new Node(symb); //make a node for the operator
                stack.push(tempNode); //push it on the stack
                tempNode.left = node2; //and make the two ints its children, so the operator operates on the last two ints on the stack...
                tempNode.right = node1; //as it should be in postfix order
            }
            symb = str.charAt(++i); //symb becomes next character in string
        }
        root = stack.pop(); //at the end, in postfix notation, the last item on the stack will be an operation, and it will be the last operation perform, and thus at the top of the tree as the root
    }

    public void showPreOrderTree(Node temproot) { //this method traverses the tree and displays prefix operators

        if (temproot != null) { //recall that in an expression tree, the operators are the internal nodes. so all we have to do to make a preorder traversal is print operator first, then the leaves

            System.out.print(temproot.data); //print operator first
            showPreOrderTree(temproot.left); //then left leaf and/or associated subtree
            showPreOrderTree(temproot.right); //then right leaf and/or associated subtree
        }
    }

    public void showPostOrderTree(Node temproot) { //see above comments. to make a postorder traversal, we print operator after the leaves and/or associated subtrees

        if (temproot != null) {

            showPostOrderTree(temproot.left); //print left leaf and/or associated subtree
            showPostOrderTree(temproot.right); //print right leaf and/or associated subtree
            System.out.print(temproot.data); //print operator
        }
    }
}

class IntStack { //we use the intstack to evaulate a postfix expression. ints go on the stack, operators pull them off, perform operation, and push result on the stack

    private int[] stack; //we implement this stack with an array since we know about how many ints we may need
    private int top; //we use one int to specify where the top currently is

    public IntStack(int explength) {
        stack = new int[explength]; //we make a stack as big as the length of expression being evaulated. We could be more efficient, since we know that only slightly more than half of the expression will be ints, but since we are only evaulating small expressions, we didn't bother
        top = 0; //this was chosen to be 0 rather than -1 to avoid an index out of bounds error that would happen with our stringanalyzer method above if we didn't. since we don't need to know when the stack is empty, this is fine
    }

    public void push(int key) { 
        stack[++top] = key; //to put a number on the stack, increase the top index by one and reference the new number there
    }

    public int pop() {
        return (stack[top--]); //to take a number off the stack
    }

}

class NodeStack { //this is an exact analog of the IntStack, but with Nodes instead of ints

    private Node[] stack;
    private int top;

    public NodeStack(int explength) {
        stack = new Node[explength]; //this time, the stack will actually be this size, so we are less inefficient
        top = -1; //based on how our pop method works, we must chose top=-1 as the empty state
    }

    public void push(Node key) {
        stack[++top] = key;
    }

    public Node pop() {
        return (stack[top--]);
    }
    
}

class CharStack { //exact analog of NodeStack, but with chars

    private char[] stack;
    private int top;

    public CharStack(int explength) {
        stack = new char[explength];
        top = -1;
    }

    public void push(char key) {
        stack[++top] = key;
    }

    public char pop() {
        return (stack[top--]);
    }

    public boolean isEmpty() { //we need an isEmpty method, where our definition of empty as top==-1 finally becomes important
        return (top == -1);
    }
}

class StringReorder { //This is where the magic happens. We take a string and turn it into a string and stack in the right order
    
    private CharStack stack; //stack we need to properly reorder everything
    private String input; //input of the user
    private String output = ""; //properly reordered output

    public StringReorder(String str) { //constructor method which takes string and...
        input = str;
        stack = new CharStack(str.length()); //...makes a CharStack of the proper size which will allow us to properly reorder everything
    }

    public String reorder() {
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i); //for each character, look at its BNF classification
            switch (ch)
            {
                case '+':
                case '-':
                    opanalyzer(ch, 1); //if it is an expression, evaluate last
                    break;
                case '*':
                case '/':
                    opanalyzer(ch, 2); //if it is a term, eveluate next to last
                    break;
                case '(':
                    stack.push(ch); //parenthesis make a factor, which must be evaluated first
                    break;
                case ')':
                    parenthesis();
                    break;
                case ' ':
                    break; //if it is a space, skip it. this gives some flexibility if users do not use proper spacing in their input epxression
                default:
                    output = output + ch; //if it is a number, simply add it onto output string
            }
        }
        while (!stack.isEmpty())
            output = output + stack.pop(); //once we are done, pop everything off the stack
        return output;
    }

    private void opanalyzer(char oper, int type) { //if given a character and type
        while (!stack.isEmpty()) { //if the stack isn't empty
            char op = stack.pop(); //we peek at the previous operator
            if (op == '(') { //if it's a parenthesis
                stack.push(op); //we put it back on the stack, and
                break; // we break opanalyzer and finish the calling method to build the mini tree
            } else { // otherwise, if not a parenthesis
                int typecomp;
                if (op == '+' || op == '-') { //if the op we peeked at is expression
                    typecomp = 1; //it has value '1'
                } else {
                    typecomp = 2; //otherwise, if it is a term, it has value '2'
                }
                if (typecomp <= type) { //the new oper will be evaluated first unless the one we peeked at is a term and the new one is an expression
                    stack.push(op); //to make the new oper get evaluated first, we put the old one on the stack first
                    break;
                } else {
                    output = output + op; //but if the old op needs to get evaluated first, we go ahead and add it to the output string
                }
            }
        }
        stack.push(oper); //finally we throw the new oper on the stack, with or without the old op below it depending on the above analysis
    }

    private void parenthesis() { //when we have parenthesis...
        while (!stack.isEmpty()) { //...as long as the stack isn't empty...
            
            char ch = stack.pop(); //...we pop everything off the stack to make its own mini expression tree...
            if (ch == '(') { //...until we hit the initial parenthesis that started it all
                break; //...in which case we stop because we are done...
            }
            else {
                output = output + ch; //...but until we do, we make a string which will be inserted as is in the postfix expression
            }
        }
    }
}