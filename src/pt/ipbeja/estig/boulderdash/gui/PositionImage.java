package pt.ipbeja.estig.boulderdash.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pt.ipbeja.estig.boulderdash.model.GetMap;
import pt.ipbeja.estig.boulderdash.model.Position;

import java.util.Arrays;

/**
 * The boulderdash image in position (knows its position in the grid)
 * 
 * @author João Paulo Barros
 * @version 2021/05/20
 * images generated in https://text2image.com/en/
 */
public class PositionImage extends ImageView
{
	public final static int SIZE = 100;
	private Position lineCol;

	public PositionImage(String imageName, Position lineCol)
	{
		// this could be used to populate icons on the map
		this.setImageByName(imageName);
		this.setLineColAndXY(lineCol);
	}

	public void setLineColAndXY(Position position) {
		this.lineCol = position;
		this.setX(position.getCol() * SIZE);
		this.setY(position.getLine() * SIZE);
	}

	public void updateLineColAndXY(int dCol, int dLine) {
		int col = this.lineCol.getCol() + dCol;
		int line = this.lineCol.getLine() + dLine;
		this.setLineColAndXY(new Position(line, col));
	}

	public void updateLineCol(int dCol, int dLine) {
		int col = this.lineCol.getCol() + dCol;
		int line = this.lineCol.getLine() + dLine;
		this.lineCol = new Position(line, col);
	}

	/**
	 * @return the position
	 */
	public Position getLineCol() {
		return this.lineCol;
	}

	/**
	 * sets the text and image for the button
	 * @param imageName image name to set
	 */
	public void setImageByName(String imageName)
	{
		assert(false);
		// https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx
		String filename = "/resources/images/" + imageName + ".png";
		if (imageName.equals("empty")) {
			this.setImage(null);
			System.out.println(Arrays.toString(GetMap.mapDimensions()));
			//System.out.println(Arrays.toString(GetMap.mapTopography()));
			char[][] arr = GetMap.mapTopography();
			for (int i = 0; i < arr.length; i++) {
				System.out.println(Arrays.toString(arr[i]));
			}
			System.out.println(Arrays.toString(GetMap.playerStart()));
			System.out.println(Arrays.toString(GetMap.movingEnemyStart()));
			System.out.println(Arrays.toString(GetMap.staticEnemyStart()));
			System.out.println(Arrays.toString(GetMap.bonusStart()));
		}
		else {
			Image img = new Image(filename, SIZE, SIZE, false, false);
			this.setImage(img);
		}
	}
}
