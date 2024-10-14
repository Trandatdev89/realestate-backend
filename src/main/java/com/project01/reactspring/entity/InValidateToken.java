package com.project01.reactspring.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invalidatetoken")
public class InValidateToken {

    @Id
    private String id;

    @Column
    private Date expirytime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

}
