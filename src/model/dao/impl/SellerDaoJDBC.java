package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement(

					"INSERT INTO seller" + "(Name, Email, BirthDate, BaseSalary, DepartmentId)" + "VALUES"
							+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				Db.closeResultSet(rs);
			}

			else {
				throw new DbException("Unexpected error: no one rows affected.");
			}
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			Db.closeStatment(st);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

			st.setInt(1, id);

			int row = st.executeUpdate();
		
			if (row == 0) {
				throw new DbException("no one register deleted");
			}

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {

				Department dep = loadDepartment(rs);
				Seller seller = loadSeller(rs, dep);
				return seller;

				// o método captura o campo afetado através do comando sql
				// e guarda as suas informações nos objetos de entidade
				// Seller e Department e retorna um Seller. Porém, primeiro verifica se o
				// resultSet não
				// está vazio com o if(). Caso esteja, retorna nulo.
			}

			return null;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			Db.closeStatment(st);
			Db.closeResultSet(rs);

			// a conexão não é fechada, pois pode ser que o usuário queira fazer uma outra
			// opeação.
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();

			Map<Integer, Department> map = new HashMap<>();
			// uma maneira de verificar se o departamento existe ou não usando
			// o map.

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));
				// instancia um objeto Department recebendo o valor referente
				// ao id de pesquisa guardado na estrutura de dados.

				if (dep == null) {
					// caso o map estiver vazio, o objeto vai apontar pra nulo
					// e cair nessa condição if(). Sendo assim, vai ser instanciado um
					// novo registro de departamento, carregando as informações
					// e atribuindo ao map.

					// essa lógica se deve ao fato de que vários registros de
					// sellers podem apontar pra um departamento apenas.
					// porque a cardinalidade atende ao conceito
					// 1 pra n. sendo 1 vendedor pode pertencer apenas
					// a um departamento e um departamento pode ter vários
					// vendedores.

					dep = loadDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller seller = loadSeller(rs, dep);
				list.add(seller);
			}

			return list;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Seller> findDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();

			Map<Integer, Department> map = new HashMap<>();
			// uma maneira de verificar se o departamento existe ou não usando
			// o map.

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));
				// instancia um objeto Department recebendo o valor referente
				// ao id de pesquisa guardado na estrutura de dados.

				if (dep == null) {
					// caso o map estiver vazio, o objeto vai apontar pra nulo
					// e cair nessa condição if(). Sendo assim, vai ser instanciado um
					// novo registro de departamento, carregando as informações
					// e atribuindo ao map.

					// essa lógica se deve ao fato de que vários registros de
					// sellers podem apontar pra um departamento apenas.
					// porque a cardinalidade atende ao conceito
					// 1 pra n. sendo, 1 vendedor pode pertencer apenas
					// a um departamento e um departamento pode ter vários
					// vendedores.

					dep = loadDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller seller = loadSeller(rs, dep);
				list.add(seller);
			}

			return list;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			Db.closeStatment(st);
			Db.closeResultSet(rs);
		}
	}

	private Seller loadSeller(ResultSet rs, Department dep) throws SQLException {

		Seller seller = new Seller();

		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(dep);

		return seller;
	}

	private Department loadDepartment(ResultSet rs) throws SQLException {

		Department dep = new Department();

		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));

		return dep;
	}

}
