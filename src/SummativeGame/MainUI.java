/*
 * This holds the JFrame of the game page along with all its components. 
 * What the game is: The screen will show 30 items and mix them up very fast.
 * You must find the correct items (2) before the timer runs out or you run out of moves.
 */
package SummativeGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author bella
 */
public class MainUI extends javax.swing.JFrame {
    ///// Image Declarations /////
    java.net.URL correctItemURL = MainUI.class.getResource("KoyaEarr.png"); //image URL
    ImageIcon correctIcon = new ImageIcon(correctItemURL); //Creating image icon variable
    java.net.URL imageURL1 = MainUI.class.getResource("Chimmy.png");
    ImageIcon image1 = new ImageIcon(imageURL1);
    java.net.URL imageURL2 = MainUI.class.getResource("Mang.png");
    ImageIcon image2 = new ImageIcon(imageURL2);
    java.net.URL imageURL3 = MainUI.class.getResource("RJ.png");
    ImageIcon image3 = new ImageIcon(imageURL3);
    java.net.URL imageURL4 = MainUI.class.getResource("Cooky.png");
    ImageIcon image4 = new ImageIcon(imageURL4);
    java.net.URL imageURL5 = MainUI.class.getResource("Tata.png");
    ImageIcon image5 = new ImageIcon(imageURL5);
    java.net.URL imageURL6 = MainUI.class.getResource("presentImage.png");
    ImageIcon present = new ImageIcon(imageURL6);
    java.net.URL imageURL7 = MainUI.class.getResource("evilKoya.png");
    ImageIcon evil = new ImageIcon(imageURL7);
    java.net.URL noStarURL = GameClass.class.getResource("noStarImage.png");
    ImageIcon noStarImage = new ImageIcon(noStarURL);
    ////////////////////////////
    
    ///// Variables /////
    ArrayList<Integer> answers = new ArrayList();
    ArrayList<Boolean> clicked=new ArrayList();
    boolean usedPowerUp=false;
    static boolean evilK, started=false;
    static int itemClick=-1;
    static int pointss=-1;
    static int counter=7;
    Timer timer,eTimer;
    static Timer gameTimer;
    //////////////////
    /**
     * Creates new form GamePage
     */
    public MainUI() {
        initComponents();
        this.setLocationRelativeTo(null); //open the JFrame in the middle of the screen
        
        ///// Initialize any necessary variables /////
        ImageIcon[] images={correctIcon,image1,image2,image3,image4,image5, present, evil}; //array for different types of icons
        JLabel[] items = {item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12,item13,item14,item15,item16,item17,item18,item19,item20,item21,item22,item23,item24,item25,item26,item27,item28,item29,item30};
        JLabel[] stars = {star1, star2, star3, star4, star5, extraStar};
        timerText.setText("");

        for (int i=0 ; i<items.length ; i++){
            clicked.add(false);
        }
        
        if (!started){
            try {
                text(); //initialize variables if it's a new game
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Set the necessary variables to the values in the file since it's possible that the user left and came back
        File file = new File("data.txt"); 
        Scanner scan;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()){
                String str = scan.nextLine();
                if (str.equals("clicked")){ //find the line that says "clicked" to read the following correct values
                    for (int i=0 ; i<items.length ; i++){
                        clicked.set(i, scan.nextBoolean()); //update the array to the values in the file
                    }
                }
                else if (str.equals("answers")){ //find the line that says "answers" to read the following correct values
                    for (int i=0 ; i<items.length ; i++){
                        answers.add(scan.nextInt()); //update the array
                    }
                }
                else if (str.equals("timer")){ //find the line that says "timer" and let counter equa; the following line
                    counter=scan.nextInt();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        /////////////////////////////////////
        
        ///// Instantiation of objects /////
        GameClass game = new GameClass(items, answers,clicked,images, stars, gameText, extraStar);
        PowerUpClass powerUp = new PowerUpClass(clicked, PowerUpButton, items, answers, images, gameText);
        SettingsClass settings = new SettingsClass (restartLabel, helpLabel, exitLabel, this);
        ///////////////////////////////////
        
        ///// Main Thread /////
        new Thread(new Runnable() { //Thread to make randomizeAnswers method run and update jPanel inside a for loop
        @Override
        public void run() {
            if (started){ //set the game to how the user's progress
                game.coverItems();
                game.startedGame();
                powerUp.startedPowerUp();
                timerBar.setValue(counter);
            }
            else{
                for (int i=0; i<17; i++) { //run method 17 times
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            game.randomizeAnswers(); //call method
                        }
                    });
                    try{ 
                        Thread.sleep(95); 
                    } 
                    catch (InterruptedException e) {
                    }
                }
                game.coverItems(); //cover the items right after the answers are randomized
            }
            game.userPicksItem(); //check what item is clicked
            
            ActionListener action = new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event) //Delay before the ending page opens
                {
                    if (itemClick==2 || pointss==0 || counter==0){
                        if (counter==0){
                            counter=-1; //must set counter to another value or else it might keep opening the ending page until the 600 ms is done
                        }
                        timer.stop(); //stop all the current timers so they don't run in the background unnecessarily
                        eTimer.stop();
                        gameTimer.stop(); 
                        game.openEnd();
                        itemClick=-1; //initialize in case user replays the game
                        pointss=-1;
                    }
                }
            };
            timer= new Timer (600,action);
            timer.start();
            
            ActionListener evilAction = new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event) //delay before items are covered again after evil item is chosen
                {
                    if (evilK==true){
                        game.coverItems();
                    }
                }
            };
            eTimer= new Timer (800,evilAction);
            eTimer.start();

            powerUp.usePowerUp(); //check if the power up button was clicked
            settings.settings(); //Check if the settings buttons were clicked
            
            ActionListener timerAction = new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event) 
                {
                    counter--;
                    timerBar.setValue(counter);
                    switch (counter) { //warn the user starting when there is 3 seconds left
                        case 3:
                            timerText.setText("3 SECONDS LEFT!");
                            break;
                        case 2:
                            timerText.setText("2 SECONDS LEFT!!");
                            break;
                        case 1:
                            timerText.setText("1 SECOND LEFT!!!");
                            break;
                        default:
                            timerText.setText("");
                            break;
                    }
                }
            };
            gameTimer= new Timer (1000,timerAction);
            gameTimer.start();
        }
        }).start();
        /////////////////////////
    }
    
    ///// Methods /////
    private void text() throws IOException {
        BufferedWriter dataFile = new BufferedWriter(new FileWriter("data.txt",true));
        BufferedWriter data = Files.newBufferedWriter(Paths.get("data.txt"));
        data.write(""); //clear file
        data.flush();
        dataFile.write("clicked"+"\n"); //write an initial value for clicked arraylist
        for (Boolean click : clicked){
            dataFile.write(false+"\n");
        }   
        dataFile.write("answers"+"\n"); //write an initial value for answers arraylist
        for (int i=0 ; i<30 ; i++){
            dataFile.write(0+"\n");
        }
        dataFile.write("points"+"\n"); //write an initial value for points
        dataFile.write(5+"\n");
        dataFile.write("correct"+"\n"); //write an initial value for correct number of items
        dataFile.write(0+"\n");
        dataFile.write("power"+"\n"); //write an initial value for if the user used the power up or not
        dataFile.write("false"+"\n");
        dataFile.write("timer"+"\n"); //write an initial value for the timer
        dataFile.write(7+"\n");
        dataFile.close();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        item1 = new javax.swing.JLabel();
        item2 = new javax.swing.JLabel();
        item3 = new javax.swing.JLabel();
        item4 = new javax.swing.JLabel();
        item5 = new javax.swing.JLabel();
        item6 = new javax.swing.JLabel();
        item7 = new javax.swing.JLabel();
        item8 = new javax.swing.JLabel();
        item9 = new javax.swing.JLabel();
        item10 = new javax.swing.JLabel();
        item11 = new javax.swing.JLabel();
        item12 = new javax.swing.JLabel();
        item13 = new javax.swing.JLabel();
        item14 = new javax.swing.JLabel();
        item15 = new javax.swing.JLabel();
        item16 = new javax.swing.JLabel();
        item17 = new javax.swing.JLabel();
        item18 = new javax.swing.JLabel();
        item19 = new javax.swing.JLabel();
        item20 = new javax.swing.JLabel();
        item21 = new javax.swing.JLabel();
        item22 = new javax.swing.JLabel();
        item23 = new javax.swing.JLabel();
        item24 = new javax.swing.JLabel();
        item25 = new javax.swing.JLabel();
        item26 = new javax.swing.JLabel();
        item28 = new javax.swing.JLabel();
        item29 = new javax.swing.JLabel();
        item30 = new javax.swing.JLabel();
        item27 = new javax.swing.JLabel();
        star1 = new javax.swing.JLabel();
        star2 = new javax.swing.JLabel();
        star3 = new javax.swing.JLabel();
        star4 = new javax.swing.JLabel();
        star5 = new javax.swing.JLabel();
        PowerUpButton = new javax.swing.JButton();
        gameText = new javax.swing.JLabel();
        extraStar = new javax.swing.JLabel();
        restartLabel = new javax.swing.JLabel();
        helpLabel = new javax.swing.JLabel();
        exitLabel = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        timerBar = new javax.swing.JProgressBar();
        timerText = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(820, 600));

        jPanel1.setBackground(new java.awt.Color(171, 209, 236));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        item1.setText("jLabel1");

        item2.setText("jLabel2");

        item3.setText("jLabel3");

        item4.setText("jLabel4");

        item5.setText("jLabel5");

        item6.setText("jLabel6");

        item7.setText("jLabel7");

        item8.setText("jLabel8");

        item9.setText("jLabel9");

        item10.setText("jLabel10");

        item11.setText("jLabel1");

        item12.setText("jLabel2");

        item13.setText("jLabel3");

        item14.setText("jLabel4");

        item15.setText("jLabel5");

        item16.setText("jLabel6");

        item17.setText("jLabel7");

        item18.setText("jLabel8");

        item19.setText("jLabel9");

        item20.setText("jLabel10");

        item21.setText("jLabel1");

        item22.setText("jLabel2");

        item23.setText("jLabel3");

        item24.setText("jLabel4");

        item25.setText("jLabel5");

        item26.setText("jLabel6");

        item28.setText("jLabel8");

        item29.setText("jLabel9");

        item30.setText("jLabel10");

        item27.setText("jLabel7");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(item11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(item1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item2)
                    .addComponent(item12)
                    .addComponent(item22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item3)
                    .addComponent(item13)
                    .addComponent(item23))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item4)
                    .addComponent(item14)
                    .addComponent(item24))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(item15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(item5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item16)
                    .addComponent(item26)
                    .addComponent(item6))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(item27, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(item17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item28)
                    .addComponent(item18)
                    .addComponent(item8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item19)
                    .addComponent(item29)
                    .addComponent(item9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item20)
                    .addComponent(item30)
                    .addComponent(item10))
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(item1)
                    .addComponent(item2)
                    .addComponent(item3)
                    .addComponent(item4)
                    .addComponent(item5)
                    .addComponent(item6)
                    .addComponent(item7)
                    .addComponent(item8)
                    .addComponent(item9)
                    .addComponent(item10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(item11)
                    .addComponent(item12)
                    .addComponent(item13)
                    .addComponent(item14)
                    .addComponent(item15)
                    .addComponent(item16)
                    .addComponent(item17)
                    .addComponent(item18)
                    .addComponent(item19)
                    .addComponent(item20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(item28, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(item21)
                        .addComponent(item22)
                        .addComponent(item23)
                        .addComponent(item24)
                        .addComponent(item25)
                        .addComponent(item26)
                        .addComponent(item29)
                        .addComponent(item30)
                        .addComponent(item27)))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        star1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        star2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        star3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        star4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        star5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        PowerUpButton.setBackground(new java.awt.Color(255, 255, 255));
        PowerUpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/ShookyPowerUp.png"))); // NOI18N
        PowerUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PowerUpButtonActionPerformed(evt);
            }
        });

        gameText.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        gameText.setText("TEXT");

        extraStar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        restartLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        restartLabel.setText("RESTART");

        helpLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        helpLabel.setText("HELP");

        exitLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        exitLabel.setText("EXIT");

        jLabel37.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel37.setText("~ CAN'T YOU SEE ME? ~");

        timerBar.setBackground(new java.awt.Color(255, 255, 255));
        timerBar.setForeground(new java.awt.Color(153, 255, 255));
        timerBar.setMaximum(7);
        timerBar.setToolTipText("");
        timerBar.setValue(7);

        timerText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        timerText.setForeground(new java.awt.Color(255, 0, 0));
        timerText.setText("TEXT PROGRESS");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/gamePageImg.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PowerUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(restartLabel)
                                    .addComponent(helpLabel)
                                    .addComponent(exitLabel)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(star1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(star2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(star3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(star4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(star5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(extraStar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(gameText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 374, Short.MAX_VALUE)))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timerText)
                            .addComponent(timerBar, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(timerText))
                    .addComponent(timerBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gameText)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(extraStar)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(star1)
                            .addComponent(star2)
                            .addComponent(star3)
                            .addComponent(star4)
                            .addComponent(star5))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PowerUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(restartLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(helpLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:  
    }//GEN-LAST:event_jPanel1MouseClicked

    private void PowerUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PowerUpButtonActionPerformed
        
    }//GEN-LAST:event_PowerUpButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws InterruptedException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PowerUpButton;
    private javax.swing.JLabel exitLabel;
    private javax.swing.JLabel extraStar;
    private javax.swing.JLabel gameText;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JLabel item1;
    private javax.swing.JLabel item10;
    private javax.swing.JLabel item11;
    private javax.swing.JLabel item12;
    private javax.swing.JLabel item13;
    private javax.swing.JLabel item14;
    private javax.swing.JLabel item15;
    private javax.swing.JLabel item16;
    private javax.swing.JLabel item17;
    private javax.swing.JLabel item18;
    private javax.swing.JLabel item19;
    private javax.swing.JLabel item2;
    private javax.swing.JLabel item20;
    private javax.swing.JLabel item21;
    private javax.swing.JLabel item22;
    private javax.swing.JLabel item23;
    private javax.swing.JLabel item24;
    private javax.swing.JLabel item25;
    private javax.swing.JLabel item26;
    private javax.swing.JLabel item27;
    private javax.swing.JLabel item28;
    private javax.swing.JLabel item29;
    private javax.swing.JLabel item3;
    private javax.swing.JLabel item30;
    private javax.swing.JLabel item4;
    private javax.swing.JLabel item5;
    private javax.swing.JLabel item6;
    private javax.swing.JLabel item7;
    private javax.swing.JLabel item8;
    private javax.swing.JLabel item9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel restartLabel;
    private javax.swing.JLabel star1;
    private javax.swing.JLabel star2;
    private javax.swing.JLabel star3;
    private javax.swing.JLabel star4;
    private javax.swing.JLabel star5;
    private javax.swing.JProgressBar timerBar;
    private javax.swing.JLabel timerText;
    // End of variables declaration//GEN-END:variables
}
