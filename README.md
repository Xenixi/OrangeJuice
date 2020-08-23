# OrangeJuice
*AES encrypt/decrypt strings with passwords*

**Instructions:**</br></br>
      ***Command Line:*** </br>
    Jar file can be run from command line with arguments '-e' and '-d' for encryption and decryption modes respectively.
  </br></br>***Integration:***</br>
    This package is easy to integrate into your project. Simply download the jar file and add it to your project.
    The following methods are available for use. 
    
     OJ.encrypt(String APIKEY, String password);
     
     OJ.decrypt(String text, String password);
     
*This was initially intended for API key encryption for use embedded in code to prevent unauthorized access to sensitive API keys through decompiling, hence the APIKEY field's name.</br> This field may be used to pass in any string you wish to encrypt.*

-256 bit AES with salt-
