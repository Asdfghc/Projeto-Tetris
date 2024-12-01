import java.awt.CardLayout;
import java.awt.Container;
import javax.swing.JPanel;

public class UI extends JPanel {
    
    private static Container container;
    private static CardLayout cardlayout;
    private static int highScore = 0;
    private static int highScore4Wide = 0;
    Menu menu = new Menu();
    
    public UI() {
        cardlayout = new CardLayout();
        container = new JPanel(cardlayout);
        setLayout(cardlayout);
        add(container);
        container.add(menu, "Menu");
        menu.requestFocus();
    }

    public void menuScreen(Game game) {
        if(game.getBoardWidth() == 10 && game.getBoardHeight() == 20) {
            highScore = game.getHighScore();
        } else {
            highScore4Wide = game.getHighScore();
        }
        
        cardlayout.show(container, "Menu");
        container.remove(game);

        menu.requestFocus();
    }

    public static void gameScreen(Game game) {
        container.add(game, "Game");
        cardlayout.show(container, "Game");
        game.requestFocus();
    }

    public static int getHighScore() {
        return highScore;
    }

    public static int getHighScore4Wide() {
        return highScore4Wide;
    }
}

