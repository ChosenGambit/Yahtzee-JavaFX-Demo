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
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Example of a JavaFX solution for the game: Yahtzee
 * @author Chosen Gambit
 */
public class Main extends Application {
    
    Stage stage;

     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args); // this is necessary to launch JavaFX application                        
    }    

    /**
     * Start JavaFX application
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;        
        restart();
    }
    
    /**
     * Restart the game
     */
    public void restart() {
        stage.close();
        FXMLLoader loader = new FXMLLoader();
        String path = "/main/resources/main.fxml";
        URL fxmlUrl = getClass().getResource(path);
        loader.setLocation(fxmlUrl);
        
        AnchorPane root;
        try {
             root = loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        // set reference to this
        MainController mc = (MainController) loader.getController();
        mc.setRef(this);        
             
        stage.setResizable(false);        
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    /**
     * Returns stage ref
     * @return 
     */
    public Stage getStage() {
        return this.stage;
    }
    
    
    
}
