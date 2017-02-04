package com.Brandon;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by brandon on 2/4/2017.
 */
public class ChatClient extends JFrame implements Runnable {
    Socket socket;
    JTextArea textA;
    JButton Msend, logout;
    JTextField textF;

    Thread thread;

    DataInputStream din;
    DataOutputStream dout;

    String LoginName;

    ChatClient(String login) throws UnknownHostException, IOException{
        super(login);
        LoginName = login;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    dout.writeUTF(LoginName +  " " + "LOGOUT");
                    System.exit(1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        textA = new JTextArea(15, 50);
        textF = new JTextField(50);

        textF.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    try {
                        if (textF.getText().length()>0)
                        dout.writeUTF(LoginName + " " + "DATA " + textF.getText().toString());
                        textF.setText("");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        Msend = new JButton("Send");
        logout = new JButton("Logout");

        Msend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textF.getText().length()>0)
                    dout.writeUTF(LoginName + " " + "DATA " + textF.getText().toString());
                    textF.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dout.writeUTF(LoginName + " " + "LOGOUT");
                    System.exit(1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        socket = new Socket("localhost", 5218);

        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        dout.writeUTF(LoginName);
        dout.writeUTF(LoginName + " " + "LOGIN");

        thread = new Thread(this);
        thread.start();
        setup();

    }

    private void setup(){
        setSize(600, 400);

        JPanel panel = new JPanel();

        panel.add(new JScrollPane(textA));
        panel.add(textF);
        panel.add(Msend);
        panel.add(logout);

        add(panel);

        setVisible(true);

    }
    @Override
    public void run(){
        while (true){
            try {
                textA.append("\n" + din.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
