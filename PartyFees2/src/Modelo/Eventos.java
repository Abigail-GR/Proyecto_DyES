/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

/**
 *
 * @author dmesc
 */
public class Eventos {
    public void textKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if (!Character.isLetter(car) && (car != (char) KeyEvent.VK_BACK_SPACE)
                && (car != (char) KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }
 
public void numberKeyPress(KeyEvent evt, JTextField textField) {
    char car = evt.getKeyChar();
    
    // Verifica si la tecla es un número o la tecla de retroceso
    if ((car >= '0' && car <= '9') || car == (char) KeyEvent.VK_BACK_SPACE) {
        // Verifica si ya hay 10 dígitos en el campo de texto
        if (textField.getText().length() >= 10 && car != (char) KeyEvent.VK_BACK_SPACE) {
            evt.consume(); // Si hay 10 dígitos y no es la tecla de retroceso, consume el evento
        }
    } else {
        evt.consume(); // Si la tecla no es un número ni la tecla de retroceso, consume el evento
    }
}
 
    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        } else if ((car < '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }
    
    public void limitCharacters(JTextField textField, int limit, KeyEvent evt) {
        PlainDocument document = (PlainDocument) textField.getDocument();
        if (textField.getText().length() >= limit && evt.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }
}

