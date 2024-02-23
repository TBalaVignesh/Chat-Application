import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame implements Runnable,ActionListener {
    JButton button;
    JTextField textField;
    JTextArea textArea;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread thread;
    Server(){

        textArea = new JTextArea();
        textArea.setBounds(100,100,300,300);
        textField = new JTextField();
        textField.setBounds(100,450,100,25);
        button = new JButton("Send");
        button.setBounds(250,450,100,25);
        button.addActionListener(this);
        add(textArea);
        add(textField);
        add(button);
        try {
            serverSocket = new ServerSocket(999);
            socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream((socket.getOutputStream()));
        }
        catch (Exception E){
            System.out.println(E.getMessage());
        }
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                System.exit(0);
            }
        });
        setTitle("Chat");
        setSize(600,600);
        setLayout(null);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        String message = textField.getText();
        textArea.append("User 2 "+message+"\n");
        textField.setText("");
        try{
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        }catch (Exception E){
            System.out.println(E.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server();
    }
    public void run(){
        while (true){
            try{
            String meg = dataInputStream.readUTF();
            textArea.append("User 1: "+meg+"\n");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
