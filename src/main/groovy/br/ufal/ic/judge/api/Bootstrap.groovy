package br.ufal.ic.judge.api

import br.ufal.ic.judge.api.domain.User
import br.ufal.ic.judge.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct


@Component
class Bootstrap {

    @Autowired
    UserRepository userRepository

    @PostConstruct
    def run() {
        createUsers()
    }

    def createUsers() {
        userRepository.save(new User(name: "Anderson Santos", email: "anderson.mends@gmail.com", username: "anderson", password: "secret"))
        userRepository.save(new User(name: "Daniel Silva", email: "dbfs@ic.ufal.br", username: "daniel", password: "secret"))
        userRepository.save(new User(name: "Gustavo Neto", email: "gustavocosta@ic.ufal.br", username: "gustavo", password: "secret"))
        userRepository.save(new User(name: "Henrique Ferreira Alves", email: "hfa@ic.ufal.br", username: "henrique", password: "secret"))
        userRepository.save(new User(name: "Jário Júnior", email: "jjsj@ic.ufal.br", username: "jario", password: "secret"))
        userRepository.save(new User(name: "Luccas Augusto", email: "luccasaugusto@ic.ufal.br", username: "luccas", password: "secret"))
        userRepository.save(new User(name: "Manoel Neto", email: "mjrn@ic.ufal.br", username: "manoel", password: "secret"))
        userRepository.save(new User(name: "Marcio Guimarães", email: "marcioaugustosg@gmail.com", username: "marcio", password: "secret"))
        userRepository.save(new User(name: "Marcos Neto", email: "marcosjfneto@gmail.com", username: "marcos", password: "secret"))
        userRepository.save(new User(name: "Romero Malaquias", email: "romero.malaquias@gmail.com", username: "romero", password: "secret"))
    }

}
