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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Chosen Gambit
 */
public class MainController implements Initializable {

    private List<IDie> diceList = new ArrayList<>();
    private List<Hyperlink> hyperlinkArray = new ArrayList<>();
    private List<Hyperlink> numberArray = new ArrayList<>();
    private Main main;
    
    @FXML private Die die_1;
    @FXML private Die die_2;
    @FXML private Die die_3;
    @FXML private Die die_4;
    @FXML private Die die_5;
    @FXML private Button roll_dice;
    @FXML private Hyperlink set_ones;
    @FXML private Hyperlink set_twos;
    @FXML private Hyperlink set_threes;
    @FXML private Hyperlink set_fours;
    @FXML private Hyperlink set_fives;
    @FXML private Hyperlink set_sixes;
    @FXML private Hyperlink set_small_straight;
    @FXML private Hyperlink set_large_straight;
    @FXML private Hyperlink set_full_house;
    @FXML private Hyperlink set_three_of_a_kind;
    @FXML private Hyperlink set_four_of_a_kind;
    @FXML private Hyperlink set_chance;
    @FXML private Hyperlink set_yahtzee;
    @FXML private Text throws_left;
    @FXML private Text sum;
    @FXML private Text bonus;
    @FXML private Text total;
    
    /**
     * Set reference to main
     * @param main 
     */
    public void setRef(Main main) {
        this.main = main;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // load die graphic
        Image image = new Image("/main/resources/dice.png", 300, 60, true, true);
        
        // can only initialize the ImageViews inside an array here
        diceList = Arrays.asList(die_1,die_2,die_3,die_4,die_5);
        
        // put all hyperlinks in a list
        hyperlinkArray.addAll(
            Arrays.asList(
                    set_ones, set_twos, set_threes, set_fours, set_fives, set_sixes,
                    set_small_straight, set_large_straight, set_full_house, set_three_of_a_kind,
                    set_four_of_a_kind, set_chance, set_yahtzee
            )
        );
        
        // all loose numbers also in a seperate array for easy reference, to calculate bonus
        numberArray.addAll(
            Arrays.asList(
                    set_ones, set_twos, set_threes, set_fours, set_fives, set_sixes
            )
        );
        
        // generate 5 dice
        for (IDie die : diceList) {    
            die.initialize(image, 1, 6); 
        }          
        
        // add initial value for rethrows
        throws_left.setText(""+GameLogic.MAX_RETHROW);
    }    

    /**
     * Roll the dice button is clicked
     * @param event 
     */
    @FXML
    private void roll_the_dice(MouseEvent event) {
        
        // we stop this round if we cannot throw dices anymore.
        if (!minusOneThrow()) return;       
        
        // clear score fields
        clearScoreFields();
        
        // roll the dice
        diceList.forEach(d -> { d.roll(); });
        
        // see what we rolled
        int[] frequencies = GameLogic.frequencies(diceList);
        int[] scores = GameLogic.scores(frequencies);
        
        /*
            fill in the score table
        */
        
        // solve numbers on score list
        setHyperlinkText(set_ones, scores[0]);
        setHyperlinkText(set_twos, scores[1]);
        setHyperlinkText(set_threes, scores[2]);
        setHyperlinkText(set_fours, scores[3]);
        setHyperlinkText(set_fives, scores[4]);
        setHyperlinkText(set_sixes, scores[5]);
        
        // check straights
        setHyperlinkText(set_small_straight, GameLogic.straight(frequencies, GameLogic.SMALL_STRAIGHT));
        setHyperlinkText(set_large_straight, GameLogic.straight(frequencies, GameLogic.LARGE_STRAIGHT));
        
        // check full house
        setHyperlinkText(set_full_house, GameLogic.fullHouse(frequencies));
        
        // check of a kind
        setHyperlinkText(set_three_of_a_kind, GameLogic.ofAKind(frequencies, GameLogic.THREE_OF_A_KIND));
        setHyperlinkText(set_four_of_a_kind, GameLogic.ofAKind(frequencies, GameLogic.FOUR_OF_A_KIND));
        setHyperlinkText(set_yahtzee, GameLogic.ofAKind(frequencies, GameLogic.YAHTZEE));
        
        // change
        setHyperlinkText(set_chance, GameLogic.sum(scores));               
        
        // end of flow wait for user input ...
    }
    
    /**
     * Only set text if hyperlink is not disabled yet
     * @param hyperlink
     * @param score 
     */
    private void setHyperlinkText(Hyperlink hyperlink, int score) {
        if (!hyperlink.isDisable())
            hyperlink.setText(""+score);        
    }
        
    /**
     * A hyperlink is clicked (and therefore a kind of score is chosen)
     * @param hyperlink 
     */
    private void scoreSelect(Object object) {
        if (object instanceof Hyperlink) {
            Hyperlink hyperlink = (Hyperlink) object;
            hyperlink.setDisable(true);
            throws_left.setText(""+GameLogic.MAX_RETHROW);
            
            // clear score fields
            clearScoreFields();
            
            // release all dice
            diceList.forEach(d -> { d.setHold(false); });                       
            
            // check if we have to calculate the sum, if number 1-6 is pressed
            boolean anyMatch = numberArray.stream().anyMatch(hl -> hl.getId().equals(hyperlink.getId()));
            if (anyMatch) {
                int sumScore = parseStringToInt(sum.getText()) + parseStringToInt(hyperlink.getText());
                sum.setText(""+ sumScore);
                if (GameLogic.earnedBonus(sumScore)) {
                    bonus.setText(""+GameLogic.POINTS_BONUS);
                }
            }  
            
             // check if it is the end of the game
            if (hyperlinkArray.stream().allMatch(h -> h.isDisabled() == true)) {
                // calculate the grand total
                int score = hyperlinkArray.stream().filter(h -> true).mapToInt(h -> parseStringToInt(h.getText())).sum();
                total.setText(""+score);
                throws_left.setText(""+0);
            }
        }
    }
    
   /**
    * Clears score fields which are not disabled
    */
    private void clearScoreFields() {
        // clear all hyperlink fields               
        hyperlinkArray.forEach(h -> { if (!h.isDisable()) h.setText(""); });
    }

    /**
     * Check if we may swap our die to be active / inactive
     * @param die 
     */
    private void swapHoldDie(Die die) {
        if (getThrowsLeft() != GameLogic.MAX_RETHROW) {
            die.swapHold();
        }
    }
    
    /**
     * Helper method to parse String to int
     * @param str
     * @return 
     */
    private int parseStringToInt(String str) {
        try {
            int number = Integer.parseInt(str);
            return number;
        }
        catch(NumberFormatException e) {
            System.out.println("Could not convert String to integer "+e.toString());
        }        
        return 0;
    }

    
    /**
     * Get the number of throws we have left
     * @return 
     */
    private int getThrowsLeft() {
        return parseStringToInt(throws_left.getText());
    }
    
    /**
     * Set throws left to be minus one, returns true if this was still possible 
     * @param number
     * @return 
     */
    private boolean minusOneThrow() {
        int throwsLeft = getThrowsLeft() - 1;
        if (throwsLeft > -1) {
            throws_left.setText(""+throwsLeft);
            return true;            
            
        }        
        return false;
    }
    
    // hold or release a die so it won't or will be thrown
    @FXML private void die_1_mouse_released(MouseEvent event) { swapHoldDie(die_1); }
    @FXML private void die_2_mouse_released(MouseEvent event) { swapHoldDie(die_2); }
    @FXML private void die_3_mouse_released(MouseEvent event) { swapHoldDie(die_3); }
    @FXML private void die_4_mouse_released(MouseEvent event) { swapHoldDie(die_4); }
    @FXML private void die_5_mouse_released(MouseEvent event) { swapHoldDie(die_5); }

    @FXML
    private void set_ones_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_twos_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_threes_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_fours_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_fives_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_sixes_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_small_straight_click(ActionEvent event) {
        scoreSelect(event.getSource());        
    }

    @FXML
    private void set_large_straight_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_full_house_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_three_of_a_kind_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_four_of_a_kind_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_chance_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void set_yahtzee_click(ActionEvent event) {
        scoreSelect(event.getSource());
    }

    @FXML
    private void start_new_game(ActionEvent event) {
        this.main.restart();
    }    
    
    
}
