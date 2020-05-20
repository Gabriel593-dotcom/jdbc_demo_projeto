package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		Department department = new Department(2, null);
		
		System.out.println("<TESTE FIND BY ID>");
		Seller seller = sellerDao.findById(9);
		System.out.println(seller);
		
		System.out.println("\n<TESTE FIND BY DEPARTMENT>");
		List<Seller> list = sellerDao.findDepartment(new Department(2, null));
		for(Seller s : list) {
			System.out.println(s.toString());
		}
		
		System.out.println("\n<TESTE FIND ALL>");
		list = sellerDao.findAll();
		for(Seller s : list) {
			System.out.println(s.toString());
		}
		
		System.out.println("\n SELLER INSERT");
		Seller seller1 = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(seller1);
		System.out.println("registred: new seller id:" + seller1.getId());
	}

}
