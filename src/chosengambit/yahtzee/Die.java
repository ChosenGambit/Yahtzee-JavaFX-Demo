/*
 * Copyright (C) 2021 ChosenGambit
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package chosengambit.yahtzee;

import java.io.IOException;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;


/**
 * This is a controller class for a Die, which contains a graphical presentation
 * and functionality for a die.
 * 
 * 
 * @author Timon Suk
 */
public class Die extends AnchorPane implements IDie  {
    
    @FXML private ImageView imageView;    
    private Rectangle2D[][] cellClips;
    private int numRows, numColumns;
    private Image image;
    private int dieNumber = 1;
    private boolean hold = false;
    
   /**
    * Constructor for custom JavaFX component
    */
    public Die() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/Die.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }    
    
    /**
     * Initialize a new die
     * @param image
     * @param imageView
     * @param numRows
     * @param numColumns 
     */
    public void initialize(Image image, int numRows, int numColumns) {        
        
        this.image = image;
        this.numRows = numRows > 0 ? numRows : 1;
        this.numColumns = numColumns > 0 ? numColumns : 1;

        double cellWidth  = image.getWidth() / numColumns;
        double cellHeight = image.getHeight() / numRows;

        this.cellClips = new Rectangle2D[numRows][numColumns];
        
        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < numColumns; x++) {            
                cellClips[y][x] = new Rectangle2D(
                        (double)x * cellWidth,                        
                        (double)y * cellHeight,                         
                        cellWidth, 
                        cellHeight
                );
            }
        }
        
        this.imageView.setImage(image);
        this.imageView.setViewport(setDieNumber(1));        
    }      
    
    /**
     * Check if die is hold
     * @return 
     */
    public boolean isHold() {
        return this.hold;
    }

    /**
     * Set to true so die will not be re-rolled
     * @param hold 
     */
    public boolean setHold(boolean hold) {
        this.hold = hold;
        // grey out
        if (hold) {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setSaturation(-1);
            this.imageView.setEffect(adjust);
        }
        else {
            ColorAdjust adjust = new ColorAdjust();
            adjust.setSaturation(0);
            this.imageView.setEffect(adjust);
        }
        return this.hold;
    }
    
    public boolean swapHold() {
        return setHold(!this.hold);
    }
    
    /**
     * Get the integer this die represents
     * @return 
     */
    public int getDieNumber() {
        return this.dieNumber;
    }
    
    /**
     * Roll this die.
     * Sets this.dieNumber and matching graphic
     * @return 
     */
    public int roll() {
        if (this.hold) return this.dieNumber;
        
        int random = (int) (Math.random()*6)+1;
        //System.out.println("random "+ random);
        this.imageView.setViewport(setDieNumber(random));
            
        return this.dieNumber;
    }
    
    /**
     * Get Rectangle2D location of Image where die with this number is
     * @param number
     * @return Rectangle2D
     */
    private Rectangle2D setDieNumber(int number) {
        if (number < 1 || number > 6) {            
            throw new NumberFormatException("Die must be 1, 2, 3, 4, 5 or 6");            
        }
        this.dieNumber = number;           
        return this.cellClips[0][number-1];
    }
    
    /**
     * get Rectangle2D where image is divided in columns and rows
     * @return 
     */
    public Rectangle2D[][] getRectangle2D() {
        return this.cellClips;
    }
    
    /**
     * Get image
     * @return 
     */
    public Image getImage() {
        return this.image;
    }

}
