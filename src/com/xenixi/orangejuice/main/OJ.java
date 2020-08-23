package com.xenixi.orangejuice.main;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class OJ {
	public static void main(String[] args) {

		if (args.length == 0 || args[0].equalsIgnoreCase("-e")) {
			System.out.println("SSGrid Encryption manager - Enter Key: ");
			Scanner scan = new Scanner(System.in);
			final String APIKEY = scan.nextLine();
			System.out.println("Enter desired password: ");
			final String password = scan.nextLine();

			// if (System.getProperty("os.name").contains("Windows"))
			// Runtime.getRuntime().exec("cls");
			// else
			// Runtime.getRuntime().exec("clear");
			scan.close();
			System.out.println("Displaying final:");
			System.out.println(encrypt(APIKEY, password));

		} else if (args[0].equalsIgnoreCase("-d")) {
			String password;

			Scanner scan = new Scanner(System.in);

			System.out.println("Enter encrypted string: ");
			String text = scan.nextLine();
			System.out.println("Enter password: ");
			password = scan.nextLine();
			scan.close();
			System.out.println("Displaying Decrypted: ");
			System.out.println(decrypt(text, password));

		} else {
			System.out.println("SSGrid Encryption: Invalid parameter | Options: -d, -e");
		}
	}

	public static String encrypt(String APIKEY, String password) {
		try {
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[8];
			random.nextBytes(salt);

			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

			SecretKey keyTmp = keyFact.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(keyTmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);

			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] cipherText = Base64.getEncoder().encode(cipher.doFinal(APIKEY.getBytes("UTF-8")));
			String plain = new String(cipherText);

			String ivString = new String(Base64.getEncoder().encode(iv));

			return (plain + "ORANGEJUICE" + ivString + "ORANGEJUICE" + new String(Base64.getEncoder().encode(salt)));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.err.println("ERROR! Exiting...");
			System.exit(0);
		} catch (Exception w) {
			w.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String text, String password) {
		String[] total = (text.split("ORANGEJUICE"));
		if (total.length < 3) {
			System.err.println("Invalid!!!");
			System.exit(0);
		}

		try {
			byte[] cipherText, iv, salt;
			salt = Base64.getDecoder().decode(total[2].getBytes());
			iv = Base64.getDecoder().decode(total[1].getBytes());
			cipherText = Base64.getDecoder().decode(total[0].getBytes());

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecretKey keyTmp = keyFact.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(keyTmp.getEncoded(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			String plaintext = "FAILED - INCORRECT PASSWORD";

			plaintext = new String(cipher.doFinal(cipherText), "UTF-8");
			return (plaintext);
		} catch (BadPaddingException x) {

		} catch (Exception g) {
			g.printStackTrace();
		}
		return null;

	}
}
