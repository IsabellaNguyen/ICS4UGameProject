/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author bella
 */
public class SettingsClass {
    private final JLabel settingsIcon;
    private final JButton restart;
    private final JButton help;
    private final JButton exit;
    private final JButton resume;
    private final JFrame mainFrame;
    private final JPanel panel;
    
    boolean leave=false;
    static ArrayList<Boolean> alrClicked = new ArrayList();
    static ArrayList<Integer> answers = new ArrayList();
    static int points=5;
    static int correct=0;
    static boolean usedPowerUp;
    
    public SettingsClass(JLabel settingsIcon, JButton restart, JButton help, JButton exit, JButton resume, JFrame frame, JPanel panel){
        this.settingsIcon=settingsIcon;
        this.restart=restart;
        this.help=help;
        this.exit=exit;
        this.resume=resume;
        mainFrame=frame;
        this.panel=panel;
    }
    
    public void settings() {
        settingsIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.setVisible(true);
                try {
                    fileSettings();
                } catch (IOException ex) {
                    Logger.getLogger(SettingsClass.class.getName()).log(Level.SEVERE, null, ex);
                }
                restart.addMouseListener(new MouseAdapter() { //restart button
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        MainUI.started=false;
                        mainFrame.dispose();
                        TitlePage title = new TitlePage();
                        title.setVisible(true);
                    }
                });
                help.addMouseListener(new MouseAdapter() { //help button
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        MainUI.started=true;
                        mainFrame.dispose();
                        InstructionsPage instructions = new InstructionsPage();
                        instructions.setVisible(true);
                    }
                });
                exit.addMouseListener(new MouseAdapter() { //exit button
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.exit(0);
                    }
                });
                resume.addMouseListener(new MouseAdapter() { //resume button
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        panel.setVisible(false);
                        //leave=true;
                    }
                });
            }
        });
    }
    
    public static void gameData(ArrayList clicked, ArrayList answerss, int pointss, int correctt){
        alrClicked=clicked;
        answers=answerss;
        points=pointss;
        correct=correctt;
    }
    
    public static void gameDataP (boolean powerUpUsed){
        usedPowerUp=powerUpUsed;
    }
    
    private void fileSettings() throws IOException{
        BufferedWriter dataFile = new BufferedWriter(new FileWriter("data.txt",true));
        BufferedWriter data = Files.newBufferedWriter(Paths.get("data.txt")); 
        data.write("");
        data.flush();
        
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
        
        dataFile.close();
    }
}
