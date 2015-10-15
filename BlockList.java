package klotski;

import java.awt.Rectangle;

class BlockList {
  static Block[] blocks = new Block[] {
    new Block(1, 0, 2, 2, "2by2"),
    new Block(0, 0, 1, 2, "1by2"),
    new Block(0, 2, 1, 2, "1by2"),
    new Block(3, 0, 1, 2, "1by2"),
    new Block(3, 2, 1, 2, "1by2"),
    new Block(1, 2, 2, 1, "2by1"),
    new Block(1, 3, 1, 1, "1by1"),
    new Block(2, 3, 1, 1, "1by1"),
    new Block(0, 4, 1, 1, "1by1"),
    new Block(3, 4, 1, 1, "1by1")
  };
 
  static boolean[][] occupied = new boolean[Board.W][Board.H];
 
  static void rebuildOccupancy() {
    // Initial empty board
    for (int i = 0;i < Board.W;i++) {
      for (int j = 0;j < Board.H;j++) {
        occupied[i][j] = false;
      }
    }
    // Build occupancy array
    for (Block block : blocks) {
      Rectangle rect = block.getBounds();
      int ulx = rect.x / Unit.W;
      int uly = rect.y / Unit.H;
      int w = rect.width / Unit.W;
      int h = rect.height / Unit.H;
      for (int i = ulx;i < ulx + w;i++) {
        for (int j = uly;j < uly + h;j++) {
          occupied[i][j] = true;
        }
      }
    }
  }
}
