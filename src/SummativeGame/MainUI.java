/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    ImageIcon[] images;
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
    
    JLabel[] items;
    ArrayList<Integer> answers = new ArrayList();
    ArrayList<Boolean> clicked=new ArrayList();
    JLabel[] stars;
    boolean usedPowerUp=false;
    static boolean evilK, started=false;
    static int itemClick=-1;
    static int pointss=-1;
    Timer timer,eTimer;
    
    /**
     * Creates new form GamePage
     */
    public MainUI() {
        initComponents();
        ImageIcon[] images={correctIcon,image1,image2,image3,image4,image5, present, evil};
        JLabel[] items = {jLabel1,jLabel2,jLabel3,jLabel4,jLabel5,jLabel6,jLabel7,jLabel8,jLabel9,jLabel10,jLabel11,jLabel12,jLabel13,jLabel14,jLabel15,jLabel16,jLabel17,jLabel18,jLabel19,jLabel20,jLabel21,jLabel22,jLabel23,jLabel24,jLabel25,jLabel26,jLabel27,jLabel28,jLabel29,jLabel30};
        JLabel[] stars = {jLabel31, jLabel32, jLabel33, jLabel34, jLabel35, extraStar};
        
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
        
        File file = new File("data.txt"); 
        Scanner scan;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()){
                String str = scan.nextLine();
                if (str.equals("clicked")){
                    for (int i=0 ; i<items.length ; i++){
                        clicked.set(i, scan.nextBoolean());
                    }
                }
                else if (str.equals("answers")){
                    for (int i=0 ; i<items.length ; i++){
                        answers.add(scan.nextInt());
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GameClass game = new GameClass(items, answers,clicked,images, stars, gameText, extraStar);
        PowerUpClass powerUp = new PowerUpClass(clicked, PowerUpButton, items, answers, images, gameText);
        SettingsClass settings = new SettingsClass (restartLabel, helpLabel, exitLabel, this);
        
        new Thread(new Runnable() { //Thread to make randomizeAnswers method run and update jPanel inside a for loop
        @Override
        public void run() {
            
            if (started){
                game.coverItems();
                game.startedGame();
                powerUp.startedPowerUp();
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
                public void actionPerformed(ActionEvent event)
                {
                    if (itemClick==2 || pointss==0){
                        //timer.restart();
                        timer.stop();
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
                public void actionPerformed(ActionEvent event)
                {
                    if (evilK==true){
                        eTimer.restart();
                        game.coverItems();
                        //eTimer.stop();
                        
                    }
                }
            };
            eTimer= new Timer (800,evilAction);
            eTimer.start();

            powerUp.usePowerUp(); //check if the power up button was clicked
            settings.settings();
        }
        }).start();
        
    }
    
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        PowerUpButton = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        gameText = new javax.swing.JLabel();
        extraStar = new javax.swing.JLabel();
        restartLabel = new javax.swing.JLabel();
        helpLabel = new javax.swing.JLabel();
        exitLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(820, 600));
        setMinimumSize(new java.awt.Dimension(820, 600));

        jPanel1.setBackground(new java.awt.Color(171, 209, 236));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        jLabel7.setText("jLabel7");

        jLabel8.setText("jLabel8");

        jLabel9.setText("jLabel9");

        jLabel10.setText("jLabel10");

        jLabel11.setText("jLabel1");

        jLabel12.setText("jLabel2");

        jLabel13.setText("jLabel3");

        jLabel14.setText("jLabel4");

        jLabel15.setText("jLabel5");

        jLabel16.setText("jLabel6");

        jLabel17.setText("jLabel7");

        jLabel18.setText("jLabel8");

        jLabel19.setText("jLabel9");

        jLabel20.setText("jLabel10");

        jLabel21.setText("jLabel1");

        jLabel22.setText("jLabel2");

        jLabel23.setText("jLabel3");

        jLabel24.setText("jLabel4");

        jLabel25.setText("jLabel5");

        jLabel26.setText("jLabel6");

        jLabel28.setText("jLabel8");

        jLabel29.setText("jLabel9");

        jLabel30.setText("jLabel10");

        jLabel27.setText("jLabel7");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel12)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel13)
                    .addComponent(jLabel23))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel14)
                    .addComponent(jLabel24))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel26)
                    .addComponent(jLabel6))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(jLabel18)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel29)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel30)
                    .addComponent(jLabel10))
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel22)
                        .addComponent(jLabel23)
                        .addComponent(jLabel24)
                        .addComponent(jLabel25)
                        .addComponent(jLabel26)
                        .addComponent(jLabel29)
                        .addComponent(jLabel30)
                        .addComponent(jLabel27)))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        PowerUpButton.setBackground(new java.awt.Color(255, 255, 255));
        PowerUpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/ShookyPowerUp.png"))); // NOI18N
        PowerUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PowerUpButtonActionPerformed(evt);
            }
        });

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/pixlr-bg-result.png"))); // NOI18N

        gameText.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        gameText.setText("TEXT");

        extraStar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SummativeGame/star.png"))); // NOI18N

        restartLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        restartLabel.setText("RESTART");

        helpLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        helpLabel.setText("HELP");

        exitLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        exitLabel.setText("EXIT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel36)
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extraStar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gameText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 374, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PowerUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(restartLabel)
                            .addComponent(helpLabel)
                            .addComponent(exitLabel))))
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(gameText)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(extraStar)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel31)
                                .addComponent(jLabel32)
                                .addComponent(jLabel33)
                                .addComponent(jLabel34)
                                .addComponent(jLabel35)))
                        .addGap(21, 21, 21)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(jLabel36))
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel restartLabel;
    // End of variables declaration//GEN-END:variables
}
