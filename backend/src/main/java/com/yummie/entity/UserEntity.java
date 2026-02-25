package com.yummie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "is_enable")
    private boolean isEnable;

    @Column(name = "is_registered")
    private boolean isRegistered;

    @Column(name = "auth_provider")
    private String authProvider;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenEntity> refeshTokenEntities;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private VerificationCodeEntity verificationCodeEntity;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviewEntities;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> cartItemEntities;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orderEntities;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<DeliveryInfoEntity> deliveryInfoEntityList;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private ProfileEntity profileEntity;

    public void addProfile(ProfileEntity profileEntity) {
        profileEntity.setUserEntity(this);
        this.profileEntity = profileEntity;
    }
}
