package home.alicesmagic.mw;

import home.alicesmagic.mw.view.UIGeneral;
import org.pushingpixels.substance.api.skin.*;

import javax.swing.*;

public class MainApplication extends JFrame {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteChalkLookAndFeel());
            } catch (Exception e) {
                System.out.println("Failed to initialize");
            }
            new UIGeneral();
        });
    }
}
