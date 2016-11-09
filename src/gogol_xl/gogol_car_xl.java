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
    * double judicieusement des arretes pour que le graphe possede un graphe eulerien
    */
    public void rendreEulerien() {

    }

    /**
     * fonction de test de la classe
     */
    public static void main(String[] args) {

        gogol_car_xl car = new gogol_car_xl();
        car.parser("../instances/test.txt");

        System.out.println(car);

        car.rendreEulerien();

        arborescence arbo = car.creer_arborescence();

        car.numeroter_rec(arbo);

        car.cycle_gogol(arbo.get_sommet());

    }

}
