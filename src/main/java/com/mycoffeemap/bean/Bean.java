package com.mycoffeemap.bean;


import java.util.ArrayList;
import java.util.List;

import com.mycoffeemap.cafe.CafeBean;

import com.mycoffeemap.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "beans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  
    @OneToMany(mappedBy = "bean", cascade = CascadeType.ALL)
    private List<CafeBean> cafeBeans = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bean)) return false;
        Bean bean = (Bean) o;
        return id != null && id.equals(bean.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    // 연관된 사용자 정보
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

	    
}
