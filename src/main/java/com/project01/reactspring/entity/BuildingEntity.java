package com.project01.reactspring.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "building")
public class BuildingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String street;

    @Column
    private String ward;

    @Column
    private String district;

    @Column
    private String demand;

    @Column
    private String structure;

    @Column
    private Long numberofbasement;

    @Column
    private Long floorarea;

    @Column
    private String direction;

    @Column
    private String level;

    @Column
    private Long rentprice;

    @Column
    private String servicefee;

    @Column
    private String brokeragefee;

    @Column
    private String type;

    @Column
    private String note;

    @Column
    private String managername;

    @Column
    private String managerphone;

    @Column
    private String uploadfile;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "building",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RentAreaEntity> rentarea=new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "assignmentbuilding",
            joinColumns = @JoinColumn(name = "buildingid",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "staffid",nullable = false))
    private List<UserEntity> users=new ArrayList<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions = new ArrayList<>();

}
