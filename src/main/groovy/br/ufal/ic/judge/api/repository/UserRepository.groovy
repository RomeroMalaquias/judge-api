package br.ufal.ic.judge.api.repository

import br.ufal.ic.judge.api.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository extends CrudRepository<User, Long> {

}