package com.example.Register.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Register.entity.Userr;

public interface UserrRepository extends JpaRepository<Userr, Integer> {

  Userr findByEmail(String email);
}
