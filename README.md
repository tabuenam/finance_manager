# Personal Finance Manager
A Personal Finance Manager (PFM) application that helps users track their expenses, incomes, and investments. The application should provide insights into spending habits, offer budgeting tools, and generate financial reports.
## Configure Mail service
1. Create a file named env.properties under resources<br>
   `cd src/main/resources/env.properties` <br>

2. Add the following properties and set them with your values. <br>
   `spring.mail.host=smtp.server.com` <br>
   `spring.mail.username=username` <br>
   `spring.mail.password=app-password`<br>

## Creation Of Assimetric keys.

1.  Create a certs folder in the resources directory and navigate to it: <br>
   `cd src/main/resources/certs` <br>

2.  Generate an RSA private key with a length of 2048 bits using OpenSSL (openssl genrsa). It then specifies the output file (-out keypair.pem) where the generated private key will be saved. The significance lies in creating a private key that can be used for encryption, decryption, and digital signatures in asymmetric cryptography. <br> 
  `openssl genrsa -out keypair.pem 2048`<br>

3.   Generates a Public Key from the Private Key, this command extracts the public key from the previously generated private key (openssl rsa). It reads the private key from the file specified by -in keypair.pem and outputs the corresponding public key (-pubout) to a file named publicKey.pem. The significance is in obtaining the public key from the private key, which can be shared openly for encryption and verification purposes while keeping the private key secure. <br>
   `openssl rsa -in keypair.pem -pubout -out publicKey.pem` <br>

4.   Format the Private Key (keypair.pem) in Supported Format (PKCS8 format), this line converts the private key generated in the first step (keypair.pem) into PKCS#8 format, a widely used standard for private key encoding (openssl pkcs8). It specifies that the input key format is PEM (-inform PEM), the output key format is also PEM (-outform PEM), and there is no encryption applied (-nocrypt). The resulting private key is saved in a file named private.pem. The significance is in converting the private key into a standard format that is interoperable across different cryptographic systems and applications. <br>
   `openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out privateKey.pem` <br>

  
