package com.paypro.wallet.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paypro.wallet.wallet.model.Blockchain;




@Repository
public interface BlockChainRepository extends CrudRepository<Blockchain, Long> {
	
	public Blockchain findByIdBlockchain(int idBlockChain);

}
