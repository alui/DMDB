
package mx.unam.ciencias.edd;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.lang.Math;
import java.util.NoSuchElementException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.net.URL;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.Document;



import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
/**
 * Interfaz Grafica para mi graficadora.
 */
public class Controlador{
    
    
    private static final int pixelesVistaX = 600;
    private static final int pixelesVistaY = 600;
    /* Componentes de la interfaz gráfica. */
    private JFrame ventana;
    
    private PanelPintar panel;
    private JButton[] buttons;
    
    private JSpinner spinnerAncho;
    private JSpinner spinnerAlto;
    private JLabel estado;
    
    //Cargamos la vista y la mostramos.
    private void vistaCargar()
    {
        
        
        //Tiene la vista que maneja, y la carga
        ventana = new JFrame("Graficador");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JToolBar bh = creaBarraHerramientas();
        ventana.add(bh, BorderLayout.NORTH);
        panel = new PanelPintar();
        panel.setPreferredSize(new Dimension(pixelesVistaX,pixelesVistaY));
        ventana.repaint();
        
        
        estado = new JLabel("Graficador. Tip: Se puede arrastrar con el raton.");
        ventana.add(estado, BorderLayout.SOUTH);
        ventana.add(panel);
        ventana.setVisible(true);
        ventana.setResizable(true);
        
        ventana.pack();
        
    }
    
    /* Crea la barra de herramientas. */
    private JToolBar creaBarraHerramientas() {
        
        JToolBar bh = new JToolBar("Barra de Herramientas");
        
        buttons = new JButton[5];
        
        
        JLabel label = new JLabel("   f(x)=");
        bh.add(label);
        
        //fifth component is NOT a button!
        JTextField textField = new JTextField("x");
        textField.setColumns(10);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                try{
                    Parser.scanf( textField.getText());
                    
                    
                    estado.setText("Funcion valida: "+textField.getText());
                    panel.generaPuntos(textField.getText());
                    ventana.repaint();
                }
                catch(LexicalSimbolException lexical)
                {
                    estado.setText(lexical.getMessage());
                    
                }
                

            }
        });
        bh.add(textField);
        
        ChangeListener listener;
        listener= new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                
                JSpinner j = (JSpinner)e.getSource();
                String s = j.getValue().toString();

//ALTURA
                double  r =Double.parseDouble(s);
                
                panel.update(r,0,0,0,0,0);

                ventana.repaint();
                
                
            }
        };
        
        
        bh.add(new JLabel("   Eje Y: "));
        spinnerAlto = creaRotativo( 50,-10000, 10000, 10, "#0");

        
        
        spinnerAlto.addChangeListener(listener);
        bh.add(spinnerAlto);
        
        
        listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                
                JSpinner j = (JSpinner)e.getSource();
                String s = j.getValue().toString();

//ANCHURA
                double  r =Double.parseDouble(s);
                
                panel.update(0,r,0,0,0,0);

                ventana.repaint();
                
                
            }
        };
        bh.add(new JLabel("   Eje X: "));
        spinnerAncho = creaRotativo( 50,-10000, 10000, 5, "#0");
        
        spinnerAncho.addChangeListener(listener);
        
        bh.add(spinnerAncho);
        
//        
//        
        JButton clear = new JButton(" Borrar ");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                panel.reset();
                panel.generaPuntos("");
                estado.setText("Vista borrada y centrada. Tip: Se puede arrastar la vista con el raton.");
                ventana.repaint();
            }
        });
        bh.add(clear);
        
        JButton png = new JButton(" PNG ");
        png.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarPNG();
            }
        });
        bh.add(png);
        
        JButton svg = new JButton(" SVG ");
        svg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarSVG();
            }
        });
        bh.add(svg);
        
        
//        JMenu menu = new JMenu("Ayuda");
//        
//        menu.setMnemonic(KeyEvent.VK_Y);
//        bh.add(menu);
//        
//        
//        JMenuItem acercaDe = new JMenuItem("Acerca de...", KeyEvent.VK_D);
//        acercaDe.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                acercaDe();
//            }
//        });
//        menu.add(acercaDe);
        
        
        return bh;
        
        
    }
    
    private void acercaDe() {
        JOptionPane.showMessageDialog(
                                      ventana,
                                      "Graficador, v1.0\n" +
                                      "© 2015 Alfonso Casanova",
                                      "Acerca de...",
                                      JOptionPane.INFORMATION_MESSAGE);
    }
    public Controlador() {
        vistaCargar();
        
        
    }
    /* Crea un rotativo con los valores recibidos. */
    private JSpinner creaRotativo(double valor, double min,
                                  double max, double paso,
                                  String formato) {
        JSpinner spinner =
        new JSpinner(new SpinnerNumberModel(valor, min, max, paso));
        
        JSpinner.NumberEditor ne = new JSpinner.NumberEditor(spinner, formato);
        JTextField tf = ne.getTextField();
        tf.setColumns(2);
        spinner.setEditor(ne);
        return spinner;
    }
    
    private void generarPNG()
    {
        
//        System.out.println("Here:");
        BufferedImage bi = new BufferedImage((int)panel.getSize().width,(int)panel.getSize().height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        panel.paint(g);
        g.dispose();

        try{
            ImageIO.write(bi,"png",new File("pngFunciones.png"));
        }catch (Exception e) {
//            System.out.println("Error:" + e);
        }
    }
    private void generarSVG()
    {
        
        
        String m  = "";
        m+= headCreator("", 2);
        m+= panel.toScalableVectorGraphics();
        m= encapusuladorHtmlConRegreso(null,m,"Funciones");
        writeStringToFile(m,new File("svgFunciones.html"));
        
    }
    
    private static String headCreator(String title, int i)
    {
        return String.format("\n<h%d>%s</h%d>\n",i,title,i);
    }
    private static String encapusuladorHtmlConRegreso(String paginaAnterior, String aEncapsular, String tituloActual)
    {
        
        String m = ("<!DOCTYPE html>\n<html>\n<head>\n"+
                    
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"\n>"+
                    "<link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">"+
                    "<link rel=\"stylesheet\" href=\"styles.css\" />\n"+
                    "</head>");
        m+= String.format("\n<body>\n<h1 class=\"mainTitle\">%s</h1>\n<hr/>\n",tituloActual);
        m+= aEncapsular;
        m+="<br/><br/>";
        if(paginaAnterior!=null)
            m+=String.format("<a href=\"%s.html\">&larr; Volver</a>",paginaAnterior);
        m+="\n</body>\n</html>";
        
        return m ;
    }
    private static void writeStringToFile(String stringToWrite, File file)
    {
        
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            BufferedWriter out = new BufferedWriter(osOut);
            out.write(stringToWrite);
            out.close();
            
        } catch (IOException ioe) {
            System.exit(1);
        }
    }
}


class PanelPintar extends JPanel{
    
    private static int pixelesVistaX = 600;
    private static int pixelesVistaY = 600;
    

    
    
    
    double anchura;
    double altura;
    double centerX;
     double centerY;
    
    double lastX;
    double lastY;
    
    
            public Diccionario<String,ArbolSintactico<Ficha>> arboles;
            public Lista<Lista<Line2D.Double>> graficas;

        public PanelPintar() {
            
            
            reset();

            
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.black));
            
            addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e){
                    lastX = e.getX();
                    lastY = e.getY();
                }
            });
            
            addMouseMotionListener(new MouseAdapter(){
                public void mouseDragged(MouseEvent e){
                    double magX =  e.getX() -lastX;
                    double magY =  e.getY() -lastY;
                    
                    update(0,0,magX,magY,0,0);

                    
                    lastX = e.getX();
                    lastY = e.getY();
                    repaint();
                }
            });
            
            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    
                    Dimension d = e.getComponent().getSize();
                    int lastPixelesVistaX = (int)d.getWidth() - pixelesVistaX;
                    int lastPixelesVistaY = (int)d.getHeight() - pixelesVistaY;
                    
                    update(0,0,0,0,lastPixelesVistaX,lastPixelesVistaY);
                }
            });

            
        }
    public void reset(){
        
        altura=50.0;
        anchura=50.0;
        centerX = pixelesVistaX/2;
        centerY = pixelesVistaY/2;

        arboles = new Diccionario<String, ArbolSintactico<Ficha>>();
        graficas = new Lista<Lista<Line2D.Double>>();
        
    }
    
    
    
    public void update(double alt, double anch, double x, double y, int pixX,int pixY ){
        
        if(alt!=0)
            altura=alt;
        if(anch!=0)
            anchura =anch;
        
            centerX +=x;
            centerY +=y;


        
        pixelesVistaX+=pixX;
        pixelesVistaY+=pixY;
        
        generaPuntos("");
    }
    
    
    
    public void generaPuntos(String s) {

        ArbolSintactico<Ficha> as ;

        if(arboles.contiene(s)){
            as = arboles.get(s);
        }
        else
        {
            as= Parser.scanf(s);
            if(as!=null){
                //estado fallo.
                arboles.agrega(s,as);
            }
                
        }
        
        
        graficas = new Lista<Lista<Line2D.Double>>();
//
        for(ArbolSintactico<Ficha> a: arboles)
        {
//            puntos =new Lista<Line2D.Double>();
//
            Lista<Line2D.Double> perk  = new Lista<Line2D.Double>();
//            if(as!=null)

//        puntos = new Lista<Line2D.Double>();
//        if(a!=null)
//        {
            double x1;

//            for(double x = -300.0 ; x <= 300; x++)
            for(double x = -(centerX) ; x <= -(centerX-(pixelesVistaX/2))+pixelesVistaX; x++)
            {
                try{
                        double y = a.evaluar(x/anchura);
                    
//                    System.out.println("--X:"+x+"--Y:"+y);

                        y = y*altura;

                        //Estos es por la cantidad de pixeles.
                        y*=-1;//Por que el eje de Y de swing es positivo hacia abajo.
                        y+=centerY;
                        x1=x+centerX;
                        perk.agregaFinal(new Line2D.Double(x1,y,x1,y));
//                        puntos.agregaFinal(new Line2D.Double(x1,y,x1,y));
                    
                    
                    }catch(IllegalArgumentException e){}
            }
            
            
            graficas.agregaFinal(perk);
        }
            
            
            
        
        
    
        
    }
    public void paintComponent(Graphics g) {
            super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
        
        

            //Por cada funcion.
             for(Lista<Line2D.Double> puntos :graficas){
        
                Point2D.Double last = null;
                 //Graficamos puntos.
                for(Line2D.Double p :puntos){

                    if(last!=null){
                        
                        //Quitamos asintotas.
                        if(p.getY1()>0 &&  p.getY1()<=pixelesVistaY){
                            
                            g2.draw(new Line2D.Double(last,p.getP1()));
                        }
                        else
                            if(last.getY()>0 &&  last.getY()<=pixelesVistaY){
                            
                            g2.draw(new Line2D.Double(last,p.getP1()));

                            
                            }
                        
                        }
                    last = (Point2D.Double)p.getP1();
//                    g2.draw(p);

                }
             }
            g2.draw(new Line2D.Double(centerX,0,centerX,pixelesVistaY));
            g2.draw(new Line2D.Double(0,centerY, pixelesVistaX,centerY));
        }

    private double distancia(double x1,double y1, double x2, double y2)
    {
        x1= Math.abs(x1);
        y1= Math.abs(y1);
        x2= Math.abs(x2);
        y2= Math.abs(y2);
        return Math.sqrt( Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }
    public String toScalableVectorGraphics()
    {
        String m = "";
        
        m+=svgSize(getSize().width,getSize().height);
        m+=svglineTag(centerX,0,centerX,pixelesVistaY);
        m+=svglineTag(0,centerY, pixelesVistaX,centerY);
        
        //Por cada funcion.
        for(Lista<Line2D.Double> puntos :graficas){
            
            Point2D.Double last = null;
            //Graficamos puntos.
            for(Line2D.Double p :puntos){

                if(last!=null){
                    
                    //Quitamos asintotas.
                    if(p.getY1()>0 &&  p.getY1()<=pixelesVistaY){
                        
                        m+=svglineTag(last.getX(),last.getY(),p.getX1(),p.getY1());
                    }
                    else if(last.getY()>0 &&  last.getY()<=pixelesVistaY){
                            
                        m+=svglineTag(last.getX(),last.getY(),p.getX1(),p.getY1());
                            
                            
                        }
                    
                }
                last = (Point2D.Double)p.getP1();
                
//                m+=svglineTag(p.getX1(),p.getY1(),p.getX1(),p.getY1());
                
            }
        }

        return m;
        
    }
    private String svgSize(double width , double height)
    {
        return String.format("\n<div class=\"svgContainer\"> \n<svg width='%.2f' height='%.2f'>\n<g>\n",width,height);
    }
    private String svglineTag(double x1, double y1, double x2, double y2)
    {
        
        return String.format("<line x1='%.2f' y1='%.2f' x2='%.2f' y2='%.2f' stroke='black' stroke-width='0.2' />\n",x1,y1,x2,y2 );
    }

    
}
