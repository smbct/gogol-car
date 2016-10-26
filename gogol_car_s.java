import java.util.Hashtable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Integer;
import java.util.TreeSet;
import java.io.IOException;

public class gogol_car_s {

	private listeSuccesseur racine;
	
	public gogol_car_s(String fichier) {
        FileReader fr;
        try {
            fr = new FileReader(fichier);
        	
    		BufferedReader reader = new BufferedReader(fr);
    		String tmp, tmp1, tmp2, tmp3;
    		Hashtable<String, TreeSet<listeSuccesseur> > noeuds = new Hashtable<String, TreeSet<listeSuccesseur>> (0); //nomplaces => rue adjacentes

    		tmp = reader.readLine();
            int nbplace = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));
            tmp = reader.readLine();
            int nbrue = Integer.parseInt(tmp.substring(0,tmp.lastIndexOf(".")));

            //lecteure nom des places
            for(int i = 0; i<nbplace; ++i) {
            	tmp = reader.readLine();
            	noeuds.put(tmp.substring(0,tmp.lastIndexOf(".")), new TreeSet<listeSuccesseur>());
            }
            
            //lecture des rues
            listeSuccesseur parc = null;
            for(int i = 0; i<nbrue; ++i) {
            	tmp = reader.readLine();
            	tmp1 = tmp.substring(0, tmp.indexOf(";")-1); //nom rue
            	tmp2 = tmp.substring(tmp.indexOf(";")+1,tmp.lastIndexOf(";")-1 ); //nom extremite 1
            	tmp3 = tmp.substring(tmp.lastIndexOf(";")+1, tmp.lastIndexOf(".")); //nom extremite 2
                parc = new listeSuccesseur(tmp1 , tmp2 , tmp3);
                for(listeSuccesseur j : noeuds.get(tmp2)) {
                    parc.ajoutSucc(j);
                }
                for(listeSuccesseur j : noeuds.get(tmp3)) {
                    parc.ajoutSucc(j);   
                }
                noeuds.get(tmp2).add(parc);
                noeuds.get(tmp3).add(parc);
            }
            this.racine = parc;
        } catch(IOException ex) {
            System.out.println("erreur a l'ouvertue du ficher");
        }
    }	

    public String toString() {
        return racine.toString();
    }

    public String parcours() {
        return "depart de " + encommun(racine, racine.getSucc().first()) + " : \n" + racine.parcours();
    }

    private String encommun(listeSuccesseur depart, listeSuccesseur arrive) {
        if ((depart.getdeb().equals(arrive.getdeb())) || (depart.getdeb().equals(arrive.getfin()))) {
            return depart.getfin();
        } else {
            return depart.getdeb();
        }
    }

    public static void main(String[] argc) {
        gogol_car_s test = new gogol_car_s("test.txt");
        System.out.println(test.parcours());
    }

}