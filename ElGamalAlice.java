import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;
import java.util.Random;

public class ElGamalAlice
{
	// IMPLEMENT THIS FUNCTION;
	// Public key
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		// Really hope this is the right algorithm (I could not find the right textbook, otherwise the textbooks in 
		// the syllabus didn't have the algorithms really)
		return g.modPow(d, p);
	}

	// IMPLEMENT THIS FUNCTION;
	private static BigInteger computeK(BigInteger p)
	{
		// To lazy to use a random big integer too, my code is already overly complicated
		Random rand = new Random();
		
		// .One is a constant in the big integer class to represent 1, could've written 1 but this looks
		// fancier
		BigInteger kMin = BigInteger.ONE;
        BigInteger kMax = p.subtract(BigInteger.ONE);
        
        BigInteger k = new BigInteger(kMax.bitLength(), rand);
        while (k.compareTo(kMin) < 0 || k.compareTo(kMax) > 0) {
            k = new BigInteger(kMax.bitLength(), rand);
        }
        
        return k;
	}
	
	// Method to clarify if it's relatively prime
	public static BigInteger gcd(BigInteger a, BigInteger b) {
        return b.equals(BigInteger.ZERO) ? a : gcd(b, a.mod(b));
    }
	
	// IMPLEMENT THIS FUNCTION;
	// Signature
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		return g.modPow(k, p);
	}

	// IMPLEMENT THIS FUNCTION;
	// Signature
	private static BigInteger computeB(String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		// Turns the message into a big integer
		StringBuilder num_message = new StringBuilder();
        for (char c : message.toCharArray()) {
        	num_message.append((int) c);
        }
        BigInteger converted_message = new BigInteger(num_message.toString());
        
        // Compute (M - d * a)
        BigInteger message_minus = converted_message.subtract(d.multiply(a));
        
        // Compute the modular inverse of k
        BigInteger k_inverse = k.modInverse(p.subtract(BigInteger.ONE));
        
        // Calculate B
        BigInteger b = message_minus.multiply(k_inverse).mod(p.subtract(BigInteger.ONE));
        
        return b;
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}