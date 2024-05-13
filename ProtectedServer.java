import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	// IMPLEMENT THIS FUNCTION;
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);
		String user = in.readUTF();
        long t = in.readLong();
        double q = in.readDouble();
        // Clients MD
        byte[] byte_md = new byte[32]; 
        
        in.readFully(byte_md);
		
		// Splits them to separate username and password
		// String[] splitData = authentication_data.split(":");
		
	    String password = lookupPassword(user);
	    
	    String all = user + ":" + password + password + ":" + t + ":" + q;
		
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] received_digest = md.digest(all.getBytes());
        
        boolean isAuthenticated = MessageDigest.isEqual(byte_md, received_digest);
		
        in.close();
        
		return isAuthenticated;
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}