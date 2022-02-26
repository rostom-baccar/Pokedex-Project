/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresql;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author auria
 */
public class Ajout {
     
//Pokémon    
private static int hauteur, poids, pv, stade, id_type1, id_type2, id_pokemon;
private static String nom, description;
private static boolean evolution;

//Attaque
private static int id_typeA;
private static String nomA, descriptionA;

//Avoir
private static int idPok, idAtt;


private static String AjoutSQL;

    
    public static void ajouter (Statement stmt, int idP, int idA) {              //todo: rajouter un while pour pouvoir ajouter plusieurs choses
                                                               
                                                                
     System.out.println("Souhaitez-vous ajouter un Pokémon (1) ou une Attaque (2)?");
     int reponse =Lire.i();
     
     if (reponse==1){
         System.out.println("Quel est le nom de ce Pokémon?");
         nom = Lire.S();
         System.out.println("Combien mesure-t-il (en centimètres)?");
         hauteur = Lire.i();
         System.out.println("Combien pèse-t-il (en grammes)?");
         poids = Lire.i();
         System.out.println("Quel est son PV ?");
         pv = Lire.i();
         System.out.println("Quel est son stade d'évolution?");
         stade = Lire.i();
         System.out.println("Le Pokémon peut-il encore évoluer? (y or n)");
         char evo = Lire.c();
         if (evo == 'y'){
             evolution = true;
         } else if (evo == 'n'){
             evolution = false;
         }
         System.out.println("Combien de type a ce Pokémon?");
         int nbType = Lire.i();
         if (nbType==1){
             System.out.println("De quel type est le nouveau Pokémon? Normal, Feu, Eau, Plante, Electrik, Insecte, Roche, Sol, Acier, Poison, Combat, Spectre, Tenebre, Psy, Glace, Dragon, Vol");
             String idType1=Lire.S();
             id_type1=getIdType(idType1);
             id_type2=0;
         } else if (nbType==2){
             System.out.println("Quel est le type majoritaire du nouveau Pokémon? Normal, Feu, Eau, Plante, Electrik, Insecte, Roche, Sol, Acier, Poison, Combat, Spectre, Tenebre, Psy, Glace, Dragon, Vol");
             String idType1=Lire.S();
             id_type1=getIdType(idType1);
             System.out.println("Quel est le second type du Pokémon? Normal, Feu, Eau, Plante, Electrik, Insecte, Roche, Sol, Acier, Poison, Combat, Spectre, Tenebre, Psy, Glace, Dragon, Vol");
             String idType2=Lire.S();
             id_type2=getIdType(idType2);
         }
         System.out.println("Le Pokémon est-il l'évolution d'un autre Pokémon? (y or n)");
         char evoPokemon = Lire.c();
         if (evoPokemon == 'y'){
             System.out.println("Quel est le nom de ce Pokémon?");
             String nomPokEvo=Lire.S();
             id_pokemon = 1;                                             //TODO get id du pokémon ayant le nom nomPokEvo 
         } else if (evoPokemon == 'n'){
             id_pokemon = 0;
         }
         System.out.println("Souhaitez-vous ajouter une description du Pokémon? (y or n)");
         char descrip = Lire.c();
         if (descrip == 'y'){
             System.out.println("Ecrivez cette description:");
             description=Lire.S(); 
         } else if (descrip == 'n'){
             description = " ";
         }
         AjoutSQL = ajouterPokemon(idP, nom, hauteur, poids, pv, stade, evolution, id_type1, id_type2, id_pokemon, description);
         try {
             stmt.executeUpdate(AjoutSQL);
         } catch (Exception e){
                  
         }
     } else if (reponse==2){
         System.out.println("Quel est le nom de cette nouvelle attaque?");
         nomA = Lire.S();
         System.out.println("De quel type est l'attaque? Normal, Feu, Eau, Plante, Electrik, Insecte, Roche, Sol, Acier, Poison, Combat, Spectre, Tenebre, Psy, Glace, Dragon, Vol");
         String idType=Lire.S();
         id_typeA=getIdType(idType);
         System.out.println("Souhaitez-vous ajouter une description du Pokémon? (y or n)");
         char descripA = Lire.c();
         if (descripA == 'y'){
             System.out.println("Ecrivez cette description:");
             descriptionA=Lire.S();
         } else if (descripA == 'n'){
             descriptionA = "";
         }
         AjoutSQL= ajouterAttaque(idA, nomA, id_typeA, descriptionA);
         try {
             stmt.executeUpdate(AjoutSQL);
         } catch (Exception e){
                  
         }
     } else {
         System.out.println("Le pokédex n'a pas compris votre souhait, veuillez réessayer.");         
     }
}
   
    public static String ajouterPokemon (int id, String nom, int hauteur, int poids, int pv, int stade, boolean evolution, int id_type1, int id_type2, int id_pokemon, String description){
        String sql = "INSERT INTO Pokemon (id,nom,hauteur,poids,pv,stade,evolution,id_type1,id_type2,id_pokemon,description) "
                     + "VALUES ("+id+",'"+nom+"',"+hauteur+","+poids+","+pv+","+stade+",'"+evolution+"',"+id_type1+","+id_type2+","+id_pokemon+",'"+description+"');";
        return sql;
    }
    
    public static String ajouterAttaque(int id, String nom, int idType, String description){
        String sql="INSERT INTO Attaque (id, nom, id_type, description)"+ "VALUES ("+id+",'"+nom+"',"+idType+",'"+description+"');";
        return sql;
    }
    
    public static void ajouterAvoir(PostgreSQL bd, Statement stmt){
        try{
            System.out.println("Souhaitez-vous attribuer des attaques à un Pokémon? (y or n)");
            char avoir = Lire.c();
            if (avoir == 'y'){
                int av = 1;
                while (av==1){
                    System.out.println("Quel est le nom du Pokémon?");
                    String nomPok = Lire.S();
                    ResultSet idPoke = bd.execReq( "SELECT id FROM pokemon WHERE pokemon.nom like '"+nomPok+"';" );
                    while ( idPoke.next() ) {
                        idPok = idPoke.getInt("id");
                    }
                    System.out.println("Quel est le nom de l'attaque?");
                    String nomAtt=Lire.S();
                    ResultSet idAtta = bd.execReq( "SELECT id FROM attaque WHERE attaque.nom like '"+nomAtt+"';" );
                    while ( idAtta.next() ) {
                       idAtt = idAtta.getInt("id");
                    }
                 
                    String sql="INSERT INTO Avoir (id_pokemon, id_attaque)"+ "VALUES ("+idPok+","+idAtt+"');";
                    stmt.executeUpdate(sql);
                    System.out.println("Voulez-vous réitérer l'opération? (y or n)");
                    char x =Lire.c();
                    if (x == 'y'){
                        av=1;
                    } else {
                        av=0;
                    }
                }
            } else if (avoir == 'n'){
                //il ne se passe rien, on continue le programme
            } 
        } catch (Exception e){   
        }
    }

    public static int getIdType (String type){
        int id=0;
        if (type.equalsIgnoreCase("Normal")){
            id=1;
        } else if (type.equalsIgnoreCase("Feu")){
            id=2;
        } else if (type.equalsIgnoreCase("Eau")){
            id=3;
        } else if (type.equalsIgnoreCase("Plante")){
            id=4;
        } else if (type.equalsIgnoreCase("Electrik")){
            id=5;
        } else if (type.equalsIgnoreCase("Insecte")){
            id=6;
        } else if (type.equalsIgnoreCase("Roche")){
            id=7;
        } else if (type.equalsIgnoreCase("Sol")){
            id=8;
        } else if (type.equalsIgnoreCase("Acier")){
            id=9;
        } else if (type.equalsIgnoreCase("Poison")){
            id=10;
        } else if (type.equalsIgnoreCase("Combat")){
            id=11;
        } else if (type.equalsIgnoreCase("Spectre")){
            id=12;
        } else if (type.equalsIgnoreCase("Tenebre")){
            id=13;
        } else if (type.equalsIgnoreCase("Psy")){
            id=14;
        } else if (type.equalsIgnoreCase("Glace")){
            id=15;
        } else if (type.equalsIgnoreCase("Dragon")){
            id=16;
        } else if (type.equalsIgnoreCase("Vol")){
            id=17;
        } 
        return (id);
    }

}


