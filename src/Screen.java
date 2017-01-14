import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Created by alimousa on 8/9/16.
 */
public interface Screen {
    void displayOutput(AsciiPanel terminal);

    Screen respondToUserInput(KeyEvent key);
}
