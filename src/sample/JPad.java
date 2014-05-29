package sample;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class JPad extends JFrame {

    private static final String DEFAULT_TITLE = "Untitled - JPad";
    private static final String SAVE_CHANGES = "Save changes?";
    private static final String TITLE_AYS = "Are you sure?";
    private static final String DEFAULT_STATUS = "JPad 1.0";
    private final JFileChooser fc;
    private JLabel lbl;
    private JTextArea ta;
    private boolean fDirty;

    public JPad(String[] args) {

        fc = new JFileChooser(".");
        FileFilter ff = new FileNameExtensionFilter("TXT documents (*.txt)", "txt");
        fc.setFileFilter(ff);

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
        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doOpen();
            }
        };
        miOpen.addActionListener(al);
        mFile.add(miOpen);

        JMenuItem miSave = new JMenuItem("Save");
        miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        mFile.add(miSave);

        JMenuItem miSaveAs = new JMenuItem("Save As...");
        mFile.add(miSaveAs);
        mFile.addSeparator();
        JMenuItem miExit = new JMenuItem("Exit");
        mFile.add(miExit);

        MenuListener ml;
        ml = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                lbl.setText("New doc|Open existing doc|Save changes|Exit");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                lbl.setText(DEFAULT_STATUS);
            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        };
        mFile.addMenuListener(ml);

        mb.add(mFile);
        JMenu mEdit = new JMenu("Edit");

        final JMenuItem miCut = new JMenuItem("Cut");
        miCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        miCut.setEnabled(false);

        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.cut();
            }
        };
        miCut.addActionListener(al);
        mEdit.add(miCut);

        final JMenuItem miCopy = new JMenuItem("Copy");
        miCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        miCopy.setEnabled(false);

        al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.copy();
            }
        };
        miCopy.addActionListener(al);
        mEdit.add(miCopy);
        mb.add(mEdit);

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

        CaretListener cl;
        cl = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (ta.getSelectedText() != null) {
                    miCopy.setEnabled(true);
                    miCut.setEnabled(true);
                } else {
                    miCopy.setEnabled(false);
                    miCut.setEnabled(false);
                }
            }
        };
        ta.addCaretListener(cl);

        getContentPane().add(lbl = new JLabel(DEFAULT_STATUS), BorderLayout.SOUTH);
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
            if (fc.showOpenDialog(JPad.this) == JFileChooser.APPROVE_OPTION)
                file = fc.getSelectedFile();
        }

        if (file == null) return;

        fc.setCurrentDirectory(file.getParentFile());
    }

    private void doExit() {
        if (fDirty) {
            switch (JOptionPane.showConfirmDialog(JPad.this, SAVE_CHANGES, TITLE_AYS, JOptionPane.YES_NO_OPTION)) {
                case JOptionPane.YES_OPTION: if (doSave()) dispose(); break;
                case JOptionPane.NO_OPTION: dispose();
            }
        } else {
            dispose();
        }
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
