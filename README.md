# Personal Finance Manager
A Personal Finance Manager (PFM) application that helps users track their expenses, incomes, and investments. The application should provide insights into spending habits, offer budgeting tools, and generate financial reports.

## Creation Of Assimetric keys for security


1.  Create a certs folder in the resources directory and navigate to it: <br>
   `cd src/main/resources/certs`

2.  Generates an RSA private key with a length of 2048 bits using OpenSSL (openssl genrsa). It then specifies the output file (-out keypair.pem) where the generated private key will be saved. The significance lies in creating a private key that can be used for encryption, decryption, and digital signatures in asymmetric cryptography. <br>
  `openssl genrsa -out keypair.pem 2048`
  
