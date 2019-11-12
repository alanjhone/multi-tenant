/**
 * 
 */
package br.com.dev.sysos.helpers;

/**
 * @author Alan Jhone
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

import br.com.dev.sysos.exceptions.NegocioException;
 
public class DBScriptRunner {

	private BufferedReader reader;
	private Connection con;
	private Statement statement;
	
	public DBScriptRunner() {
		reader = null;
		con = null;
		statement = null;
	}
	
   private Connection getConnection() throws SQLException {
	   return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/sysos", "postgres", "postgres");
   }

   private void executeScript(String schema, String scriptFilePath) throws IOException, SQLException, NegocioException {
	   try {
			con = getConnection();
			statement = con.createStatement();
			reader = new BufferedReader(new FileReader(scriptFilePath));
		
			Stream<String> stream = reader.lines();
			
			statement.execute("create schema " + schema);
			
			stream.map(s -> s.trim())
		    .filter(s -> !s.isEmpty() && !s.contains("sequence"))
		    .map(line -> line.replace("schema_acesso", schema))
		    .forEach(line -> {
		    	try {
		    		System.out.println(line);
					statement.execute(line);
				} catch (SQLException e) {
					throw new RuntimeException(e.getMessage());
				}
		    });
		
		} catch (Exception e) {
			throw new NegocioException("Não foi possível realizar esta operação!");
			
		} finally {
			reader.close();
			con.close();
		}
   }
   
	public void createSchema(String schema) throws IOException, SQLException, NegocioException, ClassNotFoundException {
		HibernateSchemaExport hse = new HibernateSchemaExport();
		hse.exportSchema();
		
		executeScript(schema, hse.getOutputSqlFile());
	}
}
