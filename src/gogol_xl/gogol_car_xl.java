package gogol_xl;

import gogol_l.*;

import java.util.ArrayList;
import java.lang.String;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.*;

import java.util.Hashtable;

public class gogol_car_xl {

    private gogol_car_l car_l;

    public gogol_car_xl() {
        car_l = new gogol_car_l();
    }

    public void parser(String nom) {
        car_l.parser(nom);
    }

    public String toString() {
        return car_l.toString();
    }

    /**
     * création de couples de sommets impairs de façon à obtenir une distance assez petite
     */
    public void couplerGraphe(ArrayList<Integer> sommetsImpairs, int distances[][], ArrayList<couple> couplage) {

        int nbCouple = sommetsImpairs.size()/2;

        ArrayList<Boolean> utilise = new ArrayList<Boolean>(sommetsImpairs.size());
        for(int i = 0; i < sommetsImpairs.size(); i++) {
            utilise.add(Boolean.FALSE);
        }

        while(nbCouple != 0) {

            // recherche de deux sommets non utilisés qui sont reliés par le plus court chemin
            int distMin = -1;
            Integer sommetMin1 = null, sommetMin2 = null;
            for(int ind1 = 0; ind1 < sommetsImpairs.size(); ind1++) {
                for(int ind2 = 0; ind2 < sommetsImpairs.size(); ind2++) {

                    // un couple peut être former par ces deux sommets
                    if(ind1 != ind2 && !utilise.get(ind1) && !utilise.get(ind2)) {
                        if(distMin == -1 || distances[sommetsImpairs.get(ind1)][sommetsImpairs.get(ind2)] < distMin) {
                            distMin = distances[sommetsImpairs.get(ind1)][sommetsImpairs.get(ind2)];
                            sommetMin1 = ind1;
                            sommetMin2 = ind2;
                        }
                    }
                }
            }

            // ces deux sommets sont désormais utilisés
            utilise.set(sommetMin1, Boolean.TRUE);
            utilise.set(sommetMin2, Boolean.TRUE);

            couplage.add(new couple(sommetsImpairs.get(sommetMin1), sommetsImpairs.get(sommetMin2)));

            nbCouple --;
        }

    }

    /**
    * double judicieusement des arretes pour que le graphe possede un circuit eulerien
    */
    public void rendreEulerien() {

        ArrayList<Integer> sommetImpair = new ArrayList<Integer>();

        //recherche des sommet de degre impair
        for(Integer parc : car_l.getDegre() ) {
            if (parc % 2 != 0) {
                sommetImpair.add(parc);
            }
        }

        //calcul des distances entre tous les sommets
        int matriceAdjacence [][] = new int[car_l.getNbPlace()][car_l.getNbPlace()];
        int successeurs [][] = new int[car_l.getNbPlace()][car_l.getNbPlace()];
        //remplissage initila des matrices
        car_l.transformeMatrice(matriceAdjacence, successeurs);
        //floy-warshall avec memorisation des chemin
        for(int k=0; k<car_l.getNbPlace(); k++) {
            for(int i=0; i<car_l.getNbPlace(); i++) {
                for(int j=0; j<car_l.getNbPlace(); j++) {
                    if (matriceAdjacence[i][k] + matriceAdjacence[k][j] < matriceAdjacence[i][j]) {
                        matriceAdjacence[i][j] = matriceAdjacence[i][k] + matriceAdjacence[k][j];
                        successeurs[i][j] = k; //on doit passer par k pour avoir le plus cour chemin
                    }
                }
            }
        }

        //recherche du couplage de poid minimal (non exact) de sommetImpair selon les distances de matriceAdjacence
        ArrayList<couple> couplage = new ArrayList<couple>();

        // créer les couples
        couplerGraphe(sommetImpair, matriceAdjacence, couplage);
        System.out.println(couplage);

        // ameliore(couplage, matriceAdjacence);

        //doublage des arretes le long des chemin du couplage
        int parcrec, precrec;
        for(couple parc : couplage) {
            parcrec = parc.premier;
            while (parcrec != successeurs[parcrec][parc.deuxieme]) {
                precrec = parcrec;
                parcrec = successeurs[parcrec][parc.deuxieme];
                car_l.ajout_arrete (precrec, parcrec);
            }
            car_l.ajout_arrete (parcrec, parc.deuxieme);
        }

        System.out.println("graphe : "+car_l);
    }


    /**
    * ameliore de manière heuristique le couplage
    * utilise en descente un operateur qui teste pour toute paire de couple {(i,j),(k,l)} les couples {(i,l),(k,j)} et {(i,k),(j,l)}
    */
    private void ameliore(ArrayList<couple> couplage, int matriceAdjacence [][]) {
        int tmp;
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i<couplage.size(); i++) { //pour tout les couples
                for(int j = 0; j<couplage.size(); j++) { //on essaye de croiser avec toutes les autres couples
                    if ( (matriceAdjacence[couplage.get(i).premier][couplage.get(i).deuxieme] + matriceAdjacence[couplage.get(j).premier][couplage.get(j).deuxieme]) >
                        (matriceAdjacence[couplage.get(i).premier][couplage.get(j).deuxieme] + matriceAdjacence[couplage.get(j).premier][couplage.get(i).deuxieme]) ) {
                        //le croisement est efficace
                        tmp = couplage.get(i).deuxieme;
                        couplage.get(i).deuxieme = couplage.get(j).deuxieme;
                        couplage.get(j).deuxieme = tmp;
                        improved = true;
                    } else if ( (matriceAdjacence[couplage.get(i).premier][couplage.get(i).deuxieme] + matriceAdjacence[couplage.get(j).premier][couplage.get(j).deuxieme]) >
                        (matriceAdjacence[couplage.get(i).premier][couplage.get(j).premier] + matriceAdjacence[couplage.get(i).deuxieme][couplage.get(j).deuxieme]) ) {
                        //l'autre croisement est eficace
                        tmp = couplage.get(i).deuxieme;
                        couplage.get(i).deuxieme = couplage.get(j).premier;
                        couplage.get(j).premier = tmp;
                        improved = true;
                    }
                }
            }
        }
    }

    /**
     * fonction de test de la classe
     */
    public static void main(String[] args) {

        gogol_car_xl car = new gogol_car_xl();
        car.parser("../instances/NantesPasEuler.txt");

        System.out.println(car);

        car.rendreEulerien();

        arborescence arbo = car.car_l.creer_arborescence();

        car.car_l.numeroter_rec(arbo);

        car.car_l.cycle_gogol(arbo.get_sommet());

    }

}
