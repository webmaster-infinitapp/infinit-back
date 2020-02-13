package com.paypro.wallet.wallet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.paypro.wallet.wallet.model.TokenMdl;


@Repository
public interface TokenRepository extends CrudRepository<TokenMdl, Long>  {
	

	public TokenMdl findByTelefonoAndIdBlockchainAndContractAddress(String telefono,int idBlockChain, String contractAddress);

	public List<TokenMdl> findByTelefonoAndIdBlockchain(String telefono,int idBlockChain);
}


