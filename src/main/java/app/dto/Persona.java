package app.dto;

public class Persona {
    // persona ID
   private String personaID; //PARTIAL KEY

   // persona image
   private String image;
   private String name;
   private String description;
   private String needs;
   private String goals;
   private String skills;
   /**
    * Create a Persona and set the fields
    */
   public Persona(String personaID, String image, String name, String description,String needs, String goals, String skills) {
      this.personaID = personaID;
      this.image = image;
      this.name = name;
      this.description = description;
      this.needs = needs;
      this.goals = goals;
      this.skills = skills;
   }

   public String getpersonaID() {
      return personaID;
   }

   public String getImage() {
      return image;
   }

   public String getname() {
    return name;
 }

 public String getdescription() {
    return description;
 }

 public String getneeds() {
    return needs;
 }

 public String getgoals() {
    return goals;
 }

 public String getskills() {
    return skills;
 }

 
}
