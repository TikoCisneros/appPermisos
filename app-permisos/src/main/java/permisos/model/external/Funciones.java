package permisos.model.external;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

public class Funciones {
	
	private static  final int num_provincias = 24;
	/**
	 * Atributos para el manejo de estados
	 */
	public static String estadoActivo = "A";
	public static String valorEstadoActivo = "Activo";
	public static String estadoInactivo = "I";
	public static String valorEstadoInactivo = "Inactivo";
	
	public static Boolean validacionCedula(String cedula){
        //verifica que los dos primeros dígitos correspondan a un valor entre 1 y NUMERO_DE_PROVINCIAS
        int prov = Integer.parseInt(cedula.substring(0, 2));
        if (!((prov > 0) && (prov <= num_provincias))) {
        	//addError("La cédula ingresada no es válida");
        	System.out.println("Error: cedula ingresada mal");
            return false;
        }
        //verifica que el último dígito de la cédula sea válido
        int[] d = new int[10];
        //Asignamos el string a un array
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(cedula.charAt(i) + "");
        }
        int imp = 0;
        int par = 0;
        //sumamos los duplos de posición impar
        for (int i = 0; i < d.length; i += 2) {
            d[i] = ((d[i] * 2) > 9) ? ((d[i] * 2) - 9) : (d[i] * 2);
            imp += d[i];
        }
        //sumamos los digitos de posición par
        for (int i = 1; i < (d.length - 1); i += 2) {
            par += d[i];
        }
        //Sumamos los dos resultados
        int suma = imp + par;
        //Restamos de la decena superior
        int d10 = Integer.parseInt(String.valueOf(suma + 10).substring(0, 1) + "0") - suma;
        //Si es diez el décimo dígito es cero        
        d10 = (d10 == 10) ? 0 : d10;
        //si el décimo dígito calculado es igual al digitado la cédula es correcta
        if (d10 == d[9]) {
        	return true;
        }else {
        	//addError("La cédula ingresada no es válida");
        	return false;
        }
	}
	
	/**
	 * Convierte un cadena en codigo MD5
	 * @param input entrada de cadena para convertirla en MD5
	 * @return String MD5
	 */
	public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * Convierte un cadena en codigo SHA2
	 * @param input  entrada de cadena para convertirla en SHA2
	 * @return String MD5
	 */
	public String getSHA2(String input) {
	    try {
	        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
	        String salt = "some_random_salt";
	        String passWithSalt = input + salt;
	        byte[] passBytes = passWithSalt.getBytes();
	        byte[] passHash = sha256.digest(passBytes);             
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< passHash.length ;i++) {
	            sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));         
	        }
	        String generatedPassword = sb.toString();
	        return generatedPassword;
	    } catch (NoSuchAlgorithmException e) { 
	    	e.printStackTrace(); 
	    	return null;
	    }       
	}
	
	/**
	 * Transforma un array de Byte en un String Base64
	 * @param array
	 * @return String
	 */
	public static String getBASE64(byte [] array){
		if(array==null || array.length == 0)
			return "";
		else{
			return Base64.encodeBase64(array).toString();
		}
	}
	
	/**
	 * Transforma un String Base64 en un array de Byte
	 * @param base64
	 * @return byte []
	 */
	public static byte [] getByteArray(String base64){
		if(base64.isEmpty())
			return null;
		else{
			return Base64.decodeBase64(base64);
		}
	}
	
	/**
	 * Transforma una fecha a String
	 * @param fecha
	 * @return String
	 */
	public static String dateToString(Date fecha){
		DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		if(fecha==null)
			return "";
		else
			return formato.format(fecha).toString();
	}
	
	/**
	 * Transforma un string de fecha en Date
	 * @param fecha
	 * @return Date
	 * @throws ParseException
	 */
	public static Date stringToDate(String fecha) throws ParseException{
		DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		return formato.parse(fecha);
	}
	
	/**
	 * Evalua si una cadena es numerica
	 * @param cadena
	 * @return
	 */
	public static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	/**
	 * Evalua un Entero para convertirlo a String
	 * @param dato
	 * @return String
	 */
	public static String evaluarInteger(Integer dato){
		if(dato==null)
			return "";
		else
			return dato.toString();
	}
	
	public static String asciiPass(int longitud){
		String getAscii = "";
        for(int i = 1; i <= longitud;i++){
            int NumAleatorio = 32+(int)(Math.random()* 92);
            System.out.println((char)NumAleatorio);
            getAscii += (char)NumAleatorio+"".replace('"', 'x');
            if(i == longitud){
                  System.out.println(getAscii);
                  break;
            }    
        }
        return getAscii;
	}
}
