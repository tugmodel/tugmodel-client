/*
 * Copyright (c) 2017- Cristian Donoiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tugmodel.client.tug.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.meta.Attribute;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.BaseTug;

/**
 * Wrapper tug used for stuff like error handling and delegation to proxied tug.
 */
public class SQLTug<M extends Model<?>> extends BaseTug<M> {

	
//	private createMisingTable(M model, Meta meta) {
//		
//	}
	
	public M fetch(M model) {
		
		
		return model;
	}
		
    public M create(M model) {
		
		Connection con = null;
		Statement stmt = null;
		PreparedStatement ps = null;
    	try {
			// TODO: user DBCP for connection pooling.
			// http://stackoverflow.com/questions/2835090/how-to-establish-a-connection-pool-in-jdbc
			Class.forName(getConfig().asString("dbDriver"));

			con = DriverManager.getConnection(getConfig().asString("dbUrl"), getConfig().asString("user"), getConfig().asString("pass"));

			Meta meta = Meta.s.where("class=?", model.getClass()).get(0);

//			String sql = "CREATE TABLE IF NOT EXISTS EMPLOYEE (\n" + "	id integer PRIMARY KEY,\n"
//					+ "	name text NOT NULL\n" + ");";
			
			String tableName = meta.getId().toUpperCase();
			// TODO: Implement a template similar to the one in Spring or look for one free. Anyway the user is free to come with his own version of SQLTug.
			StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (\n");
			for (Attribute a : meta.getAttributes()) {
				sql.append(a.getId() + " varchar,");   // TODO: use attribute datatype by looking for a conversion map in the meta.
			}
			sql.append("EXTRA text);");  // For extra attributes.
			stmt = con.createStatement();
			stmt.execute(sql.toString());

			//String insertTableSQL = "INSERT INTO EMPLOYEE(ID, NAME) VALUES (?,?)";
			StringBuilder insertSQL = new StringBuilder("INSERT INTO "+ tableName + "(");
			for (Attribute a : meta.getAttributes()) {
				insertSQL.append(a.getId() + ",");
			}
			insertSQL.append(" EXTRA) VALUES (");
			for (Attribute a : meta.getAttributes()) {
				insertSQL.append("?,");
			}
			insertSQL.append("?)");
			
			ps = con.prepareStatement(insertSQL.toString());

			// Not all databases allow for a non-typed Null to be sent to the
			// backend. For maximum portability, the setNull or the
			// setObject(int parameterIndex, Object x, int sqlType) method
			// should be used instead of setObject(int parameterIndex, Object
			// x).
			// http://www.service-architecture.com/articles/database/mapping_sql_and_java_data_types.html
			//ps.setObject(1, 13);
			//ps.setObject(2, "mkyong", Types.CHAR);
			
			int i = 1;
			// Make sure getters are called and objects are serialized to the tug underlying format.
			// TODO: try mapper.serialize.
			Map values = this.getConfig().getMapper().convert(model, Map.class);
			for (Attribute a : meta.getAttributes()) {
				ps.setObject(i++, values.get(a.getId()));				
			}
			Map extra = model.getExtraAttributes();
			String extraAttrs = this.getConfig().getMapper().convert(extra, String.class);
			ps.setObject(i++, extraAttrs);
			
			int n = ps.executeUpdate();
			if (n != 1)
				System.out.println("copuld not insert.");
    	} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				ps.close();
				stmt.close();
				con.close();
		   	} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
    	return model;
    	
    }
}
