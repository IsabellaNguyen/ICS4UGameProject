/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author bella
 */
public class GameClass {
    private ArrayList<Integer> answers = new ArrayList(); //instance variables
    private ArrayList<Boolean> alrClicked = new ArrayList();
    private final JLabel[] items;
    private final ImageIcon[] icons;
    private final JLabel[] stars;
    private final JLabel text;
    private final JLabel extraStar;
    
    int numOfCorrect, numOfPresent, numOfEvil; //other variables
    int correctItem=0;
    int correct=0;
    int points=5;
    int present=6;
    int evil=7;
    static boolean used,isEvil=false;
    java.net.URL backURL = GameClass.class.getResource("ImageOnBackk.png");
    ImageIcon backIcon = new ImageIcon(backURL);
    java.net.URL noStarURL = GameClass.class.getResource("noStarImage.png");
    ImageIcon noStarImage = new ImageIcon(noStarURL);
    java.net.URL starURL = GameClass.class.getResource("star.png");
    ImageIcon star = new ImageIcon(starURL);
    java.net.URL backMouseURL = GameClass.class.getResource("ImageOnBackDark.png");
    ImageIcon backMouse = new ImageIcon(backMouseURL);
    
    public GameClass(JLabel[] items, ArrayList answers, ArrayList alrClicked, ImageIcon[] icons, JLabel[] stars, JLabel text, JLabel extraStar){
        this.items=items;
        this.answers=answers;        
        this.alrClicked=alrClicked;
        this.icons=icons;
        this.stars=stars;
        this.text=text;
        this.extraStar=extraStar;
        
        File file = new File("data.txt"); 
        Scanner scan;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()){
                String str = scan.nextLine();
                if (str.equals("points")){ //initalize the necessary variables via text file
                    points=scan.nextInt();
                }
                else if (str.equals("correct")){
                    correct=scan.nextInt();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void randomizeAnswers() {
        extraStar.setVisible(false);
        Random rand = new Random();
        answers.clear(); //reset arraylist
        numOfCorrect=0;
        numOfPresent=0;
        numOfEvil=0;
        text.setText("DON'T BLINK!!!");
        for (int i=0 ; i<items.length ; i++){
            int ans = rand.nextInt(8);
            if (ans==correctItem){ //Check if the random number is the correct item
                numOfCorrect++;
                if (numOfCorrect>2){ //If there are already two correct items
                    i--; 
                    continue; //do not add another correct item into the arraylist
                }
            }
            else if(ans==present){ //Check if the random number is the present
                numOfPresent++;
                if (numOfPresent>1){ //If there is already a present
                    i--;
                    continue; //Make sure there is only one present
                }
            }
            else if (ans==evil){ //Checl if the random number is the evil item
                numOfEvil++;
                if (numOfEvil>1){ //Make sure there is only one
                    i--;
                    continue;
                }
            }
            answers.add(ans);
        }
        if (numOfCorrect<2){ //does this even work 
                int place=rand.nextInt(30);
                answers.set(place, 0);
            }

        for(int i=0;i<items.length;i++){ //display the answers
            items[i].setText("");
            items[i].setIcon(icons[answers.get(i)]);

        }
        //in case the user clicks the help button before clicking any buttons
        SettingsClass.alrClicked=alrClicked;
        SettingsClass.answers=answers;
        SettingsClass.points=points;
        SettingsClass.correct=correct;
    }
    
    public void coverItems(){ //Cover items with square
        for (JLabel item : items) {
            item.setIcon(backIcon);
        }
        text.setText("FIND KOYA'S EARS");
    }
    
    public void userPicksItem(){ //determine which item is clicked
        extraStar.setVisible(false);
        for (int i=0 ; i<items.length ; i++){ //scan each item
            final int p=i;
            items[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) { //hovering mouse
                    if (alrClicked.get(p)==false){
                        items[p].setIcon(backMouse);
                    }
                }
                @Override
                public void mouseExited(MouseEvent evt){ //hovering mouse leaves
                    if (alrClicked.get(p)==false){
                        items[p].setIcon(backIcon);
                    }
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (alrClicked.get(p)==false){ //check if the item has been clicked before
                        alrClicked.set(p, true);
                        items[p].setIcon(icons[answers.get(p)]); //display clicked icon
                        if (answers.get(p)==correctItem){
                            correct++;
                            if (correct==2){
                                text.setText("YOU FOUND BOTH OF KOYA'S EARS!");
                            }
                            else{
                                text.setText("YOU FOUND ONE!");
                            }
                        }
                        else if (answers.get(p)==present){ //if the user chose the present
                            text.setText("YOU FOUND THE PRESENT!");
                            if (points==5){
                                if (extraStar.isVisible()){ //possibility that user picks the present, then evil item, then present again
                                    stars[points].setIcon(star);
                                }
                                else{
                                    extraStar.setVisible(true);
                                }
                            }
                            else{
                                stars[points].setIcon(star);
                            }
                            points++;
                        }
                        else if (answers.get(p)==evil){ //if the user chose the evil item
                            text.setText("OH NO! IT'S EVIL KOYA!");
                            correct=0;
                            for (int i=0 ; i<items.length ; i++){
                                alrClicked.set(i,false);
                            }
                            isEvil=true;
                            
                            points--;
                            stars[points].setIcon(noStarImage);
                        }
                        else{ //User chose a normal item
                            points--;
                            stars[points].setIcon(noStarImage);
                            if (points==0){
                                text.setText("TRY AGAIN NEXT TIME");
                            }
                            else{
                                text.setText("NOT QUITE");
                            }
                        }
                    }
                    //update variables in other classes according to user's choices
                    MainUI.itemClick=correct;
                    MainUI.pointss=points;
                    MainUI.evilK=isEvil;
                    isEvil=false; //reset
                    SettingsClass.alrClicked=alrClicked;
                    SettingsClass.answers=answers;
                    SettingsClass.points=points;
                    SettingsClass.correct=correct;
                    BufferedWriter dataFile;
                    try {
                        dataFile = new BufferedWriter(new FileWriter("data.txt",true));
                        BufferedWriter data = Files.newBufferedWriter(Paths.get("data.txt")); 
                        data.write(""); //clear data
                        data.flush();
                        
                        //Write in points and used because the ending page needs these variables
                        dataFile.write("points"+"\n");
                        dataFile.write(points+"\n");

                        dataFile.write("power"+"\n");
                        dataFile.write(used+"\n");

                        dataFile.close();
                    } catch (IOException ex) {
                        Logger.getLogger(GameClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }
    
    public void startedGame(){ //for if user already started the game (left to see instructions and came back)
        for (int i=0 ; i<items.length ; i++){
            items[i].setText("");
            if (alrClicked.get(i)){
                items[i].setIcon(icons[answers.get(i)]); //draw the items that were already clicked
            }
        }
        for (int i=5 ; i>points-1 ; i--){
            stars[i].setIcon(noStarImage); //also draw in the stars/points
        }
    }
        
    public void openEnd() {
        File file = new File("data.txt"); 
        Scanner scan;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()){
                String str = scan.nextLine();
                if (str.equals("points")){ //scan the points 
                    points=scan.nextInt();
                }
                else if (str.equals("power")){ //scan if user already used power up
                    used=scan.nextBoolean();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        java.awt.Window window[] = java.awt.Window.getWindows(); //this is to close all windows in case they don't close
        for(int i=0;i<window.length;i++){ 
            window[i].dispose(); 
            //window[i]=null;
        } 
        EndingPage endingPage = new EndingPage(points, used); //open ending page
        endingPage.setVisible(true);
        MainUI.started=false;
    }
    
}