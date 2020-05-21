package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import db.Db;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);
			st = conn.prepareStatement("INSERT INTO department " + "(Name) " + "VALUES " + "(?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			List<Department> filteredDepartment = findAll().stream().filter(d -> d.equals(obj))
					.collect(Collectors.toList());

			if (filteredDepartment.isEmpty()) {

				int row = st.executeUpdate();

				if (row > 0) {
					ResultSet rs = st.getGeneratedKeys();
					if (rs.next()) {
						obj.setId(rs.getInt(1));
					}

					Db.closeResultSet(rs);
				}

				conn.commit();
			}

			else {
				throw new DbException("Error in register this department. Caused by: this department already exists.");
			}

		}

		catch (SQLException e) {

			try {
				conn.rollback();
				throw new DbException(e.getMessage());
			}

			catch (SQLException e1) {
				throw new DbException("Error em try to rollback. Caused by: " + e.getMessage());
			}

		}

		finally {
			Db.closeStatment(st);
		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);
			st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			int row = st.executeUpdate();

			conn.commit();
		}

		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException(e.getMessage());
			}

			catch (SQLException e1) {
				throw new DbException("Error in try to rollback. Caused by: " + e.getMessage());
			}

		}

	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
			conn.commit();
		}

		catch (SQLException e) {

			try {
				conn.rollback();
				throw new DbException(e.getMessage());
			}

			catch (SQLException e1) {
				throw new DbException("Error in try to rollback. Caused by: " + e.getMessage());
			}

		}

	}

	@Override
	public Department findById(Integer obj) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
			st.setInt(1, obj);
			rs = st.executeQuery();

			Department dep = new Department();

			if (rs.next()) {
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
			}

			return dep;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			List<Department> list = new ArrayList<>();

			st = conn.prepareStatement("SELECT * FROM department");

			rs = st.executeQuery();

			while (rs.next()) {
				Department dep = new Department();
				dep = loadDepartment(rs);

				list.add(dep);
			}

			return list;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	private Department loadDepartment(ResultSet rs) throws SQLException {

		Department dep = new Department();

		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));

		return dep;
	}

}
