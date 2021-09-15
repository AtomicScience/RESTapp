package ru.atomicscience.restapp.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atomicscience.restapp.models.User;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UsersCrudRepository extends CrudRepository<User, String> {}