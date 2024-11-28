import java.awt.CardLayout;
import java.awt.Container;
import javax.swing.JPanel;

public class UI extends JPanel {
    
    private static Container container;
    private static CardLayout cardlayout;
    private Game game;
    private static int highScore = 0;
    
    public UI() {
        cardlayout = new CardLayout();
        container = new JPanel(cardlayout);
        setLayout(cardlayout);
        add(container);
        Menu menu = new Menu();
        container.add(menu, "Menu");
    }

    public void menuScreen() {
        highScore = game.getHighScore();
        cardlayout.show(container, "Menu");
        container.remove(game);
    }

    public static void gameScreen(Game game) {
        container.add(game, "Game");
        cardlayout.show(container, "Game");
        game.requestFocus();
    }

    public static int getHighScore() {
        return highScore;
    }
}

