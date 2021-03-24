/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package J1;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author DELL
 */
public class SGraphViz extends JFrame {

    private JMenuBar fileMenuBar;
    private JMenu fileMenu;
    private JMenuItem newFile;
    private JMenuItem saveFile;
    private JMenuItem openFile;
    private JTextArea txtDes;
    private JButton btnViz;
    private JPanel pnlGraph;
    private JButton btnSaveGraph;
    private JButton btnClose;
    private JPanel pnlBody;
    private JPanel pnlRightForGraphFunction;
    private JPanel pnlBottomOfRight;
    private DrawGraph canvas = new DrawGraph();
    private JScrollPane scrGraphView;
    public Graph graph;
    boolean savedImage = false;
    boolean savedDocument = false;
    
    public SGraphViz() {
        initComponents();
    }

    public void initComponents() {
        initializeComponents();
        fileMenuBar.add(fileMenu);
        fileMenu.add(newFile);
        fileMenu.add(saveFile);
        fileMenu.add(openFile);
        this.add(fileMenuBar, BorderLayout.NORTH);
        this.add(pnlBody);
        pnlBody.add(txtDes);
        pnlBody.add(btnViz);
        pnlBody.add(pnlRightForGraphFunction);
        pnlRightForGraphFunction.add(pnlGraph);
        pnlRightForGraphFunction.add(pnlBottomOfRight);
        pnlBottomOfRight.add(btnSaveGraph, BorderLayout.WEST);
        pnlBottomOfRight.add(btnClose, BorderLayout.EAST);
    }

    public void initializeComponents() {

        fileMenuBar = new JMenuBar();
        fileMenu = new JMenu("FILE");
        newFile = new JMenuItem("New");
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFile(e);
            }
        });
        saveFile = new JMenuItem("Save");
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(e);
            }
        });
        openFile = new JMenuItem("Open");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile(e);
            }
        });
        pnlBody = new JPanel(new FlowLayout());
        txtDes = new JTextArea("TrafficLight{\n"
                + "	//vertices\n"
                + "	R [label=\"RED\",color=\"red\"]\n"
                + "	G [label=\"GREEN\",color=\"green\"]\n"
                + "	Y [label=\"YELLOW\",color=\"yellow\"]\n"
                + "	\n"
                + "	//edges\n"
                + "	R->G [label=\"45\"]\n"
                + "	G->Y [label=\"65\"]\n"
                + "	Y->R [label=\"5\"]\n"
                + "}");
        txtDes.setPreferredSize(new Dimension(300, 535));
        btnViz = new JButton("Visualize");
        btnViz.setPreferredSize(new Dimension(100, 30));
        btnViz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualization(evt);
            }
        });
        pnlRightForGraphFunction = new JPanel(new FlowLayout());
        pnlRightForGraphFunction.setPreferredSize(new Dimension(450, 535));
        pnlGraph = new JPanel();
        //pnlGraph.setBorder(new TitledBorder(new CompoundBorder(new EtchedBorder(2), new EtchedBorder(1)), "", 1, 1, new Font("Arial", 1, 12), Color.BLACK));
        
        pnlGraph.setPreferredSize(new Dimension(450, 470));
        pnlBottomOfRight = new JPanel(new BorderLayout());
        pnlBottomOfRight.setPreferredSize(new Dimension(450, 40));
        btnSaveGraph = new JButton("Save graph as Image");
        btnSaveGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGraphAsImage();
            }
        });
        btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCloseDoClick(e);
            }
        });

        
        //scrGraphView = new JScrollPane();
        //scrGraphView.setPreferredSize(new Dimension(450, 470));
        pnlGraph.setLayout(new BorderLayout());
        pnlGraph.add(canvas);
        
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                windowStateChange(e);
            }
        });
    }
    
    private void windowStateChange(WindowEvent e){
        
    }

    private boolean checkGraphHaveReasonableDescription() {
        if (graph.getVertices() == null || graph.getEdges() == null || graph.getNameGraph().isEmpty()) {
            return false;
        }
        return true;
    }

    private void visualization(ActionEvent evt) {
        graph = new Graph(txtDes.getText());
        if (checkGraphHaveReasonableDescription()) {
            canvas.setGraph(graph);
            //pnlGraph.setBorder(new TitledBorder(new CompoundBorder(new EtchedBorder(2), new EtchedBorder(1)), graph.getNameGraph(), 1, 2, new Font("Arial", 1, 12), Color.BLACK));
            System.out.println(graph.getNameGraph());
            
            for (Vertex vertex : graph.getVertices()) {
                System.out.println(vertex.toString());
            }
        }

        for (Edge edge : graph.getEdges()) {
            System.out.println(edge.toString());
        }

        for (Map.Entry<String, String> entry : graph.getAdjMatrix().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        for (Map.Entry<String, String> entry : graph.getAdjUndirectedMatrix().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        for (Vertex vertexe : graph.getVertices()) {
            System.out.println("x = " + vertexe.getCoordinate().getX() + " - y = " + vertexe.getCoordinate().getY());
        }

        canvas.repaint();
    }

    private void newFile(ActionEvent evt) {
        if (savedDocument) {
            txtDes.setText("");
            txtDes.requestFocus();
            savedDocument = false;
        } else {
            if (!txtDes.getText().isEmpty()) {
                int conf = JOptionPane.showConfirmDialog(null, "Do you wanna save current description before create a new file?", "Save before new file?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    saveFile(evt);
                    txtDes.setText("");
                    txtDes.requestFocus();
                } else if (conf == JOptionPane.NO_OPTION) {
                    txtDes.setText("");
                    txtDes.requestFocus();
                }
            }
        }
    }

    private void saveFile(ActionEvent evt) {
        savedDocument = true;
        OutputStream outputStream = null;
        JFileChooser jfile = new JFileChooser();
        jfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = jfile.showDialog(null, "Save here!");
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File dir = jfile.getSelectedFile();
                String fileName = "";
                do {
                    fileName = JOptionPane.showInputDialog("Name of file: ");
                    if (fileName.contains(".")) {
                        JOptionPane.showMessageDialog(null, "Name of file must be not contain \".\" !");
                    }
                } while (fileName.contains("."));
                fileName = "/" + fileName + ".txt";
                File file = new File(dir.getAbsolutePath() + fileName);
                outputStream = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                try {
                    String contentWrite = txtDes.getText();
                    osw.write(contentWrite, 0, contentWrite.length());
                    osw.flush();
                } catch (IOException ex) {
                    System.out.println("EEROR");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } finally {
                try {
                    outputStream.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }
    }

    private void openFile(ActionEvent evt) {
        savedDocument = true;
        JFileChooser jfile = new JFileChooser();
        jfile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File file = new File("");
        String extendPart = "";
        String content = "";
        int option = jfile.showOpenDialog(this);
        do {
            if (option == JFileChooser.APPROVE_OPTION) {
                file = jfile.getSelectedFile();
                extendPart = getExtendPart(file.getPath());
                if (!extendPart.equals("txt")) {
                    JOptionPane.showMessageDialog(null, "Only open file with the extend part: .txt");
                } else {
                    System.out.println("true");
                    FileReader fr = null;
                    BufferedReader bf = null;
                    try {
                        try {
                            fr = new FileReader(file);
                        } catch (FileNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        bf = new BufferedReader(fr);
                        String line = null;
                        while ((line = bf.readLine()) != null) {
                            content += line + "\n";
                            System.out.println(line);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    } finally {
                        try {
                            if (fr != null && bf != null) {
                                bf.close();
                                fr.close();
                            }
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                        }
                    }
                }
            }
        } while (!extendPart.equals("txt"));
        txtDes.setText(content);
    }

    private String getExtendPart(String path) {
        String extendPart = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '.') {
                for (i++; i < path.length(); i++) {
                    extendPart += path.charAt(i);
                }
                break;
            }
        }
        return extendPart;
    }
    
    public void saveGraphAsImage() {
        BufferedImage image = new BufferedImage(430, 470, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        System.out.println("graph is null: "+canvas.graph == null);
        //g2.setBackground(Color.WHITE);
        //g2.clearRect(0, 0, 440, 470);
        canvas.paint(g2);
        canvas.analyzeDrawing(g2);
        JFileChooser jfile = new JFileChooser();
        jfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = jfile.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            String nameImage = JOptionPane.showInputDialog("Name of image: ");
            String dirPath = jfile.getSelectedFile().getPath();
            try {
                try {
                    ImageIO.write(image, "png", new File(dirPath + "\\" + nameImage + ".png"));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        
    }

    public void btnCloseDoClick(ActionEvent evt) {
        if (savedDocument) {
            JOptionPane.showMessageDialog(null, "Bye! See ya!");
        } else {
            int conf = JOptionPane.showConfirmDialog(null, "Do you wanna save description before quit!", "Save before quit?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                saveFile(evt);
                btnCloseDoClick(evt);
            } else if (conf == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "Bye! See ya!");
            }
        }
        this.dispose();
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

        } catch (Exception e) {

        }
        SGraphViz main = new SGraphViz();
        main.setTitle("Simple Graph Visualization Software!");
        main.setSize(900, 600);
        main.setLocationRelativeTo(null);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);
        main.setResizable(false);
        main.setVisible(true);
    }
}
