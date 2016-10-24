import java.util.Hashtable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Integer;
import java.util.ArrayList;
import java.io.IOException;

public class gogol_car_s {

	private listeSuccesseur racine;
	
	public void parser(String fichier) {
        FileReader fr;
        try {
        	fr = new FileReader(fichier);
    	} catch(IOException ex) {
			System.out.println("erreur a l'ouvertue du ficher");
			fr = null;
		}
		BufferedReader reader = new BufferedReader(fr);
		String tmp, tmp1, tmp2, tmp3;
		Hashtable<String, ArrayList<listeSuccesseur> > noeuds = new Hashtable<String, ArrayList<listeSuccesseur>> (0); //nomplaces => rue adjacentes

		try { tmp = reader.readLine(); } catch (IOException ex) {System.out.println("erreur a la lecture du fichier"); tmp = "";}
        int nbplace = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));
        try { tmp = reader.readLine(); } catch (IOException ex) {System.out.println("erreur a la lecture du fichier"); tmp = "";}
        int nbrue = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));
        for(int i = 0; i<nbplace; i++) {
        	try { tmp = reader.readLine(); } catch (IOException ex) {System.out.println("erreur a la lecture du fichier"); tmp = "";}
        	noeuds.put(tmp.substring(0,tmp.lastIndexOf(".")), new ArrayList<listeSuccesseur>());
        }
        for(int i = 0; i<nbrue; i++) {
        	try { tmp = reader.readLine(); } catch (IOException ex) {System.out.println("erreur a la lecture du fichier"); tmp = "";}
        	tmp1 = tmp.substring(0, tmp.indexOf(";")-1); //nom rue
        	tmp2 = tmp.substring(tmp.indexOf(";")+1,tmp.indexOf(";", tmp.indexOf(";")+1 )-1 ); //nom extremite 1
        	tmp3 = tmp.substring(tmp.indexOf(";", tmp.indexOf(";")+1 )+1, tmp.lastIndexOf(".")); //nom extremite 2
        }

    }	

    public static void main(String[] argc) {
        System.out.println("hello graphe");
    }

}