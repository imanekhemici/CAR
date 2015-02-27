package ftpTest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import ftp.FTPRequest;

public class FTPRequestTest {
	public static final String END_LINE ="\r\n";
	public static final String WELCOME = "220 Bonjour";
	public static final String ERREUR_IDENTIFICATION = "332: Utilisateur non reconnu";
	public static final String GOODBYE = "221 QUIT";
	public static final String USER_CON = "331: Utilisateur reconnu! en attente de mot de passe";
	public static final String ANONYMOUS ="230: Connecte au tant qu'un anonymous";
	public static final String LOGIN_OK ="230: connexion avec login reussie";
	public static final String LOGIN_KO= "332: echec de connexion ";
	@Test
	public void testConnection() throws IOException {
		String answer;
		Socket s = new Socket("localhost", 1024);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
		s.getInputStream()));
		answer = reader.readLine();
		assertEquals("connexion au serveur", answer, WELCOME);
				
	}
	
	@Test
	public void TestUser() throws UnknownHostException, IOException {
	String answer;
	Socket s = new Socket("localhost", 1024);
	BufferedReader reader = new BufferedReader(new InputStreamReader(
	s.getInputStream()));
	DataOutputStream writer = new DataOutputStream(s.getOutputStream());
	answer = reader.readLine();// le Bonjour
	
	
	// //////////////// USER ///////////////////////////////
	writer.writeBytes("USER toto" + END_LINE);
	answer = reader.readLine();
	assertEquals("user reconnu", answer, ERREUR_IDENTIFICATION);
	
	writer.writeBytes("USER anonymous" + END_LINE);
	answer = reader.readLine();
	assertEquals("test du user anonymous", answer, ANONYMOUS);
	
	writer.writeBytes("USER utilisateur" + END_LINE);
	answer = reader.readLine();
	assertEquals("test du user utilisateur", answer, USER_CON);
	}
	@Test
	public void TestMDP() throws UnknownHostException, IOException{
		String answer;
		Socket s = new Socket("localhost", 1024);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
		s.getInputStream()));
		DataOutputStream writer = new DataOutputStream(s.getOutputStream());
		answer = reader.readLine();// le Bonjour
		
		writer.writeBytes("USER utilisateur" + END_LINE);
		answer = reader.readLine();
		
		// ///////////////// PASS //////////////////////////////
		
		writer.writeBytes("PASS mpk" + END_LINE);
		answer = reader.readLine();
		assertEquals("test du pass utilisateur erroné", answer, LOGIN_KO);
		
		writer.writeBytes("PASS mdp" + END_LINE);
		answer = reader.readLine();
		assertEquals("test du pass utilisateur correcte", answer, LOGIN_OK);
		
		
	}
	
	@Test
	public void TestDeconnexion() throws UnknownHostException, IOException {
		String answer;
		Socket s = new Socket("localhost", 1024);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		DataOutputStream writer = new DataOutputStream(s.getOutputStream());
		answer = reader.readLine();// le welcome
		writer.writeBytes("QUIT" + END_LINE);
		answer = reader.readLine();
		assertEquals("test de la réponse de fin d'envoie de donné", answer,
				GOODBYE);
	}
}
