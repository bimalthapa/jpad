package sample;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class JPad extends JFrame {

    private static final String DEFAULT_TITLE = "Untitled - JPad";
    private static final String SAVE_CHANGES = "Save changes?";
    private static final String TITLE_AYS = "Are you sure?";
    private JLabel lbl;
    private JTextArea ta;
    private boolean fDirty;

    public JPad(String[] args) {

        JMenuBar mb = new JMenuBar();
        JMenu mFile = new JMenu("File");
        JMenuItem miNew = new JMenuItem("New");
        miNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        ActionListener al;
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fDirty) {
                    switch (JOptionPane.showConfirmDialog(JPad.this, SAVE_CHANGES, TITLE_AYS, JOptionPane.YES_NO_OPTION)) {
                        case JOptionPane.YES_OPTION: if (doSave()) doNew(); break;
                        case JOptionPane.NO_OPTION: doNew();
                    }
                } else {
                    doNew();
                }
            }
        };
        miNew.addActionListener(al);
        mFile.add(miNew);

        JMenuItem miOpen = new JMenuItem("Open");
        miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        mFile.add(miOpen);

        JMenuItem miSave = new JMenuItem("Save");
        miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        mFile.add(miSave);

        JMenuItem miSaveAs = new JMenuItem("Save As...");
        mFile.add(miSaveAs);
        mFile.addSeparator();
        JMenuItem miExit = new JMenuItem("Exit");
        mFile.add(miExit);

        mb.add(mFile);

        JMenu mFormat = new JMenu("Format");
        JCheckBoxMenuItem cbmiWordWrap = new JCheckBoxMenuItem("Word Wrap");
        mFormat.add(cbmiWordWrap);
        mb.add(mFormat);

        setJMenuBar(mb);

        getContentPane().add(new JScrollPane(ta = new JTextArea()));
        DocumentListener dl;
        dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fDirty = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fDirty = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        ta.getDocument().addDocumentListener(dl);

        getContentPane().add(lbl = new JLabel("JPad 1.0"), BorderLayout.SOUTH);
        setSize(400, 400);
        setTitle(DEFAULT_TITLE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doExit();
            }
        });
        setVisible(true);

        if (args.length != 0) {
            doOpen(new File(args[0]));
        }
    }

    private void doOpen() {
        doOpen(null);
    }

    private void doOpen(File file) {
        if (file == null) {
            return;
        }
    }

    private void doExit() {
        dispose();
    }

    private void doNew() {}

    private boolean doSave() {
        return true;
    }

    private void doSaveAs() {}

    private String read(File f) throws IOException{
        return "";
    }

    private void write(File f, String text) throws IOException {}

    public static void main(final String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                new JPad(args);
            }
        };

        EventQueue.invokeLater(r);
    }
}
