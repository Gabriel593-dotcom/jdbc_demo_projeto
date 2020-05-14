package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.Db;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller fidById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"WHERE seller.Id = ?");
			
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				
				Department dep = new Department();
				
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				
				Seller seller = new Seller();
				
				seller.setId(rs.getInt("Id"));
				seller.setName(rs.getString("Name"));
				seller.setEmail(rs.getString("Email"));
				seller.setBirthDate(rs.getDate("BirthDate"));
				seller.setBaseSalary(rs.getDouble("BaseSalary"));
				seller.setDepartment(dep);
				return seller;
				
				// o m�todo captura o campo afetado atrav�s do comando sql
				// e guarda as suas informa��es nos objetos de entidade
				// Seller e Department e retorna um Seller. Por�m, primeiro verifica se o resultSet n�o
				// est� vazio com o if(). Caso esteja, retorna nulo.
			}
			
			return null;
		}
		
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			Db.closeStatment(st);
			Db.closeResultSet(rs);
			
			// a conex�o n�o � fechada, pois pode ser que o usu�rio queira fazer uma outra opea��o. 
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
