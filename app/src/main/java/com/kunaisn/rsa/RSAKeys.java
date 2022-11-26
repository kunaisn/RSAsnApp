package com.kunaisn.rsa;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

class RSAKeys {

    private int keyLen = -1;
    private int charLen;
    private BigInteger n = new BigInteger("-1");
    private BigInteger e = new BigInteger("-1");
    private BigInteger d = new BigInteger("-1");
    private int strLen;

    // 鍵長を指定して鍵を生成
    RSAKeys(int keyLen) {
        this.keyLen = keyLen;
        charLen = this.keyLen / 4;
        strLen = (int)(this.keyLen/16 * (0.5 + 0.25 + 0.125 + 0.0625));
        createKeys();
    }

    // 内部ストレージから鍵を復元
    RSAKeys(Context context) throws FileNotFoundException {
        try {
            File file = new File(context.getFilesDir(), "keys.txt");
            Scanner scan = new Scanner(file);
            this.keyLen = Integer.parseInt(scan.nextLine());
            charLen = this.keyLen / 4;
            this.n = new BigInteger(scan.nextLine());
            this.e = new BigInteger(scan.nextLine());
            this.d = new BigInteger(scan.nextLine());
            strLen = (int)(this.keyLen/16 * (0.5 + 0.25 + 0.125 + 0.0625));
        } catch(Exception e) {
            throw e;
        }
    }

    // 入力された文字列から、公開鍵だけのインスタンスを生成
    RSAKeys(String str) {
        try {
            String data = "";
            for(int i=0, counter = 0; i<str.length(); i++) {
                if(str.charAt(i) == '&') {
                    switch(counter) {
                        case 0:
                            this.keyLen = Integer.parseInt(data);
                            break;
                        case 1:
                            this.n = hexToDec(data);
                            break;
                        case 2:
                            this.e = hexToDec(data);
                            break;
                        default:
                    }
                    counter++;
                    data = "";
                    str = str.substring(i, str.length());
                    i = 0;
                } else {
                    data += str.charAt(i);
                }
            }
            this.charLen = this.keyLen / 4;
            strLen = (int)(this.keyLen/16 * (0.5 + 0.25 + 0.125 + 0.0625));
        } catch(Exception e) {
            throw e;
        }
    }

    // 共通鍵と秘密鍵を生成する
    private void createKeys() {
        Random rnd = new Random();
        BigInteger p, q, l;
        p = BigInteger.probablePrime(keyLen/2, rnd);
        q = BigInteger.probablePrime(keyLen/2, rnd);
        l = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        n = p.multiply(q);
        e = createE(l);
        createD(e, l);
        d = x.add(l).remainder(l);
    }

    // 「l」と乱数を使って「E」を決める
    private BigInteger createE(BigInteger num) {
        Random rnd = new Random();
        long gcdNum = 0;
        BigInteger ePro = BigInteger.ZERO;
        while(gcdNum != 1) {
            ePro = new BigInteger(keyLen/2, rnd);
            gcdNum = ePro.gcd(num).longValue();
        }
        return ePro;
    }

    // 不定方程式的な何か、「E」と「L」で「D」を探す
    private BigInteger x;
    private BigInteger y;
    private BigInteger createD(BigInteger eNum, BigInteger lNum) {
        if(lNum.equals(BigInteger.ZERO)) {
            x = BigInteger.ONE;
            y = BigInteger.ZERO;
            return eNum;
        }
        BigInteger tmp = x;
        x = y;
        y = tmp;
        BigInteger d = createD(lNum, eNum.remainder(lNum));
        tmp = x;
        x = y;
        y = tmp;
        y = y.subtract(eNum.divide(lNum).multiply(x));
        return d;
    }

    BigInteger getN() {
        return n;
    }
    BigInteger getE() {
        return e;
    }
    BigInteger getD() {
        return d;
    }
    String getNHex() {
        return decToHex(n);
    }
    String getEHex() {
        return decToHexHalf(e);
    }
    String getKeyLen() {
        return "" + keyLen;
    }
    @Override
    public String toString() {
        return keyLen + "\n" +
                getN().toString() + "\n" +
                getE().toString() + "\n" +
                getD().toString() + "\n";
    }

    String decToHex(BigInteger b) {
        BigInteger f = new BigInteger(0xf + "");
        String str = "";
        while(str.length() < charLen) {
            BigInteger letter = b.and(f);
            str = Integer.toHexString(letter.intValue()) + str;
            b = b.shiftRight(4);
        }
        return str;
    }

    String decToHexHalf(BigInteger b) {
        BigInteger f = new BigInteger(0xf + "");
        String str = "";
        int charLenHalf = charLen / 2;
        while(str.length() < charLenHalf) {
            BigInteger letter = b.and(f);
            str = Integer.toHexString(letter.intValue()) + str;
            b = b.shiftRight(4);
        }
        return str;
    }

    private BigInteger hexToDec(String str) {
        BigInteger bitText = BigInteger.ZERO;
        for(int j=0; j<str.length(); j++) {
            bitText = bitText.shiftLeft(4);
            BigInteger tmp = new BigInteger("" + Integer.parseInt("" + str.charAt(j), 16));
            bitText = bitText.add(tmp);
        }
        return bitText;
    }

    // 公開鍵を使用して暗号化する
    String rsaEncryption(String str) {
        int strLen = (int)(keyLen/16 * (0.5 + 0.25 + 0.125 + 0.0625));
        String encryptStr = "";
        int blocks = (str.length()-1) / strLen + 1;
        for(int i=0; i<blocks; i++) {
            String block;
            try {
                block = str.substring(i*strLen, (i + 1)*strLen);
            } catch(Exception e) {
                block = str.substring(i*strLen, str.length());
            }
            BigInteger bitText = BigInteger.ZERO;
            for(int j=0; j<strLen; j++) {
                bitText = bitText.shiftLeft(16);
                try {
                    bitText = bitText.add(new BigInteger((int)block.charAt(j) + ""));
                } catch(Exception e) {
                    bitText = bitText.add(BigInteger.ZERO);
                }
            }
            // 暗号文の文章化は4ビットずつ行う。
            BigInteger result = bitText.modPow(e, n);
            encryptStr += decToHex(result);
        }
        return encryptStr;
    }

    // 秘密鍵を使用して復号する
    String rsaDecryption(String str) {
        String decryptStr = "";
        int blocks = (str.length()-1) / charLen + 1;
        for(int i=0; i<blocks; i++) {
            String block;
            try {
                block = str.substring(i*charLen, (i + 1)*charLen);
            } catch(Exception e) {
                block = str.substring(i*charLen, str.length());
            }
            BigInteger bitText = hexToDec(block);
            BigInteger result = bitText.modPow(d, n);
            BigInteger f4 = new BigInteger(0xffff + "");
            String tmpStr = "";
            for(int j=0; j<strLen; j++) {
                int[] ary = new int[1];
                ary[0] = result.and(f4).intValue();
                result = result.shiftRight(16);
                tmpStr = new String(ary, 0, 1) + tmpStr;
            }
            decryptStr = tmpStr;
        }
        return decryptStr;
    }

}
