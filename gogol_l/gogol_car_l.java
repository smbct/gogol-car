import java.util.ArrayList;
import java.lang.String;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.*;

import java.util.Hashtable;

/**
 * @class gogol_car_l
 */
public class gogol_car_l {

    private ArrayList<String> places;
    // dictionnaire association chaque ville à son numéro de sommet dans le graphe
    private Hashtable<String, Integer> numeroPlaces;
    private int nbPlace;
    private int nbRue;

    // rues de la ville
    private ArrayList<ArrayList<String>> rues;

    // matrice d'adjacence du graphe
    private ArrayList<ArrayList<Boolean> > matrice;

    public void parser(String nom) {

        FileReader fr;
        try {
        	fr = new FileReader(nom);
            BufferedReader reader = new BufferedReader(fr);

            String ligne;

            // lecture du nombre de place
            ligne = reader.readLine();
            nbPlace = Integer.parseInt(ligne.substring(0, ligne.length()-1));

            // création de la matrice d'adjacence et de la matrice des rues
            matrice = new ArrayList<ArrayList<Boolean>>(nbPlace);
            for(int indPlace = 0; indPlace < nbPlace; indPlace ++) {
                matrice.add(new ArrayList<Boolean>(nbPlace));
                for(int indPlace2 = 0; indPlace2 < nbPlace; indPlace2 ++) {
                    matrice.get(indPlace).add(Boolean.FALSE);
                }
            }

            // création de la matrice des rues
            rues = new ArrayList<ArrayList<String>>(nbPlace);
            for(int indPlace = 0; indPlace < nbPlace; indPlace ++) {
                rues.add(new ArrayList<String>(nbPlace));
                for(int indPlace2 = 0; indPlace2 < nbPlace; indPlace2 ++) {
                    rues.get(indPlace).add(null);
                }
            }


            // lecture du nombre de rue
            ligne = reader.readLine();
            nbRue = Integer.parseInt(ligne.substring(0, ligne.length()-1));

            // lecture du nom des places
            for(int indPlace = 1; indPlace <= nbPlace; indPlace ++) {
                ligne = reader.readLine();
                places.add(ligne.substring(0, ligne.length()-1));
                numeroPlaces.put(places.get(places.size()-1), new Integer(places.size()-1));
            }


            // lecture du nom des rues et des places adjacentes
            for(int indRue = 1; indRue <= nbRue; indRue ++) {
                ligne = reader.readLine();
                String[] noms = ligne.split(";");

                int num1 = numeroPlaces.get(noms[1].substring(0, noms[1].length()-1));
                int num2 = numeroPlaces.get(noms[2].substring(0, noms[2].length()-1));

                String nomRue = noms[0].substring(0, noms[0].length()-1);

                rues.get(num1).set(num2, nomRue);
                rues.get(num2).set(num1, nomRue);

                matrice.get(num1).set(num2, Boolean.TRUE);
                matrice.get(num2).set(num1, Boolean.TRUE);
            }

    	} catch(IOException ex) {
			System.out.println("Erreur à l'ouverture du ficher");
			fr = null;
		}


    }

    /**
     * constructeur par défaut
     */
    public gogol_car_l() {

        numeroPlaces = new Hashtable<String, Integer>();

        // création de la liste des places
        places = new ArrayList<String>();
    }

    public String toString() {
        /*String str = "liste des places : \n";

        for(int i = 0; i < nbPlace; i++) {
            str += places.get(i) + "\n";
        }

        str += "Liste des rues : \n";

        for(int i = 0; i < nbPlace; i++) {
            for(int j = i+1; j < nbPlace; j++) {
                if(rues.get(i).get(j) != null) {
                    str += "entre " + places.get(i);
                    str += " et " + places.get(j) + " : " + rues.get(i).get(j) + "\n";
                }
            }
        }*/

        String str = "";

        for(int i = 0; i < nbPlace; i++) {
            for(int j = 0; j < nbPlace; j++) {
                if(matrice.get(i).get(j)) {
                    str += "X ";
                } else {
                    str += "  ";
                }
            }
            str += "\n";
        }

        return str;
    }

    /**
     * création de l'arborescence de la ville
     * @pre le graphe est connexe
     */
    private arborescence anti_arborescence() {

        ArrayList<Boolean> visite = new ArrayList<Boolean>(nbPlace);
        for(int numPlace = 1; numPlace <= nbPlace; numPlace ++) {
            visite.add(Boolean.FALSE);
        }

        arborescence res = creer_arborescence_rec(0, visite);

        return res;
    }

    /**
     * création récursivement d'un arbre recouvrant du graphe par parcours en profondeur
     */
    private arborescence creer_arborescence_rec(int sommet, ArrayList<Boolean> visite) {

        arborescence arbo = new arborescence(sommet);

        visite.set(sommet, Boolean.TRUE);

        for(int i = 0; i < nbPlace; i++) {
            if(matrice.get(sommet).get(i) && !visite.get(i)) {
                arbo.ajouterFils(creer_arborescence_rec(i, visite));
            }
        }

        return arbo;
    }

    /**
     * fonction de test de la classe
     */
    public static void main(String[] args) {

        gogol_car_l car = new gogol_car_l();
        car.parser("../test.txt");

        // System.out.println(car);

        arborescence arbo = car.anti_arborescence();

        System.out.println(arbo);

    }

}
