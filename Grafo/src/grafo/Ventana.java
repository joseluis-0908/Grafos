/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.awt.Color;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;  

/**
 *
 * @author Jose
 */
public class Ventana extends JFrame {

    public Ventana() {
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("GRAFO");
        
         JLabel etiqueta1 = new JLabel("- DIGITE EL NODO CON EL CLICK DERECHO DEL MAUSE.");
        etiqueta1.setBounds(10, 10, 500, 10);
        this.add(etiqueta1);
        
        JLabel etiqueta2 = new JLabel("- PARA CREAR UNA ARISTA, SELECCIONE EL PRIMER NODO CON EL");
        etiqueta2.setBounds(10, 25, 500, 10);
        this.add(etiqueta2);
        
        JLabel etiqueta3 = new JLabel(" CLICK IZQUIERDO, LUEGO DAR CLIC IZQUIERDO AL SEGUNDO NODO.");
        etiqueta3.setBounds(10, 40, 500, 10);
        this.add(etiqueta3);

        this.add(etiqueta1);
        
        iniciarComponentes();
    }

    public void iniciarComponentes() {
      colocarLienzo();         
    }
    
     private void colocarLienzo(){
       Lienzo lienzo=new Lienzo();
//       panel.add(lienzo);
       this.add(lienzo);
   }    
}
