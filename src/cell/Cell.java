package cell;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Cell {
	
	private int type, xPos, yPos, value;
	
	public Cell(int type, int xPos, int yPos, int value) {
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
