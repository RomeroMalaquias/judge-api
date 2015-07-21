package br.ufal.ic.judge.api.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Submission {

    enum Status {
        WAITING,
        CORRECT,
        WRONG_ANSWER,
        COMPILATION_ERROR
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id

    String code
    String output
    String input
    String language
    Status status = Status.WAITING
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    Date dateCreated = new Date()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "submission")
    List<Similarity> similarities
}
