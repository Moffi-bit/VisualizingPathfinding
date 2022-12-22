package cell;

/**
 * 
 * @author Lorenzo Stiavelli
 *
 */
public class Cell {
	
	private int type, xPos, yPos, value, id;
	
	public Cell(int type, int xPos, int yPos, int value, int id) {
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
		this.value = value;
		this.id = id;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getxPos() {
		return this.xPos;
	}

	public int getyPos() {
		return this.yPos;
	}

	public int getValue() {
		return this.value;
	}
	
	public int getID() {
		return this.id;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
