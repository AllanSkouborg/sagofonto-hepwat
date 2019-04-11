package dk.artogis.hepwat.common.utility;


import java.io.Serializable;

public class Encryption implements Serializable
    {
        //C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private byte[] _key = { 113, 227, 195, 121, 23, 27, 95, 45, 164, 124, 26, 122, 67, 12, 22, 219, 251, 124, 15, 14, 179, 153, 206, 209, 214, 76, 67, 18, 231, 236, 3, 219 };
        private byte[] _key = {113, (byte)227, (byte)195, 121, 23, 27, 95, 45, (byte)164, 124, 26, 122, 67, 12, 22, (byte)219, (byte)251, 124, 15, 14, (byte)179, (byte)153, (byte)206, (byte)209, (byte)214, 76, 67, 18, (byte)231, (byte)236, 3, (byte)219};
        //C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private byte[] _vector = { 246, 164, 171, 11, 25, 43, 113, 119, 231, 121, 21, 172, 7, 132, 124, 56 };
        private byte[] _vector = {(byte)246, (byte)164, (byte)171, 11, 25, 43, 113, 119, (byte)231, 121, 21, (byte)172, 7, (byte)132, 124, 56};
        private String _cryptPrefix = "aes:";

        private String Username;
        public final String getUsername()
        {
            return Username;
        }
        public final void setUsername(String value)
        {
            Username = value;
        }
        private String Password;
        public final String getPassword()
        {
            return Password;
        }
        public final void setPassword(String value)
        {
            Password = value;
        }

        //C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlIgnore] public bool PasswordEncrypted
        public final boolean PasswordEncrypted()
        {
            return ( Password != null && !Password.isEmpty() && Password.startsWith(_cryptPrefix));
        }

        public Encryption(String username, String password)
        {
            setUsername(username);
            setPassword(password);
        }

        /**
         Encrypts password

         @param clearTextPassword Password in clear text
         @return Encrypted password
         */
        public final String EncryptPassword(String clearTextPassword)
        {
            if (clearTextPassword.startsWith(_cryptPrefix)) //already encrypted
            {
                return clearTextPassword;
            }
            else
            {
                return _cryptPrefix + Encrypt(clearTextPassword);
            }
        }

        private String Encrypt(String clearTextPassword)
        {
//            RijndaelManaged rm = new RijndaelManaged();
//            ICryptoTransform EncryptorTransform = rm.CreateEncryptor(this._key, this._vector);
//            System.Text.UTF8Encoding UTFEncoder = new System.Text.UTF8Encoding();
//
////C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
////ORIGINAL LINE: Byte[] bytes = UTFEncoder.GetBytes(clearTextPassword);
//            byte[] bytes = UTFEncoder.GetBytes(clearTextPassword);
//
//            java.io.ByteArrayOutputStream memoryStream = new java.io.ByteArrayOutputStream();
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#region Write the decrypted value to the encryption stream
//            CryptoStream cs = new CryptoStream(memoryStream, EncryptorTransform, CryptoStreamMode.Write);
//            cs.Write(bytes, 0, bytes.length);
//            cs.FlushFinalBlock();
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#endregion
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#region Read encrypted value back out of the stream
//            memoryStream.Position = 0;
////C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
////ORIGINAL LINE: byte[] encrypted = new byte[memoryStream.Length];
//            byte[] encrypted = new byte[memoryStream.getLength()];
//            memoryStream.Read(encrypted, 0, encrypted.length);
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#endregion
//
//            //Clean up.
//            cs.Close();
//            memoryStream.close();
//
//            return ByteArrToString(encrypted);
            return  null;
        }
        /**
         Decrypts password

         @param encryptedPassword Encrypted password
         @return Decrypted password
         */
        public final String DecryptPassword(String encryptedPassword)
        {
//            if (encryptedPassword.startsWith(_cryptPrefix))
//            {
//                encryptedPassword = encryptedPassword.substring(_cryptPrefix.length());
//            }
//
//            RijndaelManaged rm = new RijndaelManaged();
//            ICryptoTransform DecryptorTransform = rm.CreateDecryptor(this._key, this._vector);
//            System.Text.UTF8Encoding UTFEncoder = new System.Text.UTF8Encoding();
//
////C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
////ORIGINAL LINE: byte[] EncryptedValue = StrToByteArray(encryptedPassword);
//            byte[] EncryptedValue = StrToByteArray(encryptedPassword);
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#region Write the encrypted value to the decryption stream
//            java.io.ByteArrayOutputStream encryptedStream = new java.io.ByteArrayOutputStream();
//            CryptoStream decryptStream = new CryptoStream(encryptedStream, DecryptorTransform, CryptoStreamMode.Write);
//            decryptStream.Write(EncryptedValue, 0, EncryptedValue.length);
//            decryptStream.FlushFinalBlock();
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#endregion
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#region Read the decrypted value from the stream.
//            encryptedStream.Position = 0;
////C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
////ORIGINAL LINE: Byte[] decryptedBytes = new Byte[encryptedStream.Length];
//            byte[] decryptedBytes = new byte[encryptedStream.getLength()];
//            encryptedStream.Read(decryptedBytes, 0, decryptedBytes.length);
//            encryptedStream.close();
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//            ///#endregion
//
//            return UTFEncoder.GetString(decryptedBytes);
            return  null;
        }
    }



