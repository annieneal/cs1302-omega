package cs1302.game;

/**
 * This class represents a Snake object.
 */
public class Snake {

    int x;
    int y;

    /**
     * Constructs a link of a {@code Snake} object.
     * @param x the x coordinate of the snake link
     * @param y the y coordinate of the snake link
     */
    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
    } //Snake

    /**
     * Setter for x value.
     * @param x the value to set the x coordinate to
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter for y value.
     * @param y the value to set the y coordinate to
     */
    public void setY(int y) {
        this.y = y;
    }


} //Snake
