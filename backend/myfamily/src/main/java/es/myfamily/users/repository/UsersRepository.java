package es.myfamily.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.myfamily.users.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

  Optional<Users> findByEmail(String email);
}
