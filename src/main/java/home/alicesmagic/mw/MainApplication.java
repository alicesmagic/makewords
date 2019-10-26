package home.alicesmagic.mw;

import home.alicesmagic.mw.model.storage.DataStorage;
import home.alicesmagic.mw.view.UIGeneral;
import org.pushingpixels.substance.api.skin.*;

import javax.swing.*;
import java.util.Arrays;
import java.util.TreeSet;

public class MainApplication extends JFrame {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteChalkLookAndFeel());
            } catch (Exception e) {
                System.out.println("Failed to initialize");
            }
            UIGeneral gen = new UIGeneral();
        });
        DataStorage dataStorage = new DataStorage("rus");
        TreeSet<String> ts = dataStorage.load();
        dataStorage.save(ts);
        for (String w: ts) {
            System.out.println(w);
        }


    }
}