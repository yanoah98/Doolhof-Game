package doolhof.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Class that generates a Game and also paints the field
 * @author Yanoah Wiersma & Remon Turk
 * Group: 3.6.2
 * Date: 21-03-2018 t/m 02-04-2018
 * Version: 1.0 
 */
public class Game extends JComponent {
    private Field field;
    
    /**
     * constructs a game object which automatically sets the field to a null object;
     */
    public Game()
    {
        this.field = null;
    }
    
    /**
     * accessor method that gets the field
     * @return returns a field object
     */
    public Field getField()
    {
        return field;
    }
    
    /**
     * mutator method that sets a specific field
     * @param field an instance of the class field
     */
    public void setField(Field field)
    {
        this.field = field;
    }
    
    /**
     * repaints the field after every move the player makes
     */
    public void updateField(){
        repaint();
    }
    
    /**
     * resest the game, the setUpField method is being called which initializes the field again, after that the game is repainted.
     * @param filename file that contains the startlocations of all objects
     * @throws IOException when the file cannot be found
     */
    public void resetGame(String filename) throws IOException{
        field.setUpField(filename); //Initializing the field again
        field.getPlayer().setField(field); //Setting the field to player again
        repaint(); //Repainting the game
    }
    
    /**
     * Paints a field using the paint methods that we made for every specific Item
     * @param g has to be given in every paint method
    */
    public void paintField(Graphics g){
        final int size = field.getCellSize();
        final int rows = field.getRows();
        final int columns = field.getColumns();
        Tile[][] tiles = field.getGridGame();
        try{
            for(int x = 0; x < rows; x++){
                for(int y = 0; y < columns; y++){
                    Tile tile = tiles[x][y];
                    if(tile.isWall()){
                        paintWall(g, x, y, size); //When the item on the tile is a Wall, call the paintWall method
                    } else if(tile.isKey()){
                        paintKey(g, ((Key) tile.getItem()), x, y, size); //When the item on the tile is a Key, call the paintKey method
                    } else if(tile.isBarricade()){
                        paintBarricade(g, ((Barricade) tile.getItem()), x, y, size); //When the item on the tile is a Barricade, call the paintBarricade method
                    } else if(tile.isFinish()){
                        paintFinish(g, x, y, size); //When the item on the tile is a Finish, call the paintFinish method
                    } else{
                        paintEmpty(g, x, y, size); //When there is no item on the tile, call the paintEmpty method
                    }
                paintBorders(g, x, y, size); //When all items are painted, paint borders around the items with the paintBorders method
                }
            }
        paintPlayer(g, field.getPlayer(), field.getPlayer().getPosX(), field.getPlayer().getPosY(), size); //When the entire array is painted, draw the player on top of it with the paintPlayer method
        } catch(IOException e){
            System.out.println("File not found"); //When one of the paint methods cannot be executed, throw a IOException
        }
    }
    
    /**
     * has to be used when you make a paint method
     * @param g has to be given in every paint method
     */
    @Override
    public void paintComponent(Graphics g){
        paintField(g);
    }
    
    /**
     * method that paints a wall on to the field
     * @param g has to be given in every paint method
     * @param x the row where the wall that has to be painted is located in
     * @param y the column where the wall that has to be painted is located in
     * @param size the cell size
     * @throws IOException 
     */
    public void paintWall(Graphics g, int x, int y, int size) throws IOException{
        Image image;
        image = ImageIO.read(new File("src/doolhof/game/data/Wall.png")); //Get input from a image file
        g.drawImage(image, (y * size),(x * size), null);
    }
   
    /**
     * method that paints a key on to the field
     * @param g has to be given in every paint method
     * @param key paints a specific key because there are different type of keys
     * @param x the row where the key that has to be painted is located in
     * @param y the column where the key that has to be painted is located in
     * @param size the cell size
     * @throws IOException 
     */
    public void paintKey(Graphics g, Key key, int x, int y, int size) throws IOException{
        Image image;
        String pincode = "src/doolhof/game/data/K" + Integer.toString(key.getPincode()) + ".png"; //Get input from a image file
        image = ImageIO.read(new File(pincode));
        g.drawImage(image, (y * size),(x * size), null);
    }
    
    /**
     * method that paints a barricade on to the field
     * @param g has to be given in every paint method
     * @param barricade paints a specific barricade because there are different types of barricades
     * @param x the row where the barricade that has to be painted is located in
     * @param y the column where the barricade that has to be painted is located in
     * @param size the cell size
     * @throws IOException 
     */
    public void paintBarricade(Graphics g, Barricade barricade, int x, int y, int size) throws IOException{
        Image image;
        String lock = "src/doolhof/game/data/B" + Integer.toString(barricade.getLock()) + ".png"; //Get input from a image file
        image = ImageIO.read(new File(lock));
        g.drawImage(image, (y * size),(x * size), null);
    }
     
    /**
     * method that paints a finish on to the field
     * @param g has to be given in every paint method
     * @param x the row where the finish that has to be painted is located in
     * @param y the column where the finish that has to be painted is located in
     * @param size the cell size 
     */
    public void paintFinish(Graphics g, int x, int y, int size){
        g.setColor(Color.GREEN);
        g.fillRect(y * size, x * size, size, size);
    }
    
    /**
     * method that paints an empty tile on to the field
     * @param g has to be given in every paint method
     * @param x the row where the empty tile that has to be painted is located in
     * @param y the column where the empty tile that has to be painted is located in
     * @param size the cell size 
     */
    public void paintEmpty(Graphics g, int x, int y, int size){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(y * size, x * size, size, size);
    }
    
    /**
     * method that paints the player on to the field
     * @param g has to be given in every paint method
     * @param player an object of the class player that is getting painted on top of the field
     * @param x the x position of the player, this variable might change after the player makes a move
     * @param y the y position of the player, this variable might change after the player makes a move
     * @param size the cell size
     * @throws IOException 
     */
    public void paintPlayer(Graphics g, Player player, int x, int y, int size) throws IOException{
        Image image;
        image = ImageIO.read(new File("src/doolhof/game/data/Player.png")); //Get input from a image file
        g.drawImage(image, (x * size),(y * size), null);
        g.setColor(Color.BLACK);
        g.drawRect(field.getPlayer().getPosX() * size, field.getPlayer().getPosY() * size, size, size); //Draw borders again because player is not part of the Tiles array
    }
    
    /**
     * method that paints a border around the all cells
     * @param g has to be given in every paint method
     * @param x paints a border for every x coordinate in the 2DArray
     * @param y paints a border for every y coordinate in the 2DArray
     * @param size the cell size 
     */
    public void paintBorders(Graphics g, int x, int y, int size){
        g.setColor(Color.BLACK);
        g.drawRect(y * size, x * size, size, size);
    }
}
