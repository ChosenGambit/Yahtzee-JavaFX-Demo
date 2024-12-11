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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.control.Hyperlink;

/**
 *
 * @author Chosen Gambit
 */
public class GameLogic {
   
    public static final int SMALL_STRAIGHT = 4;
    public static final int LARGE_STRAIGHT = 5;
    public static final int THREE_OF_A_KIND = 3;
    public static final int FOUR_OF_A_KIND = 4;
    public static final int YAHTZEE = 5;
    public static final int POINTS_SMALL_STRAIGHT = 30;
    public static final int POINTS_LARGE_STRAIGHT = 40;
    public static final int POINTS_FULL_HOUSE = 25;
    public static final int POINTS_YAHTZEE = 50;
    public static final int POINTS_BONUS = 35;  
    public static final int MAX_RETHROW = 3;
    public static final int BONUS_THRESHOLD = 63;
        
    /**
     * frequencies of dice
     * @param dice 
     */
    public static int[] frequencies(List<IDie> dice) {
        int[] freqs = new int[6];
        List<Integer> intList = convertToIntList(dice);
        for (int i = 0; i < dice.size(); i++) {
            int number = intList.get(i); // is 1 - 6
            freqs[number-1]++; // add + one to array
        }
        return freqs;
    }
    
    /**
     * return an array with die number and scores attached
     * @param freqs
     * @return 
     */
    public static int[] scores(int[] freqs) {
        int[] scoreArray = new int[6];
        for (int i = 0; i < freqs.length; i++) {
            scoreArray[i] = freqs[i] * (i+1);
        }
        return scoreArray;
    }
    
    /**
     * Check if there is a straight in the dice values
     * @param freqs
     * @param size
     * @return 
     */
    public static int straight(int [] freqs, int size) {
        int conseq = 0;
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] != 0) conseq++;
            else conseq = 0;
            if (conseq == size && size == SMALL_STRAIGHT) return POINTS_SMALL_STRAIGHT;
            if (conseq == size && size == LARGE_STRAIGHT) return POINTS_LARGE_STRAIGHT;
        }
        return 0;
    }
    
    /**
     * Return true if we found full house
     * @param freqs
     * @return 
     */
    public static int fullHouse(int[] freqs) {
        boolean size2 = false, size3 = false;
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] == 3) size3 = true;
            if (freqs[i] == 2) size2 = true;
        }
        return (size2 && size3) ? POINTS_FULL_HOUSE : 0 ;
    }
    
    /**
     * Returns score for "of a kind" (and Yahtzee)
     * @param freqs
     * @param size
     * @return 
     */
    public static int ofAKind(int[] freqs, int size) {
        int sum = 0;
        boolean foundSize = false;
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] >= size) {
                foundSize = true;
                // Yahtzee!
                if (size == 5) return POINTS_YAHTZEE;
            }
            sum += (i+1) * freqs[i];
        }
        return foundSize ? sum : 0;
    }
    
    /**
     * Converts List<Die> to List<Integer>
     * @param dice
     * @return 
     */
    public static List<Integer> convertToIntList(List<IDie> dice) {
        List<Integer> list = dice.stream().map(d -> d.getDieNumber()).collect(Collectors.toList());
        return list;
    }
    
    /**
     * Returns sum of List<Integer>
     * @param diceIntegers
     * @return 
     */
    public static int sum(List<Integer> diceIntegers) {
        return diceIntegers.stream().reduce(0, (a,b) -> a + b);
    }
    
    /**
     * Returns sum of all scores
     * @param scores
     * @return 
     */
    public static int sum(int[] scores) {
        int sum = 0;
        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];
        }
        return sum;
    }
    
    /**
     * Returns true if score exceeds bonus threshold score
     * @param score
     * @return 
     */
    public static boolean earnedBonus(int score) {
        return score >= BONUS_THRESHOLD;
    }
    
    /**
     * Deprecated, use frequencies() instead
     * Return the dice list of which the number equals the given number in a 
     * map with key "match", the others are within key "other"
     * This method also sets the hyperlink to match the maximum points possible
     * @param dice
     * @param hyperlink
     * @param number
     * @return 
     */
    @Deprecated
    public static Map<String, List<IDie>> countNumbers(List<Die> dice, Hyperlink hyperlink, int number) {
               
        List<IDie> match = dice.stream().filter(d -> d.getDieNumber() == number).collect(Collectors.toList());
        List<IDie> other = dice.stream().filter(d -> d.getDieNumber() != number).collect(Collectors.toList());
        List<Integer> intList = convertToIntList(match);
        hyperlink.setText(""+sum(intList));
        
        // create dictionary which holds a list with matches and other dice
        Map<String, List<IDie>> map = new HashMap<>();
        map.put("match", match);
        map.put("other", other);
        return map;
    }
    
}
