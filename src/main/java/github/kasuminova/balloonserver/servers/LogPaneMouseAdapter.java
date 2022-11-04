package github.kasuminova.balloonserver.servers;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogPaneMouseAdapter extends MouseAdapter {
    private final JPopupMenu logPaneMenu;
    private final JTextPane logPane;

    public LogPaneMouseAdapter(JPopupMenu logPaneMenu, JTextPane logPane) {
        this.logPaneMenu = logPaneMenu;
        this.logPane = logPane;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            logPaneMenu.show(logPane, e.getX(), e.getY());
        }
    }
}
