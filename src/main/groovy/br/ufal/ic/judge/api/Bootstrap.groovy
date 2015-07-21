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
        userRepository.save(new User(name: "Anderson Santos", email: "romero.malaquias+1@gmail.com", username: "anderson", password: "secret"))
        userRepository.save(new User(name: "Daniel Silva", email: "romero.malaquias+2@gmail.com", username: "daniel", password: "secret"))
        userRepository.save(new User(name: "Gustavo Neto", email: "romero.malaquias+3@gmail.com", username: "gustavo", password: "secret"))
        userRepository.save(new User(name: "Henrique Ferreira Alves", email: "romero.malaquias+4@gmail.com", username: "henrique", password: "secret"))
        userRepository.save(new User(name: "Jário Júnior", email: "romero.malaquias+5@gmail.com", username: "jario", password: "secret"))
        userRepository.save(new User(name: "Luccas Augusto", email: "romero.malaquias+6@gmail.com", username: "luccas", password: "secret"))
        userRepository.save(new User(name: "Manoel Neto", email: "romero.malaquias+7@gmail.com", username: "manoel", password: "secret"))
        userRepository.save(new User(name: "Marcio Guimarães", email: "romero.malaquias+8@gmail.com", username: "marcio", password: "secret"))
        userRepository.save(new User(name: "Marcos Neto", email: "romero.malaquias+9@gmail.com", username: "marcos", password: "secret"))
        userRepository.save(new User(name: "Romero Malaquias", email: "romero.malaquias+10@gmail.com", username: "romero", password: "secret"))
    }

}
