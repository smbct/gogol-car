package main;

import java.util.Scanner;

/**
 * classe permettant d'intéragir avec l'utilisateur et de lancer les options
 */
public class Main {


    /**
     * constructeur par défaut
     */
    public Main() {

    }

    /**
     * point d'entrée du programme
     */
    public static void main(String[] argc) {

        Main programme = new Main();
        programme.proposerIntineraire();

    }

    /**
     * affichage du menu proposant à l'utilisateur l'option à choisir pour calculer l'itinéraire
     */
    public void afficherMenu() {
        System.out.println("Veuillez choisir une option pour calculer votre itinéraire : (entier entre 1 et 3 attendu)");
        System.out.println("\t1) Photographies des rues en deux temps (gogol S)");
        System.out.println("\t2) Photographies des rues en un temps sur un graphe eulerien (gogol L)");
        System.out.println("\t3) Photographies des rues en un temps sur un graphe non eulerien (gogol XL)");
    }

    /**
     * Permet à l"utilisateur de saisir sa demande et lance l'alternative choisie
     */
    public void proposerIntineraire() {

        System.out.println("Bonjour et bienvenue dans le service d'attribution d'itinéraire pour la gogol car.\n");

        int saisie = -1;
        Scanner scan = new Scanner(System.in);

        do {

            afficherMenu();

            try {
                saisie = scan.nextInt();
            } catch(Exception ex) {
                scan.nextLine();
            }

            if(saisie != 1 && saisie != 2 && saisie != 3) {
                System.out.println("La saisie ne semble pas correcte. Veuillez saisir votre demande à nouveau :");
            }

        } while(saisie != 1 && saisie != 2 && saisie != 3);

        System.out.println("Veuillez maintenant indiquer le nom du fichier contenant votre ville :");
        String ligne = scan.next();

        switch(saisie) {

            case 1:
                gogolS.gogol_car_s car = new gogolS.gogol_car_s(ligne);
                car.calculItineraire();
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;

        }

        System.out.println("Bon trajet !");

    }



}
