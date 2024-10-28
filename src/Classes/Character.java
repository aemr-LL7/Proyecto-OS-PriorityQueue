/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author andre
 */
public class Character {

    private String id;
    private int health_pts;
    private int strength_pts;
    private int agility_pts;
    private boolean skills;  // Verdadero si sus habilidades son de calidad, Falso caso contrario
    private int prio_level;
    private int starvation_counter; // Para incrementar prioridad en momentos de inanicion
    private String series; // "Star Wars" o "Star Trek"

    public Character(String id, String series) {
        this.id = id;
        this.series = series;
        this.health_pts = this.generateHealthPoints();
        this.strength_pts = this.generateStrengthPoints();
        this.agility_pts = this.generateAgilityPoints();
        this.skills = this.generateSkills();
        this.prio_level = this.determinePriority();
        this.starvation_counter = 0;
    }

    public void printCHRAttribs() {
        System.out.println("Character ID: " + this.getId());
        System.out.println("Health Points: " + this.getHealth_pts());
        System.out.println("Strength: " + this.getStrength_pts());
        System.out.println("Agility: " + this.getAgility_pts());
        System.out.println("Has Skills: " + (this.isSkills() ? "Yes" : "No"));
        System.out.println("Priority Level: " + this.getPrio_level());
        System.out.println("-----------------------------");
    }

    // Metodos para generar atributos del pj, basado en las probs mencionadas en el enunciado
    private int generateHealthPoints() {
        double randomValue = Math.random();
        if (randomValue < 0.7) { // 70% de probabilidad de ser de calidad
            return (int) (Math.random() * 106) + 95; // Rango: 95-200
        } else {
            return (int) (Math.random() * 106) + 95; // rango min que sea 95
        }
    }

    private int generateStrengthPoints() {
        double randomValue = Math.random();
        if (randomValue < 0.5) { // 50% de probabilidad de ser de calidad
            return (int) (Math.random() * 116) + 35; // Rango: 35-150
        } else {
            return (int) (Math.random() * 30) + 20; // Este rango es mas bajo para asegurar la calidad
        }
    }

    private int generateAgilityPoints() {
        double randomValue = Math.random();
        if (randomValue < 0.4) { // 40% de probabilidad de ser de calidad
            return (int) (Math.random() * 131) + 30; // Rango: 30-160
        } else {
            return (int) (Math.random() * 10) + 10; // Min de 10 para asegurar que no sea demasiado bajo
        }
    }

    private boolean generateSkills() {
        return Math.random() < 0.6; // 60% de probabilidad de que las habilidades sea de calidad
    }

    // Nivel de prioridad bnasado en los atributos unicos de cada personaje
    private int determinePriority() {
        int qualityCount = 0;

        if (this.getHealth_pts() >= 100) { // Rango de salud: 100-200
            qualityCount++;
        }
        if (this.getStrength_pts() >= 50) { // Rango de fuerza: 50-150
            qualityCount++;
        }
        if (this.getAgility_pts() >= 30) { // Rango de agilidad: 30-160
            qualityCount++;
        }
        if (this.isSkills()) { // Verifica tiene habilidades
            qualityCount++;
        }

        // Nivel de prioridad
        if (qualityCount >= 3) {
            return 1; // Exceptional
        } else if (qualityCount == 2) {
            return 2; // Average
        } else {
            return 3; // Deficient
        }
    }

    public void incrementStarvationCounter() {
        this.setStarvation_counter(this.getStarvation_counter() + 1);
    }

    public void resetStarvationCounter() {
        this.setStarvation_counter(0);
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
     * @return the health_pts
     */
    public int getHealth_pts() {
        return health_pts;
    }

    /**
     * @param health_pts the health_pts to set
     */
    public void setHealth_pts(int health_pts) {
        this.health_pts = health_pts;
    }

    /**
     * @return the strength_pts
     */
    public int getStrength_pts() {
        return strength_pts;
    }

    /**
     * @param strength_pts the strength_pts to set
     */
    public void setStrength_pts(int strength_pts) {
        this.strength_pts = strength_pts;
    }

    /**
     * @return the agility_pts
     */
    public int getAgility_pts() {
        return agility_pts;
    }

    /**
     * @param agility_pts the agility_pts to set
     */
    public void setAgility_pts(int agility_pts) {
        this.agility_pts = agility_pts;
    }

    /**
     * @return the skills
     */
    public boolean isSkills() {
        return skills;
    }

    /**
     * @param skills the skills to set
     */
    public void setSkills(boolean skills) {
        this.skills = skills;
    }

    /**
     * @return the prio_level
     */
    public int getPrio_level() {
        return prio_level;
    }

    /**
     * @param prio_level the prio_level to set
     */
    public void setPrio_level(int prio_level) {
        this.prio_level = prio_level;
    }

    /**
     * @return the starvation_counter
     */
    public int getStarvation_counter() {
        return starvation_counter;
    }

    /**
     * @param starvation_counter the starvation_counter to set
     */
    public void setStarvation_counter(int starvation_counter) {
        this.starvation_counter = starvation_counter;
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
    public void setSeries(String series) {
        this.series = series;
    }

}
