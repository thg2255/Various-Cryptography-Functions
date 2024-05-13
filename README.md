# Various-Cryptography-Functions
Demonstration of various security principles in Java

## Classes

### Hash 
Takes in a string and hashes it in either MD5 or SHA-256

### HashTester
Takes in a string an hashes it depending on the mode chosen

### Protection
*makeBytes* - takes in a long integer and a double, then converts them into a single byte array

*makeDigest* - takes in a byte array, a timestamp, and a random number, then generates a digest using SHA, the second version  takes in a user name, a password, a timestamp, and  a random number, then generates a digest using SHA

### ProtectedClient
Takes in user name, password, and an output stream as the function inputs

### ProtectedServer
*main* - Creates a server process

*authenticate* - Takes in data from the in stream and authenticates the user trying to login is correct

### ElGamalAlice
Sender class to demonstrate ElGamal key generation

### ElGamalBob
Receiver class to demonstrate ElGamal

### CipherClient
Generates a DES key, stores it in a file, encrypts a string with the key and sends it out

### CipherServer
Takes in key generated in client class and uses it to decrypt a message

### RSAClient
Sends a message out and communicates with server with RSA and AES keys

### RSAServer
Takes in string message and decrypts it with RSA key

### X509Client
Uses server_certificate.cer to authenticate with X509 server

### X509Server
Uses a keystore to provide secure authentication with X509 client
