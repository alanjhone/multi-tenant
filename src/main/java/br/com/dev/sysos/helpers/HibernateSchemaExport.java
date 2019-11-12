package br.com.dev.sysos.helpers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

@SuppressWarnings("deprecation")
public class HibernateSchemaExport {

	private final String packageName = "br.com.dev.sysos.domain.shared";
	private final String outputSqlFile = "/home/lii/eclipse-workspace/schema.sql";

    @SuppressWarnings("rawtypes")
	private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
    	
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        
        List<File> dirs = new ArrayList<File>();
        
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        
        ArrayList<Class> classes = new ArrayList<Class>();
       
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        
        return classes;
    }

    @SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        
        if (!directory.exists()) {
            return classes;
        }
        
        File[] files = directory.listFiles();
        
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        
        return classes;
    
    }

	public void exportSchema() throws ClassNotFoundException, IOException {
		
		Configuration config = new Configuration();
		
		Properties properties = new Properties();
		properties.setProperty(Environment.DIALECT, PostgreSQLDialect.class.getName());
		properties.setProperty(Environment.DEFAULT_SCHEMA, "schema_acesso");
		config.setProperties(properties);
 
		getClasses(packageName).forEach(clazz -> config.addAnnotatedClass(clazz));

		SchemaExport schema = new SchemaExport(config);
		
		schema.setFormat(false);
		schema.setDelimiter(";");
		schema.setOutputFile(outputSqlFile);
		schema.create(true,  false);
		
	}

	/**
	 * @return the outpuSqlFile
	 */
	public String getOutputSqlFile() {
		return outputSqlFile;
	}

}