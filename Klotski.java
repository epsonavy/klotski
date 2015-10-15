
package klotski;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


class Klotski {
    
  void initScene() {
    JFrame frame = new JFrame("Klotski");
    frame.setSize(new java.awt.Dimension(Board.W * Unit.W +40, Board.H * Unit.H + 40));
    //frame.getContentPane().setLayout(null);
    
    frame.setLayout(new BorderLayout());
    BufferedImage bgImage = null;
    try {
      bgImage = ImageIO.read(Klotski.class.getResourceAsStream("all.jpg"));
    } catch (IOException ex) {
      System.out.println("Could not find the file!");
    }
    frame.setContentPane(new JLabel(new ImageIcon(bgImage)));
    frame.setLayout(new FlowLayout());
    
    JPanel gameBoard = new JPanel();   
    gameBoard.setSize(new java.awt.Dimension(Board.W * Unit.W, Board.H * Unit.H));
    //gameBoard.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 20));
    gameBoard.setLocation(20,20);
   
    frame.setLocation(50,100);
    frame.add(gameBoard);
    //frame.setResizable(false);
    //frame.pack();
    gameBoard.setLayout(null);

    for (Block block : BlockList.blocks) {
      gameBoard.add(block);
    }
    BlockList.rebuildOccupancy();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    
    // Just for refresh :) Not optional!
    frame.setSize(Board.W * Unit.W +41, Board.H * Unit.H + 41);
    frame.setSize(Board.W * Unit.W +40, Board.H * Unit.H + 40);
  }
/*
  public static void main(String[] args) {
      SwingUtilities.invokeLater( ()->new Klotski().initScene() );
  }
   */ 
}
