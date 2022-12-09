import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client{

public static void main (String[] args) {

    final File[] fileToSend = new File[1];

    JFrame f = new JFrame ("Client" ) ;
    f.setSize (400, 400);
    f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel titre = new JLabel("Transferer un fichier");
    titre.setBorder(new EmptyBorder(20, 0, 10, 0 ));
    titre.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel fichier = new JLabel("Choisir un fichier a envoyer.");
    fichier.setBorder(new EmptyBorder( 50, 0, 0, 0));
    fichier.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel boutton = new JPanel();
    boutton.setBorder(new EmptyBorder(75, 0, 10, 0));

    JButton envoyer = new JButton("Envoyer un fichier");
    envoyer.setPreferredSize(new Dimension(150, 75));
    JButton choisir = new JButton("Choisir un fichier");
    choisir.setPreferredSize(new Dimension(150, 75));

    boutton.add(envoyer);
    boutton.add(choisir); 

    choisir.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser choix_de_fichier = new JFileChooser();
            choix_de_fichier.setDialogTitle("Choisit le fichier a envoyer.");

            if (choix_de_fichier.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                 fileToSend[0] = choix_de_fichier.getSelectedFile(); 
                 fichier.setText("Le fichier que vous voulez transferer :" + fileToSend[0].getName());
            }
        }
    });
 
     envoyer.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            if (fileToSend[0] == null){
                fichier.setText("Choisir le fichier en premier.");
            } else{
                try{
                    FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                    Socket socket = new Socket ("localhost", 5000);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    String fileName = fileToSend[0].getName();
                    byte[] fileNameBytes = fileName.getBytes();

                    byte[] fileContentBytes = new byte[(int)fileToSend[0].length()];
                    fileInputStream.read(fileContentBytes);

                    dataOutputStream.writeInt(fileNameBytes.length); 
                    dataOutputStream.write(fileNameBytes);

                    dataOutputStream.writeInt(fileContentBytes.length);
                    dataOutputStream.write(fileContentBytes);
                } catch (IOException error){
                    error.printStackTrace();
                }
            }
        }
    });

    f.add(titre);
    f.add(fichier);
    f.add(boutton);
    f.setVisible(true);
    }
}