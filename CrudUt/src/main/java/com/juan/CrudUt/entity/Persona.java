package com.juan.CrudUt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "persona")
@Data
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer identificacion;

    @Column(nullable = false)
    private String pnombre;

    private String snombre;

    @Column(nullable = false)
    private String papellido;

    private String sapellido;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechanacimiento;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false)
    private String edadclinica;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Usuario usuario;
}
