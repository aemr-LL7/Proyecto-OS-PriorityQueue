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
    private String name;

    private int health_pts;
    private int strength_pts;
    private int block_factor;

    private int agilityModifier;//Extra a la iniciativa por calidad
    private int attackModifier;// Extra al ataque por calidad
    private int defenceModifier;//Extra al bloqueo por calidad

    private String skills;  // Verdadero si sus habilidades son de calidad, Falso caso contrario
    private int prio_level; //Prioridad actual del personaje
    private final int quality; //calidad del personaje: excelente-0, promedio-1, malo-2, 4-FAGSHIP(sin implementar). La calidad determina los stats, la prioridad cuando pelean.
    private int starvation_counter; // Para incrementar prioridad en momentos de inanicion
    private String series; // "Star Wars" o "Star Trek"

    //Las peleas se definen en 4 rondas, el que tenga mas puntos al final es el ganador, sin importar si mato o no al otro. Introducir mecanicas de criticos y de muerte.
    public Character(String id, String name, String series, int quality) {
        this.id = id;
        this.series = series;
        this.name = name;
        this.quality = quality;

        //generamos stats base
        this.health_pts = this.generateHealthPoints();
        this.strength_pts = this.generateStrengthPoints();
        this.block_factor = this.generateBlockFactor();
        this.agilityModifier = this.generateAgilityModifier();

        this.prio_level = this.determinePriority(); //generamos la prio para los modificadores

        //generamos modificadores
        this.attackModifier = this.generatAttackModifier();
        this.defenceModifier = this.generateDefenceModifier();

        this.skills = this.generateSkills();
        this.prio_level = this.determinePriority();
        this.starvation_counter = 0;
    }

    //Las peleas se definen en 4 rondas, el que tenga mas puntos al final es el ganador, sin importar si mato o no al otro. Introducir mecanicas de criticos y de muerte.
    public Character(String id, String name, String series) {
        this.id = id;
        this.series = series;
        this.name = name;
        this.quality = -1;

        //generamos stats base
        this.health_pts = this.generateHealthPoints();
        this.strength_pts = this.generateStrengthPoints();
        this.block_factor = this.generateBlockFactor();
        this.agilityModifier = this.generateAgilityModifier();

        this.prio_level = this.determinePriority(); //generamos la prio para los modificadores

        //generamos modificadores
        this.attackModifier = this.generatAttackModifier();
        this.defenceModifier = this.generateDefenceModifier();

        this.skills = this.generateSkills();
        this.prio_level = this.determinePriority();
        this.starvation_counter = 0;
    }

    public void printCHRAttribs() {
        System.out.println("Character ID: " + this.getId());
        System.out.println("Health Points: " + this.getHealth_pts());
        System.out.println("Strength: " + this.getStrength_pts());
        System.out.println("Agility: " + this.getAgilityModifier());
        System.out.println("Has Skill: " + (this.skills));
        System.out.println("Priority Level: " + this.getPrio_level());
        System.out.println("-----------------------------");
    }

    // Metodos para generar atributos del pj, basado en las probs mencionadas en el enunciado
    private int generateHealthPoints() {
        int baseHp = (int) (Math.random() * 106);
        int hpQualityProb = (int) (Math.random() * 101);

        if (this.quality == 0) {
            return (int) (baseHp + 95);//de 95 a 200
        } else if (this.quality == 1) {
            return (int) (baseHp + 75);//de 75 a 180
        } else if (this.quality == 2) {
            return (int) (baseHp + 55);//de 55 a 160
        } else if (this.quality == -1) {
            if (hpQualityProb < 70) {//70% de ser de calidad
                return (int) ((baseHp + 95));
            } else if (hpQualityProb < 85) { //15% de ser normal
                return (int) ((baseHp + 75));
            } else if (hpQualityProb < 100) { //15% de ser maloS
                return (int) ((baseHp + 55));
            }
        }

        return 0;
    }

    private int generateStrengthPoints() {
        int baseAtt = (int) (Math.random() * 51);
        int strPointsQualityProb = (int) (Math.random() * 100);//50% de prob de ser de calidad

        if (this.quality == 0) {
            return (int) (baseAtt + 50); //de 50 a 100
        } else if (this.quality == 1) {
            return (int) (baseAtt + 40); //de 40 a 100
        } else if (this.quality == 2) {
            return (int) (baseAtt + 30); //de 30 a 100
        } else if (this.quality == -1) {
            if (strPointsQualityProb < 50) {
                return (int) ((baseAtt + 50));
            } else if (strPointsQualityProb < 75) { //25% de ser normal
                return (int) ((baseAtt + 40));
            } else if (strPointsQualityProb < 100) { //25% de ser malo
                return (int) ((baseAtt + 30));
            }
        }

        return 0;

    }

    private int generateAgilityModifier() {

        int agiModifierProb = (int) (Math.random() * 100);

        //speed, con esta se define quien va primero: 1d6+AgilityPoints
        if (this.quality == 0) {
            return (int) (Math.random() * 6) + 2;
        } else if (this.quality == 1) {
            return (int) (Math.random() * 6) + 1;
        } else if (this.quality == 2) {
            return (int) (Math.random() * 6) + 0;
        } else if (this.quality == -1) {
            if (agiModifierProb < 40) { //40% de ser de calidad
                return (int) (Math.random() * 6) + 2;
            } else if (agiModifierProb < 70) { //30% de ser normal
                return (int) (Math.random() * 6) + 1;
            } else if (agiModifierProb < 100) { //30% de ser malo
                return (int) (Math.random() * 6) + 0;
            }
        }

        return 0;

    }

    private int generatAttackModifier() {

        if (this.quality == 0 || this.getPrio_level() == 0) {
            return (int) (Math.random() * 6) + 2;
        } else if (this.quality == 1 || this.getPrio_level() == 1) {
            return (int) (Math.random() * 6) + 1;
        } else if (this.quality == 2 || this.getPrio_level() == 2) {
            return (int) (Math.random() * 6) + 0;
        }

        return 0;
    }

    private int generateDefenceModifier() {

        if (this.quality == 0 || this.getPrio_level() == 0) {
            return (int) (Math.random() * 6) + 2;
        } else if (this.quality == 1 || this.getPrio_level() == 1) {
            return (int) (Math.random() * 6) + 1;
        } else if (this.quality == 2 || this.getPrio_level() == 2) {
            return (int) (Math.random() * 6) + 0;
        }

        return 0;
    }

    private int generateBlockFactor() {
        //Este es el dano que se niega siempre por la defenza del personaje.

        if (this.quality == 0) {
            return (int) (Math.random() * 51); //de 0 a 50
        } else if (this.quality == 1) {
            return (int) (int) (Math.random() * 41); //de 0 a 40
        } else if (this.quality == 2) {
            return (int) (int) (Math.random() * 31); //de 0 a 30
        }

        return 0;

    }

    private String generateSkills() {
        int skillsProb = (int) (Math.random() * 100); // 60% de probabilidad de que las habilidades sea de calidad

        if (skillsProb < 60) {
            int chosenSkill = (int) (Math.random() * 6);
            if (chosenSkill < 2) {
                return "Critical Strike"; //El personaje tiene una probabilidad de eliminar al enemigo de 1 golper
            } else if (chosenSkill < 4) {
                return "Frotitude"; //El personaje tiene una probabilidad de bloquear un ataque al 100%
            } else if (chosenSkill < 6) {
                return "Bleeding"; //El personaje inflige un extra de 10% de la vida del enemigo como danyo adicional cada ronda
            }
        }
        return "No";
    }
    // Nivel de prioridad bnasado en los atributos unicos de cada personaje

    private int determinePriority() {
        int qualityCount = 0;

        if (this.getHealth_pts() >= 128) { // Rango de salud: 100-200
            qualityCount++;
        }

        if (this.getStrength_pts() >= 65) { // Rango de fuerza: 50-150
            qualityCount++;
        }

        if (this.getBlock_factor() >= 20) {
            qualityCount++;
        }

        if (this.getAgilityModifier() >= 5.5) { // Rango de agilidad: 30-160
            qualityCount++;
        }

        if (this.getAttackModifier() >= 5.5) {
            qualityCount++;
        }

        if (this.getDefenceModifier() >= 5.5) {
            qualityCount++;
        }

//        if (this.isSkills()) { // Verifica tiene habilidades
//            qualityCount++;
//        }
        // Nivel de prioridad
        if (qualityCount >= 3) {
            return 0; // Exceptional
        } else if (qualityCount == 2) {
            return 1; // Average
        } else if (qualityCount < 2) {
            return 2; // Deficient
        }

        return 2;
    }

    public int rollInitiative(){//simula 2d6 + el modificador de agilidad
        int roll_1 = (int) (Math.random() * 6) +1;
        int roll_2 = (int) (Math.random() * 6) +1;
        return roll_1 + roll_2 + this.agilityModifier;
    }
    
    public int rollAttack(){//simula 2d6 + el modificador de fuerza
        int roll_1 = (int) (Math.random() * 6) +1;
        int roll_2 = (int) (Math.random() * 6) +1;
        return roll_1 + roll_2 + this.attackModifier;
    }

    public int rollDefence(){//simula 2d6 + el modificador de defenza
        int roll_1 = (int) (Math.random() * 6) +1;
        int roll_2 = (int) (Math.random() * 6) +1;
        return roll_1 + roll_2 + this.defenceModifier;
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
     * @return the agilityModifier
     */
    public int getAgilityModifier() {
        return agilityModifier;
    }

    /**
     * @param agility_pts the agilityModifier to set
     */
    public void setAgilityModifier(int agility_pts) {
        this.agilityModifier = agility_pts;
    }

    /**
     * @param skills the skills to set
     */
    public void setSkills(String skills) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuality() {
        return quality;
    }

    public int getBlock_factor() {
        return block_factor;
    }

    public void setBlock_factor(int block_factor) {
        this.block_factor = block_factor;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public int getDefenceModifier() {
        return defenceModifier;
    }

    public void setDefenceModifier(int defenceModifier) {
        this.defenceModifier = defenceModifier;
    }

    private int generateRandomQuality() {
        int qualityModifier = (int) (Math.random() * 101);

        if (qualityModifier >= 70) {
            return 0;
        } else if (qualityModifier < 70 && qualityModifier >= 50) {
            return 1;
        } else if (qualityModifier < 50) {
            return 2;
        }

        return 2;
    }

}
