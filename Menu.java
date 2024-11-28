import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel {
    private final JButton PlayButton;
    private Game game;

    public Menu() {
        setLayout(null);
        PlayButton = new JButton("New Game");
        int windowHeight = (int) (getSize().getHeight()/2);
        int windowWidth = (int) (getSize().getWidth()/2);
    
        PlayButton.setBounds(windowWidth+500, windowHeight+500,100,50);
    
        PlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = new Game(UI.getHighScore(), 0, 10, 20);
                UI.gameScreen(game);
            }
        });
        add(PlayButton);
    }

@Override 
public void paintComponent(Graphics g) {
    
    int windowHeight = (int) (getSize().getHeight());
    int windowWidth = (int) (getSize().getWidth());
    //int imageOriginY = windowHeight/2 - imageHeight/2;

        try {
            BufferedImage background = ImageIO.read(new File("src\\TetrisMenuBackground.png"));
            Image scaledBackground = background.getScaledInstance(windowWidth, windowHeight, BufferedImage.SCALE_DEFAULT);
            g.drawImage(scaledBackground, 0, 0, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}