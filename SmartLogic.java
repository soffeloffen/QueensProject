import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class SmartLogic implements IQueensLogic{

    /*TO RUN IN COMMAND PROMPT: java Queens.java SmartLogic 6 */ 

    /*
    
    CLASS DESCRIPTION
    
    */ 

    private int size;		// Size of quadratic game board (i.e. size = #rows = #columns)
    private int[][] board;	// Content of the board. Possible values: 0 (empty), 1 (queen), -1 (no queen allowed)
    private BDDFactory factory;
    private BDD True;
    private BDD False;
    private BDD bdd;
    private int n = 8;

    public void initializeBoard(int size) {
        this.size = size;
        this.board = new int[size][size];
        buildBDD();
        updateInvalid();
    }
   
    public int[][] getBoard() {
        return board;
    }

    public void insertQueen(int column, int row) {
       queenInsert(column, row);
    }   
    
    public void buildBDD() {
        //Factory
        this.factory = JFactory.init(2000000, 200000); // set buffer etc.

        //64 fields => 64 variables
        this.factory.setVarNum(this.n*this.n);

        //For clarity
        this.False = this.factory.zero();
        this.True = this.factory.one();

        //Initalize our bdd to true
        this.bdd = True;

        //Add the rules to the bdd
        createRules();
        createEightRule();
    }

    public void createEightRule(){

        for (int y = 0; y < n; y++) {
            BDD sub_bdd = False;
           
          for (int x = 0; x < n; x++) {
            sub_bdd = sub_bdd.or(this.factory.ithVar(place(x,y)));
          }
          
          //sub_bdd must be true
          this.bdd = this.bdd.and(sub_bdd);
        }
    }

    private void createRules() {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                createCellRule(x,y);
            }
        }
      }

      private void createCellRule(int x,int y) {
        BDD sub_bdd = False;
        BDD rest_false_bdd = True;
        
        //All other y's must be false
        for (int yy = 0; yy < n; yy++) {
          if (y != yy) {
            rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(x,yy)));
          }
        }
  
        //All other x's must be false
        for (int xx = 0; xx < n; xx++) {
          if (x != xx) {
            rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(xx,y)));
          }
        }
  
        //All other y+xx-x must be false
        for (int xx = 0; xx < n; xx++) {
          if (x != xx) {
            if ((y+xx-x < 8) && (y+xx-x > 0)) {
              rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(xx,y+xx-x)));
            }
          }
        }
  
        //All other y-xx+xx must be false
        for (int xx = 0; xx < n; xx++) {
          if (x != xx) {
            if ((y-xx+x < 8) && (y-xx+x > 0)) {
              rest_false_bdd = rest_false_bdd.and(this.factory.nithVar(place(xx,y-xx+x)));
            }
          }
        }
  
        //Either the x,y is false
        sub_bdd = sub_bdd.or(this.factory.nithVar(place(x,y)));
        //Or (if the x,y is true) the rest is false
        sub_bdd = sub_bdd.or(rest_false_bdd);
  
        //sub_bdd must be true
        this.bdd = this.bdd.and(sub_bdd);
      }
  

    private int place(int column,int row) {
        return row*this.n+column;
      }


    /** Use n*n variables and make a method for retrieving the correct 
     * variable or variable number for a given position (column and row).*/
    public int retrieveVariable(){
        return 1;
    }

    private boolean placeInvalid(int column,int row) {
        //add queen at x ,y
        BDD test_bdd = this.bdd.restrict(this.factory.ithVar(place(column,row))); 
        //check if unsatisfiable?
        return test_bdd.isZero();
      }

      private void updateInvalid() {
        //For each cell, check if placing a queen there makes it invalid
        //If so, make that places graphic be -1
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (placeInvalid(i,j)) {
                  board[i][j] = -1;
                }
            }
        }
    }
     
    public boolean queenInsert(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        //Set a queen in graphic
        board[column][row] = 1;

        //Set a queen in the bdd
        this.bdd = this.bdd.restrict(this.factory.ithVar(row*this.n+column)); 

        updateInvalid();

        return true;
    }
}
