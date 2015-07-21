package br.ufal.ic.judge.api.controller

import br.ufal.ic.judge.api.domain.User
import br.ufal.ic.judge.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController @RequestMapping("/users")
class UserController {

    @Autowired
    UserRepository userRepository

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    def get(@PathVariable Long id) {
        userRepository.findOne(id)
    }

    @RequestMapping(method = RequestMethod.GET)
    def list() {
        userRepository.findAll()
    }

    @RequestMapping(method = RequestMethod.POST)
    def save(@RequestBody User user) {
        userRepository.save(user)
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    def update(@PathVariable Long id, @RequestBody User user) {

        User userInstance = userRepository.findOne(id)

        userInstance.name = user.name ?: userInstance.name
        userInstance.username = user.username ?: userInstance.username
        userInstance.email = user.email ?: userInstance.email
        userInstance.password = user.password ?:userInstance.password

        userRepository.save(userInstance)
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    def delete(@PathVariable Long id) {

        User userInstance = userRepository.findOne(id)

        if (userInstance) {
            userRepository.delete(userInstance)
            new ResponseEntity<String>(HttpStatus.NO_CONTENT)
        } else {
            new ResponseEntity<String>(HttpStatus.NOT_FOUND)
        }
    }
}
