package com.paypro.wallet.wallet.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paypro.wallet.wallet.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	
	public User findByTelefono(String telefono);
	

}