/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
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
    private final JFrame mainFrame;
    private final JLabel[] stars;
    private final JLabel text;
    
    int numOfCorrect; //other variables
    int correctItem=0;
    int correct=0;
    int points=5;
    static boolean used=false;
    java.net.URL backURL = GameClass.class.getResource("ImageOnBackk.png");
    ImageIcon backIcon = new ImageIcon(backURL);
    java.net.URL noStarURL = GameClass.class.getResource("noStarImage.png");
    ImageIcon noStarImage = new ImageIcon(noStarURL);
    
    public GameClass(JLabel[] items, ArrayList answers, ArrayList alrClicked, ImageIcon[] icons, JFrame frame, JLabel[] stars, JLabel text){
        this.items=items;
        this.answers=answers;        
        this.alrClicked=alrClicked;
        this.icons=icons;
        mainFrame=frame;
        this.stars=stars;
        this.text=text;
    }
    
    public void randomizeAnswers() throws InterruptedException{
        Random rand = new Random();
        answers.clear(); //reset arraylist
        numOfCorrect=0;
        for (int i=0 ; i<items.length ; i++){
            int ans = rand.nextInt(6);
            if (ans==correctItem){ //Check if the random number is the correct item
                numOfCorrect++;
                if (numOfCorrect>2){ //If there are already two correct items
                    i--; 
                    continue; //do not add another correct item into the arraylist
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
            
    }
    
    public void coverItems(){ //Cover items with square
        for (JLabel item : items) {
            item.setIcon(backIcon);
        }
    }
    
    public static void getUsedPowerUp(boolean usedPowerUp){
        used= usedPowerUp;
    }
    
    public void userPicksItem(){ //determine which item is clicked
        for (int i=0 ; i<items.length ; i++){ //scan each item
            final int p=i;
            items[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (alrClicked.get(p)==false){ //check if the item has been clicked before
                        alrClicked.set(p, true);
                        items[p].setIcon(icons[answers.get(p)]); //display clicked icon
                        if (answers.get(p)==correctItem){
                            correct++;
                            if (correct==2){
                                text.setText("YOU FOUND BOTH OF KOYA'S EARS!");
                                /*mainFrame.dispose(); //dispose of the main frame
                                EndingPage endingPage = new EndingPage(points, used); //open ending page
                                endingPage.setVisible(true);*/
                            }
                            else{
                                text.setText("YOU FOUND ONE!");
                            }
                        }
                        else{
                            points--;
                            stars[points].setIcon(noStarImage);
                            if (points==0){
                                text.setText("TRY AGAIN NEXT TIME");
                                /*mainFrame.dispose();
                                EndingPage endingPage = new EndingPage(points,used);
                                endingPage.setVisible(true);*/
                            }
                            else{
                                text.setText("NOT QUITE");
                            }
                        }
                        
                    }
                    MainUI.itemClicked(correct, points);
                }
            });
        }
    }
    
    public void openEnd(){
        mainFrame.dispose(); //dispose of the main frame
        EndingPage endingPage = new EndingPage(points, used); //open ending page
        endingPage.setVisible(true);
    }
    
}
