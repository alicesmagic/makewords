package home.alicesmagic.mw.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class PopupMenu extends JPanel {
    private JMenuItem google;
    private JMenuItem yandex;
    private JMenuItem wiki;
    private JMenuItem widi;
    private JMenuItem del;
    private JPopupMenu popupMenu;

    PopupMenu() {
        popupMenu = new JPopupMenu();
        google = new JMenuItem("Google");
        yandex = new JMenuItem("Яндекс");
        wiki = new JMenuItem("Википедия");
        widi = new JMenuItem("Викисловарь");
        del = new JMenuItem("Удалить из словаря");

        popupMenu.add(google);
        popupMenu.add(yandex);
        popupMenu.add(wiki);
        popupMenu.add(widi);
        popupMenu.addSeparator();
        popupMenu.add(del);
        popupMenu.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.setEnabled(false);
    }

    public void setEnabled(boolean b) {
        google.setEnabled(b);
        yandex.setEnabled(b);
        wiki.setEnabled(b);
        widi.setEnabled(b);
        del.setEnabled(b);
    }

    void searchNet(String word, String searcher) {
        try {
            String googPath = "https://www.google.com/search?q=";
            String yandPath = "https://yandex.ru/search/?text=";
            String wikiPath = "https://ru.wikipedia.org/wiki/";
            String widiPath = "https://ru.wiktionary.org/wiki/";
            switch (searcher) {
                case "g":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                            .isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(googPath + word));
                        break;
                    }
                case "y":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                            .isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(yandPath + word));
                        break;
                    }
                case "w":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                            .isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(wikiPath + word));
                        break;
                    }
                case "d":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                            .isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(widiPath + word));
                        break;
                    }
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    AbstractButton getGoogle() {
        return google;
    }

    AbstractButton getYandex() {
        return yandex;
    }

    AbstractButton getWiki() {
        return wiki;
    }

    AbstractButton getWidi() {
        return widi;
    }

    AbstractButton getDel() {
        return del;
    }

    JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
