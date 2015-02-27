package ftp;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ftpServer {

	
	public static final int Port=1024 ;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(Port);
			while(true){
				Socket s = ss.accept();
				FTPRequest fr = new FTPRequest(s);
				Thread th = new Thread(fr);
				th.start();			
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
}
