// Tests if Message Digest works
public class HashTester extends Hash {
	
	public static void main(String[] args) {
		
		String password = "123456";
		Hash hashed_password = new Hash();
		System.out.print(hashed_password.hash(password, "SHA-256"));
	}
}
