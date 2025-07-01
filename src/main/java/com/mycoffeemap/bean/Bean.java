package com.mycoffeemap.bean;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "beans")
public class Bean {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
    private String name;

    private String origin;

    @Enumerated(EnumType.STRING)
    private RoastLevel roastLevel;
    
    public enum RoastLevel {LIGHT, MEDIUM, DARK}

    private String flavorNotes;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

	    
}
