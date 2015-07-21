package br.ufal.ic.judge.api.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity
class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id

    String name

    @Column(unique = true)
    String username

    @Column(unique = true)
    String email

    @JsonIgnore
    String password

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    List<Submission> submissions

}
