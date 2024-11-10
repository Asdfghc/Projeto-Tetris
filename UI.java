import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UI extends JPanel implements ActionListener {
    private static boolean paused = false;
    private static boolean forcePaused = false;
    private Board board;
    private static AudioPlayer musicPlayer = new AudioPlayer();
    private static AudioPlayer soundPlayer = new AudioPlayer();

    Container container;
    CardLayout cardlayout;
    JButton PlayButton;
    private JPanel Game;
    
    public UI() {
        cardlayout = new CardLayout();
        container = new JPanel(cardlayout);

        JPanel Menu = new JPanel();
        PlayButton = new JButton("New Game");
        PlayButton.addActionListener(this);
        Menu.add(PlayButton);

        board = new Board();
        board.newPiece();

        Game = new JPanel() {
            
            @Override 
            public void paintComponent(Graphics g) {
        
                super.paintComponent(g);
        
                //Menu UI
                    //Main Tittle
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Clarendo", Font.BOLD, 80));
                    g.drawString("TETRIS", getWindowWidth()/2 , (int) ((Board.BOARD_HEIGHT - 6.6)*getSquareSize()));
                
        
                //Board UI
                    // all Panel Color
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWindowWidth(), getWindowHeight());
        
                    // Board Box Color
                    g.setColor(Color.CYAN);
                    g.fillRect(getWindowWidth()/2 - (int) ((Board.BOARD_WIDTH/2+0.25)*getSquareSize()), getWindowHeight()/20 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH+0.5)*getSquareSize()), (int) ((Board.BOARD_HEIGHT+0.5)*getSquareSize()));
        
                    //inside Board color
                    g.setColor(Color.BLACK);
                    g.fillRect(getWindowWidth()/2 - Board.BOARD_WIDTH/2*getSquareSize(), getWindowHeight()/20 - 1, getSquareSize()*Board.BOARD_WIDTH, getSquareSize()* Board.BOARD_HEIGHT);
        
                // Score UI
                    //Box UI
                    g.setColor(Color.CYAN);
                    g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/17 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));
        
                    g.setColor(Color.black);
                    g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/14 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));
        
                    // Text UI
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Clarendo", Font.BOLD, 20));
                    g.drawString("Score: " + board.getScore(), getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.10)*getSquareSize()));
        
                // Next Pice UI
                    //Box UI
                    g.setColor(Color.CYAN);
                    g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/4 - (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  4)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 14.75)*getSquareSize()));
        
                    g.setColor(Color.black);
                    g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()), (int) ((Board.BOARD_WIDTH -  4.5)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 15.25)*getSquareSize()));
        
                    // Piece UI
                    g.setColor(board.getNextPiece().getMainColor()); //TODO: mudar para a cor da peça completa
                    for(int[] square : board.getNextPiece().getShape(0)){
                        g.fillRect( (square[0] * getSquareSize()) + getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2.5 )*getSquareSize()), (square[1] * getSquareSize()) + getWindowHeight()/4 - (int) (0.25*getSquareSize()) + (int) (0.25*getSquareSize()), getSquareSize() -1, getSquareSize() -1);
                    }
                    
                // Level UI
                    //Box UI
                    g.setColor(Color.CYAN);
                    g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/2 -  (int) (0.25*getSquareSize()) + 3* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));
                    
                    g.setColor(Color.black);
                    g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/2 - (int) (0.25*getSquareSize()) + 4* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));
        
                    // Text UI
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Clarendo", Font.BOLD, 20));
                    g.drawString("Level: " + board.getLevel(), getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 2)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 6.6)*getSquareSize()));
                
                //Lines UI maybe
                    //Box UI
                    //TODO: fazer as caixas
                    //g.setColor(Color.CYAN);
                    //g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1)*getSquareSize()), getWindowHeight()/2 -  (int) (0.25*getSquareSize()) + 3* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  2.75)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17)*getSquareSize()));
                    
                    //g.setColor(Color.black);
                    //g.fillRect(getWindowWidth()/2  + (int) ((Board.BOARD_WIDTH/2 + 1.25)*getSquareSize()), getWindowHeight()/2 - (int) (0.25*getSquareSize()) + 4* (((int) (0.25*getSquareSize()))), (int) ((Board.BOARD_WIDTH -  3.25)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.5)*getSquareSize()));
        
        
                    //Text UI
                    //g.setColor(Color.WHITE);
                    //g.setFont(new Font("Clarendo", Font.BOLD, 20));
                    //g.drawString("Lines: " + board.getLines(), (int) ((Board.BOARD_WIDTH -  4)*getSquareSize()), (int) ((Board.BOARD_HEIGHT - 17.10)*getSquareSize()));
                    
                for (int i = 0; i < Board.BOARD_WIDTH; i++) {
                    for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
                        g.setColor(board.getSquare(i, j).getMainColor());
                        g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2) + 1, getWindowHeight()/20 + getSquareSize()*j + 1, getSquareSize() - 2, getSquareSize() - 2);
                        g.setColor(board.getSquare(i, j).getHighlightColor());
                        g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2) + 1, getWindowHeight()/20 + getSquareSize()*j + 1, getSquareSize()/6, getSquareSize()/6);
                        if (!board.getSquare(i, j).isVariant()) {
                            g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2) + getSquareSize()/6 + 1, getWindowHeight()/20 + getSquareSize()*j + getSquareSize()/6 + 1, getSquareSize()/3, getSquareSize()/6);
                            g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2) + getSquareSize()/6 + 1, getWindowHeight()/20 + getSquareSize()*j + getSquareSize()/6 + 1, getSquareSize()/6, getSquareSize()/3);
                        } else {
                            g.fillRect(getWindowWidth()/2 + getSquareSize()*(i-Board.BOARD_WIDTH/2) + getSquareSize()/6 + 1, getWindowHeight()/20 + getSquareSize()*j + getSquareSize()/6 + 1, getSquareSize()*2/3, getSquareSize()*2/3);
                        }
                    }
                }
            }
        };
        
        container.add(Menu, "Menu");

        setLayout(new CardLayout());
        add(container);

        cardlayout.show(container, "Menu");

    }
    
    public static void playMusic(String path) {
        musicPlayer.playMusic(path);
    }

    public static void stopMusic() {
        musicPlayer.stopMusic();
    }

    /*
    public static void pauseMusic() {
        musicPlayer.pause();
    }

    public static void resumeMusic() {
        musicPlayer.resume();
    }
    */

    public static void playSound(String path) {
        soundPlayer.playSound(path);
    }

    public void restart() {
        board = new Board();
        forceUnpause();
        if(paused) pause();
        board.newPiece();
    }

    public static boolean isPaused() {
        return paused || forcePaused;
    }

    public void pause() {
        paused = !paused;
        if (paused) {
            musicPlayer.pause();
        } else {
            musicPlayer.resume();
        }
    }

    public static void forcePause() {
        forcePaused = true;
    }

    public static void forceUnpause() {
        forcePaused = false;
    }

    public int getWindowHeight() {
        return (int) (getSize().getHeight());
    }

    public int getWindowWidth() {
        return (int) (getSize().getWidth());
    }

    public int getSquareSize() {
        return getWindowHeight()*9/10/Board.BOARD_HEIGHT;
    }

    public void rotatePieceRight() {
        if (!isPaused()) {
            board.tryMove(0, 0, 1);
            repaint();
            if(!soundPlayer.getSoundIsRunning()) {
                soundPlayer.playSound("src\\RotatePieceSound.wav");
            }
        }
    }

    public void rotatePieceLeft() {
        if (!isPaused()) {
            board.tryMove(0, 0, 3);
            repaint();
            if(!soundPlayer.getSoundIsRunning()) {
                soundPlayer.playSound("src\\RotatePieceSound.wav");
            }
        }
    }

    public void movePieceDown() {
        if (!isPaused()) {
            board.movePieceDown();
            repaint();
        }
    }

    public void movePieceLeft() {
        if (!isPaused()) {
            board.tryMove(-1, 0, 0);
            repaint();
            if(!soundPlayer.getSoundIsRunning()) {
                soundPlayer.playSound("src\\MovingPieceSound.wav");
            }
        }
    }

    public void movePieceRight() {
        if (!paused && !forcePaused) {
            board.tryMove(1, 0, 0);
            repaint();
            if(!soundPlayer.getSoundIsRunning()) {
                soundPlayer.playSound("src\\MovingPieceSound.wav");
            }
        }
    }

    public static void repaintUI() {
        Tetris.game.repaint();
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == PlayButton) {
            
            container.add(Game, "Game");
            restart();
            cardlayout.show(container, "Game");
            Game.requestFocus();
        }
    }
}

