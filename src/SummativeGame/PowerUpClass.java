/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author bella
 */
public class PowerUpClass {
    private ArrayList<Integer> answers = new ArrayList(); //instance variables
    private ArrayList<Boolean> alrClicked = new ArrayList();
    private final JButton powerUp;
    private final JLabel[] items;
    private final ImageIcon[] icons;
    private final JLabel text;
    
    private boolean usedPowerUp=false; //other variables
    int correctItem=0;
    int present=6;
    int evil=7;
    
    public PowerUpClass(ArrayList alrClicked, JButton powerUp, JLabel[] items, ArrayList answers, ImageIcon[] icons, JLabel text){
        this.alrClicked=alrClicked;
        this.powerUp=powerUp;
        this.items=items;
        this.answers=answers;
        this.icons=icons;
        this.text=text;
        
        File file = new File("data.txt"); 
        Scanner scan;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()){
                String str = scan.nextLine();
                if (str.equals("power")){
                    usedPowerUp=scan.nextBoolean(); //initialize variable from text file
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void usePowerUp(){
        GameClass.used=usedPowerUp; //Reset after each game
        SettingsClass.usedPowerUp=usedPowerUp; //Reset after each game
        powerUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){ //hovering mouse
                if (!usedPowerUp){
                    powerUp.setBackground(new Color (144, 144, 144));
                }
            }
            @Override
            public void mouseExited(MouseEvent e){ //hovering mouse exit
                if (!usedPowerUp){
                    powerUp.setBackground(Color.WHITE);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!usedPowerUp){
                    MainUI.evilK=false; //not the evil item
                    usedPowerUp=true;
                    GameClass.used=usedPowerUp; //tell other classes the powerUp has been used
                    SettingsClass.usedPowerUp=usedPowerUp;
                    text.setText("SHOOKY HELP!!!");
                    powerUp.setBackground(Color.red); //change background so user knows they've used the powerup already
                    int min=3;
                    int max=5;
                    int randnum = (int)(Math.random() * (max - min + 1) + min); //pick random amount of items to uncover
                    for (int i=0 ; i<randnum ; i++ ){
                        Random rand = new Random(); 
                        int randItem=rand.nextInt(30); //pick random item out of the 30 available
                        if (alrClicked.get(randItem) || answers.get(randItem)==correctItem || answers.get(randItem)==present || answers.get(randItem)==evil){ //if the random item has been clicked or is not the correct item or special items
                            i--; //pick another number
                        }
                        else{
                            items[randItem].setIcon(icons[answers.get(randItem)]); //set icon
                            alrClicked.set(randItem,true);
                        }
                    }
                }
            }
        });
    }
    
    public void startedPowerUp(){
        if (usedPowerUp){
            powerUp.setBackground(Color.red);
        }
    }
}
