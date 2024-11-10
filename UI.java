import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class UI extends JPanel {
    
    private Container container;
    private CardLayout cardlayout;
    private JButton PlayButton;
    private JPanel menu;
    private Game game;
    
    public UI() {
        cardlayout = new CardLayout();
        container = new JPanel(cardlayout);
        setLayout(cardlayout);
        add(container);

        menu = new JPanel();
        PlayButton = new JButton("New Game");
        PlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen();
            }
        });
        menu.add(PlayButton);
        container.add(menu, "Menu");
    }

    public void menuScreen() {
        cardlayout.show(container, "Menu");
        container.remove(game);
    }

    public void gameScreen() {
        game = new Game();
        container.add(game, "Game");
        cardlayout.show(container, "Game");
        game.requestFocus();
    }
}

