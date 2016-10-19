 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ai.core.AI;
import ai.*;
import ai.abstraction.*;
import ai.abstraction.pathfinding.BFSPathFinding;
import gui.PhysicalGameStatePanel;

//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author santi
 */
public class GameVisualSimulationTest {
    public static void main(String args[]) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        UnitTypeTable utt2 = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load("maps/basesWorkers16x16.xml", utt);
//        PhysicalGameState pgs = MapGenerator.basesWorkers8x8Obstacle();

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 5000;
        int PERIOD = 20;
        boolean gameover = false;
        
        //myAHTN ai1 = new myAHTN(utt, new BFSPathFinding());
        //AI ai2 = new RandomAI();
        //AI ai2 = new RangedRush(utt, new BFSPathFinding());
        //AI ai2 = new HeavyRush(utt, new BFSPathFinding());  
        //AI ai2 = new LightRush(utt, new BFSPathFinding());
        //AI ai2 = new WorkerRush(utt, new BFSPathFinding());
        
        
        //AI ai1 = new RandomAI();
        //AI ai1 = new RangedRush(utt, new BFSPathFinding());
        //AI ai1 = new HeavyRush(utt, new BFSPathFinding());  
        //AI ai1 = new LightRush(utt, new BFSPathFinding());
        AI ai1 = new WorkerRush(utt, new BFSPathFinding());
        myAHTN ai2 = new myAHTN(utt, new BFSPathFinding());
        
        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);

        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do{
            if (System.currentTimeMillis()>=nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                
                // simulate:
                gameover = gs.cycle();
                w.repaint();
                nextTimeToUpdate+=PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }while(!gameover && gs.getTime()<MAXCYCLES);
        
        System.out.println("Game Over");
    }    

}
