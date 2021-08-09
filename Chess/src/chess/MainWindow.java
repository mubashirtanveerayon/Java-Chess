package chess;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
    
    Board board;
    GamePanel gamePanel;
    
    public static void main(String[] args){
        MainWindow mw=new MainWindow();
        Thread thread = new Thread(){
            public void run(){
                mw.setVisible(true);
            }
        };
        thread.start();
    }
    
    public MainWindow(){
        setSize(800,700);
        setLocation(150,60);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board = new Board();
        gamePanel=new GamePanel(board);
        initComponents();
        pack();
    }
    
    public void initComponents(){
        gamePanel.setLocation(100, 10);
        this.add(gamePanel);
    }
    
    
    
}
