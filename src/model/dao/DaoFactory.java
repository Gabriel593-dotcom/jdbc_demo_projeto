package model.dao;

import db.Db;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(Db.getConnection());
	}
}


// A classe DaoFactory possui métodos para instanciar
// objetos de banco de dados de forma que não exponha 
// a implementação no programa. 