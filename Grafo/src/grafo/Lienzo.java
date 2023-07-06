/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sun.security.util.Length;

/**
 *
 * @author Jose
 */
public class Lienzo extends JPanel implements MouseListener, MouseMotionListener {

    private Vector<Nodo> vectorNodos;
    private Vector<Enlace> vectorEnlaces;
    private Point p1, p2, p1copia, p2copia;
    private Nodo nodoMover;
    private int iNodo;
    private String[][] matriz1;
    
    private int uno = 0, unoconta = 0;
    private int uno1 = 0, unoconta1 = 0;
    private JLabel etiqNodo1;
    private JLabel etiqNodo2;
    private JLabel etiqcajaEliNodo;
    private JTextField cajaEliNodo;
    private JTextField cajaEliArNodo1;
    private JTextField cajaEliArNodo2;
    private JButton eliminarN;
    private JButton eliminarE;

    public Lienzo() {
        this.vectorNodos = new Vector<>();
        this.vectorEnlaces = new Vector<>();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Nodo nodos : vectorNodos) {
            nodos.pintar(g);
        }
        for (Enlace enlace : vectorEnlaces) {
            enlace.pintar(g);
        }
    }

    private boolean isEncima(Point p) {
        Rectangle figura = new Rectangle(p.x - Nodo.d / 2, p.y - Nodo.d / 2, Nodo.d, Nodo.d);
        for (Nodo nodo : vectorNodos) {
            if (new Rectangle(nodo.getX() - Nodo.d / 2, nodo.getY() - Nodo.d / 2, Nodo.d, Nodo.d).intersects(figura)) {
                return false;
            }
        }
        return true;
    }
    
    
    private boolean covertirNumero(String frase){
        try {
            Integer.parseInt(frase);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String validarNodoNumeroYRepetidos(String nombre) {

        boolean sw1 = false;
        while (covertirNumero(nombre) == true) {
            nombre = JOptionPane.showInputDialog("Invalido solo palabra. Ingrese nombre nodo");

            if (covertirNumero(nombre) != true) {
//                System.out.println("entre1");
                int rep = 0;
                while (rep < vectorNodos.size() && sw1 == false) {
                    if (nombre.equals(vectorNodos.get(rep).getNombre())) {
//                        System.out.println("entre2");
                        nombre = JOptionPane.showInputDialog("Invalido Ya existe. Ingrese nombre nodo");
                        if (covertirNumero(nombre) == true) {
                            sw1 = true;
                        }
                        rep = -1;
                    }
                    rep++;
                }
            }
        }

        return nombre;
    }
    
    private String validarNodoRepetidosYNumero(String nombre) {

        boolean sw2 = false;
        int rep = 0;
        while (rep < vectorNodos.size() && sw2 == false) {
            if (nombre.equals(vectorNodos.get(rep).getNombre())) {
//                        System.out.println("entre2");
                nombre = JOptionPane.showInputDialog("Invalido Ya existe. Ingrese nombre nodo");
                if (covertirNumero(nombre) == true) {
                    while (covertirNumero(nombre) == true) {
                        nombre=validarNodoNumeroYRepetidos(nombre);
                    }
                    sw2 = true;
                }
                rep = -1;
            }
            rep++;
        }

        return nombre;
    }
    
    private String validarAristaNumero(String nombre) {
        while (covertirNumero(nombre) != true) {
            nombre = JOptionPane.showInputDialog("Invalido solo numero. Ingrese nombre arista");
        }
        return nombre;
    }
    

   
    @Override
    public void mouseClicked(MouseEvent e) {
        if (isEncima(e.getPoint())) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                String nombre = JOptionPane.showInputDialog("Ingrese nombre nodo");
                if (covertirNumero(nombre) == true) {
                    nombre = validarNodoNumeroYRepetidos(nombre);
                } else {
                    nombre = validarNodoRepetidosYNumero(nombre);
                }
                this.vectorNodos.add(new Nodo(e.getX(), e.getY(), nombre));
                matriz1 = new String[vectorNodos.size()][vectorNodos.size()];
                repaint();
                uno1=1+unoconta1;
                unoconta1++;
            }
        }
        if (this.vectorNodos.size() != 0 && uno1 == 1) {
            
            componentes();
        }
        
 
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (Nodo nodo : vectorNodos) {
                if (new Rectangle(nodo.getX() - Nodo.d / 2, nodo.getY() - Nodo.d / 2, Nodo.d, Nodo.d).contains(e.getPoint())) {
                    if (p1 == null) {
                        p1 = new Point(nodo.getX(), nodo.getY());
                        p1copia = new Point(nodo.getX(), nodo.getY());

                    } else {
                        p2 = new Point(nodo.getX(), nodo.getY());
                        p2copia = new Point(nodo.getX(), nodo.getY());
                        boolean busca=buscarArista(p1,p2);
                        boolean busca1=noDejaInsertarAristaEnMismoNodo(p1,p2);
                        if (busca == true || busca1==true) {
                            if(busca == true){
                                JOptionPane.showMessageDialog(this, "Ya se encuentra esa arista", "Error!", JOptionPane.ERROR_MESSAGE);
                            }
                            if(busca1==true){
                                JOptionPane.showMessageDialog(this, "No se puede insertar en el mismo nodo", "Error!", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            String nombre = JOptionPane.showInputDialog("Ingrese nombre arista");
                            nombre = validarAristaNumero(nombre);
                            this.vectorEnlaces.add(new Enlace(p1.x, p1.y, p2.x, p2.y, nombre));
                            repaint();
                            uno = 1 + unoconta;
                            unoconta++;
                        }
                        
                        if (this.vectorEnlaces.size() != 0 && uno == 1 && p2 != null) {
                            componentes1();
                        }

                        p1 = null;
                        p2 = null;
                    }

                }
            }
        }
    }

     public void componentes() {
        StarLabels();
        StarButton();
    }
     public void componentes1() {
        StarLabels1();
        StarButton1();
    }
     
    public void StarLabels() {
        
        cajaEliNodo=new JTextField();
        cajaEliNodo.setBounds(460,480, 100, 20);
       
        this.add(cajaEliNodo);
        
        etiqcajaEliNodo=new JLabel("Digite Nodo");
        etiqcajaEliNodo.setBounds(480,500, 80, 20);
        this.add(etiqcajaEliNodo);
        
        setLayout(null); ////para que los botones y demas cosas no cambien de ubicacion
        
    }
    public void StarLabels1() {        
        cajaEliArNodo1=new JTextField();        
        cajaEliArNodo1.setBounds(10,480, 80, 20);
        this.add(cajaEliArNodo1);
        
        etiqNodo1=new JLabel("Digite Nodo 1");
        etiqNodo1.setBounds(10,500, 80, 20);
        this.add(etiqNodo1);
        
        cajaEliArNodo2=new JTextField();
        cajaEliArNodo2.setBounds(105,480, 80, 20);
        this.add(cajaEliArNodo2);
        
         etiqNodo2=new JLabel("Digite Nodo 2");
        etiqNodo2.setBounds(105,500, 80, 20);
        this.add(etiqNodo2);
        
        setLayout(null); ////para que los botones y demas cosas no cambien de ubicacion
    }

    public void StarButton() {
       
        eliminarN = new JButton("Eliminar Nodo");
        eliminarN.setBounds(450, 520, 120, 20);
        add(eliminarN);
        eliminarN.addActionListener(oyente);
        
        setLayout(null); ////para que los botones y demas cosas no cambien de ubicacion


    }
    
    public void StarButton1() {        
        eliminarE = new JButton("Eliminar Arista");
        eliminarE.setBounds(25, 520, 120, 20);
        add(eliminarE);
        eliminarE.addActionListener(oyente);
        
        setLayout(null); ////para que los botones y demas cosas no cambien de ubicacion
    }
    
    public void EliminarNodo() {
        eliminarNodo();
        verNodo();    
         eliminarNodoSuelto();
        revalidate();
        repaint();
    }
    
    public void EliminarArista() {
        eliminarArista();
        verArista();
        eliminarNodoSuelto();
        revalidate();
        repaint();
    }
    ActionListener oyente = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == eliminarN) {
                EliminarNodo();
//                eliminarN.setLocation(0, 0);
            }
            if (e.getSource() == eliminarE) {
                EliminarArista();
            }
        }

    };
    
    private void eliminarNodo() {
        String frase = cajaEliNodo.getText();
        boolean sw = false;
        if (frase.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nodo eliminar Vacío", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
            int i = 0;
            while (i < vectorNodos.size() && sw == false) {
                if (frase.equals(vectorNodos.get(i).getNombre())) {
                    for (int ii = 0; ii < vectorEnlaces.size(); ii++) {
                        if ((vectorNodos.get(i).getX() == vectorEnlaces.get(ii).getX1())
                                || (vectorNodos.get(i).getY()== vectorEnlaces.get(ii).getY1())
                                || (vectorNodos.get(i).getX() == vectorEnlaces.get(ii).getX2())
                                || (vectorNodos.get(i).getY()== vectorEnlaces.get(ii).getY2())) {
                            vectorEnlaces.remove(ii);
                            ii--;
                        }
                    }
                    vectorNodos.remove(i);
                    sw = true;

                }
                i++;
            }
            if(sw==false){
                 JOptionPane.showMessageDialog(this, "No se encontro el nodo", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
    
    private void eliminarNodoSuelto() {
        
        int i = 0;
        while (i < vectorNodos.size()) {
            if (vectorEnlaces.size() != 0) {
                int j=0;
                boolean sw=false;
                while(j<vectorEnlaces.size() && sw==false){
                    if ((vectorNodos.get(i).getX() == vectorEnlaces.get(j).getX1())
                            || (vectorNodos.get(i).getX() == vectorEnlaces.get(j).getX2())
                            || (vectorNodos.get(i).getY()== vectorEnlaces.get(j).getY1())
                            || (vectorNodos.get(i).getY()== vectorEnlaces.get(j).getY2())) {
                        sw=true;
                    }
                    j++;
                }
                if(sw==false){
                    vectorNodos.remove(i);
                    i--;
                }
            }else{
                vectorNodos.remove(i);
                i--;
            }
            i++;
        }
    }
    
    private boolean buscarArista(Point p1, Point p2){
       // System.out.println("grafo.Lienzo.buscarArista()"+cajaEliArNodo1);
        boolean sw2 = false;

        int x1 = p1.x;
        int y1=p1.y;
        int x2 = p2.x;
        int y2 = p2.y;

        
        if (vectorEnlaces.size() != 0) {
            //System.out.println("se puede ");  
            for (int i = 0; i < vectorEnlaces.size(); i++) {
                if ((x1 == vectorEnlaces.get(i).getX1() || x1 == vectorEnlaces.get(i).getX2())
                        && (x2 == vectorEnlaces.get(i).getX1() || x2 == vectorEnlaces.get(i).getX2())
                        && (y1 == vectorEnlaces.get(i).getY1() || y1 == vectorEnlaces.get(i).getY2())
                        && (y2 == vectorEnlaces.get(i).getY1() || y2 == vectorEnlaces.get(i).getY2())) {
                    sw2 = true;
                }
            }

        }
               
        return sw2;
    }
    
    private boolean noDejaInsertarAristaEnMismoNodo(Point p1, Point p2) {
        // System.out.println("grafo.Lienzo.buscarArista()"+cajaEliArNodo1);
        boolean sw2 = false;

        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;

        if (x1==x2 && y1==y2) {
            sw2 = true;
        }

        return sw2;
    }
    

    private void eliminarArista() {
        String nodo1 = cajaEliArNodo1.getText();
        String nodo2 = cajaEliArNodo2.getText();
        boolean sw = false;
        boolean sw1 = false;
        int x1 = 0, x2 = 0;
        int y1 = 0, y2 = 0;

        if ((!nodo1.equals(nodo2)) && (!nodo1.isEmpty() && !nodo2.isEmpty()) && vectorEnlaces.size() != 0) {
            for (int i = 0; i < vectorNodos.size(); i++) {
                if (nodo1.equals(vectorNodos.get(i).getNombre())) {
                    sw = true;
                    x1 = vectorNodos.get(i).getX();
                    y1 = vectorNodos.get(i).getY();

                }
                if (nodo2.equals(vectorNodos.get(i).getNombre())) {
                    sw1 = true;
                    x2 = vectorNodos.get(i).getX();
                    y2 = vectorNodos.get(i).getY();
                }
            }
        }

        boolean sw2 = false;
        if (sw == true && sw1 == true && !nodo1.equals(nodo2) && (!nodo1.isEmpty() && !nodo2.isEmpty()) && vectorEnlaces.size() != 0) {
            //System.out.println("se puede ");  
            for (int i = 0; i < vectorEnlaces.size(); i++) {
                if ((x1 == vectorEnlaces.get(i).getX1() || x1 == vectorEnlaces.get(i).getX2())
                        && (x2 == vectorEnlaces.get(i).getX1() || x2 == vectorEnlaces.get(i).getX2())
                        && (y1 == vectorEnlaces.get(i).getY1() || y1 == vectorEnlaces.get(i).getY2())
                        && (y2 == vectorEnlaces.get(i).getY1() || y2 == vectorEnlaces.get(i).getY2())) {
                    sw2 = true;
                    vectorEnlaces.remove(i);
                }
            }

        }

        if (nodo1.isEmpty() || nodo2.isEmpty()) {
            if (nodo1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nodo1 Vacío", "Error!", JOptionPane.ERROR_MESSAGE);
            }
            if (nodo2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nodo2 Vacío", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (sw2 == false) {
                JOptionPane.showMessageDialog(this, "No encontro la arista", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    
   private void verNodo(){
       for (Nodo nodos : vectorNodos) {
            Graphics g = getGraphics();
            nodos.pintar(g);
        }
   }
   private void verArista(){
        for (Enlace enlace : vectorEnlaces) {
            Graphics g = getGraphics();
            enlace.pintar(g);
        }
       
   }

    @Override
    public void mousePressed(MouseEvent e) {
        int iN = 0;
        for (Nodo nodo : vectorNodos) {
            if (new Rectangle(nodo.getX() - Nodo.d / 2, nodo.getY() - Nodo.d / 2, Nodo.d, Nodo.d).contains(e.getPoint())) {
                nodoMover = nodo;
                iNodo = iN;
                break;
            }
            iN++;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        nodoMover = null;
        iNodo = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (nodoMover != null) {
            this.vectorNodos.set(iNodo, new Nodo(e.getX(), e.getY(), nodoMover.getNombre()));
            int iE = 0;
            for (Enlace enlace : vectorEnlaces) {
                if (new Rectangle(enlace.getX1() - Nodo.d / 2, enlace.getY1() - Nodo.d / 2, Nodo.d, Nodo.d).contains(e.getPoint())) {
                    this.vectorEnlaces.set(iE, new Enlace(e.getX(), e.getY(), enlace.getX2(), enlace.getY2(), enlace.getNombre()));
                } else if (new Rectangle(enlace.getX2() - Nodo.d / 2, enlace.getY2() - Nodo.d / 2, Nodo.d, Nodo.d).contains(e.getPoint())) {
                    this.vectorEnlaces.set(iE, new Enlace(enlace.getX1(), enlace.getY1(), e.getX(), e.getY(), enlace.getNombre()));
                }
                iE++;
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
