package app.dto;

    /**
 * Class represeting a foodgrp from the Studio Project database
 *
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 */

public class Commodity {
    // foodgrp Code
    private String cpcc; //PARTIAL KEY
 
    // foodgrp Name
    private String name;
 
    /**
     * Create a commmodity and set the fields
     */
    public Commodity(String cpcc, String name) {
       this.cpcc = cpcc;
       this.name = name;
    }
 
    public String cpc() {
       return cpcc;
    }
 
    public String getName() {
       return name;
    }
 }
