package Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.Console;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

public class GameControls{
   public Console cn = Enigma.getConsole("Twins - Maze Game", 150, 20, 12);
   public TextMouseListener tmlis; 
   public KeyListener klis; 

   // ------ Standard variables for mouse and keyboard ------
   public int mousepr;          
   public int mousex, mousey;   
   public int keypr;   
   public int rkey;    
   // ----------------------------------------------------
   
   GameControls(GameBoard board) throws Exception{   
                 
      // ------ Standard code for mouse and keyboard ------
      tmlis=new TextMouseListener() {
         public void mouseClicked(TextMouseEvent arg0) {}
         public void mousePressed(TextMouseEvent arg0) {
            if(mousepr==0) {
               mousepr=1;
               mousex=arg0.getX();
               mousey=arg0.getY();
            }
         }
         public void mouseReleased(TextMouseEvent arg0) {}
      };
      cn.getTextWindow().addTextMouseListener(tmlis);
    
      klis=new KeyListener() {
         public void keyTyped(KeyEvent e) {}
         public void keyPressed(KeyEvent e) {
            if(keypr==0) {
               keypr=1;
               rkey=e.getKeyCode();
            }
         }
         public void keyReleased(KeyEvent e) {}
      };
      cn.getTextWindow().addKeyListener(klis);
      
      board.printBoard(cn);
      
      // Rastgele Başlangıç Noktası (Spawner)
      RandomSpawner spawner = new RandomSpawner();
      int[] spawnPoint = spawner.getSpawnPoint(board.getMap());
      int px = spawnPoint[0]; 
      int py = spawnPoint[1];
      
      // İlk Çizim: (px * 2) + 4 ve py + 2 formülü ile doğru orantılı çizilir
      cn.getTextWindow().output((px * 2) + 4, py + 2, 'P');
      
      while(true) {
         if(mousepr==1) {  
            cn.getTextWindow().output(mousex,mousey,'#');  
            // Karakterin fareye ışınlanmasını engellemek için px=mousex satırını kaldırdık
            mousepr=0;     
         }
         
         if(keypr==1) {    
             int nextX = px;
             int nextY = py;

             if(rkey==KeyEvent.VK_LEFT) nextX--;   
             if(rkey==KeyEvent.VK_RIGHT) nextX++;
             if(rkey==KeyEvent.VK_UP) nextY--;
             if(rkey==KeyEvent.VK_DOWN) nextY++;
             
             // Çarpışma Kontrolü
             CollisionControl collision = new CollisionControl();
             if (collision.canMove(board.getMap(), nextX, nextY)) {
                 px = nextX;
                 py = nextY;
             }
             
             // Hocanın Test Kodu + Görsel Düzeltme Entegrasyonu
             char rckey=(char)rkey;
             if(rckey=='%' || rckey=='\'' || rckey=='&' || rckey=='(') {
                 
                 
                 // 2. Doğru koordinata P'yi bas (Çift P sorunu çözüldü)
                 cn.getTextWindow().output((px * 2) + 4, py + 2, 'P'); 
             }
             else {
                 cn.getTextWindow().output(rckey);
             }
            
             // Boşluk Tuşu Kontrolü
             if(rkey==KeyEvent.VK_SPACE) {
                String str;         
                str=cn.readLine();     
                cn.getTextWindow().setCursorPosition(5, 20);
                cn.getTextWindow().output(str);
             }
            
             keypr=0;    
         }
         Thread.sleep(20); // Uyuma kodu olması gerektiği gibi döngünün içinde
      }
   }
}