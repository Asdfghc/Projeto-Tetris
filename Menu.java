import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel implements KeyListener {
    private static final AudioPlayer musicPlayer = new AudioPlayer();
    private static final AudioPlayer soundPlayer = new AudioPlayer();
    private final JButton PlayButtonNormal;
    private final JButton PlayButton4Wide;
    private Game game;

    public Menu() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        musicPlayer.playMusic("src\\TetrisMenuMusic.wav");
        
        setLayout(null);
        
        PlayButtonNormal = new JButton("Classic");
        add(PlayButtonNormal);
        
        
        PlayButtonNormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundPlayer.playSound("src\\TetrisMenuSelectSound.wav");
                musicPlayer.stopMusic();
                game = new Game(UI.getHighScore(), 0, 10, 20);
                UI.gameScreen(game);
            }
        });
        
        
        PlayButton4Wide = new JButton("4-Wide");
        add(PlayButton4Wide);
        
        PlayButton4Wide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundPlayer.playSound("src\\TetrisMenuSelectSound.wav");
                musicPlayer.stopMusic();
                game = new Game(UI.getHighScore4Wide(), 0, 4, 20);
                UI.gameScreen(game);
            }
        });
        
        PlayButtonNormal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                PlayButtonNormal.setForeground(Color.YELLOW);
                soundPlayer.playSound("src\\TetrisMenuOptionSound.wav");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                PlayButtonNormal.setForeground(Color.WHITE);
            }
        });
        
        PlayButton4Wide.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                PlayButton4Wide.setForeground(Color.YELLOW);
                soundPlayer.playSound("src\\TetrisMenuOptionSound.wav");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                PlayButton4Wide.setForeground(Color.WHITE);
            }
        });
        
    }
    
    @Override
    public synchronized void keyPressed(KeyEvent e) { 
        if (e.getKeyCode() == KeyEvent.VK_F11) Tetris.fullScreen();
    }
    
    @Override
    public synchronized void keyReleased(KeyEvent e) {
    }
    
    @Override
    public synchronized void keyTyped(KeyEvent e) {
    }
    
    @Override 
    public void paintComponent(Graphics g) {
        
        int windowHeight = getHeight();
        int windowWidth = getWidth();
        int imageHeight = windowHeight;
        int imageWidth = imageHeight*398/224;
        int imageOriginX = windowWidth/2 - imageWidth/2;
        
        int buttonWidth = imageWidth/4;
        int buttonHeight = imageHeight/8;
        
        PlayButtonNormal.setSize(buttonWidth,buttonHeight);
        PlayButtonNormal.setLocation(imageOriginX + (int)(imageWidth / (47.0 / 10)), (imageHeight - imageHeight/3));
        PlayButtonNormal.setContentAreaFilled(false);
        PlayButtonNormal.setBorderPainted(false);
        
        PlayButton4Wide.setSize(buttonWidth,buttonHeight);
        PlayButton4Wide.setLocation(imageOriginX + (int)(imageWidth / (7.0 / 5.55)) - buttonWidth, (imageHeight - imageHeight/3));
        PlayButton4Wide.setContentAreaFilled(false);
        PlayButton4Wide.setBorderPainted(false);
        
        
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\NESCyrillic.ttf")).deriveFont(25*1f);
            PlayButtonNormal.setFont(customFont);
            PlayButton4Wide.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getWidth());
        
        try {
            BufferedImage background = ImageIO.read(new File("src\\TetrisMenuBackground.png"));
            Image scaledBackground = background.getScaledInstance(imageWidth, imageHeight, BufferedImage.SCALE_DEFAULT);
            g.drawImage(scaledBackground, imageOriginX, 0, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}