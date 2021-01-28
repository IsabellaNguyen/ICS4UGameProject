/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author bella
 */
public class SettingsClass {
    private final JLabel restart;
    private final JLabel help;
    private final JLabel exit;
    private final JFrame mainFrame;
    
    static ArrayList<Boolean> alrClicked = new ArrayList();
    static ArrayList<Integer> answers = new ArrayList();
    static int points=5;
    static int correct=0;
    static boolean usedPowerUp;
    boolean inSettings;
    
    public SettingsClass(JLabel restart, JLabel help, JLabel exit, JFrame frame){
        this.restart=restart;
        this.help=help;
        this.exit=exit;
        mainFrame=frame;
    }
    
    public void settings() {
        restart.addMouseListener(new MouseAdapter() { //restart button
            @Override
            public void mouseEntered(MouseEvent e){ //hovering
                restart.setForeground(new Color(235, 96, 168 ));
            }
            @Override
            public void mouseExited(MouseEvent e){
                restart.setForeground(Color.BLACK);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                MainUI.started=false; //start new game
                MainUI.gameTimer.stop();
                mainFrame.dispose();
                TitlePage title = new TitlePage();
                title.setVisible(true);
            }
        });
        help.addMouseListener(new MouseAdapter() { //help button
            @Override
            public void mouseEntered(MouseEvent e){ //hovering
                help.setForeground(new Color(235, 96, 168 ));
            }
            @Override
            public void mouseExited(MouseEvent e){
                help.setForeground(Color.BLACK);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    fileSettings();
                } catch (IOException ex) {
                    Logger.getLogger(SettingsClass.class.getName()).log(Level.SEVERE, null, ex);
                }
                MainUI.started=true; //make sure to continue progress
                MainUI.gameTimer.stop();
                mainFrame.dispose();
                InstructionsPage instructions = new InstructionsPage();
                instructions.setVisible(true);
            }
        });
        exit.addMouseListener(new MouseAdapter() { //exit button
            @Override
            public void mouseEntered(MouseEvent e){ //hovering
                exit.setForeground(new Color(235, 96, 168 ));
            }
            @Override
            public void mouseExited(MouseEvent e){
                exit.setForeground(Color.BLACK);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void fileSettings() throws IOException{
        BufferedWriter dataFile = new BufferedWriter(new FileWriter("data.txt",true));
        BufferedWriter data = Files.newBufferedWriter(Paths.get("data.txt")); 
        data.write(""); //clear file
        data.flush();
        
        //record the progress for every necessary variable in the file
        dataFile.write("clicked"+"\n");
        for (Boolean click : alrClicked){
            dataFile.write(Boolean.toString(click)+"\n");
        }
        
        dataFile.write("answers"+"\n");
        for (Integer answer : answers){
            dataFile.write(Integer.toString(answer)+"\n");
        }
        
        dataFile.write("points"+"\n");
        dataFile.write(points+"\n");
        
        dataFile.write("correct"+"\n");
        dataFile.write(correct+"\n");
        
        dataFile.write("power"+"\n");
        dataFile.write(usedPowerUp+"\n");
        
        dataFile.write("timer"+"\n");
        dataFile.write(MainUI.counter+"\n");
        
        dataFile.close();
    }
}
