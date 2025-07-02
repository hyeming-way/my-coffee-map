package com.mycoffeemap.bean;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.mycoffeemap.user.User;

@Entity
@Table(name = "bean_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "bean_id"})
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeanLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bean_id")
    private Bean bean;

    private LocalDateTime likedAt;
}
