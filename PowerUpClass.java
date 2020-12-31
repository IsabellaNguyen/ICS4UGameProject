/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
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
    
    private boolean usedPowerUp=false; //other variables
    int correctItem=0;
    
    public PowerUpClass(ArrayList alrClicked, JButton powerUp, JLabel[] items, ArrayList answers, ImageIcon[] icons){
        this.alrClicked=alrClicked;
        this.powerUp=powerUp;
        this.items=items;
        this.answers=answers;
        this.icons=icons;
    }
    
    public void usePowerUp(){
        GameClass.getUsedPowerUp(usedPowerUp); //Reset after each game
            powerUp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!usedPowerUp){
                        usedPowerUp=true;
                        GameClass.getUsedPowerUp(usedPowerUp);
                        powerUp.setBackground(Color.red); //change background so user knows they've used the powerup already
                        int min=3;
                        int max=5;
                        int randnum = (int)(Math.random() * (max - min + 1) + min); //pick random amount of items to uncover
                        System.out.println(randnum); //TESTING DELETE LATER
                        for (int i=0 ; i<randnum ; i++ ){
                            Random rand = new Random(); 
                            int randItem=rand.nextInt(30); //pick random item out of the 30 available
                            if (alrClicked.get(randItem) || answers.get(randItem)==correctItem){ //if the random item has been clicked or not is the correct item
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
    
}
