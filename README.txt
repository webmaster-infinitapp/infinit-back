Este proyecto requiere:
- Crearse modelo de datos, con el script adjunto 
- Crearse nodo blockchain ethereum, una cuenta y un keystore asociado a esa cuenta (para desbloquearla y utilizarla)
- Crearse certificado ssl.p12 y sustituirlo por your_ssl_here.p12.txt
- Crearse una cuenta twilio para envio de SMS
- Cambiar las ips apuntando al nodo en: 
	- En Account.java, método deleteKeystore
		//TODO cambiar "ruta_keystore" por la ruta donde se encuentre el keystore de la cuenta asociada
    	rutaKeyStore = "ruta_keystore";
	- En BlockchainInterface.java
		//TODO cambiar "ruta_keystore" por la ruta donde está el keystore de la cuenta
		this.blockchain.setRutaKeyStore("ruta_keystore");	
	- En WallerController.java
		//TODO Una vez que se ha dado de alta la cuenta twilio, meter los datos aquí
		String randomID = apiTwilio.genRandomID();	
	- En application.properties
		# TODO cambiar el datasource apuntando a la base de datos
		spring.datasource.url=jdbc:mysql://IP:PORT/name_bbdd
		spring.datasource.username=your_username_here
		spring.datasource.password=your_password_here
		
		# TODO Cambiar los datos del ssl
		server.port: your_port_ssl_here
		server.ssl.key-store: your_ssl_keystore_here.p12
		server.ssl.key-store-password: your_password_ssl_here
		server.ssl.keyStoreType: your_type_keystore (recomended) PKCS12
		server.ssl.keyAlias: your_alias_here
		
	- En Persistence.xml
		<!-- TODO Cambiar IP:PUERTO/nombre_bbdd -->
		<property name="javax.persistence.jdbc.url" value="jdbc:mysql://IP:PUERTO/nombre_bbdd" />
		
En el modelo de base de datos, tabla Blockchain, 
	- se pondrá en el campo direccion, la IP donde esté el nodo
	- en el campo puerto, el puerto, que normalmente será 8545.
	- campo rutaKeystore, la ruta donde se almacenará la keystore de la cuenta  		
			