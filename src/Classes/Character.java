/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author andre Critical Strike: Probabilidad de eliminar al enemigo en un
 * golpe (muy rara, 5%). Si no se activa el golpe crítico, aumenta un 45% el
 * daño. Aumenta la probabilidad de esquivar en un 20%. Fortitude: Probabilidad
 * del 30% de bloquear un ataque al 100%. Bleeding: Inflige un 10% del HP actual
 * del enemigo como daño adicional cada ronda.
 */
public class Character {

    private String id;
    private String name;
    private String series; // "Star Wars" o "Star Trek"
    private int priorityLevel; //Prioridad actual del personaje
    private int starvation_counter; // Para incrementar prioridad en momentos de inanicion
    private int wins;

    // CARACTERISTICAS
    private int skills; // Habilidades: 0 = Deficiente, 1 = De calidad
    private int health; // Puntos de vida: 0 = Deficiente, 1 = De calidad
    private int strength; // Fuerza: 0 = Deficiente, 1 = De calidad
    private int agility; // Agilidad: 0 = Deficiente, 1 = De calidad
    private String specialSkill; // Habilidad especial asignada si "skills" es de calidad
    // MODIFICADORES
    private double DEF_MODIFIER;
    private double AGY_MODIFIER;
    private double ATK_MODIFIER;
    private int block_factor;

    // IMGS
    private ImageIcon characterImage; // Atributo para la asignacion de imagenes
    private boolean isReinforced;

    //Las peleas se definen en 4 rondas, el que tenga mas puntos al final es el ganador, sin importar si mato o no al otro. Introducir mecanicas de criticos y de muerte.
    public Character(String id, String series) {
        this.id = id;
        this.name = ""; // Se puede asignar después
        this.series = series;
        this.starvation_counter = 0;
        this.wins = 0;
        this.isReinforced = false;

        // Determinar y asignar atributos
        this.skills = determineQuality(60); // 60% de probabilidad de calidad
        this.health = assignHealth();
        this.strength = assignStrength();
        this.agility = assignAgility();

        // Inicializar modificadores
        this.DEF_MODIFIER = 1.0;
        this.AGY_MODIFIER = 1.0;
        this.ATK_MODIFIER = 1.0;
        this.block_factor = 10;

        // Asignar habilidad especial si "skills" es de calidad
        if (skills == 1) {
            this.specialSkill = assignSpecialSkill();
            applySpecialModifiers();
        } else {
            this.specialSkill = "None";
        }

        // Asignar nivel de prioridad inicial basado en puntajes
        this.priorityLevel = determinePriorityLevel();

        // Imagen se asignará después, se inicializa nula
        this.characterImage = null;

    }

    // Método para imprimir los atributos del personaje
    public void printCHRAttribs() {
        System.out.println("Character ID: " + this.getId() + ", Name: " + this.getName());
        System.out.println("Studio: " + this.getSeries());
        System.out.println("Priority Level: " + this.getPriorityLevel());
        System.out.println("Starvation Counter: " + this.getStarvation_counter());
        System.out.println("Wins: " + this.getWins());
        System.out.println("Skills: " + (skills == 1 ? "Quality" : "Deficient"));
        System.out.println("Health: " + health);
        System.out.println("Strength: " + strength);
        System.out.println("Agility: " + agility);
        System.out.println("Special Skill: " + this.getSpecialSkill());
        System.out.println("Attack Modifier: " + this.getATK_MODIFIER());
        System.out.println("Defense Modifier: " + this.getDEF_MODIFIER());
        System.out.println("Agility Modifier: " + this.getAGY_MODIFIER());
        System.out.println("-----------------------------");
    }

    // Metodo para determinar si una característica es de calidad
    private int determineQuality(int probability) {
        Random random = new Random();
        return (random.nextInt(100) < probability) ? 1 : 0;
    }

    // Asignar salud con rango definido
    private int assignHealth() {
        Random random = new Random();
        int healthValue = 100 + random.nextInt(101); // Rango 100-200
        return healthValue;
    }

    // Asignar fuerza con rango definido
    private int assignStrength() {
        Random random = new Random();
        int strengthValue = 50 + random.nextInt(101); // Rango 50-150
        return strengthValue;
    }

    // Asignar agilidad con rango definido
    private int assignAgility() {
        Random random = new Random();
        int agilityValue = 30 + random.nextInt(131); // Rango 30-160
        return agilityValue;
    }

    // Método para determinar el nivel de prioridad inicial
    private int determinePriorityLevel() {
        int totalPoints = this.getHealth() + this.getStrength() + this.getAgility();

        // Determinar prioridad basado en puntos acumulados
        if (totalPoints >= 450) {
            return 1; // Excepcional
        } else if (totalPoints >= 300) {
            return 2; // Promedio
        } else {
            return 3; // Deficiente
        }
    }

    // Método para asignar habilidades especiales
    private String assignSpecialSkill() {
        Random random = new Random();
        int skillType = random.nextInt(3); // 0, 1, 2

        switch (skillType) {
            case 0:
                return "Critical Strike"; // Probabilidad de golpe crítico
            case 1:
                return "Fortitude"; // Probabilidad de bloquear ataques
            case 2:
                return "Bleeding"; // Daño adicional por ronda
            default:
                return "None";
        }
    }

    // Aplicar modificadores basados en la habilidad especial
    private void applySpecialModifiers() {
        switch (getSpecialSkill()) {
            case "Critical Strike":
                this.setATK_MODIFIER(this.getATK_MODIFIER() + 0.45); // Incrementa daño en un 45%
                this.setAGY_MODIFIER(this.getAGY_MODIFIER() + 0.15); // Incrementa probabilidad de esquivar en un 15%
                this.setBlock_factor((int) (Math.random() * 51));
                break;
            case "Fortitude":
                this.setDEF_MODIFIER(2.0); // Bloquea completamente un ataque
                this.setHealth((int) (this.getHealth() + this.getHealth() * 0.25)); // Incrementa salud en un 25%
                this.setBlock_factor((int) (Math.random() * 41));
                break;
            case "Bleeding":
                this.setATK_MODIFIER(this.getATK_MODIFIER() + 0.10); // Inflige daño adicional basado en la vida del enemigo
                this.setBlock_factor((int) (Math.random() * 31));
                break;
            default:
                // Sin modificadores
                break;
        }
    }
    
    public int rollInitiative() {//simula 2d6 + el modificador de agilidad
        int roll_1 = (int) (Math.random() * 6) + 1;
        int roll_2 = (int) (Math.random() * 6) + 1;
        return roll_1 + roll_2 + (int) this.getAGY_MODIFIER();
    }

    public int rollAttack() {//simula 2d6 + el modificador de fuerza
        int roll_1 = (int) (Math.random() * 6) + 1;
        int roll_2 = (int) (Math.random() * 6) + 1;
        return roll_1 + roll_2 + (int) this.getATK_MODIFIER();
    }

    public int rollDefence() {//simula 2d6 + el modificador de defenza
        int roll_1 = (int) (Math.random() * 6) + 1;
        int roll_2 = (int) (Math.random() * 6) + 1;
        return roll_1 + roll_2 + (int) this.getDEF_MODIFIER();
    }
    
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the series
     */
    public String getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(String newSeries) {
        this.series = newSeries;
    }

    /**
     * @return the priorityLevel
     */
    public int getPriorityLevel() {
        return priorityLevel;
    }

    /**
     * @param priorityLevel the priorityLevel to set
     */
    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    /**
     * @return the starvation_counter
     */
    public int getStarvation_counter() {
        return starvation_counter;
    }

    public void incrementStarvationCounter() {
        this.starvation_counter += 1;
    }

    public void resetStarvationCounter() {
        this.starvation_counter = 0;
    }

    /**
     * @return the wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * @param wins the wins to set
     */
    public void incrementWins() {
        this.wins += 1;
    }

    /**
     * @return the characterImage
     */
    public ImageIcon getCharacterImage() {
        return characterImage;
    }

    /**
     * @param characterImage the characterImage to set
     */
    public void setCharacterImage(ImageIcon characterImage) {
        this.characterImage = characterImage;
    }

    /**
     * @return the isReinforced
     */
    public boolean isIsReinforced() {
        return isReinforced;
    }

    /**
     * @param isReinforced the isReinforced to set
     */
    public void setIsReinforced(boolean isReinforced) {
        this.isReinforced = isReinforced;
    }

    /**
     * @return the skills
     */
    public int getSkills() {
        return skills;
    }

    /**
     * @param skills the skills to set
     */
    public void setSkills(int skills) {
        this.skills = skills;
    }

    /**
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @return the strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * @param strength the strength to set
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * @return the agility
     */
    public int getAgility() {
        return agility;
    }

    /**
     * @param agility the agility to set
     */
    public void setAgility(int agility) {
        this.agility = agility;
    }

    /**
     * @return the specialSkill
     */
    public String getSpecialSkill() {
        return specialSkill;
    }

    /**
     * @param specialSkill the specialSkill to set
     */
    public void setSpecialSkill(String specialSkill) {
        this.specialSkill = specialSkill;
    }

    /**
     * @return the DEF_MODIFIER
     */
    public double getDEF_MODIFIER() {
        return DEF_MODIFIER;
    }

    /**
     * @param DEF_MODIFIER the DEF_MODIFIER to set
     */
    public void setDEF_MODIFIER(double DEF_MODIFIER) {
        this.DEF_MODIFIER = DEF_MODIFIER;
    }

    /**
     * @return the AGY_MODIFIER
     */
    public double getAGY_MODIFIER() {
        return AGY_MODIFIER;
    }

    /**
     * @param AGY_MODIFIER the AGY_MODIFIER to set
     */
    public void setAGY_MODIFIER(double AGY_MODIFIER) {
        this.AGY_MODIFIER = AGY_MODIFIER;
    }

    /**
     * @return the ATK_MODIFIER
     */
    public double getATK_MODIFIER() {
        return ATK_MODIFIER;
    }

    /**
     * @param ATK_MODIFIER the ATK_MODIFIER to set
     */
    public void setATK_MODIFIER(double ATK_MODIFIER) {
        this.ATK_MODIFIER = ATK_MODIFIER;
    }

    /**
     * @return the block_factor
     */
    public int getBlock_factor() {
        return block_factor;
    }

    /**
     * @param block_factor the block_factor to set
     */
    public void setBlock_factor(int block_factor) {
        this.block_factor = block_factor;
    }
}
