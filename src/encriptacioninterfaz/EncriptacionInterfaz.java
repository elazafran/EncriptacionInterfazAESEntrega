/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encriptacioninterfaz;
import java.security.Key;


import javax.crypto.spec.SecretKeySpec;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author elaza
 */
public class EncriptacionInterfaz extends javax.swing.JFrame {

  
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea textArea;
 
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EncriptacionInterfaz frame = new EncriptacionInterfaz();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
 
    /**
     * Create the frame.
     */
    public EncriptacionInterfaz() {
        
        //Parametros asociados a la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
 
        textField = new JTextField();
        textField.setToolTipText("Inserta la ruta del fichero ");
        textField.setBounds(52, 26, 209, 20);
        contentPane.add(textField);
        textField.setColumns(10);
 
        JButton btnSeleccionar = new JButton("Seleccionar...");
        btnSeleccionar.setBounds(288, 25, 109, 23);
        contentPane.add(btnSeleccionar);
        /*
        JButton btnGuardar = new JButton("Guardar...");
        btnGuardar.setBounds(288, 260, 109, 23);
        contentPane.add(btnGuardar);
            */
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(52, 76, 360, 156);
 
        JScrollPane scroll=new JScrollPane(textArea);
        scroll.setBounds(52, 76, 360, 156);
        contentPane.add(scroll);
 
        btnSeleccionar.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                    //Creamos el objeto JFileChooser
                    JFileChooser fc=new JFileChooser();

                    //Indicamos que podemos seleccionar varios ficheros
                    fc.setMultiSelectionEnabled(true);

                    //Indicamos lo que podemos seleccionar
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                    //Creamos el filtro
                    FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TXT", "txt");

                    //Le indicamos el filtro
                    fc.setFileFilter(filtro);

                    //Abrimos la ventana, guardamos la opcion seleccionada por el usuario
                    int seleccion=fc.showOpenDialog(contentPane);

                    //Si el usuario, pincha en aceptar
                    if(seleccion==JFileChooser.APPROVE_OPTION){

                        try {
                            //Seleccionamos el fichero
                            File[] ficheros=fc.getSelectedFiles();
                            System.out.println("La ruta es :"+ficheros[0]);
                            
                            /*****
                             *
                             * Guardamos la ruta del fichero!!
                             *
                             ******/
                            // Generamos una clave de 128 bits adecuada para AES
                            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                            keyGenerator.init(128);
                            Key key = keyGenerator.generateKey();
                            
                            // Alternativamente, una clave que queramos que tenga al menos 16 bytes
                            // y nos quedamos con los bytes 0 a 15
                            key = new SecretKeySpec("una clave de 16 bytes".getBytes(),  0, 16, "AES");

                            System.out.println("La clave es :"+key);
                            
                            /*****
                             *
                             * Guardamos la ruta del fichero!!
                             *
                             ******/
                            
                            for(int i=0;i<ficheros.length;i++){
                                try(FileReader fr=new FileReader(ficheros[i])){
                                    String cadena="";
                                    int valor=fr.read();
                                    while(valor!=-1){
                                        cadena=cadena+(char)valor;
                                        valor=fr.read();
                                    }
                                    textArea.append(cadena+"\n");
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            
                            // Ver como se puede guardar esta clave en un fichero y recuperarla
                            // posteriormente en la clase RSAAsymetricCrypto.java

                            // Texto a encriptar que lo rescatamos del textArea
                            String texto = textArea.getText();

                            // Se obtiene un cifrador AES
                            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

                            // Se inicializa para encriptacion y se encripta el texto,
                            // que debemos pasar como bytes.
                            aes.init(Cipher.ENCRYPT_MODE, key);
                            byte[] encriptado = aes.doFinal(texto.getBytes());

                            // Se escribe byte a byte en hexadecimal el texto
                            // encriptado para ver su pinta.
                             //guardamos el fichero codificado
                            FileOutputStream os = new FileOutputStream("C:\\Users\\elaza\\Desktop\\fichero_cifrado.txt");
                            os.write(encriptado); 
                            
                            //mostramos por consola el encriptado
                            for (byte b : encriptado) {
                                System.out.print(Integer.toHexString(0xFF & b));
                            
                                
                            }
                            System.out.println();

                            
                            
                            // Se iniciliza el cifrador para desencriptar, con la
                            // misma clave y se desencripta
                            aes.init(Cipher.DECRYPT_MODE, key);
                            byte[] desencriptado = aes.doFinal(encriptado);

                            // Texto obtenido, igual al original.
                            System.out.println("Texto obtenido" +new String(desencriptado));
                            
                            
                            
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchPaddingException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidKeyException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalBlockSizeException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (BadPaddingException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(EncriptacionInterfaz.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
        });
        
        
        
         
    }

}
