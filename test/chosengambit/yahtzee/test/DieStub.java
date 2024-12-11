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

import chosengambit.yahtzee.IDie;
import javafx.scene.image.Image;

/**
 *
 * @author ChosenGambit
 */
public class DieStub implements IDie {
    
    private int dieNumber = 1;

    @Override
    public int getDieNumber() {
        return this.dieNumber;
    }
    
    public void setDieNumber(int n) {
        this.dieNumber = n;
    }
    
    public boolean setHold(boolean hold) {
        return false;
    }
    
    public void initialize(Image image, int numRows, int numColumns) {
        
    }
    
    public int roll() {
        return 0;
    }
    
    
}
