import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Created by alimousa on 8/9/16.
 */
public class StartScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {
        terminal.writeCenter("Find the Scroll of CPF and then exit to the surface!", 23/2, AsciiPanel.brightWhite);
        terminal.writeCenter("--press enter to start--", 22);
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
