package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
	}
}


// A classe DaoFactory possui m�todos para instanciar
// objetos de banco de dados de forma que n�o exponha 
// a implementa��o no programa. 