package app.dto;

    /**
 * Class represeting a foodgrp from the Studio Project database
 *
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 */

public class Commodity {
    // foodgrp Code
    private String cpcCode;
 
    // foodgrp Name
    private String name;

   public Commodity(String cpcCode, String name) {
      this.cpcCode = cpcCode;
      this.name = name;
   }

   public String getCpcCode() {
      return cpcCode;
   }

   public String getName() {
      return name;
   }
 
    
 }
