package Domini;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class Usuari {
		private String nom;
		private String password;
	
		public Usuari(String nomu, String pass) {
			this.nom = nomu;
			this.password = getSHA256Hash(pass);
		}
		private String  bytesToHex(byte[] hash) {
			        return DatatypeConverter.printHexBinary(hash);
		}
		private String getSHA256Hash(String data) {
			        String result = null;
			        try {
			            MessageDigest digest = MessageDigest.getInstance("SHA-256");
			            byte[] hash = digest.digest(data.getBytes("UTF-8"));
			            return bytesToHex(hash); // make it printable
			        }catch(Exception ex) {
			            ex.printStackTrace();
			        }
			        return result;
			    }

		public Usuari GetUsuari(String nomu, String pass) {
			//S'implementara en la seguent entrega ja que agafa un usuari de la base de dades
			return this;
		}
		public void printUsuari() {
			System.out.println(this.nom);
			System.out.println(this.password);
		}
		public void printNomUsuari() {
			System.out.println(this.nom);
		}
		public void ChangePassword(String pass) {
			String sha = getSHA256Hash(pass);
			if(this.password.equals(sha)) {
				System.out.println("Perdoni, ha introduit la mateixa contrasenya");
			}
			else this.password = sha;
		}
		public int MillorTemps(String dificultat) {
			//retornar� el millor temps de l'usuari depenent de la dificultat
			return 0;
		}
		public void SetRanking(String dificultat, Integer temps) {
			//aqui introduir� els resultats d'un usuari en en ranking
		}
		public void EsborrarUsuari() {
			//borrar� l'usuari de la base de Dades
		}
		
}
