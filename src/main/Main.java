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
        System.out.println("\t4) Quitter le programme");
    }

    /**
     * Permet à l"utilisateur de saisir sa demande et lance l'alternative choisie
     */
    public void proposerIntineraire() {

        System.out.println("Bonjour et bienvenue dans le service d'attribution d'itinéraire pour la gogol car.\n");

        int saisie = -1;
        Scanner scan = new Scanner(System.in);

        do {

            // affichage du menu
            afficherMenu();

            // vérification que la saisie est correcte
            try {
                saisie = scan.nextInt();
            } catch(Exception ex) {
                scan.nextLine();
            }

            // si la saisie n'est pas une option valide, un message d'erreur est affiché
            if(saisie != 1 && saisie != 2 && saisie != 3 && saisie != 4) {
                System.out.println("La saisie ne semble pas correcte. Veuillez saisir votre demande à nouveau :");
            }

        } while(saisie != 1 && saisie != 2 && saisie != 3 && saisie != 4);

        // saisie le la ville
        String ligne = null;
        if(saisie != 4) {
            System.out.println("Veuillez maintenant indiquer le nom du fichier contenant votre ville :");
            ligne = scan.next();
        }

        switch(saisie) {

            case 1:
                gogolS.gogol_car_s car_s = new gogolS.gogol_car_s();
                car_s.calculItineraire(ligne);
                break;
            case 2:
                gogol_l.gogol_car_l car_l = new gogol_l.gogol_car_l();
                car_l.calculItineraire(ligne);
                break;
            case 3:
                gogol_xl.gogol_car_xl car_xl = new gogol_xl.gogol_car_xl();
                car_xl.calculItineraire(ligne);
                break;
            default:
                break;
        }

    }



}
