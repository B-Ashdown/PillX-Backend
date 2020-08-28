package Rever.PillX.Medicine;


import java.util.List;

public abstract class AbsMedicine {

    //Add new administration methods here
    public enum AdministrationMethods {
        PILL, INTRAVENOUS
    }

    public String austR;
    public String name;
    public String description;
    public String dosageDescription;
    public Medicine.AdministrationMethods routeOfAdministration;
    public String sideEffects;

    public Dosage recommendedDosage;
    public List<String> ingredients;
    public List<String> drugInteractions;

    public AbsMedicine() {}

    public AbsMedicine(String austR) {
        this.austR = austR;
    }

    public AbsMedicine(Medicine medicine) {
        this.name = medicine.name;
        this.description = medicine.description;
        this.dosageDescription = medicine.dosageDescription;
        this.routeOfAdministration = medicine.routeOfAdministration;
        this.sideEffects = medicine.sideEffects;
        this.recommendedDosage = medicine.recommendedDosage;
        this.ingredients = medicine.ingredients;
        this.drugInteractions = medicine.drugInteractions;
    }
}