package com.company.Writing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.DosFileAttributes;

import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_S;
//import static java.lang.ClassLoader.getClassLoader;

public class Main {
    //TODO system file for saving
    //TODO password lock for the app
    //TODO - no option to change the password

    public static void main(String[] args) {
        try {
            File folder = new File("/source");
            folder.mkdir();
            if (!Files.exists(Paths.get("/source/Save_16x16.png"))) {
                Files.copy(Paths.get("Resources/TextDocument_256x256.png"), Paths.get("/source/TextDocument_256x256.png"), StandardCopyOption.COPY_ATTRIBUTES);
                Files.copy(Paths.get("Resources/Settings_16x16.png"), Paths.get("/source/Settings_16x16.png"), StandardCopyOption.COPY_ATTRIBUTES);
                Files.copy(Paths.get("Resources/Save_16x16.png"), Paths.get("/source/Save_16x16.png"), StandardCopyOption.COPY_ATTRIBUTES);
                Files.copy(Paths.get("Resources/Open_16x16.png"), Paths.get("/source/Open_16x16.png"), StandardCopyOption.COPY_ATTRIBUTES);
            }
        }catch (IOException e){
            // nothing
        }

        //Creating the frame
        JFrame frame = new JFrame("Thoughts");
        JPanel panel = new JPanel();

        //Creating the TextArea
        JTextArea textArea = new JTextArea();

        //Creating menu parts
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem SaveItem = new JMenuItem("Save");
        JMenuItem openItem = new JMenuItem("Open");

        //creating Menu
        menuBar.add(menu);
        menu.add(SaveItem);
        menu.add(openItem);
        SaveItem.addActionListener(actionListener(textArea));
        openItem.addActionListener(openActionListener(textArea));

        //setting icons for the menu
        openItem.setIcon(new ImageIcon("/source/Open_16x16.png"));
        SaveItem.setIcon(new ImageIcon("/source/Save_16x16.png"));
        menu.setIcon(new ImageIcon("/source/Settings_16x16.png"));

        //TextArea settings
        textArea.setLineWrap(true);
        textArea.addKeyListener(saveKey(textArea));

        //ScrollPane settings
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(325, 300));

        //panel settings
        panel.setLayout(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(400, 500));

        //adding labels to the panel
        panel.add(new JLabel(""), BorderLayout.LINE_START);
        panel.add(new JLabel(""), BorderLayout.LINE_END);
        panel.add(new JLabel(""), BorderLayout.PAGE_END);
        panel.add(new JLabel(""), BorderLayout.PAGE_START);

        //adding the content to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

        //frame settings
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(panel);
        frame.pack();

        frame.setIconImage(new ImageIcon("/source/TextDocument_256x256.png").getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    //The method that writes the text to the file
    public static void writeToFile(JTextArea textArea) {
        try {
            //creating the file instance

            File saveFile = new File("/source/savedThoughts.txt");

            //if the file doesn't exist create it
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            setHiddenAttrib(saveFile);

            //creating the writer and appending so it won't override
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/source/savedThoughts.txt", true));
            bufferedWriter.append(textArea.getText()).append("\n");
            textArea.setText("saved");
            bufferedWriter.close();


        } catch (IOException e) {
            //if something breaks, write it in the textarea
            textArea.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String readFromFile(JTextArea textArea) {

        StringBuilder reading = new StringBuilder();
        try {
            //Reading from the file
            FileReader fileReader = new FileReader("/source/savedThoughts.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i;
            while ((i = bufferedReader.read()) != -1) {
                reading.append((char) i).append(bufferedReader.readLine()).append("\n");

            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            textArea.setText(e.getMessage());
        }

        return reading.toString();
    }

    public static ActionListener actionListener(JTextArea textArea) {
        ActionListener actionListener = e -> writeToFile(textArea);
        return actionListener;
    }

    public static ActionListener openActionListener(JTextArea textArea) {
        ActionListener actionListener = e -> textArea.setText(readFromFile(textArea));
        return actionListener;
    }

    public static KeyListener saveKey(JTextArea textArea) {
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == VK_O) {
                    textArea.setText(readFromFile(textArea));
                }
                if (e.isControlDown() && e.getKeyCode() == VK_S) {
                    writeToFile(textArea);
                    System.out.println(e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        return keyListener;
    }

    private static void setHiddenAttrib(File filePaths) {
        Path filePath = filePaths.toPath();
        try {
            DosFileAttributes attr = Files.readAttributes(filePath, DosFileAttributes.class);
            System.out.println(filePath.getFileName() + " Hidden attribute is " + attr.isHidden());
            Files.setAttribute(filePath, "dos:hidden", true);
            attr = Files.readAttributes(filePath, DosFileAttributes.class);
            System.out.println(filePath.getFileName() + " Hidden attribute is " + attr.isHidden());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

