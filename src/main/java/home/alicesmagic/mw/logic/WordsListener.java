package home.alicesmagic.mw.logic;

import home.alicesmagic.mw.view.UITabWords;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WordsListener implements ActionListener, ChangeListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        UITabWords.getTpResult().setText("Hello!");
        System.out.println("Hello!");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        UITabWords.setMaxLetters((int)UITabWords.getsLength().getValue());
    }
}
