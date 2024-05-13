import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Hash {
	
	public String hash(String input, String hash_type) {
		
		// MessageDigest is a built in class for hashing
		java.security.MessageDigest hash_function;
		
		// What will be returned to the user
		String output = "Error";
		
		// MD5 Hashing
		if(hash_type == "MD5") {
			
			// Java required I do a try/ catch 
			try {
				hash_function = java.security.MessageDigest.getInstance("MD5");
				hash_function.update(input.getBytes());
				byte [] digest = hash_function.digest();
				output = DatatypeConverter.printHexBinary(digest).toUpperCase();
				
			} catch (NoSuchAlgorithmException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// SHA1 Hashing
		else if(hash_type == "SHA-256") {
			
			// Java required I do a try/ catch 
			try {
				hash_function = java.security.MessageDigest.getInstance("SHA-256");
				hash_function.update(input.getBytes());
				byte [] digest = hash_function.digest();
				output = DatatypeConverter.printHexBinary(digest).toUpperCase();
				
			} catch (NoSuchAlgorithmException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return output;
	}
}
