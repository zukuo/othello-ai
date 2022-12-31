import java.util.ArrayList;

public class MoveChooser {

    public static int posInf = Integer.MAX_VALUE;
    public static int negInf = Integer.MIN_VALUE;

    public static int[][] staticEvalTable = {
        {120, -20, 20, 5, 5, 20, -20, 120},
        {-20, -40, -5, -5, -5, -5, -40, -20},
        {20, -5, 15, 3, 3, 15, -5, 20},
        {5, -5, 3, 3, 3, 3, -5, 5},
        {5, -5, 3, 3, 3, 3, -5, 5},
        {20, -5, 15, 3, 3, 15, -5, 20},
        {-20, -40, -5, -5, -5, -5, -40, -20},
        {120, -20, 20, 5, 5, 20, -20, 120}
    };
    
    public static Move chooseMove(BoardState boardState) {
        ArrayList<Move> moves = boardState.getLegalMoves();
        if (moves.isEmpty()) {
            return null;
        }
        
        // return moves.get(0);
        return topCall(boardState, moves);
    }
    
    public static Move topCall(BoardState boardState, ArrayList<Move> moves) {
        int alpha = negInf;
        int beta = posInf;
        int moveIndex = 0;
        Move bestMove = null;

        for (int i = 0; i < moves.size(); i++) {
            BoardState boardCopy = boardState.deepCopy();
            boardCopy.makeLegalMove(moves.get(i).x, moves.get(i).y);
            int eval = minimax(boardCopy, Othello.searchDepth-1, alpha, beta, false);
            if (alpha < eval) {
                alpha = eval;
                moveIndex = i;
            }
        }

        bestMove = moves.get(moveIndex);
        return bestMove;
    }

    public static int staticEvaluation(BoardState boardState) {
        int eval = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState.getContents(i, j) == 1) {
                    eval += staticEvalTable[i][j];
                } else if (boardState.getContents(i, j) == -1) {
                    eval -= staticEvalTable[i][j];
                }
            }
        }
        
        return eval;
    }

    public static int minimax(BoardState boardState, int depth, int alpha, int beta, boolean maximisingPlayer) {
        ArrayList<Move> moves = boardState.getLegalMoves();
        
        if (depth == 0 || boardState.gameOver()) {
            return staticEvaluation(boardState);
        }

        if (moves.size() == 0) {
            BoardState boardAlter = boardState.deepCopy();
            boardAlter.colour *= (-1);
            if (boardAlter.colour == 1) {
                return minimax(boardAlter, depth-1, alpha, beta, true);
            }
            else {
                return minimax(boardAlter, depth-1, alpha, beta, false);
            }
        }
        
        else if (maximisingPlayer == true) {
            alpha = negInf;
            for (int i = 0; i < moves.size(); i++) {
                BoardState boardCopy = boardState.deepCopy();
                boardCopy.makeLegalMove(moves.get(i).x, moves.get(i).y);
                int eval = minimax(boardCopy, depth-1, alpha, beta, false);
                alpha = Math.max(alpha, eval);
                if (alpha >= beta) {
                    break;
                }
            }
            return alpha;
        }

        else {
            beta = posInf;
            for (int i = 0; i < moves.size(); i++) {
                BoardState boardCopy = boardState.deepCopy();
                boardCopy.makeLegalMove(moves.get(i).x, moves.get(i).y);
                int eval = minimax(boardCopy, depth-1, alpha, beta, true);
                beta = Math.min(beta, eval);
                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        }

    }
}
