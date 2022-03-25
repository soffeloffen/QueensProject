public class SmartLogic implements IQueensLogic{

    /*TO RUN IN COMMAND PROMPT: java Queens.java SmartLogic 6 */ 

    /*
    
    CLASS DESCRIPTION
    
    */ 

    private int size;		// Size of quadratic game board (i.e. size = #rows = #columns)
    private int[][] board;	// Content of the board. Possible values: 0 (empty), 1 (queen), -1 (no queen allowed)
    
    public void initializeBoard(int size) {
        this.size = size;
        this.board = new int[size][size];
    }
   
    public int[][] getBoard() {
        return board;
    }

    public void insertQueen(int column, int row) {
        
    }   
    
    /** Use n*n variables and make a method for retrieving the correct 
     * variable or variable number for a given position (column and row).*/
    public int retrieveVariable(){
        return 1;
    }
}
