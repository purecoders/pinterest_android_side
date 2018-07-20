package com.farjami.mohsen.pinterest.system;

import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class G extends Application {

  public static Context context;
//  public static final String MAIN_URL="http://pinterest.ud";
//  public static final String MAIN_URL="http://192.168.10.107/pinterest";
  public static final String MAIN_URL="https://ittiktak.com/pinterest/public/api";
//  public static final String MAIN_URL="http://192.168.43.225/pinterest";
  public static final String SALT="7c3d596ed03ab9116c547b0eb678b247";

  public static final String SALT_COPY="7c3d596ed03ab9116c547b0eb678b247";

  private static final String AES = "AES";
  public static final String APP_NAME = "pinterest";

  public static  boolean isLoggedIn = false;


  @Override
  public void onCreate() {
    super.onCreate();
    context=getApplicationContext();
  }

 /* public static String getDeviceId(Context context){
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    return telephonyManager.getDeviceId();//IMEI
  }

  public static String getSubscriberId(Context context){
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    return telephonyManager.getSubscriberId();//IMSI
  }*/

  public static String getDeviceModel(){
    //String myDeviceModel = android.os.Build.MODEL;
    return android.os.Build.MODEL;
  }



  public static String getAndroidId(Context context){
    if(Secure.getString(context.getContentResolver(),Secure.ANDROID_ID).length()>0) {
      return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }else{
      return "";
    }
  }


  public static String getHashedString(String text){
    MessageDigest m = null;
    try {
      m = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    if (m != null) {
      m.reset();
    }
    if (m != null) {
      m.update(text.getBytes());
    }
    byte[] digest = new byte[0];
    if (m != null) {
      digest = m.digest();
    }
    BigInteger bigInt = new BigInteger(1,digest);
    String hashed_text = bigInt.toString(16);
    while(hashed_text.length() < 32 ){
      hashed_text = "0"+ hashed_text;
    }
    return hashed_text;
  }


  public static String getClientKey(String username){
    return G.getHashedString(
        G.SALT +
       // G.getDeviceId(G.context) +
        username +
        G.getDeviceModel()
       // G.getSubscriberId(G.context)
       // G.getAndroidId(G.context)
    );
  }



  public static String encrypt(String strClearText){
    try {
      SecretKey secretKey=generateKey();
      for (int i=0;i<Character.getNumericValue(getHashedString(SALT).charAt(10))-1;i++) {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(strClearText.getBytes("UTF-8"));
        strClearText=Base64.encodeToString(encrypted, Base64.DEFAULT);
      }
      return strClearText;

    } catch (Exception e) {
      e.printStackTrace();
      return "not encrypted";
    }
  }



  public static String decrypt(String strEncrypted){
    try {
      SecretKey secretKey=generateKey();
      for (int i=0;i<Character.getNumericValue(getHashedString(SALT).charAt(10))-1;i++) {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.decode(strEncrypted, Base64.DEFAULT));
        strEncrypted= new String(decrypted, "UTF-8");
      }
      return strEncrypted;
    } catch (Exception e) {
      e.printStackTrace();
      return "not decrypted";
    }
  }



  public static SecretKeySpec generateKey() {
    byte[] ENCRYPTION_KEY = new byte[]{
        '0',(byte)getHashedString(SALT).charAt(20),
        '4',(byte)getHashedString(SALT).charAt(16),
        '3',(byte)getHashedString(SALT).charAt(14),
        'i',(byte)getHashedString(SALT).charAt(4) ,
        'm',(byte)getHashedString(SALT).charAt(0) ,
        'a',(byte)getHashedString(SALT).charAt(6) ,
        'j',(byte)getHashedString(SALT).charAt(13),
        'r',(byte)getHashedString(SALT).charAt(15),
        'a',(byte)getHashedString(SALT).charAt(26),
        'F',(byte)getHashedString(SALT).charAt(30),
        'n',(byte)getHashedString(SALT).charAt(18),
        'e',(byte)getHashedString(SALT).charAt(7) ,
        's',(byte)getHashedString(SALT).charAt(2) ,
        'h',(byte)getHashedString(SALT).charAt(25),
        'o',(byte)getHashedString(SALT).charAt(30),
        'M',(byte)getHashedString(SALT).charAt(29),
      };
    return new SecretKeySpec(ENCRYPTION_KEY, AES);
  }


}