package com.paypro.wallet.wallet.blockchain;



import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

public class Account {

    private String address;
    
    public Account(String address) {
		super();
		this.address = address;
	}
    
    public Account(String address2, String id, int i) {
		// TODO Auto-generated constructor stub
	}

	private File[] getFileList(String dirPath) {
        File dir = new File(dirPath);   

        File[] fileList = dir.listFiles(new FilenameFilter() 
        	{
        	private String name = Numeric.cleanHexPrefix(address.toLowerCase());
        	
            public boolean accept(File dir, String name) {
                return name.endsWith(this.name);
            }
        });
        return fileList;
    }
    
    public String getRutaAccountKeyStore(String rutaKeyStore) throws Exception
    {
    	File[] archivo = this.getFileList(rutaKeyStore);
    	if (archivo.length == 1)
    	{
    		return String.valueOf(archivo[0]);
        	
    	}
    	else if (archivo.length == 0)
    	{
    		throw new Exception("File no found");
    	}else {
    		throw new Exception("More than 1 found");
    	}
		
    }
    
    public void changePassword(String oldPassword, String newPassword,String rutaKeyStore) throws Exception {
    	
    	String archivo = this.getRutaAccountKeyStore(rutaKeyStore);

		
    	Credentials KeySoterDecrypt = WalletUtils.loadCredentials(oldPassword, archivo);
    	System.out.println(KeySoterDecrypt.getEcKeyPair().getPrivateKey().toString(16));
    	
    	System.out.println(Keys.getAddress(KeySoterDecrypt.getEcKeyPair().getPublicKey()));
        WalletUtils.generateWalletFile(newPassword, KeySoterDecrypt.getEcKeyPair(), new File(rutaKeyStore), true);
        
        //FIXME borrar keystores antiguos: Se borrarán los que tengan más de un día, por lo que no incluirán los que se acaban de generar.
        this.deleteKeystore(rutaKeyStore);
        
    }
    
    private void deleteKeystore(String rutaKeyStore) {
    	
    	//TODO cambiar "ruta_keystore" por la ruta donde se encuentre el keystore de la cuenta asociada
    	rutaKeyStore = "ruta_keystore";
		
    	 File directorio = new File(rutaKeyStore);
         File f;
         if (directorio.isDirectory()) {
             String[] files = directorio.list();
             if (files.length > 0) {            
                 for (String archivo : files) {
                     System.out.println(archivo);
                     f = new File(rutaKeyStore + File.separator + archivo);
                     System.out.println("Ultima modificación: " + new Date(f.lastModified()));
                     long Time;
                     Time = (System.currentTimeMillis() - f.lastModified());
                     long cantidadDia = (Time / 86400000);
                     System.out.println("Age of the file is: " + cantidadDia + " days");
                     // Attempt to delete it 86400000 ms is equivalent to one day
                     if (Time > (86400000 * 1)) {
                         System.out.println("Borrado:" + archivo);
                         f.delete();
                         f.deleteOnExit();
                     }

                 }
             }
             else {
            	 System.out.println(" Directorio vacio: " + rutaKeyStore);
             }
         }
         
         
		
	}

	public Credentials loadCredentials(String password,String rutaKeyStore) throws Exception {
    	
    	String archivo = this.getRutaAccountKeyStore(rutaKeyStore);

		
    	Credentials KeySoterDecrypt = WalletUtils.loadCredentials(password, archivo);
    	
    	return KeySoterDecrypt;
    	
    }

}
