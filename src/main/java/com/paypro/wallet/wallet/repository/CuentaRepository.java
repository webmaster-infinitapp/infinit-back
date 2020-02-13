package com.paypro.wallet.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paypro.wallet.wallet.model.CuentaBlockchain;



@Repository
public interface CuentaRepository extends CrudRepository<CuentaBlockchain, Long> {

	public CuentaBlockchain findByTelefonoAndIdBlockchain(String telefono,int idBlockChain);
	public CuentaBlockchain findByTelefonoAndCuenta(String telefono,String cuenta);
	
}
