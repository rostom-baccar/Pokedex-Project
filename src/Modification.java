package a1;
 
import java.sql.Statement;
import java.sql.ResultSet;
 
//remarques:
//les majuscules et minuscules dans la saisie ne posent pas problème
//un contrôle de saisie est implémenté pour toutes les questions pour que le Pokedex comprenne le choix de l'utilisateur
//une vérification d'existence de pokémons et d'attaques dans la bdd est implémentée
 
public class Suppression {
 
    private static String SuppressionSQL, UpdateSQL, reponse, reponseG;
    private static String nomP, nomA, nom;
    private static String String_evolution;
    private static boolean boolean_evolution, bAvoir1, bAvoir2, bAttaque1, bAttaque2, bPokemon1, bPokemon2, b1, b2, booleanreponse1, booleanreponse2;
    public static final String[] t = {"", ""};
    private static int id, id_pokemon;
 
 
    //fonction qui renvoie un String avec la 1ère lettre en majuscule et le reste en minuscule
    //son rôle est de faire correspondre ce que l'utilisateur saisit à la façon dont c'est écrit dans la bdd
    public static String nomcorrect(String ch) {
        //problème
        String ch2;
        if (ch.charAt(2) == '-') {
            ch2 = "Ho-Oh";
        } else {
            char C = Character.toUpperCase(ch.charAt(0));
            String ch1 = ch.substring(1).toLowerCase();
            ch2 = C + ch1;
        }
        return ch2;
    }
 
 
    //fonction supprimer générale où l'utilisateur choisit ce qu'il veut supprimer
    public static void supprimer(Statement stmt, PostgreSQL bd) {
        int reponse;
        do {
            do {
                System.out.println("Souhaitez-vous supprimer un Pokémon (1) ou une Attaque (2), ou une attaque d'un Pokémon (3) ?");
                reponse = Lire.i();
                if (reponse == 1) {
                    suppressionPokemon(stmt, bd);
 
                } else if (reponse == 2) {
                    suppressionAttaque(stmt, bd);
                } else if (reponse == 3) {
                    suppressionAvoir(stmt, bd);
                } else {
                    System.out.println("Le Pokedex n'a pas compris votre choix, veullez répondre par 1, 2 ou 3.");
                }
                //On ne sort de la boucle while que si l'utilisateur répond non à "voulez-vous supprimer quelque chose"
                //si l'utilisateur répond par autre chose que oui ou non, on ne sort pas de la sous-boucle de vérification de réponseG
                do {
                    System.out.println("Voulez-vous également supprimer autre chose ? Répondre par oui ou non");
                    reponseG = Lire.S().toUpperCase();
                    booleanreponse1 = reponseG.equals("OUI");
                    booleanreponse2 = reponseG.equals("NON");
                    if (booleanreponse1 == false && booleanreponse2 == false) {
                        System.out.println("Le Pokedex n'a pas compris ce que vous avez dit");
                    }
                } while (booleanreponse1 == false && booleanreponse2 == false);
            } while (booleanreponse1);
 
        } while (reponse != 1 && reponse != 2 && reponse != 3);
        System.out.println("Vos suppressions ont contribué au bon fonctionnement de notre Pokedex, merci !");
    }
 
 
//fonction de suppression de pokémon
    public static void suppressionPokemon(Statement stmt, PostgreSQL bd) {
 
        do {
            System.out.println("Quel est le nom du Pokémon que vous voulez supprimer?");
            int i = 0;
            //boucle servant à vérifier l'existance d'un pokémon dans la bdd
            do {
                nomP = nomcorrect(Lire.S());
                try {
                    //on récupère l'id du pokémon qui existe potentiellement dans la bdd
                    ResultSet nom_set = bd.execReq("SELECT id FROM pokemon WHERE nom = '" + nomP + "' ");
                    while (nom_set.next()) {
                        id = nom_set.getInt("id");
                        i++;
                    }
                    nom_set.close();
                } catch (Exception e) {
                }
                if (i == 0) { //si i n'a pas été incrémenté, la variable id n'a rien reçu, donc le pokémon n'existe pas
                    System.out.println("Ce Pokémon n'existe pas dans notre base de données, veuillez saisir un Pokémon existant");
                }
            } while (i == 0); //on sort de la boucle si on trouve le pokémon en question, çad si i est différent de 0
            System.out.println("Ce Pokémon existe bien dans notre base de données");
            try { //on récupère la valeur booléenne de l'attribut evolution du pokémon (s'il peut évoluer ou non=)
                ResultSet evo = bd.execReq("SELECT evolution FROM pokemon WHERE nom='" + nomP + "';");
                while (evo.next()) { //boucle servant à vérifier si un pokémon donné a une ou des évolutions
                    String_evolution = evo.getString("evolution");
                }
                if (String_evolution.equals("t")) {
                    boolean_evolution = true;
                }
 
                evo.close();
            } catch (Exception e) {
 
            }
 
            String reponse = "";
            if (boolean_evolution) {
                boolean t1, t2;
                do { //si le pokémon a une ou des évolutions, cette proposition apparait, s'il répond oui le pokémon et sa/ses
                    //évolutionss sont supprimées
                    System.out.println("Voulez-vous également supprimer toutes les évolutions de ce Pokémon? Répondre par oui ou non");
                    reponse = Lire.S().toUpperCase();
                    t1 = reponse.equals("OUI");
                    t2 = reponse.equals("NON");
                    if (t1 == false && t2 == false) {
                        System.out.println("Le Pokedex n'a pas compris ce que vous avez dit");
                    }
                } while (t1 == false && t2 == false);
            }
            //en revanche, si le pokémon n'a pas d'évolution, ou si l'utilisateur a répondu non à la proposition précédente 
            //de vouloir supprimer la ou les évolutions, on ne supprime que le pokémon en question
            if (boolean_evolution == false || reponse.equals("OUI")) {
                try {
                    //on supprime le pokémon en question
                    SuppressionSQL = supprimerPokemon(nomP);
                    stmt.executeUpdate(SuppressionSQL);
                    //on supprime également le fait qu'il ait eu des attaques dans la table avoir
                    //en effet si un pokémon n'existe plus dans la bdd il ne peut plus avoir d'attaques
                    //attention il ne s'agit pas de supprimer les attaques qu'il avait mais bien le fait qu'il les avait
                    SuppressionSQL = supprimerAvoir1(nomP, "pokemon");
                    stmt.executeUpdate(SuppressionSQL);
 
                } catch (Exception e) {
 
                }
                /*si l'utilisateur ne veut pas supprimer les évolutions du pokémon en question, on supprime simplement
                 *le pokémon en question par l'intermédiaire de la fonction update_id_pokemon :
                 *cette fonction attribue la valeur null à l'attribut id_pokemon de la version élvoluée du pokémon en question
                 *en effet, s'il vaut null, cela voudrait dire que le pokémon n'est plus l'évolution d'aucun pokémon
                 *on peut alors supprimer le pokémon de départ sans cascade sur sa/ses évolutions
                 *id_pokemon est différente de null si le pokémon a un antécédent, çad s'il est l'évolution d'un pokémon */
            } else if (reponse.equals("NON")) {
 
                try {
                    //récupération de l'id de la version évoluée du pokémon en question
                    ResultSet id_set = bd.execReq("SELECT id FROM pokemon WHERE id_pokemon =( SELECT id FROM pokemon WHERE nom = '" + nomP + "') ");
                    while (id_set.next()) {
                        id = id_set.getInt("id");
                    }
                    id_set.close();
                } catch (Exception e) {
                }
 
                try {
                    UpdateSQL = update_id_pokemon(id); //fonction expliquée dans ce qui précède
                    stmt.executeUpdate(UpdateSQL);
 
                    SuppressionSQL = supprimerPokemon(nomP);
                    stmt.executeUpdate(SuppressionSQL);
                    SuppressionSQL = supprimerAvoir1(nomP, "pokemon");
                    stmt.executeUpdate(SuppressionSQL);
 
                } catch (Exception e) {
 
                }
 
            }
            System.out.println("Le Pokémon " + nomP + " a été supprimé avec succès");
            do {
                System.out.println("Voulez-vous reitérer le processus ? Répondre par oui ou non");
                reponse = Lire.S();
                bPokemon1 = reponse.toUpperCase().equals("OUI");
                bPokemon2 = reponse.toUpperCase().equals("NON");
                if (bPokemon1 == false && bPokemon2 == false) {
                    System.out.println("Le Pokedex n'a pas compris votre choix");
                }
            } while (bPokemon1 == false && bPokemon2 == false);
        } while (bPokemon1);
 
    }
 
 
//fonction de suppression d'attaque
    public static void suppressionAttaque(Statement stmt, PostgreSQL bd) {
        do {
            System.out.println("Quel est le nom de l'attaque que vous voulez supprimer?");
            int i = 0;
            do {
                nomA = nomcorrect(Lire.S());
                try {
 
                    ResultSet nom_set = bd.execReq("SELECT id FROM attaque WHERE nom = '" + nomA + "' ");
                    while (nom_set.next()) {
                        id = nom_set.getInt("id");
                        i++;
                    }
                    nom_set.close();
                } catch (Exception e) {
                }
                if (i == 0) {
                    System.out.println("Cette attaque n'existe pas dans notre base de données, veuillez saisir une attaque existante");
                }
            } while (i == 0);
            System.out.println("Cette attaque existe bien dans notre base de données");
            try {
 
                SuppressionSQL = supprimerAttaque(nomA);
                stmt.executeUpdate(SuppressionSQL);
 
                SuppressionSQL = supprimerAvoir1(nomA, "attaque");
                stmt.executeUpdate(SuppressionSQL);
 
            } catch (Exception e) {
 
            }
            System.out.println("L'attaque " + nomA + " a été supprimée avec succès");
            do {
                System.out.println("Voulez-vous reitérer le processus ? Répondre par oui ou non");
                reponse = Lire.S();
                bAttaque1 = reponse.toUpperCase().equals("OUI");
                bAttaque2 = reponse.toUpperCase().equals("NON");
                if (bAttaque1 == false && bAttaque2 == false) {
                    System.out.println("Le Pokedex n'a pas compris votre choix");
                }
            } while (bAttaque1 == false && bAttaque2 == false);
        } while (bAttaque1);
    }
 
 
//fonction de suppression de possession d'un pokémon d'une attaque / association d'une attaque avec un pokémon
    public static void suppressionAvoir(Statement stmt, PostgreSQL bd) {
        do {
            System.out.println("Quel est le nom du Pokémon dont vous voulez supprimer l'attaque ?");
            int i = 0;
            do {
 
                nomP = nomcorrect(Lire.S());
 
                try {
 
                    ResultSet nom_set = bd.execReq("SELECT id FROM pokemon WHERE nom = '" + nomP + "' ");
 
                    while (nom_set.next()) {
 
                        id = nom_set.getInt("id");
                        i++;
                    }
                    nom_set.close();
                } catch (Exception e) {
                }
                if (i == 0) {
                    System.out.println("Ce Pokémon n'existe pas dans notre base de données, veuillez saisir un Pokémon existant");
                }
            } while (i == 0);
            System.out.println("Ce Pokémon existe bien dans notre base de données");
 
            System.out.println(nomP + " a les attaques suivantes : ");
            try {
                ResultSet nom_set = bd.execReq("SELECT nom FROM attaque WHERE id in( SELECT id_attaque FROM avoir WHERE id_pokemon ="
                        + "( SELECT id FROM pokemon WHERE nom =  '" + nomP + "' ) ) ");
                i = -1;
                while (nom_set.next()) {
                    nom = nom_set.getString("nom");
                    i++;
                    t[i] = nom; //le tableau t à deux cases maximum se remplit si nom reçoit une valeur qui est automatiquement stockée
                    //si nom reçoit un deuxième nom d'attaque, i est incrémenté pour stocker la deuxième attaque également
                    System.out.println(nom);
                }
                nom_set.close();
            } catch (Exception e) {
            }
            do {
                System.out.println("Quelle est le nom de l'attaque que vous voulez supprimer ?");
                nomA = nomcorrect(Lire.S());
                System.out.println("Vous avez saisi : "+nomA);
                b1 = nomA.equals(nomcorrect(t[0]));
                if (t[1].length()>0){b2 = nomA.equals(nomcorrect(t[1]));};
                if (b1 == false && b2 == false) {
                    System.out.println(nomP + " n'a pas cette attaque, ses attaques sont : " + t[0] + " , " + t[1]);
                }
            } while (b1 == false && b2 == false);
 
            try {
                SuppressionSQL = supprimerAvoir2(nomP, nomA);
                stmt.executeUpdate(SuppressionSQL);
            } catch (Exception e) {
 
            }
            System.out.println("L'attaque " + nomA + " de " + nomP + " a été supprimée avec succès");
            do {
                System.out.println("Voulez-vous reitérer le processus ? Répondre par oui ou non");
                reponse = Lire.S();
                bAvoir1 = reponse.toUpperCase().equals("OUI");
                bAvoir2 = reponse.toUpperCase().equals("NON");
                if (bAvoir1 == false && bAvoir2 == false) {
                    System.out.println("Le Pokedex n'a pas compris votre choix");
                }
            } while (bAvoir1 == false && bAvoir2 == false);
        } while (bAvoir1);
 
        try {
 
        } catch (Exception e) {
 
        }
    }
 
 
//fonction qui renvoie la requête pour supprimer un pokémon donné
    public static String supprimerPokemon(String nom) {
        String sql = "DELETE FROM pokemon WHERE nom = '" + nom + "';";
        return sql;
    }
 
 
//fonction qui renvoie la requête pour supprimer une attaque donnée
    public static String supprimerAttaque(String nom) {
        String sql = "DELETE FROM attaque WHERE nom = '" + nom + "';";
        return sql;
    }
 
 
//fonction qui renvoie la requête pour supprimer le fait qu'un pokémon ait eu une attaque si ce pokémon est supprimé 
//OU pour supprimer le fait qu'une attaque ait été eue par un pokémon si cette attaque est supprimée
//si le pokémon est supprimé toutes les lignes qui lui associaient une attaque dans la table avoir son supprimées
//si le l'attaque est supprimée toutes les lignes qui lui associaient un pokémon dans la table avoir son supprimées
    public static String supprimerAvoir1(String nom, String type_supression) {
        String sql = "DELETE FROM avoir WHERE id_" + type_supression + " =( SELECT id FROM " + type_supression
                + " WHERE nom = '" + nom + "') ;";
        return sql;
    }
 
 
//fonction qui renvoie la requête pour supprimer le fait qu'un pokémon spécifique aie une attaque spécifique   
    public static String supprimerAvoir2(String nomP, String nomA) {
        String sql = "DELETE FROM avoir WHERE id_pokemon = ( SELECT id FROM pokemon WHERE nom = '" + nomP + "' )  AND "
                + " id_attaque = ( SELECT id FROM attaque WHERE nom = '" + nomA + "' ) ";
        return sql;
    }
 
 
//fonction qui renvoie la requête pour associer la valeur null à l'attribut id_pokemon d'un pokémon donné
    public static String update_id_pokemon(int id) {
        String sql = "UPDATE pokemon SET id_pokemon = null WHERE id = '" + id + "'";
        return sql;
    }
 
}