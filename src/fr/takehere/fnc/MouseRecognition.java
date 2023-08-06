package fr.takehere.fnc;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseRecognition implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Fnc.isSelecting){
            if (Fnc.coord1 == null){
                Fnc.coord1 = e.getLocationOnScreen();
                Fnc.instructions.setText("Cliquez sur la deuxième coordonnée !");
            }else {
                Fnc.coord2 = e.getLocationOnScreen();
                Fnc.window.setBackground(new Color(0,0,0,0));
                Fnc.instructions.setText("Cheat lancé !");
                Fnc.cheat();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
