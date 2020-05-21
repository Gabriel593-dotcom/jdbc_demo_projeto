package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao depDao = DaoFactory.createDepartment();
		Department dep = new Department(null, "Agro");

		System.out.println("<TESTE FIND ALL>");
		List<Department> list = depDao.findAll();
		list.forEach(System.out::println);

//		System.out.println("\n<TESTE INSERT>");
//		depDao.insert(dep);
//		System.out.println("Registred: " + dep.getId());

		System.out.println("\n<TESTE UPDATE>");
		depDao.update(new Department(1, "Redes"));
		System.out.println("Updated!");

		System.out.println("\n<TESTE DELETE>");
		depDao.deleteById(11);
		System.out.println("deleted!");

		System.out.println("\n<TESTE FIND BY ID>");
		System.out.println(depDao.findById(1));

	}
}
