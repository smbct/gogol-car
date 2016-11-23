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
            for(Integer sommet1 : sommetsImpairs) {
                for(Integer sommet2 : sommetsImpairs) {

                    // un couple peut être former par ces deux sommets
                    if(!utilise.get(sommet1) && ! utilise.get(sommet2)) {
                        if(distMin == -1 || distances[sommet1][sommet2] < distMin) {
                            distMin = distances[sommet1][sommet2];
                            sommetMin1 = sommet1;
                            sommetMin2 = sommet2;
                        }
                    }
                }
            }

            // ces deux sommets sont désormais utilisés
            utilise.set(sommetMin1, Boolean.TRUE);
            utilise.set(sommetMin2, Boolean.TRUE);

            couplage.add(new couple(sommetMin1, sommetMin1));

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
        int succecesseurs [][] = new int[car_l.getNbPlace()][car_l.getNbPlace()];
        //remplissage initila des matrices
        car_l.transformeMatrice(matriceAdjacence, succecesseurs);
        //floy-warshall avec memorisation des chemin
        for(int k=0; k<car_l.getNbPlace(); k++) {
            for(int i=0; i<car_l.getNbPlace(); i++) {
                for(int j=0; j<car_l.getNbPlace(); j++) {
                    if (matriceAdjacence[i][k] + matriceAdjacence[k][j] < matriceAdjacence[i][j]) {
                        matriceAdjacence[i][j] = matriceAdjacence[i][k] + matriceAdjacence[k][j];
                        succecesseurs[i][j] = k; //on doit passer par k pour avoir le plus cour chemin
                    }
                }
            }
        }

        //recherche du couplage de poid minimal (non exact) de sommetImpair selon les distances de matriceAdjacence
        ArrayList<couple> couplage = new ArrayList<couple>();

<<<<<<< HEAD
        // créer les couples
        couplerGraphe(sommetImpair, matriceAdjacence, couplage);
=======
        ameliore(couplage, matriceAdjacence);
>>>>>>> f62e43be466c8febc3c8277d70e900987bf07136

        //doublage des arretes le long des chemin du couplage
        int parcrec, precrec;
        for(couple parc : couplage) {
            parcrec = parc.premier;
            while (parcrec != succecesseurs[parcrec][parc.deuxieme]) {
                precrec = parcrec;
                parcrec = succecesseurs[parcrec][parc.deuxieme];
                car_l.ajout_arrete (precrec, parcrec);
            }
            car_l.ajout_arrete (parcrec, parc.deuxieme);
        }
    }

<<<<<<< HEAD
=======
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
>>>>>>> f62e43be466c8febc3c8277d70e900987bf07136
    }

    /**
     * fonction de test de la classe
     */
    public static void main(String[] args) {

        gogol_car_xl car = new gogol_car_xl();
        car.parser("../instances/test.txt");

        System.out.println(car);

        car.rendreEulerien();

        arborescence arbo = car.car_l.creer_arborescence();

        car.car_l.numeroter_rec(arbo);

        car.car_l.cycle_gogol(arbo.get_sommet());

    }

}
