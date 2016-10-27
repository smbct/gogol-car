package gogol_l;

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
     * Numérotation des arêtes du graphe en vue d'obtenir un circuit eulerien
     * @param arbo une arborescence du graphe qui va permettre de numéroter
     * @return la numérotation trouvée
     */
    private ArrayList<ArrayList<Integer>> numeroter(arborescence arbo) {

        ArrayList<ArrayList<Integer>> numeros = new ArrayList<ArrayList<Integer>>(nbPlace);
        for(int i = 0; i < nbPlace; i++) {
            numeros.add(new ArrayList<Integer>(nbPlace));
            for(int j = 0; j < nbPlace; j++) {
                numeros.get(i).add(-1);
            }
        }

        numeroter_rec(arbo, numeros);

        System.out.print("   ");
        for(int i = 0; i < nbPlace; i++) {
            System.out.print(i + " ");
            if(i < 10) {
                System.out.print(" ");
            }
        }
        System.out.println("");
        for(int i = 0; i < nbPlace; i++) {
            System.out.print(i + " ");
            if(i < 10) {
                System.out.print(" ");
            }
            for(int j = 0; j < nbPlace; j++) {
                if(matrice.get(i).get(j)) {
                    System.out.print(numeros.get(i).get(j) + "  ");
                } else {
                    System.out.print(".  ");
                }
            }
            System.out.println("");
        }

        return numeros;
    }

    /**
     * Calcule le cycle que doit effectuer la voiture de gogol
     * @param numeros la numérotation du graphe
     * @param racine la racine de l'arborescence
     */
    private void cycle_gogol(ArrayList<ArrayList<Integer>> numeros, int racine) {

        ArrayList<ArrayList<Boolean>> utilise = new ArrayList<ArrayList<Boolean>>(nbPlace);
        for(int i = 0; i < nbPlace; i++) {
            utilise.add(new ArrayList<Boolean>(nbPlace));
            for(int j = 0; j < nbPlace; j++) {
                utilise.get(i).add(Boolean.FALSE);
            }
        }

        boolean continuer = Boolean.TRUE;

        ArrayList<Integer> chemin = new ArrayList<Integer>();

        int sommetCourant = racine;
        chemin.add(sommetCourant);

        while(continuer) {

            // recherche du prochain sommet
            int sommetMin = -1;
            int numeroMin = -1;
            for(int j = 0; j < nbPlace; j++) {
                if(matrice.get(sommetCourant).get(j)) {

                    if(!utilise.get(sommetCourant).get(j) && (sommetMin == -1 || numeros.get(sommetCourant).get(j) < numeroMin)) {
                        sommetMin = j;
                        numeroMin = numeros.get(sommetCourant).get(j);
                    }

                }
            }

            // si un sommet a été trouvé, la recherche continue
            if(sommetMin != -1) {
                utilise.get(sommetCourant).set(sommetMin, Boolean.TRUE);
                utilise.get(sommetMin).set(sommetCourant, Boolean.TRUE);
                chemin.add(sommetMin);
                sommetCourant = sommetMin;
            } else {
                continuer = false;
            }

        }

        for(int sommet : chemin) {
            System.out.print(places.get(sommet) + ", ");
        }
        System.out.println("");

    }

    /**
     * Numérotation des sommets du graphe à partir de l'anti-arborescence de manière récurisive
     * @param arbo l'arborescence utilisée pour numéroter
     * @param numeros les numeros des arêtes de la ville
     */
    private void numeroter_rec(arborescence arbo, ArrayList<ArrayList<Integer>> numeros) {

        int sommet = arbo.get_sommet();
        int sommetPere = -1;
        if(arbo.get_pere() != null) {
            sommetPere = arbo.get_pere().get_sommet();
        }

        // calcul du nombre d'arc, nécessaire pour avoir le numéro du sommet
        int nbArcS = 0;
        for(int j = 0; j < nbPlace; j++) {
            if(matrice.get(sommet).get(j)) {
                nbArcS ++;
                if(sommetPere == -1) { // initialisation du sommet si c'estla racine
                    sommetPere = j;
                }
            }
        }

        // initialisation du numéro
        numeros.get(sommet).set(sommetPere, nbArcS);

        // fin de la numérotation des arcs sortant
        int numero = nbArcS - 1;
        for(int j = 0; j < nbPlace; j++) {
            if(matrice.get(sommet).get(j) && j != sommetPere) {
                numeros.get(sommet).set(j, numero);
                numero --;
            }
        }

        // numérotation récursive des autres sommets
        for(arborescence fils : arbo.get_fils()) {
            numeroter_rec(fils, numeros);
        }
    }

    /**
     * fonction de test de la classe
     */
    public static void main(String[] args) {

        /*gogol_car_l car = new gogol_car_l();
        car.parser("../euler_city.txt");

        System.out.println(car);

        arborescence arbo = car.anti_arborescence();

        System.out.println(arbo);

        ArrayList<ArrayList<Integer>> numeros = car.numeroter(arbo);

        car.cycle_gogol(numeros, arbo.get_sommet());*/

        successeur suc = new successeur(0, "test");

    }

}
