package klotski;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Define the pixels of one Unit
// You may change the size of the block
class Unit {
  static final int H = 100;
  static final int W = 100; 
}

// Define a Board with 5 X 4 Unit
class Board {
  public static final int H = 5; 
  public static final int W = 4; 
}

class Main extends JFrame {
	public Main() {
	setTitle("Klotski");
	setSize(400,500);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
	setLayout(new BorderLayout());
  BufferedImage myImage =null;
 
  try {
    myImage = ImageIO.read(Main.class.getResourceAsStream("background.jpg"));
  } catch (IOException ex) {
    System.out.println("Could not find the file!");
  }
  
	setContentPane(new JLabel(new ImageIcon(myImage)));
	//setLayout(null);

  JPanel gameBoard = new JPanel();   
  gameBoard.setSize(new java.awt.Dimension(Board.W * Unit.W, Board.H * Unit.H));
  gameBoard.setLocation(15, 15);
  gameBoard.setOpaque(false);
  add(gameBoard);
  gameBoard.setLayout(null);

  for (Block block : BlockList.blocks) {
    gameBoard.add(block);
  }
  BlockList.rebuildOccupancy();
    
	setSize(Board.W * Unit.W + 30, Board.H * Unit.H + 50);
  setResizable(false);
	}
	public static void main(String args[]) {
    new Main();
	}
}