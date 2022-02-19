/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresql;

import java.sql.Statement;

/**
 *
 * @author auria
 */
public class Accueil {
    
    private static String mdp;
    
    public static void Accueil (Statement stmt, PostgreSQL bd, int idPokemon, int idAttaque) { 
         System.out.println("Bonjour et Bienvenue dans le Pokédex, vous y trouverez toutes les informations que vous souhaitez sur les Pokémon");
         System.out.println("Voulez-vous vous connecter en mode Dresseur, mode Scientifique ou mode Invité?");
         String mode=Lire.S();
         if (mode.equalsIgnoreCase("Dresseur")){
            System.out.println("Veuillez entrer votre Mot de Passe de Dresseur:");
            mdp = Lire.S();
            if (mdp.equals("dresseur00")){
                System.out.println("Bienvenue dans le mode Dresseur, vous pouvez consulter toutes les informations présentes dans le Pokédex et en "
                        + "ajouter lorsque vous trouvez de nouveaux Pokémons ou apprenez de nouvelles attaques à vos Pokémon");
                Ajout.ajouter(stmt, idPokemon, idAttaque);    
                Ajout.ajouterAvoir(bd, stmt);
                //consulter des infos
            } else {
                System.out.println ("Mot de Passe incorrect. L'accès vous est refusé.");
            }
        } else if (mode.equalsIgnoreCase("Scientifique")){
            System.out.println("Veuillez entrer votre Mot de Passe de Scientifique:");
            mdp = Lire.S();
            if (mdp.equals("scient00")){
                System.out.println("Bienvenue dans le mode Scientifique, vous êtes autorisé à faire toutes les modifications que vous souhaitez et à "
                        + "consulter toutes les informations présentes dans le Pokédex");
                Ajout.ajouter(stmt, idPokemon, idAttaque);         
                Ajout.ajouterAvoir(bd, stmt);
                //suppression, modification ou consulter des infos
            } else {
                System.out.println ("Mot de Passe incorrect. L'accès vous est refusé.");
            }
        } else if (mode.equalsIgnoreCase("Invité")){
            System.out.println("Bienvenue dans le mode Invité, vous pouvez consulter toutes les informations présentes dans le Pokédex");
            //affichage des infos
        }
     }
}
