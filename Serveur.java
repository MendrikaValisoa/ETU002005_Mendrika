import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serveur{
    static ArrayList<MonFichier> fichier = new ArrayList<>();
    
    public static void main (String[] args) throws IOException {
    
        int fileId = 0;

        JFrame f = new JFrame("Serveur");
        f.setSize( 450, 450);
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(pan);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel titre = new JLabel("Les fichiers recus");
        titre.setFont(new Font("Arial", Font.BOLD, 25));
        titre.setBorder(new EmptyBorder(20, 0, 10, 0));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        f.add(titre);
        f.add(scroll);
        f.setVisible(true);

        ServerSocket serverSocket = new ServerSocket( 1234);

        while (true) {

            try{

                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int fileNameLength = dataInputStream.readInt();


                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes,0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = dataInputStream.readInt();

                    if (fileContentLength >0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                        JPanel row = new JPanel();
                        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

                        JLabel nom_fichier = new JLabel(fileName);
                        nom_fichier.setFont(new Font ("Arial", Font.BOLD, 20));
                        nom_fichier.setBorder(new EmptyBorder(10, 0, 10, 0));
                        nom_fichier.setAlignmentX(Component.CENTER_ALIGNMENT);

                        if(getFileExtension(fileName).equalsIgnoreCase("PNG")) {
                            row.setName(String.valueOf(fileId));
                            row.addMouseListener(getMyMouseListener());
                            // row.addMouseListener(m);

                            row.add(nom_fichier);
                            pan.add(row); 
                            f.validate(); 
                        }else{
                            row.setName(String.valueOf(fileId));
                            row.addMouseListener(getMyMouseListener());
                            // row.addMouseListener(m);

                            row.add(nom_fichier);
                            pan.add(row);

                            f.validate();
                        }

                         fichier.add(new MonFichier(fileId, fileName,fileContentBytes, getFileExtension(fileName)));
                        
                         fileId ++;
                    }
                }

            }catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    public static MouseListener getMyMouseListener() {

        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e){

                 JPanel pan = (JPanel) e.getSource();

                 int fileId= Integer.parseInt(pan.getName());

                 for(MonFichier myFile: fichier) {
                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        jfPreview.setVisible(true);
                    }
                 }
            }
             
            @Override
            public void mousePressed(MouseEvent e){}

            @Override
            public void mouseReleased(MouseEvent e){}
            
            @Override
            public void mouseEntered(MouseEvent e){}

            @Override
            public void mouseExited(MouseEvent e){}
        }; 

    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {

        JFrame f = new JFrame("Téléchargements");
        f.setSize(450, 450);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("Fichier telecharger");
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("Arial", Font.BOLD, 25));
        titre.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel jlPrompt = new JLabel("Vous voulez vraiment telecharger"  + fileName );
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jlPrompt.setBorder(new EmptyBorder( 20, 0, 10,0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton oui = new JButton("Oui");
        oui.setPreferredSize(new Dimension(150, 75));
        oui.setFont(new Font( "Arial", Font.BOLD, 20));

        JButton non = new JButton("Non");
        non.setPreferredSize(new Dimension(150, 75));
        non.setFont(new Font( "Arial", Font.BOLD, 20));

        JLabel fichier = new JLabel();
        fichier.setAlignmentX((Component.CENTER_ALIGNMENT));

        JPanel boutton = new JPanel();
        boutton.setBorder (new EmptyBorder( 20, 0, 10,0));
        boutton.add(oui);
        boutton.add(non);

        if(fileExtension.equalsIgnoreCase("PNG")) {
            fichier.setText("<html>" + "</html>");
        }else{
            fichier.setIcon(new ImageIcon(fileData));
        }
        // oui.addActionListener(new Action_oui());
        oui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownload = new File(fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);

                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    f.dispose();
                } catch(IOException error) {
                    error.printStackTrace();
                }
            }
        });

        // non.addActionListener(new Action_non());
        non.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });

        pan.add(titre);
        pan.add(jlPrompt);
        pan.add(fichier);
        pan.add(boutton);

        f.add(pan);

        return f;

    }

    public static String getFileExtension(String fileName){

        //would not work with .tar.gz 
        int i = fileName.lastIndexOf('.');

        if (i > 0) {
            return  fileName.substring(i + 1);
        } else {
            return "Aucun extension trouve.";
        }
    }
}