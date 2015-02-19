COMS 4180 Assignment 1
Dr. Debra Cook
Qingxiang Jia / qj2125

(1) Steps used to generate RSA keys.
	I wrote a class named RSAGen to genetate RSA key pairs (java.security). For convenience, as soon as they keys are generated, they are deserialized so that later other class can easily use them. The private key of client1 is named c1i.ser; its public key is named c1p.ser .

(2) How to run:
	There are two ways to run.
	(A) Using Bash scripts I wrote. In the root directory, there are three script files: s.sh, c1.sh and c2.sh. s.sh is used to test server.java (compile and run with preconfigured parameters); similarly c1.sh is for client1.java; c2.sh is for client2.java . To use them, chmod a+x <filename>. You can change the parameters in script file, and later all you need to do is in terminal type "./s.sh", "c1.sh", and "c2.sh". Notice that c1.sh needs to be launched last! There is no requirement on the order of running for s.sh and c2.sh.

	(B) The other way is just to manually compile and run the code. In this cast, you need to enter the parameters by yourself. Please make sure that the password is ENCLOSED in QUOTATION marks.

	server.java:
		Compile:
			javac server.java
		Run:
			java server <port on which server listens to client1> <port client2 listens> <client2 IP> <mode>
		Example:
			java server 8087 8088 127.0.0.1 u

	client1.java:
		Compile:
			javac client1.java
		Run:
			java client1 <password> <path to file to be sent> <server IP> <port number> <path to private key>
		Example:
			java client1 "!@#$%^&*()1A=p+-" liukanshan.jpg 127.0.0.1 8087 c1i.ser

	client2.java:
		Compile:
			javac client2.java
		Run:
			client2 <port number>
		Example:
			java client2 8088

(3) Files included:
	server.java
	client1.java
	client2.java

	Cargo.javac 		Bundle of encrypted password, ciphertext, and encrypted hash of the file. This object is the "cargo" that is been transmitted between client and server. It is deserialized prior to transmission.
	InputCheck.java		An interface that enforces input parameter check for clients and server.
	RSAGen.java			Generates RSA key pair.
	Net.java			Utility class that handles all network related operation.
	Util.java			Utility class than handles all IOs.

	c1i.ser	c1p.ser 	Private and public keys of client1.
	liukanshan.jpg		Sample file to be transmitted.

Thanks for grading.
	