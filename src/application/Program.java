package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		
		System.out.println("<TESTE FIND BY ID>");
		Seller seller = sellerDao.findById(9);
		System.out.println(seller);
		
		System.out.println("\n<TESTE FIND BY DEPARTMENT>");
		List<Seller> list = sellerDao.findDepartment(new Department(2, null));
		for(Seller s : list) {
			System.out.println(s.toString());
		}
	}

}
