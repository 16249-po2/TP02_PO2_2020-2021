package pt.ipbeja.estig.boulderdash.model;

/**
 * Positions from 0 to number of lines and columns minus 1
 * @author João Paulo Barros
 * @version 2014/05/18 - 2016/04/03
 */ 
public class AbstractPosition
{
   private final int line, col;
   private final char text;
   
   public AbstractPosition(int line, int col, char text)
   {
      this.line = line;
      this.col = col;
      this.text = text;
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "(" + line + ", " + col + ")";
   }

   /**
    * @return the line
    */
   public int getLine()
   {
      return this.line;
   }

   /**
    * @return the col
    */
   public int getCol()
   {
      return this.col;
   }

   public char getText()
   {
      return this.text;
   }

   /**
    * Checks if position is inside the board
    * @return true if inside, false otherwise
    */
   public boolean isInside()
   {
      return AbstractPosition.isInside(this.getLine(), this.getCol());
   }
   
   /**
    * Checks if line col are inside tha board
    * @param line
    * @param col
    * @return true if inside, false otherwise
    */
   public static boolean isInside(int line, int col)
   {
      return 0 <= line && line < BoulderDashModel.N_LINES &&
             0 <= col && col < BoulderDashModel.N_COLS;
   }
}
