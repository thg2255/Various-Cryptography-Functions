import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient
{
	// IMPLEMENT THIS FUNCTION;
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// Doubling the password is "technically" double strength
		// The ":" will help with protected server later		
		long t = new Date().getTime();
		double q = Math.random();
		
		String authenticationData = user + ":" + password + password + ":" + t + ":" + q;
		
		// Calculating SHA1
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] byte_md = md.digest(authenticationData.getBytes());
		
		// Writing it so they can correspond
		out.writeUTF(user);
		out.writeLong(t);
		out.writeDouble(q);
		out.write(byte_md);
		
		// Ensure that exactly 32 bytes are sent for the MD digest
	    byte[] padding = new byte[32 - byte_md.length];
	    out.write(padding);
	    
		out.flush();
	}

	// Starting point for this
	public static void main(String[] args) throws Exception 
	{
		String host = "localhost";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}