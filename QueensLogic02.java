import java.util.*;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class QueensLogic02 implements IQueensLogic{
    private int[][] board;
	private int size; 
	
	private final int nodes = 2000000;
	private final int cache = 200000;

	public BDDFactory fact = JFactory.init(nodes, cache);

	private BDD game_rules = fact.one();
	private BDD restrictions = fact.one();
	private HashSet<Integer> n_queens = new HashSet<Integer>();

	public QueensLogic02(){
		// Constructor
	}

    @Override
	public void initializeBoard(int size) {
		this.size = size;
		this.board = new int[size][size];
		setGameRules();
	}

    @Override
    public int[][] getBoard() {
		return board;
	}

    @Override
    public void insertQueen(int column, int row) {

        if (board[column][row] == -1) { 
                return;
            } 
    
            if (board[column][row] == 1) { 
                board[column][row] = 0;
                removeQueen(row * size + column);
            } else {
                board[column][row] = 1; 

                setQueen(row * size + column);
            }
    
            boolean solved = restrictions.pathCount() == 1;
                                                          
    
            for (int r = 0; r < size; r++) { 
                for (int c = 0; c < size; c++) {
                    if (board[c][r] == 1)
                        continue; 
                    if (restrictions.restrict(fact.ithVar(r * size + c)).isZero()) { 
                                                                                                                
                        board[c][r] = -1;
                    } else if (solved) { 
                                            
                        board[c][r] = 1;
                        setQueen(r * size + c);
                    } else {
                        board[c][r] = 0; 
                    }
                }
            }
        }
        

	public BDD setNQueens() {
		BDD rule = fact.one();
		for (int row = 0; row < size; row++) { 
			BDD copy = fact.zero(); 

			for (int col = 0; col < size; col++) { 
				copy.orWith(fact.ithVar(row * size + col)); 
			}
			rule.andWith(copy); 
		}

		return rule;
	}

	public BDD setRule(int i, Iterable<Integer> excludedVars) {
		BDD rule = fact.one();
		for (int excluded : excludedVars) {
			rule.andWith(fact.nithVar(excluded)); 
		}
		return fact.ithVar(i).imp(rule); 
	}


	private void setGameRules() {
		fact.setVarNum(size); 
		for (int n = 0; n < size; n++) { 
			game_rules.andWith(setRule(n, getRestrictions(n)));
		}
	
		game_rules.andWith(setNQueens());
	}

		// get all the positions that is mutually exclusive with for position 'pos' due to the rows-, columns- and diagonal restrictions
		private ArrayList<Integer> getRestrictions(int pos) {
			ArrayList<Integer> attacking_positions = new ArrayList<Integer>();
			int col = pos % size;

			// get all the indices on the same column as variable posision 'pos'
			for (int n = 0; n < size; n++) {
				int p = col + n * size;
				if (p == pos)
					continue;
				attacking_positions.add(p);
			}

			// get all the indices on the same row as variable 'pos'
			for (int m = 0; m < size; m++) {
				int p = m + size * (pos / size);
				if (p == pos)
					continue;
				attacking_positions.add(p);
			}

			// get all the indices on the two diagonals intersecting 'pos'
			int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, 1 }, { 1, -1 } };

			for (int[] vector : directions) {
				int row = pos / size + vector[0];
				col = pos % size + vector[1];
				while (row >= 0 && row < size && col >= 0 && col < size) {
					attacking_positions.add(row * size + col);
					row += vector[0];
					col += vector[1];
				}
			}
			return attacking_positions;
		}
	

	private void setQueen(int pos) {
		n_queens.add(pos);
		updateRestrictions();
	}

	private void removeQueen(int pos) {
		n_queens.remove(pos);
		updateRestrictions();
	}

	private BDD updateGameBoard() {
		BDD updated_bdd = fact.one();
		for (int n : n_queens) {
			updated_bdd.andWith(fact.ithVar(n));
		}
		return updated_bdd;
	}

	// update restriction after placement or removal of queen
	private void updateRestrictions() {
		restrictions = game_rules.restrict(updateGameBoard());
	}


}
