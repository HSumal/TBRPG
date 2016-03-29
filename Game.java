/**

 * Comptuer Science Culminating >> Game2.java
 * Text based role playing game
 * @author: Ashwin Kamalakannan, Harpreet Singh Sumal
 * Start: 01/01/2014
 * Last edited: 15/01/2014
 */

/* Description:
 * ===============
 * This is a text based, role playing game engine
 * uses an update system, meaning it has multiple functions and constantly changing data
 * This data is altered in the methods, returned and used to complete tasks such: calculate damage,
 * experience, generate enemies, etc.
 * The update system then takes this data, uses it, and then re initializes it
 * This is the basic format of a game engine and we optimized ours to be used for a role playing game.
 * The game story is used to represent growth and advancement
 * Represented by the expert classes all being real characters from anime
 * These characters have their own peronalities and are designed to match yours
 * This is based on the desicions you make in game such as which advanced class you pick
 */

//                               **UPDATES**
//=================================================================================
// public static variables do not change between methods
// enemies now generate past level 5
// clean user interface: window boxes added to simulate graphics
//                       skip and skipLine method wipes previous text in window for better format
//                       status system: health bar, mana bar, and enemy health bar 						 
// stat[0] = Attack , stat[1] = Defense , stat[2] = Magic , stat[3] = Speed
// potionAmount[0] = health potion, potionAmount[1] = mana potion, potionAmount[2] = money
// damage calculator added, enemy damage added
// stun added, DOT damage added, ultimate skill added
// story complete, player skill names 0-5 complete
// advanced class, expert class complete
// potion use in battle, money changed to potionAmount[3] instead of int playerMoney
// Difficulties added: noob, noble, heroic, legendary
// difficulty setting increases enemy stats 

import java.util.Scanner;

class Game 
{
  public static boolean game = true;
  
  // This method is used as a shorcut for user input
  public static String userInput()                                                 // allows for user input
  {
    String choice;
    Scanner choiceIn = new Scanner(System.in);
    return choiceIn.nextLine();
  }
  
  // these methods are used for formatting and structuring our program in a neat way
  public static void skipLine()                                                    // prompts user to press <enter> before continuing
  {
	  
    System.out.println("  <Press Enter to Continue>  ");
    
    for (int i = 0; i <= 6; i++)
    {
      System.out.println("");
    }
    
    userInput(); 
  }
  public static void skip()
  {
    for (int i = 0; i <= 10; i++)
    {
      System.out.println("");
    }
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // This method shows the players stats and is updated every time a battle ends
  public static void showStats(String character, int[] stat, int playerHealth, int playerMana, int playerLevel, int playerExp, int[] potionAmount )  // Shows player statistics
  { 
    int charAttack = stat[0], charDefense = stat[1], charMagic = stat[2], charSpeed = stat[3];
    int expTotal = (playerLevel + 1) * 60; 
    
    System.out.println("checking stats...");
    skipLine();
    
    System.out.println("      The " + character);
    System.out.println("Health: " + playerHealth + "    |    Mana: " + playerMana);
    System.out.println(" ============================== ");
    System.out.println("| Attack: " + charAttack + "       Defense: " + charDefense + " |");
    System.out.println("|                              |");
    System.out.println("| Magic: " + charMagic +  "        Speed: " + charSpeed + "   |");
    System.out.println(" ==============================");
    System.out.print("Level: " + playerLevel + "(" + playerExp + "/" + expTotal +")");
    System.out.println("  Money: $" + potionAmount[2]);
    System.out.print("" + potionAmount[0] + " health potion(s) |");
    System.out.println("  " + potionAmount[1] + " mana potions(s)");
    skipLine();
    skip();
  }
  
  // This method acts as an instance, and is used to purchase items which are then usable by the player 
  public static int[] store(int potionAmount[])                                                               // Store
  {
    
    while (true)                                                                         
    {
      
      String purchase = "";
      Scanner purchaseIn = new Scanner(System.in);
      Scanner itemAmountIn = new Scanner(System.in);
      
      System.out.println("Welcome to the traveling Hermes Vendor!");
      skipLine();
      skip();
      
      while (true)
      {
        int itemAmount = 0;
        
        System.out.println("  ========================================================= "); // store interface
        System.out.println(" | Available Items:                                        |");
        System.out.println(" |    Health Potion ($100)             Mana Potion ($50)   |");
        System.out.println(" |                                                         |");
        System.out.println(" |   <enter to leave>                                      |");
        System.out.println("  ========================================================= ");
        System.out.println("  Your Money: " + potionAmount[2]                            );
        purchase = purchaseIn.nextLine();
        skip();
        skip();
        
        if (purchase.equals("health potion"))                                                         
        {
          
          System.out.println("How many do you want?"); // picks # of potions
          itemAmount = itemAmountIn.nextInt();
          skip();
          if (potionAmount[2] - (100*itemAmount) < 0)
          {
            System.out.println("You don't have enough money for that");
            skipLine();
            continue;
          }
          System.out.println("You bought " + itemAmount + " health potion(s)");
          skipLine();
          potionAmount[0] += itemAmount;
          potionAmount[2] -= (100*itemAmount);
          skip();
          return potionAmount;
        }
        else if (purchase.equals("mana potion"))
        {
          System.out.println("How many do you want?"); // picks # of potions
          itemAmount = itemAmountIn.nextInt();
          skip();
          if (potionAmount[2] - (50*itemAmount) < 0)
          {
            System.out.println("You don't have enough money for that");
            skipLine();
            continue;
          }
          System.out.println("You bought " + itemAmount + " mana potion(s)");
          skipLine();
          potionAmount[1] += itemAmount;
          potionAmount[2] = potionAmount[2] - (50*itemAmount);
          skip();
          return potionAmount;
        }
        else
        {
          skip();
          System.out.println("Do you still want to buy something?"); // default option
          purchase = purchaseIn.nextLine();
          skip();
          if (purchase.equals("yes"))
          {
            skip();
            continue;
          }
          else
          {
            skip();
            return potionAmount;
          }
        }
      }
      // add other potions here if neccesary 
    }
  } 
  
  // this method calculates damage done by player on the enemy
  public static int damageCalculator(int[] stat, int baseDamage)                   // Player Damage Calculator
  {
    int finalDamage = 0; // initialize at 0 and change later in calculation
    int attack = stat[0]; // required for basic, stun, and ultimate
    int magic = stat[2]; // required for dot, stun and ultimate
    int speed = stat[3]; // required for dot, basic
    // defense not required for damage calculations
    
      switch (baseDamage) {
        case 20: finalDamage = baseDamage/50 + attack + speed; // basic attack damage
        break;
        case 10: finalDamage = baseDamage/50 + magic+ speed; // DOT damage
        break;
        case 5: finalDamage = baseDamage/50 + attack + magic; // stun damage
        break;
        case 50: finalDamage = baseDamage/50 + attack + magic + speed; // finisher damage
        break;
      }
        
    return finalDamage;
  }
  
  public static String[] playerTechniques(String character) // initiallizes skills for each class
  {
    String[] techniques = new String[4];
    
    ////////////////////////////////////////////////////////    Warrior
    if (character.equals("warrior")) // basic class
    {
    	techniques[0] = "heroic strike";
        techniques[1] = "rend";
        techniques[2] = "sparta kick";
        techniques[3] = "decapitate";
    }
    else if (character.equals("guardian")) // advanced class
    {
    	techniques[0] = "energy blast";
        techniques[1] = "honor strike";
        techniques[2] = "knock out";
        techniques[3] = "holy force";	
    }
    else if (character.equals("conquerer"))
    {
    	techniques[0] = "energy blast";
        techniques[1] = "cruel strike";
        techniques[2] = "blind";
        techniques[3] = "destructive force";	
    }
    else if (character.equals("Super Saiyan, Goku")) // expert class
    {
    	techniques[0] = "kamehameha";
        techniques[1] = "spirit barrage";
        techniques[2] = "dragons fist";
        techniques[3] = "spirit bomb";	
    }
    else if (character.equals("Super Saiyan, Vegeta"))
    {
    	techniques[0] = "galick gun";
        techniques[1] = "dirty fire works";
        techniques[2] = "solar flare";
        techniques[3] = "big bang attack";	
    }
    ///////////////////////////////////////////////////////////     Mage 
    else if (character.equals("mage"))
    {
    	techniques[0] = "ice shards";
        techniques[1] = "scorch";
        techniques[2] = "explosive blast";
        techniques[3] = "lightning storm";
    }
    else if (character.equals("alchemist"))
    {
    	techniques[0] = "bomb";
        techniques[1] = "poison wave";
        techniques[2] = "paralysis spell";
        techniques[3] = "biochemical warfare";	
    }
    else if (character.equals("heartless"))
    {
    	techniques[0] = "suffocate";
        techniques[1] = "create void";
        techniques[2] = "shape shift";
        techniques[3] = "dark matter";	
    }
    else if (character.equals("Alchemist, Edward Elric"))
    {
    	techniques[0] = "spear attack";
        techniques[1] = "impale";
        techniques[2] = "deconstruct";
        techniques[3] = "call: alphonse elric";	
    }
    else if (character.equals("Keyblade Wielder, Riku"))
    {
    	techniques[0] = "keyblade slash";
        techniques[1] = "enveloping darkbess";
        techniques[2] = "sonic blade";
        techniques[3] = "ultimate possession";	
    }
    ///////////////////////////////////////////////////////////   Rogue
    else if (character.equals("rogue")) 
    {
  	  techniques[0] = "sinister strike";
      techniques[1] = "rupture";
      techniques[2] = "feint";
      techniques[3] = "killling spree";
    }
    else if (character.equals("shinobi"))
    {
    	techniques[0] = "deadly blow";
        techniques[1] = "spike trap";
        techniques[2] = "stealth attack";
        techniques[3] = "kunai barrage";	
    }
    else if (character.equals("soul reaper"))
    {
    	techniques[0] = "downward slash";
        techniques[1] = "soul energy";
        techniques[2] = "teleport";
        techniques[3] = "bankai";	
    }
    else if (character.equals("Hokage, Naruto Uzumaki"))
    {
    	techniques[0] = "shadow clone";
        techniques[1] = "rasenshuriken";
        techniques[2] = "odama rasengan";
        techniques[3] = "kyubi mode";	
    }
    else if (character.equals("Vasto Lorde, Ichigo Kurosaki"))
    {
    	techniques[0] = "getsuga tensho";
        techniques[1] = "cero";
        techniques[2] = "flash step";
        techniques[3] = "final getsuga tensho";	
    }
    ///////////////////////////////////////////////////////////   Nomad 
    else if (character.equals("nomad"))
    {
      techniques[0] = "jab";
      techniques[1] = "tiger rend";
      techniques[2] = "ox strike";
      techniques[3] = "spiritual reckoning";
    }
    else if (character.equals("shaman"))
    {
    	techniques[0] = "power arrow";
        techniques[1] = "deep burn";
        techniques[2] = "deadly beserk";
        techniques[3] = "inner release";	
    }
    else if (character.equals("deserter"))
    {
    	techniques[0] = "shockwave";
        techniques[1] = "fire blast";
        techniques[2] = "beast forme";
        techniques[3] = "spirit armour";	
    }
    else if (character.equals("Avatar, Aang"))
    {
    	techniques[0] = "air sphere";
        techniques[1] = "dragon fire";
        techniques[2] = "tsunami";
        techniques[3] = "avatar surge";	
    }
    else if (character.equals("Avenger, Sasuke Uchiha"))
    {
    	techniques[0] = "chidori";
        techniques[1] = "amaterasu";
        techniques[2] = "kirin";
        techniques[3] = "susano";	
    }
    ///////////////////////////////////////////////////////////
      
    
    return techniques;
  }
  
  // uses player level and experience, and produces a Randomly generated enemy
  public static String enemyGenerator(int playerLevel, int playerXp)            // Random enemy Generator
  {
    String enemyName = "error < sorry we're stupid> "; // if generator fails
    
    if (playerLevel <= 5) // Mobs level 0 - 5
    {
      // tutorial
      if (playerXp == 0)
      {
        story("pre battle"); 
        enemyName = "Mindless Zombie";
      }
      
      else if (playerXp < 60 && playerXp >= 10) // lvl 0-1
      {
        double rand = (int)(Math.floor(Math.random()*4 + 1)); // random number between 1-5
        
        if (rand == 1)
          enemyName = "Flame Imp";
        else if (rand == 2)
          enemyName = "Risen Corpse";
        else if (rand == 3)
          enemyName = "Black Worm";
        else if (rand == 4)
          enemyName = "Angry Lizard";
        else if (rand == 5)
          enemyName = "Tiny Bat";
      }
      
      else if (playerLevel == 1 && playerXp == 60) // fury miniboss lvl 1
      {
        story("fury"); // Story for Fury
        enemyName = "The Last Fury";
      }
      
      else if (playerXp < 180 && playerXp >= 70) // lvl 1-3
      {
        double rand = (int)(Math.floor(Math.random()*10 + 1));
        
        if (rand == 1)
          enemyName = "Mighty Zombie";
        if (rand == 2)
          enemyName = "Horrendous Fiend";
        if (rand == 3)
          enemyName = "Ancient Sentry";
        if (rand == 4)
          enemyName = "Phantom";
        if (rand == 5)
          enemyName = "Fire Elemental";
        if (rand == 6)
          enemyName = "Wraith";
        if (rand == 7)
          enemyName = "Gargoyle";
        if (rand == 8)
          enemyName = "Barb Scorpion";
        if (rand == 9)
          enemyName = "Tauros Demon";
        if (rand == 10)
          enemyName = "Demonic Serpent";
      }
      
      else if (playerLevel == 3 && playerXp == 180) // cerberus miniboss lvl 3
      {
        story("cerberus"); //Story for Cerberus
        enemyName = "Legendary Three Headed Dog, Cerberus";
      }
      
      else if (playerXp <= 290 && playerXp >= 180) // lvl 3-5
      {
        double rand = (int)(Math.floor(Math.random()*10 + 1));
        
        if (rand == 1)
          enemyName = "Lich";
        if (rand == 2)
          enemyName = "Night Stalker";
        if (rand == 3)
          enemyName = "Gorgons";
        if (rand == 4)
          enemyName = "Lynx";
        if (rand == 5)
          enemyName = "Plagued Orc";
        if (rand == 6)
          enemyName = "Demon Raider";
        if (rand == 7)
          enemyName = "Tower Guardian";
        if (rand == 8)
          enemyName = "Cave Ogre";
        if (rand == 9)
          enemyName = "Hydra";
        if (rand == 10)
          enemyName = "Undead Minion";
      }
      
      else if (playerLevel == 5 && playerXp == 300) // lvl 5 Main Boss Charon
      {
        story("charon");
        enemyName = "Charon, Ferryman of the Underworld";
      }
    }
    
    
    if (playerLevel >= 5 && playerLevel <= 8) // Mobs level 5 - 8
    {
      if (playerXp >= 300 && playerXp <= 410 )  // lvl 5-7
      {
        double rand = (int)(Math.floor(Math.random()*10 + 1));
        
        if (rand == 1)
          enemyName = "Drake Warrior";
        if (rand == 2)
          enemyName = "Shadow Ninja";
        if (rand == 3)
          enemyName = "Giant Spider";
        if (rand == 4)
          enemyName = "Giant Scorpion";
        if (rand == 5)
          enemyName = "Wandering Hollow";
        if (rand == 6)
          enemyName = "Dark Centaur";
        if (rand == 7)
          enemyName = "Undead Cyclops";
        if (rand == 8)
          enemyName = "Dark Aura";
        if (rand == 9)
          enemyName = "Nightmare Hound";
        if (rand == 10)
          enemyName = "Magma Crawler";
      }
      
      else if (playerLevel == 7 && playerXp == 420) // Achillies mini boss lvl 7
      {
        story("achilles");
        enemyName ="Tortured Spirit of Achillies"; 
      }
      
      else if (playerXp >= 420 && playerXp <= 470) // lvl 7-8
      {
        double rand = (int)(Math.floor(Math.random()*10 + 1));
        
        if (rand == 1)
          enemyName = "Dark Knight";
        if (rand == 2)
          enemyName = "Death reaper";
        if (rand == 3)
          enemyName = "Magma Berserker";
        if (rand == 4)
          enemyName = "Fallen Automaton";
        if (rand == 5)
          enemyName = "Chaos Witch";
        if (rand == 6)
          enemyName = "Chaos Knight";
        if (rand == 7)
          enemyName = "Necromancer";
        if (rand == 8)
          enemyName = "Brimstone Warrior";
        if (rand == 9)
          enemyName = "Shadow Dragon";
        if (rand == 10)
          enemyName = "Fire Angel";
      }
      
      else if (playerLevel == 8 && playerXp == 480) // lvl 8 Main Boss HadesElshan Bieber
      {
        story("hades");
        enemyName = "Hades, God of the Underworld";
      } 
    }
    
    if (playerLevel >= 8 && playerXp >= 490)
    { 
      if (playerXp == 490) // explains tartarus
        story("tartarus");
      
      if (playerXp > 480 && playerXp <= 590 && playerLevel <= 10 )  // lvl 8-10
      {
        if (playerXp == 490)
          enemyName = "Fire Lord, Ozai";
        if (playerXp == 500)
          enemyName = "Defieler of God, Homunculus";
        if (playerXp == 510)
          enemyName = "Possesser of souls, Xemnes";
        if (playerXp == 520)
          enemyName = "";
        if (playerXp == 530)
          enemyName = "";
        if (playerXp == 540)
          enemyName = "";
        if (playerXp == 550)
          enemyName = "Traiter of the UnderWorld, Captain Aizan";
        if (playerXp == 560)
          enemyName = "Legendary Super Saiyan, Broly";
        if (playerXp == 570)
          enemyName = "";
        if (playerXp == 580)
          enemyName = "Bringer of Destruction, Madara Uchiha";
        if (playerXp == 590)
        {
          story("chronos");
          enemyName = "Lord Chronos, Governer of Time, Harbinger of the End Times";
        }
      }
      
      else if (playerLevel == 10 && playerXp == 600)
      {
        story("tartarusBoss");
        story("Tartarus, the Primevil God of Darkness and Death");  // Final Boss
      }
    }
    
    return enemyName;
  }
  
  public static int statCheck (String character, int[] stat)// Confirm character setup
  {
    String check = "yes";
    int conf = 0;
    int charAttack = stat[0];
    int charDefense = stat[1];
    int charMagic = stat[2];
    int charSpeed = stat[3];
    
    Scanner checkIn = new Scanner(System.in);
    
    while (conf == 0)
    {
      System.out.println("");
      System.out.println("  stats for the " + character);
      System.out.println(" ============================== ");
      System.out.println("| Attack: " + charAttack + "       Defense: " + charDefense + " |");
      System.out.println("|                              |");
      System.out.println("| Magic: " + charMagic +  "        Speed: " + charSpeed + "   |");
      System.out.println(" ==============================");
      
      skipLine();
      
      System.out.println("Is this selection ok?");
      check = userInput();
      skip();
      
      if (check.equals("yes"))     // returns 1 > (yes)
      {
        conf = 1;
      }
      else if (check.equals("no")) // returns 0 > (no)
      {
        conf = 0;
        break;
      }
      else                         // invalid input returns nothing> (repeat)
      {
        continue;
      }
      
    } 
    
    return conf;
  }
  
  public static int[] statSelect(String character)                                       // intialize character stats
  {
    int charAttack = 10, charDefense = 10, charMagic = 10, charSpeed = 10;
    int[] stat = new int [4];
    
    if (character.equals("warrior"))
    {
      charAttack = charAttack + 10;
      charDefense = charDefense + 5;
      charMagic = charMagic + 0;
      charSpeed = charSpeed + 2;
    }
    else if (character.equals("rogue"))
    {
      charAttack = charAttack + 5;
      charDefense = charDefense + 0;
      charMagic = charMagic + 2;
      charSpeed = charSpeed + 10;
      
    }
    else if (character.equals("nomad"))
    {
      charAttack+=2;
      charDefense = charDefense + 10;
      charMagic = charMagic + 5;
      charSpeed = charSpeed + 0;
      
    }
    else if(character.equals("mage"))
    {
      charAttack = charAttack + 0;
      charDefense = charDefense + 2;
      charMagic = charMagic + 10;
      charSpeed = charSpeed + 5;
    }
    
    stat[0] = charAttack;
    stat[1] = charDefense;
    stat[2] = charMagic;
    stat[3] = charSpeed;
    
    return stat;
    
  }
  
  // At level 5, we give the user the opportunity to pick an advanced class
  // This advanced class will determine their new skill set and some basic stats
  // This will also determine which expert class they are given once they reach lvl 10
  public static String advancedClass(String character)  
  {
	  System.out.println("Please choose an advanced class");
	  skipLine();
	  skip();
	  
	  if (character.equals("warrior"))
	  {
		  System.out.println(" ======================================= ");
	      System.out.println("|  Guardian              Conquerer      |");
	      System.out.println(" ======================================= ");
		  character = userInput();
	  }
	  if (character.equals("mage"))
	  {
		  System.out.println(" ======================================= ");
	      System.out.println("|  Alchemist             Heartless      |");
	      System.out.println(" ======================================= ");
		  character = userInput();
	  }
	  if (character.equals("rogue"))
	  {
		  System.out.println(" ======================================= ");
	      System.out.println("|  Shinobi               Soul Reaper    |");
	      System.out.println(" ======================================= ");
		  character = userInput();
	  }
	  if (character.equals("nomad"))
	  {
		  System.out.println(" ======================================= ");
	      System.out.println("|  Shaman                Deserter       |");
	      System.out.println(" ======================================= ");
		  character = userInput();
	  }
	  
	  return character;
  }
  
  // determines expert classes based on previously selected advanced class
  public static String expertClass(String character)  
  {
	  // warrior expert classes
	  if (character.equals("guardian"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("Protecter of the Universe, Super Saiyan Goku");
		  character = "Super Saiyan, Goku";
	  }
	  if (character.equals("conquerer"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("Destroyer of Worlds, Super Saiyan Vegeta");
		  character = "Super Saiyan, Vegeta";
	  }
	  // mage expert classes
	  if (character.equals("alchemist"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("The Fullmetal Alchemist, Edward Elric");
		  character = "Alchemist, Edward Elric";
	  }
	  if (character.equals("heartless"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("Legendary Keyblade Weilder, Riku");
		  character = "Keyblade Wielder, Riku";
	  }
	  // rogue expert classes
	  if (character.equals("shinobi"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("The Greatest Hokage, Naruto Uzumaki!");
		  character = "Hokage, Naruto Uzumaki";
	  }
	  if (character.equals("soul reaper"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("The Almighty Vasto Lorde, Ichigo Kurosaki");
		  character = "Vasto Lorde, Ichigo Kurosaki";
	  }
	  // nomad expert classes
	  if (character.equals("shaman"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("The Last Air Bender, Avatar Aang");
		  character = "Avatar Aang";
	  }
	  if (character.equals("deserter"))
	  {
		  System.out.println("Your character has now become..");
		  skipLine();
		  skip();
		  System.out.println("Avenger Of Clans, Sasuke Uchiha");
		  character = "Avenger, Sasuke Uchiha";
	  }
	  
	  
	  return character;
  }
  
  public static String classSelect()                                                  // choose a class
  {
    String character;
    
    while (true) 
    {
      System.out.println(" ======================================= ");
      System.out.println("|  Choose a class to play as?           |");
      System.out.println("|                                       |");
      System.out.println("|   Warrior               Rogue         |");
      System.out.println("|                                       |");
      System.out.println("|   Nomad                 Mage          |");
      System.out.println(" ======================================= ");
      
      character = userInput();
      String charName = character;
      
      if (character.equals("warrior"))
        return character; 
      else if (character.equals("rogue"))
        return character;
      else if (character.equals("nomad"))
        return character;
      else if (character.equals("mage"))
        return character;
      else
      {
        skip();
        continue;
      }
    }
    
  } 
  
  public static void className(String character)         // shows user what class they chose during pre-game
  {
	  System.out.println("Ahh, I see you have chosen the " + character); // gets character from main method
      System.out.println("Good luck young padawon,");
      System.out.println("and may the force be with you.");
      skipLine();
      skip();
  }
  
  // Allows user to choose difficulty rating
  // from easiest being noob, to hardest being legendary
  public static String chooseDifficulty()
  {
	  String difficulty = "";
	  
	  System.out.println("!!Please always type in lowerCase!!");
	  skipLine();
	  skip();
	  
	  while(difficulty.equals(""))
	  {
	      System.out.println("      Choose a Difficulty             ");    //  Difficulties
	      System.out.println(" ==================================== ");
	      System.out.println("    Noob             Noble            ");
	      System.out.println("");
	      System.out.println("    Heroic           Legendary        ");
	      System.out.println(" ==================================== ");
	      
	      difficulty = userInput();
	      
	      if (difficulty.equals("noob"))
	          return difficulty; 
	      else if (difficulty.equals("noble"))
	          return difficulty;
	      else if (difficulty.equals("heroic"))
	          return difficulty;
	      else if (difficulty.equals("legendary"))
	          return difficulty;
	      else
	      {
	    	  difficulty = "";
	          skip();
	      }	  
	  }
	  
	  return difficulty;
	  
  }
  // The story() method is used as a transitioning function
  // We explain the basics of the game and guide the player through as they progress
  public static void story(String stage)                                                                      // story
  { 
    if(stage .equals ("pregame"))
    {
      System.out.println("WARNING PLEASE ALWAYS DO THE FOLLOWING");
      System.out.println("======================================");
      System.out.println("<Enter to Continue> means you should click the enter key on your ");
      System.out.println("keyboard to continue");
      System.out.println("");
      skipLine();
      skip();
      System.out.println("The following game is created and written by");
      System.out.println("Ashwin, and Harpreet. Here is a run-down of some of the basic");
      System.out.println("controls and command you will need to familiarize yourself with");
      System.out.println("to play this game.");
      skipLine();
      skip();
      System.out.println("The game is played solely through user input, so to get through");
      System.out.println("the game you will need a keyboard and basic typing skills.");
      skipLine();
      skip();
      System.out.println("!!Please refrain from inputting something not on the screen!!");
      System.out.println("==============================================================");
      skipLine();
      skip();
      System.out.println("Here are a few of the basic inputs you will be asked to ");
      System.out.println("complete: ");
      skipLine();
      skip();
      System.out.println("When choosing an option, always type out the word itself, and ");
      System.out.println("not the number");
      skipLine();
      skip();
      System.out.println("Please type in all lower case");
      skipLine();
      skip();
      System.out.println("And most importantly… Remember to have fun!!");
      System.out.println("=============================================");
      skipLine();
      skip();
      System.out.println("Now here’s a test, please enter to continue!");
      skipLine();
      skip();
    }
    
    if(stage.equals ("intro"))
    {
      System.out.println("Why hello there young one, you’re finally awake.");
      System.out.println("Easy there, don’t be in a rush.");
      System.out.println("You’re in the Underworld, the place your spirit goes to be testified before death…");
      skipLine();
      skip();
      System.out.println("However, there is a way for you to cheat death, it being a very dangerous thing indeed.");
      System.out.println("To do so you must travel the vast expanse of the underworld,");
      System.out.println("defeating many monstrosities and miscreation’s that may linger here,");
      System.out.println("and then finally best the almighty Hades, God of the Underworld, in a duel to the death!");
      skipLine();
      skip();
      System.out.println("When you are prepared to set off,");
      System.out.println("please choose a set of gear to take with you,");
      System.out.println("each helping a specific type of person… What type are you?");
      skipLine();
      skip();
    }
    
    if(stage .equals ("warrior"))
    {
      System.out.println("The Warrior, a strong and noble fighter who wields a sword");
      System.out.println("and shield to defeat his foes. With a mixture of strong offence ");
      System.out.println("and defense the warrior is a tough foe to best. Lacking only ");
      System.out.println("in magic, a warrior has no need for such peasantries when he has his trustworthy sword!");
      skipLine();
      skip();
    }
    else if(stage .equals ("mage"))
    {
      System.out.println("The Mage, a wise wizard who uses the elements to his advantage.");
      System.out.println("Able to control Fire, Water, Wind, and Earth elements,");
      System.out.println("the Mage is a deadly and intelligent foe who can best even the mightiest of foes.");
      System.out.println("Lacking only in attack, the Mage has no need for such brutish ideals,");
      System.out.println("when he can use the elements, and magic to his aid!");
      skipLine();
      skip();
        
    }
    else if(stage .equals ("rogue"))
    {
      System.out.println("The Rogue, a stealthy thieve who lurks in the shadows,");
      System.out.println("stalking his pray, always ready to pounce when the time is right.");
      System.out.println("The rogue uses his speed and attack to his advantage,");
      System.out.println("to quickly overcome his enemy with a flurry of fast attacks.");
      System.out.println("Lacking only in defense, the rogue has no need for it if the enemy is already dead…");
      skipLine();
      skip();
    }
    else if(stage .equals ("nomad"))
    {
      System.out.println("The Nomad, a wandering traveler who uses the spirits to guide him in his path.");
      System.out.println("He is a strong fighter willing to put his life on the line for his comrades. ");
      System.out.println("Utilizing his big body, the Nomad always stands at the front of the battle,");
      System.out.println("taking the brunt of the blows.");
      System.out.println("Slow as a Tortuous but bulky like an Ox,");
      System.out.println("the Nomad has a time and place for everything.");
      System.out.println("The Tortuous did beat the Hare in the end.");
      skipLine();
      skip();
    }
    
    if(stage . equals ("pre battle"))
    {
      System.out.println("Your first enemy approaches.");
      System.out.println("By the looks of it, it seems to be a simple Mindless Zombie.");
      System.out.println("This shouldn’t be difficult for someone of the likes of you.");
      System.out.println("To begin the battle press enter, and choose a Technique to use.");
      System.out.println("Techniques are the unique abilities available to each class.");
      System.out.println("They also change depending on your level.");
      System.out.println("For now, try using Technique number 1,");
      System.out.println("for a simple damaging strike and go from there.");
      skipLine();
      skip();
    }
    if(stage .equals ("fury"))
    {
      System.out.println("The Last Fury has appeared!");
      System.out.println("Prepare yourself; this fury cares for no one,");
      System.out.println("rampaging everything in its path, it is an uncontrollable beast,");
      System.out.println("stopped by no one, not even Hades himself.");
      System.out.println("Be wary young one, this could be the end to your journey");
      skipLine();
      skip();
    }
    if(stage .equals ("cerberus"))
    {
      System.out.println("The Legendary Three Headed Dog, Cerberus has appeared!");
      System.out.println("Prepare yourself; he is the gatekeeper to the inner levels of hell,");
      System.out.println("one of Hades’ top lieutenants. Prepare with caution,");
      System.out.println("and be prepared to use you most powerful of Techniques!");
      skipLine();
      skip();
    }
    if(stage.equals ("charon"))
    {
      System.out.println("The Ferryman of the Night, Charon has appeared!");
      System.out.println("Prepare yourself; he is harder than all of your previous foe combined.");
      System.out.println("He may look scrawny, as he is ranked at only a gatekeeper,");
      System.out.println("however he is so, so much more.");
      System.out.println("With the power to control the rivers around him, and the knowledge of the");
      System.out.println("groundsa round him, he is ready to take your soul in a heartbeat");
      skipLine();
      skip();
    }
    if(stage.equals ("achilles"))
    {
      System.out.println("The Tortured Spirit of Achilles has appeared!");
      System.out.println("Prepare yourself, this spirit is not your average, cull of the meek spirit,");
      System.out.println("it is a deadly spirit able to slay you in a heartbeat.");
      System.out.println("Be on your guard young one, I fear this is not the worse of what’s to come.");
      skipLine();
      skip();
    }
    if(stage.equals ("hades"))
    {
      System.out.println("He has arrived!");
      System.out.println("After slaughtering countless amounts of his foes,");
      System.out.println("the coward Hades has finally decided to show himself.");
      System.out.println("He will not be an easy foe young one.");
      System.out.println("You must use everything in your arsenal if you wish to even have a chance at defeating him.");
      System.out.println("Even looking at him lowers my health.");
      System.out.println("Please young one, defeat him and free yourself form this wretched place.");
      System.out.println("Give the rest of us hope, hope that Hades has kept away from us spirits.");
      System.out.println("Share with us Pandora’s Box.");
      System.out.println("With this, perhaps more of us can come back to life,");
      System.out.println("and be able to revel in the sweetness that is life.");
      System.out.println("If not for yourself young one, for the rest of the spirit world,");
      System.out.println("just please… DEFEAT HADES!!!");
    }
    if(stage.equals ("tartarus"))
    {
      System.out.println("You have defeated Hades!!");
      System.out.println("However, Tartarus has gone to madness, he is unable to control");
      System.out.println("the countless hordes of demons that storm his body.");
      System.out.println("You must head down to Tartarus and help save Tartarus.");
      System.out.println("Tartarus has always remained neutral in the spirit word, however, he seems angered now.");
      System.out.println("I warn you that he may turn on you also, but if you don't go and help him, all will be lost!");
      System.out.println("The world will be thrown out of balance, and the Titans trapped in Tartarus will be free'd!");
      System.out.println("Go young one, go and defeat the Titans in Tartarus, for the demons we will deal with");
      skipLine();
      skip();
    }
    if(stage.equals ("chronos"))
    {
      System.out.println("The last of the Titans has come young one, go and slay him!");
      System.out.println("Beware, he uses time to his advantage.");
      System.out.println("Being able to twist time around him to his favour he can slow you down,");
      System.out.println("and speed himself up to make quick work of you,");
      System.out.println("so be on your guard, and ready for a time-lost battle!");
      skipLine();
      skip();
    }
    if(stage.equals ("tartarusBoss"))
    {
    	System.out.println("It looks like Tartarus has finally awoken!");
        System.out.println("He looks angry young one, be CARE----...");
        System.out.println("Hurry... young one... defeat Tartarus before it's... too... *Friendly Spirit Dies*");
        System.out.println("HAHAHA, come puny human, let me test your strength!");
        skipLine();
        skip();
    }
    if(stage.equals ("end"))
    {
      System.out.println("Nooo, NOOOOO----, Ugh, Puny human, I'm surprised you were even capable");
      System.out.println("enough to scratch me, let alone defeat me.");
      System.out.println("For your strength I reward you wtih the trip out of this place.");
      System.out.println("My thanks for destroying the pitiful Titans that liad hostage");
      System.out.println("in me. Farewell, and I hope to see you again young one...");
      skipLine();
      skip();
    }
    if(stage.equals("credits"))
    {
      System.out.println("This Text-Based Role Playing, Game was created by");
      System.out.println("Ashwin Kamalakannan and Harpreet Sumal!");
      skipLine();
      skip();
      System.out.println("We spent a lot of time creating this game,");
      System.out.println("polishing this game, and making it something");
      System.out.println("that people would want to play over and over again.");
      System.out.println("We chose a weird concept for the overall story,  and playout");
      System.out.println("of the game, that being Greek Mythology mixed in with Japanease Manga.");
      System.out.println("We thought this would be best for our game because, teenagers");
      System.out.println("like Japanease Manga, and Greek Mythology is something everyone knows");
      System.out.println("atleast something about. Thank you for taking the time");
      System.out.println("to play our game, and we hope you have a wonderful day! :D");
    }
  }
  
////////////////////////////////////////////////////////////////////////////////////////////////////////////// MAIN METHOD  
  public static void main (String args [])
  {
	// This is where the major variables are created and initiallized for the first time
	// Most of these variables change as they are updated in the game engine
    int gameLoss = 0;// number of times you lose a battle, 3 loss = game over
    int[] potionAmount = new int[3]; // number of potions and money
    potionAmount[2] = 200; // initiallizes player money at $200
    int healthPotion = 50, manaPotion = 30;
    int playerHealth = 100, originalHealth = 100, enemyHealth = 100, playerMana = 100;
    String[] techniques = new String[4];
    int enemyDamage = 10; // basic enemy damage
    int playerDamage, defenseFactor = 3, drainDamage = 5; // for damage calulations
    String tech1 = "", tech2 = "", tech3 = "", tech4 = "", tech5 = "", tech6 = ""; // options in battle
    int playerLevel = 0, playerExp = 0, lvlCheck = 1; // player level and experience check variable
    String enemyName; 
    
    // boosts enemy attack based on difficulty you picked
    String difficulty = chooseDifficulty();
    if (difficulty.equals("noob"))
    	enemyDamage+=0;
    else if (difficulty.equals("noble"))
    	enemyDamage = 20;
    else if (difficulty.equals("heroic"))
    	enemyDamage = 30;
    else if (difficulty.equals("legendary")) // Hardest difficulty
    	enemyDamage = 45;
    
    System.out.println(enemyDamage);
    skip();
    skip();
    
    // starts pregame and tutorial
    story("pregame");
    
    // Title screen
    System.out.println("     ***********************************   ");
    System.out.println("     ** ============================= **   ");
    System.out.println("     **|       A NIGHT IN HELL       |**   ");
    System.out.println("     ** ============================= **   ");
    System.out.println("     ***********************************   ");
    skipLine();
    skip();
    
    // story is called to act as a cutscene 
    // we use cutscences to transition through the game
    // cutscenes also help guide the user
    
    story("intro");     // starts intro
    
    // Invokes Character creation
    int check = 0; // user confirms creation
    int[] stat = new int[4]; // 4 character stats in array
    String character = null; // character name
    
    while (check == 0)
    {
      character = classSelect();
      skip();
      story(character); //character story
      
      skip();
      
      
      
      skip();
      stat = statSelect(character); // Creates array for statistical values
      check = statCheck(character, stat); // stats to check get sent as parameters
    }
    className(character); //after character chosen story
    
    int playerAttack = stat[0];
    int playerDefense = stat[1];
    int playerMagic = stat[2];
    int playerSpeed = stat[3];
    
    // This is the main update loop
    // This is where the game engine is contained
    // This is where all methods are called
    // All the variables regardless of where they are initiallized are cycled through this loop
    // As the variables come through they are re initiallized as neccessary
    
    while (game)           // Main Interface
    {
      System.out.println(" ==================================== ");
      System.out.println("|  What would you like to do?        |");
      System.out.println("|                                    |");
      System.out.println("|   Battle                Store      |");
      System.out.println("|                                    |");
      System.out.println("|   Stats                 Exit       |");
      System.out.println(" ==================================== ");
      
      String choice = userInput();
      skip();
      
      if (choice.equals("battle"))
      {
        playerMana = 100; enemyHealth = 100;      // initialize variables used for battle sequence
        playerDamage = 0;
        int baseDamage = 0, techMana = 0, drainBase = 0, stunBase = 0, coolDown = 0;
        
        enemyName = enemyGenerator(playerLevel, playerExp);
        System.out.println(enemyName + " has challenged you to combat!");
        skipLine();
        
        // initiallizes player skills by returning them from the playerTechniques method
        techniques = playerTechniques(character); // getting the player's techniques based on class and level 
        tech1 = techniques[0];                                 // saving these technique names on variables for later use 
        tech2 = techniques[1];
        tech3 = techniques[2];
        tech4 = techniques[3];
        tech5 = "health potion";
        tech6 = "mana potion";
        
        // Battle system goes inside the main method
        // This is because this is our most variable heavy function 
        // Rather than use excess methods we decided to put the smaller functions in methods
        while (enemyHealth > 0)
        {
          String select = "";
          
          // Uses basic characters to represent a status bar
          // The status bars grow and shrink depending on the the quantity of it's corresonding value
          
          System.out.print("Health: [");                                      // Shows health bar
          for (int i = 0; i <= playerHealth/10; i++)
          {
            if (playerHealth/10 - i == 1)
            {
              System.out.println("+] ");
              break;
            }
            System.out.print("+ "); 
          }
          
          System.out.print("Mana:   [");                                      // shows player mana
          for (int i = 0; i <= playerMana/10; i++)     
          {
            if (playerMana/10 - i == 1)
            {
              System.out.println("+] ");
              break;
            }
            System.out.print("+ ");
          }
          
          System.out.println(" ==================================== ");       //  Battle Interface
          System.out.println(" Choose a Skill                       ");
          System.out.println("");
          System.out.println("  " + tech1);
          System.out.println("  " + tech2);
          System.out.println("  " + tech3);
          System.out.println("  " + tech4);
          System.out.println("");
          System.out.println("  Health potion x " + potionAmount[0]);
          System.out.println("  Mana potion x " + potionAmount[1]);
          System.out.println(" ==================================== ");
          
          System.out.print("Enemy:  [");                                      // shows enemy health
          for (int i = 0; i <= enemyHealth/10; i++)     
          {
            if (enemyHealth/10 - i == 1)
            {
              System.out.println("+] ");
              break;
            }
            System.out.print("+ ");
          }
          
          select = userInput();  // choose a technique
          skip();
          
          if (select.equals(tech1)) // basic attack
          {
            baseDamage = 20;
            techMana = 10;
            if (playerMana < techMana)
            {
              System.out.println("You dont have enough mana for that");
              skipLine();
              continue;
            }
          }
          else if (select.equals(tech2)) // attack + DOT(damage over time), for 2 turn
          {
            baseDamage = 10;
            techMana = 20;
            if (playerMana < techMana)
            {
              System.out.println("You dont have enough mana for that");
              skipLine();
              continue;
            }
            drainBase = 5;
          }
          else if (select.equals(tech3)) // damage + stun for 2 turn
          {
            baseDamage = 5;
            techMana = 40;
            if (playerMana < techMana)
            {
              System.out.println("You dont have enough mana for that");
              skipLine();
              continue;
            }
            stunBase = 2;
          }
          else if (select.equals(tech4)) // finisher with 3 turn cooldown timer
          {
            baseDamage = 50;
            techMana = 50;
            if (playerMana < techMana)
            {
              System.out.println("You dont have enough mana for that");
              skipLine();
              continue;
            }
            else if (coolDown > 0)
            {
              System.out.println("That skill isn't ready yet");
              skipLine();
              continue;
            }
            coolDown = 5;
          }
          else if (select.equals(tech5))   // Health potion, grants health to player
          {
        	  if (potionAmount[0] > 0)
        	  {
        		  System.out.println("You used a health potion!"); // tells user if they used a potion or not
        		  skipLine();
        		  skip();System.out.println("It regenerated " + manaPotion + " mana points" );
        		  skipLine();
        		  skip();
        		  playerHealth = playerHealth + healthPotion;
            	  potionAmount[0]--;
            	  continue;
        	  }
        	  else
        	  {
        		  System.out.println("You don't have any health potions!");
        		  skipLine();
        		  skip();
        		  continue;
        	  }
          }
          else if (select.equals(tech6))   // Mana potion, grants mana to player 
          {
        	  if (potionAmount[0] > 0)
        	  {
        		  System.out.println("You used a Mana potion!"); // tells user if they used a potion or not
        		  skipLine();
        		  skip();
        		  System.out.println("It regenerated " + manaPotion + " mana points" );
        		  skipLine();
        		  skip();
        		  playerMana = playerMana + manaPotion;
            	  potionAmount[1]--;
            	  continue;
        	  }
        	  else
        	  {
        		  System.out.println("You don't have any Mana potions!"); 
        		  skipLine();
        		  skip();
        		  continue;
        	  }
        	  
          }
          
          else // redo (default) in case user types invalid option
            continue;
          
          playerDamage = damageCalculator(stat, baseDamage);              // calculates damage dealt by player
          enemyHealth = enemyHealth - playerDamage;                                    // deals damage to enemy
          System.out.println("You dealt " + playerDamage + " on " + enemyName);        // displays damage dealt by player
          skipLine();
          
          if (drainBase > 0) // enemy takes damage over time from DOT attacks
          {
        	System.out.println(enemyName + " took " + drainDamage + " DOT damage");
            enemyHealth = enemyHealth - drainDamage;
            drainBase--; // enemy DOT timer
            skipLine();
          }
          
          if (stunBase == 0) // enemies only attack if they're not stunned
          {
            playerHealth = playerHealth - (enemyDamage - (playerDefense/defenseFactor)); // deals damage on player
            System.out.println(enemyName + " dealt " + (enemyDamage - (playerDefense/defenseFactor)) + " on you" );
            skipLine();
          }
          else if (stunBase > 0) // if enemy is stunned they can't attack
          {
            System.out.println(enemyName + " is stunned and cannot move!");
            stunBase--;  // enemy stun timer 
            skipLine();
          }
          
          
          playerMana = playerMana - techMana; // calculates Mana depretiation
          
          coolDown--;  // player finisher cool down timer
          
          
          // it is still considered a victory if the enemy's and players health are both 0 on the same turn
          // it is not considered a victory if the eneny's health is greater than 0 no matter the condition
          if (enemyHealth <= 0) // Checks if you won 
          {
            System.out.println("You won :)");
            skipLine();
            System.out.println("you gained 10 experience");
            skipLine();
            System.out.println("you gained 50 dollars");
            skipLine();
            skip();
            playerExp+=10;
            potionAmount[2]+=50;
            playerHealth = originalHealth;
          }
          
          else if (playerHealth <= 0) // Checks if you lost
          {
            System.out.println("You Lost :(");
            skipLine();
            skip();
            playerHealth = originalHealth;
            gameLoss++;
            
            if (gameLoss == 3)    // 3 losses = Game Over 
            {
              System.out.println("You died 3 times D:");
              skipLine();
              System.out.println("You lost the game on the " + difficulty + " difficulty! ");
              skipLine();
              System.out.println("You can Try again next Time!");
              skipLine();
              skip();
              System.exit(0);  // exits game
            }
            break;
          }
          
        }
        
        
        if (playerExp/60 == lvlCheck)     // leveling system
        {
          playerLevel++;
          lvlCheck++;
          
          if (playerLevel == 5) // decreases amount your defense is factored by at lvl 5, and at lvl 8
            defenseFactor--;
          else if (playerLevel == 8)
            defenseFactor--;
          
          System.out.println("You are now level " + playerLevel + "!");
          skipLine();
          
          if (playerLevel == 5 && playerExp == 300) // advanced classes
          {
          	character = advancedClass(character);
          	drainDamage+=5;
          	playerAttack+=5;   
            playerDefense+=5;
            playerMagic+=5;
            playerSpeed+=5;
          }
          else if (playerLevel == 8 && playerExp == 480) // expert classes
          {
          	character = expertClass(character);
          	playerAttack+=5;   
            playerDefense+=5;
            playerMagic+=5;
            playerSpeed+=5;
          }
          else if (playerLevel == 10 && playerExp > 600) // This is the requirement to beat the game
          {
        	  story("end");
        	  skip();
        	  System.out.println("You Won The Game on the " + difficulty + " difficulty! ");
        	  skipLine();
        	  skip();
        	  System.out.println("Congratulations on your victory!");
        	  skipLine();
        	  skip();
        	  System.out.println("*pats on back*");
        	  skipLine();
        	  skip();
        	  story("credits");
        	  skipLine();
        	  skip();
        	  break;
          }
          
          originalHealth+=10;    // Changes stats with every increment in level
          enemyHealth+=10;
          enemyDamage+=10;
          playerAttack+=10;   
          playerDefense+=10;
          playerMagic+=10;
          playerSpeed+=10;
          potionAmount[2]+=100;
          
          int[] statNew = {playerAttack, playerDefense, playerMagic, playerSpeed};
          
          showStats(character, statNew, playerHealth, playerMana, playerLevel, playerExp, potionAmount);
          skip();
          continue;
        }
        
      }
      
      else if (choice.equals("store"))
      {
        potionAmount = store(potionAmount);
      }
      else if (choice.equals("stats"))
      {
        showStats(character, stat, playerHealth, playerMana, playerLevel, playerExp, potionAmount);
      }
      else if (choice.equals("exit"))
      {
        String exit;
        Scanner exitIn = new Scanner(System.in);
        
        skip();
        System.out.println("Are you sure you want to exit?");
        exit = exitIn.nextLine();
        skip();
        if (exit.equals("no"))
          continue;
        else if (exit.equals("yes"))
        {
          game = false;
        }
      }
    }
    
    skip();
    System.out.println("thankyou for playing! :)");
    skipLine();
    skip();
    System.exit(0);
    
  }
  
}
