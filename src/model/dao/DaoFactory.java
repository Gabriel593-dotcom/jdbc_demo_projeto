package model.dao;

import db.Db;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(Db.getConnection());
	}
	
	public static DepartmentDao createDepartment() {
		return new DepartmentDaoJDBC(Db.getConnection());
	}
}


// A classe DaoFactory possui m�todos para instanciar
// objetos de banco de dados de forma que n�o exponha 
// a implementa��o no programa. 