/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SummativeGame;

//import static SummativeGame.MainUI.images;
//import static SummativeGame.MainUI.items;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author bella
 */
public class GameClass {
    ArrayList<Integer> answers = new ArrayList();
    ArrayList<Boolean> alrClicked = new ArrayList();
    JLabel[] items;
    ImageIcon[] icons;
    
    int numOfCorrect;
    
    public GameClass(JLabel[] items, ArrayList answers, ArrayList alrClicked, ImageIcon[] icons){
        this.items=items;
        this.answers=answers;
        this.alrClicked=alrClicked;
        this.icons=icons;
    }
    
    public void randomizeAnswers() throws InterruptedException{
        //for (int a=0 ; a<7 ; a++){
            Random rand = new Random();
            for (int i=0 ; i<items.length ; i++){
                int ans = rand.nextInt(6);
                if (ans==0){ //Check if the random number is the correct item (0 represents the correct item)
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

            for(int i=0;i<items.length;i++){
                items[i].setText("");
                items[i].setIcon(icons[answers.get(i)]);
                
            }
            //Thread.sleep(1000);
            
            System.out.println(answers);
            answers.clear();
            numOfCorrect=0;
            System.out.println(answers);
            
        }
        
        
    //}
}