package klotski;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.Image;

// Define a Block with specific Unit and image
class Block extends JPanel implements MouseListener, MouseMotionListener {
  static final int X_AXIS = 0, Y_AXIS = 1; // Define direction for moving
  int movingDirection = -1;
  Point startingPoint = new Point();
  Point previousPoint = new Point();
  BufferedImage image = null;
  public Block(int x,int y, int w, int h, String type) {
    JPanel panel = this;
    panel.setLayout(null);
    image = getImg(type);
    image = toBufferedImage(image.getScaledInstance(w * Unit.W, h * Unit.H, Image.SCALE_SMOOTH));
    JLabel picLabel = new JLabel(new ImageIcon(image));
    panel.add(picLabel);
    Insets insets = panel.getInsets(); 
    picLabel.setBounds(insets.left, insets.top, w * Unit.W, h * Unit.H);   
    panel.setBorder(BorderFactory.createRaisedBevelBorder());
    panel.setBounds(x * Unit.W, y * Unit.H, w * Unit.W, h * Unit.H);
 
    panel.addMouseListener(this);
    panel.addMouseMotionListener(this);
  }
  
  // Get image for specific block
  BufferedImage getImg(String input) {
    String type;
    BufferedImage myImage = null;
    switch(input) {
      case "1by1":
        type = "1by1.jpg";
        break;
      case "1by2":
        type = "1by2.jpg";
        break;
      case "2by1":
        type = "2by1.jpg";
        break;
      case "2by2":
        type = "2by2.jpg";
        break;
      default:
        type = null;
    }
    try {
      myImage = ImageIO.read(Klotski.class.getResourceAsStream(type));
    } catch (IOException ex) {
      System.out.println("Could not find the file!");
    }
    return myImage;
  }
  
  // Convert Image type to BufferedImage type
  public static BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
        return (BufferedImage) img;
    }
    BufferedImage bimage = new BufferedImage
      (img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();
    return bimage;
  }
  
  @Override // MouseListener
  public void mouseExited(MouseEvent e) {
    // Do nothing
  }
 
  @Override // MouseListener
  public void mouseEntered(MouseEvent e) {
    // Do nothing
  }
 
  @Override // MouseListener
  public void mouseReleased(MouseEvent e) {
    int x = this.getX();
    int y = this.getY();
    double xRound = Math.round(x / (double)(Unit.W));
    double yRound = Math.round(y / (double)(Unit.H));
    x = (int) (xRound * Unit.W);
    y = (int) (yRound * Unit.H);
 
    this.setLocation(x,y);
    BlockList.rebuildOccupancy();
    
    // Win condition check
    if (this.getWidth() == Unit.W*2 && this.getHeight() == Unit.H*2
       && this.getX() == Unit.W && this.getY() == Unit.H*3) {
      for (Block block:BlockList.blocks) {
        block.removeMouseListener(block);
        block.removeMouseMotionListener(block);
      }
      String message = "Congratulations ! Puzzle solved !";
      javax.swing.JOptionPane.showMessageDialog(this, message);
    }
 
  }
 
  @Override // MouseListener
  public void mousePressed(MouseEvent e) {
    startingPoint = e.getPoint();
    previousPoint = e.getPoint();
    movingDirection=-1;
 
    // Remove selected block from the occupancy array
    java.awt.Rectangle rect = this.getBounds();
    int ulx = rect.x / Unit.W;
    int uly = rect.y / Unit.H;
    int w = rect.width/Unit.W;
    int h = rect.height/Unit.H;
    for (int i=ulx;i<ulx+w;i++)
      for (int j=uly;j<uly+h;j++)
        BlockList.occupied[i][j]=false;
 
  }
 
  @Override // MouseListener
  public void mouseClicked(MouseEvent e) {
    // Do nothing
  }
 
  @Override // MouseMotionListener
  public void mouseMoved(MouseEvent e) {
    // Do nothing
  }
 
  @Override // MouseMotionListener
  public void mouseDragged(MouseEvent e) { 
    int dx = e.getX() - previousPoint.x;
    int dy = e.getX() - previousPoint.y;
    previousPoint = e.getPoint();
  
    dx=e.getX() - startingPoint.x;
    dy=e.getY() - startingPoint.y;
 
    int x = this.getX();
    int y = this.getY();
 
    // When movement is > 5 pixels then determining the moving axis
    if (Math.abs(dx)<5 && Math.abs(dy)<5) return;
 
    // Restrict the movement on the same axis until the mouse is released
    if (Math.abs(dx)>Math.abs(dy) && movingDirection!=Y_AXIS) {
      for (int i=0;i<Math.abs(dx);i++)
        if (movable(dx/Math.abs(dx),0)) {
          x += dx/Math.abs(dx);
          movingDirection=X_AXIS;
          this.setLocation(x,y);
        }
    }
    if (Math.abs(dy)>Math.abs(dx) && movingDirection!=X_AXIS) {
      for (int i=0;i<Math.abs(dy);i++)
        if (movable(0,dy/Math.abs(dy))) {
          y += dy/Math.abs(dy);
          movingDirection=Y_AXIS;
          this.setLocation(x,y);
        }
    }
 
    // Allow to switch between X_AXIS and Y_AXIS direction
    if (this.getX() % Unit.W==0 && this.getY() % Unit.H==0) {
      movingDirection=-1;
    }
 
  }
 
  boolean movable(int dx, int dy) {
    int x=this.getX();
    int y=this.getY();
    int row=this.getHeight()/Unit.H;
    int column=this.getWidth()/Unit.W;
 
    x+=dx; 
    y+=dy;
 
    double xRound = Math.round(x/(double)(Unit.W));
    double yRound = Math.round(y/(double)(Unit.H));
    double xFloor = Math.floor(x/(double)(Unit.W));
    double yFloor = Math.floor(y/(double)(Unit.H));
    double xCeil = Math.ceil(x/(double)(Unit.W));
    double yCeil = Math.ceil(y/(double)(Unit.H));
 
    // Adjust the position with the floor or the ceiling function
    int newLocationX=(int) xRound;
    int newLocationY=(int) yRound;  
    if (dx<0) newLocationX=(int) xFloor;
    if (dx>0) newLocationX=(int) xCeil;
    if (dy<0) newLocationY=(int) yFloor;
    if (dy>0) newLocationY=(int) yCeil;
 
    // Not allow block move over board
    if (newLocationX<0 || newLocationY<0) return false;
    if (newLocationX+column-1>=Board.W || newLocationY+row-1 >= Board.H) return false;

    // Check its position is not already occupied
    for (int i=0;i<column;i++)
      for (int j=0;j<row;j++)
        if (BlockList.occupied[newLocationX+i][newLocationY+j]) return false;
 
    return true; 
  }
}
