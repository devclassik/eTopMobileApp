package com.gbikna.sample.etop.kimono;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptoHelper {
    String transformation = "";
    String desMode = "ECB";
    String desPadding = "NoPadding";

    public CryptoHelper() {

    }


    public static void main(String[] args) {

        CryptoHelper c = new CryptoHelper();
    }

    public String desEncrypt(String key, String clearComp) {
        String encrypted = "";

        try {
            byte[] clearKey = hex2byte (key);

            short keyLen = 1;
            Key clrKey = formDESKey(keyLen, clearKey);

            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, clrKey);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //*    LOG.debug("" + cipher.getOutputSize(3));
            byte[] clearText = hex2byte(clearComp);

            CipherOutputStream out = new CipherOutputStream(bytes, cipher);
            out.write(clearText);
            out.flush();
            out.close();
            byte[] ciphertext = bytes.toByteArray();
            bytes.flush();
            bytes.close();

            encrypted = byte2hex(ciphertext);
            //*      LOG.info("Enc 3: " + ToHexString(ciphertext));
            java.util.Arrays.fill(clearText, (byte) 0);
            java.util.Arrays.fill(ciphertext, (byte) 0);
        } catch (InvalidKeyException | IOException ex) {
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    public static String tripleDesEncrypt(String key, String data) {
        try {
            byte[] masterKey = Hex.decodeHex(key.toCharArray());
            byte[] desKey = new byte[24];
            System.arraycopy(masterKey, 0, desKey, 0, 16);
            System.arraycopy(masterKey, 0, desKey, 16, 8);

            DESedeKeySpec keySpec = new DESedeKeySpec(desKey);

            SecretKey secretKey = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);

            Cipher ecipher = Cipher.getInstance("DESede/ECB/NoPadding");
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedData = ecipher.doFinal(Hex.decodeHex(data.toCharArray()));

            return ISOUtil.byte2hex(encryptedData).toUpperCase();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getEncryptedValue (String strKey, String data) {
        String encryptedValue = "";

        try {
            byte[] clearKey = hex2byte (strKey);

            short keyLen = 2;
            Key clrKey = formDESKey (keyLen, clearKey);

            Cipher c1 = null;

            byte [] encryptedVal = doCryto(hex2byte(data), clrKey, c1.ENCRYPT_MODE);
            encryptedValue = byte2hex(encryptedVal);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return encryptedValue;
    }

    public String getDecryptedValue (String strKey, String data) {
        String decryptedValue = "";

//        Log.info(" ssk ??? = " + strKey);

        try {
            byte[] clearKey = hex2byte(strKey);

            short keyLen = 2;
            Key clrKey = formDESKey (keyLen, clearKey);

            Cipher c1 = null;

            byte [] decryptedVal = doCryto(hex2byte(data), clrKey, c1.DECRYPT_MODE);
            decryptedValue = byte2hex(decryptedVal);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return decryptedValue;
    }

    public byte[] getNewKey(short keyLength)throws NoSuchAlgorithmException {
        Key clearKey = null;
        byte[] clearKeyBytes = null;
        try {
            KeyGenerator k1 = null;
            if (keyLength > 1) {
                k1 = KeyGenerator.getInstance("DESede");
            }
            else {
                k1 = KeyGenerator.getInstance("DES");
            }

            clearKey = k1.generateKey();
            //byte[]
            clearKeyBytes = getDesKeyPart(keyLength, clearKey);
            //adjustDESParity(clearKeyBytes);
            //clearKey = formDESKey(keyLength, clearKeyBytes);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return clearKeyBytes;
    }

    public static byte[] trim (byte[] array, int length) {
        byte[] trimmedArray = new byte[length];
        System.arraycopy(array, 0, trimmedArray, 0, length);
        return  trimmedArray;
    }

    private byte[] getDesKeyPart(short keyLength,Key clearDESKey) throws Exception {
        byte[] clearKeyBytes = clearDESKey.getEncoded();
        clearKeyBytes = trim(clearKeyBytes, getBytesLength(keyLength));
        return  clearKeyBytes;

    }

    public Key formDESKey (short keyLength, byte[] clearKeyBytes) throws Exception {
        Key key = null;

        switch (keyLength) {
            case 1:
            {
                key = new SecretKeySpec(clearKeyBytes, "DES");
            };
            break;
            case 2:
            {
                clearKeyBytes = concat(clearKeyBytes, 0, getBytesLength((short)2), clearKeyBytes, 0, getBytesLength((short)1));
            }
            case 3:
            {
                key = new SecretKeySpec(clearKeyBytes, "DESede");
            }
        }

        if (key == null)
            throw  new Exception("Unsupported DES key length: " + keyLength + " bits");

        return  key;
    }

    public byte[] concat (byte[] array1, int beginIndex1, int length1, byte[] array2,
                          int beginIndex2, int length2) {
        byte[] concatArray = new byte[length1 + length2];
        System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
        System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
        return  concatArray;
    }

    public byte[] concat (byte[] array1, byte[] array2) {
        byte[] concatArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatArray, 0, array1.length);
        System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
        return  concatArray;
    }

    public int getBytesLength(short keyLength) throws Exception {
        int bytesLength = 0;
        switch (keyLength) {
            case 1: bytesLength = 8;break;
            case 2: bytesLength = 16;break;
            case 3: bytesLength = 24; break;
            default: throw new Exception("Unsupported key length: " + keyLength + " bits");
        }
        return bytesLength;
    }

    public byte[] hex2byte (byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i=0; i<len*2; i++) {
            int shift = i%2 == 1 ? 0 : 4;
            d[i>>1] |= Character.digit((char) b[offset+i], 16) << shift;
        }
        return d;
    }

    public byte[] hex2byte (String s) {
        return hex2byte (s.getBytes(), 0, s.length() >> 1);
    }

    public String byte2hex(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);
        for (int i=0; i<b.length; i++) {
            char hi = Character.forDigit ((b[i] >> 4) & 0x0F, 16);
            char lo = Character.forDigit (b[i] & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    public byte[] doCryto(byte[] data,Key key,int CipherMode)  throws Exception {
        byte[] result;
        try {
            transformation = key.getAlgorithm() + "/" + desMode + "/" + desPadding;
            Cipher c1 = Cipher.getInstance(transformation);
            c1.init(CipherMode, key);
            result = c1.doFinal(data);
        } catch (Exception e) {
            throw  new Exception(e);
        }

        return  result;
    }

    public String xorTwoValues  (String component1, String component2) {
        String xorResult = "";
        String bitComp1 = "";
        String bitComp2 = "";
        Utilz util = new Utilz();
        char[] tempComp = new char[32];

        tempComp = component1.toCharArray();
        for (int i = 0; i < tempComp.length; i++) {
            bitComp1 = bitComp1 + util.retBinaryValue(String.valueOf(tempComp[i]));
        }

        tempComp = component2.toCharArray();
        for (int i = 0; i < tempComp.length; i++) {
            bitComp2 = bitComp2 + util.retBinaryValue(String.valueOf(tempComp[i]));
        }

        char[] charBitComp1 = bitComp1.toCharArray();
        char[] charBitComp2 = bitComp2.toCharArray();

        char [] charResult = xorChars (charBitComp1, charBitComp2, 128);

        String xoredCharVal = String.valueOf(charResult);

        int i = 0;
        while (i < xoredCharVal.length()){
            xorResult = xorResult + util.retHexVal(xoredCharVal.substring(i, i + 4));
            i = i + 4;
        }

        return xorResult;
    }

    private char[] xorChars (char[] comp1, char[] comp2, int len) {
        char[] xorResult = new char[len];

        for (int i = 0; i < len; i++) {
            if (comp1[i] == '0' && comp2[i] == '0')
                xorResult[i] = '0';

            else if (comp1[i] == '0' && comp2[i] == '1')
                xorResult[i] = '1';

            else if (comp1[i] == '1' && comp2[i] == '0')
                xorResult[i] = '1';

            else if (comp1[i] == '1' && comp2[i] == '1')
                xorResult[i] = '0';
        }
        return xorResult;
    }

    //public String getXorWithParity (String xoredVal) {
    public String getOddParity (String xoredVal) {
        String xorWithParity = "";

        char [] charXoredVal = xoredVal.toCharArray();

        int i = 0;

        while (i < charXoredVal.length) {
            if (i%2 == 1) {
                if (charXoredVal[i] == '0')
                    xorWithParity = xorWithParity + '1';
                else if (charXoredVal[i] == '1')
                    xorWithParity = xorWithParity + '0';
                else if (charXoredVal[i] == '2')
                    xorWithParity = xorWithParity + '3';
                else if (charXoredVal[i] == '3')
                    xorWithParity = xorWithParity + '2';
                else if (charXoredVal[i] == '4')
                    xorWithParity = xorWithParity + '5';
                else if (charXoredVal[i] == '5')
                    xorWithParity = xorWithParity + '4';
                else if (charXoredVal[i] == '6')
                    xorWithParity = xorWithParity + '7';
                else if (charXoredVal[i] == '7')
                    xorWithParity = xorWithParity + '6';
                else if (charXoredVal[i] == '8')
                    xorWithParity = xorWithParity + '9';
                else if (charXoredVal[i] == '9')
                    xorWithParity = xorWithParity + '8';
                else if (charXoredVal[i] == 'A')
                    xorWithParity = xorWithParity + 'B';
                else if (charXoredVal[i] == 'B')
                    xorWithParity = xorWithParity + 'A';
                else if (charXoredVal[i] == 'C')
                    xorWithParity = xorWithParity + 'D';
                else if (charXoredVal[i] == 'D')
                    xorWithParity = xorWithParity + 'C';
                else if (charXoredVal[i] == 'E')
                    xorWithParity = xorWithParity + 'F';
                else if (charXoredVal[i] == 'F')
                    xorWithParity = xorWithParity + 'E';
            }

            else
                xorWithParity = xorWithParity + charXoredVal[i];

            i = i + 1;
        }

        return xorWithParity;
    }

    public String generateHash256Value(String msg, String key) {
        MessageDigest m = null;
        String hashText = null;
        byte[] actualKeyBytes = hexStringToBytes(key)
                ;

        try {
            m = MessageDigest.getInstance("SHA-256");
            m.update(actualKeyBytes, 0, actualKeyBytes.length);
            try {
                m.update(msg.getBytes("UTF-8"), 0, msg.length());
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            hashText = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        if (hashText.length() < 64) {
            int numberOfZeroes = 64 - hashText.length();
            String zeroes = "";

            for (int i = 0; i < numberOfZeroes; i++)
                zeroes = zeroes + "0";

            hashText = zeroes + hashText;

            //Log.info("Utility :: generateHash256Value :: HashValue with zeroes: " + hashText);
        }

        return hashText;

    }

    public byte[] hexStringToBytes (String s) {
        return hex2byte (s.getBytes(), 0, s.length() >> 1);
    }
}