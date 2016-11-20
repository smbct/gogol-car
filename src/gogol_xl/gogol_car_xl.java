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
