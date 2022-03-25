public class SmartLogic implements IQueensLogic{

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
        board[column][row] = 1;
    }    
}
