import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Mouse implements MouseListener  {
  Server s;
  static ArrayList<MonFichier> fichier = new ArrayList<>();

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub
     JPanel pan = (JPanel) e.getSource();

      int fileId= Integer.parseInt(pan.getName());

      for(MonFichier mon_fichier: fichier) {
         if (mon_fichier.getId() == fileId) {
              JFrame f = s.createFrame(mon_fichier.getName(), mon_fichier.getData(), mon_fichier.getFileExtension());
              f.setVisible(true);
         }
      }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }
  
}
