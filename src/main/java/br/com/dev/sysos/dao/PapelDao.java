/**
 * 
 */
package br.com.dev.sysos.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alan Jhone
 *
 */

@Component
public class PapelDao extends GenericDao {
	
    @Autowired
    public PapelDao(SessionFactory sf) {
        setSessionFactory(sf);
    }

}
