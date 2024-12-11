/*
 * Copyright (C) 2024 ChosenGambit
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
package chosengambit.yahtzee.test;

import chosengambit.yahtzee.Die;
import chosengambit.yahtzee.GameLogic;
import chosengambit.yahtzee.IDie;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ChosenGambit
 */
public class TestGameLogic {
    
    public TestGameLogic() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    private List<IDie> createDieList(int[] ints) {
        ArrayList<IDie> dieList = new ArrayList<IDie>();
        for (int i = 0; i < ints.length; i++) {
            DieStub die = new DieStub();
            die.setDieNumber(ints[i]);
            dieList.add(die);
        }
        return dieList;
    }
    
    @Test 
    public void testFrequencies() {
        List<IDie> dieList = createDieList(new int[] {3, 3, 4, 4, 4});
        int[] frequencies = GameLogic.frequencies(dieList);
        assertTrue(frequencies[2] == 2 && frequencies[3] == 3);        
    }

    @Test
    public void testFullHouse() {
        List<IDie> dieList = createDieList(new int[] {3, 3, 4, 4, 4});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.fullHouse(frequencies);
        //System.out.println(score);
        assertTrue("score = "+score+" fullhouse should be true" ,score == GameLogic.POINTS_FULL_HOUSE);
    }
    
    @Test
    public void testSmallStraight() {
        List<IDie> dieList = createDieList(new int[] {1, 1, 2, 3, 4});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.straight(frequencies, GameLogic.SMALL_STRAIGHT);      
        assertTrue(score == GameLogic.POINTS_SMALL_STRAIGHT);
    }
    
    @Test
    public void testLargeStraight() {
        List<IDie> dieList = createDieList(new int[] {1, 5, 2, 3, 4});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.straight(frequencies, GameLogic.LARGE_STRAIGHT);      
        assertTrue(score == GameLogic.POINTS_LARGE_STRAIGHT);
    }
    
    @Test
    public void testNoStraight() {
        List<IDie> dieList = createDieList(new int[] {1, 2, 3, 3, 3});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.straight(frequencies, GameLogic.SMALL_STRAIGHT);      
        assertTrue(score == 0);
    }
    
    @Test
    public void testThreeOfAKind() {
        List<IDie> dieList = createDieList(new int[] {6, 2, 3, 3, 3});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.ofAKind(frequencies, GameLogic.THREE_OF_A_KIND);      
        assertTrue(score == 17);
    }
    
    @Test
    public void testFourOfAKind() {
        List<IDie> dieList = createDieList(new int[] {6, 6, 6, 6, 1});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.ofAKind(frequencies, GameLogic.FOUR_OF_A_KIND);      
        assertTrue(score == 25);
    }
    
    @Test
    public void testChance() {
        List<IDie> dieList = createDieList(new int[] {5,5,1,3,2});
        int[] frequencies = GameLogic.frequencies(dieList);
        int[] scores = GameLogic.scores(frequencies);
        int score = GameLogic.sum(scores);      
        assertTrue(score == 16);
    }
    
    @Test
    public void testNumberScores() {
        List<IDie> dieList = createDieList(new int[] {1,4,2,5,5});
        int[] frequencies = GameLogic.frequencies(dieList);
        int[] scores = GameLogic.scores(frequencies);   
        assertTrue(scores[0] == 1 && scores[1] == 2 && scores[3] == 4 && scores[4] == 10);
    }
    
    @Test
    public void testYahtzee() {
        List<IDie> dieList = createDieList(new int[] {1,1,1,1,1});
        int[] frequencies = GameLogic.frequencies(dieList);
        int score = GameLogic.ofAKind(frequencies, 5);
        assertTrue(score == GameLogic.POINTS_YAHTZEE);
    }
    
    @Test
    public void testBonusScore() {
        assertTrue(GameLogic.earnedBonus(63) == true && GameLogic.earnedBonus(62) == false);
    }
}
