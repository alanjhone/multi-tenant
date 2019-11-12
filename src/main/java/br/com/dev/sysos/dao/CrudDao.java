package br.com.dev.sysos.dao;

import br.com.dev.sysos.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CrudDao
 *
 * @author Alan Jhone
 *
 */
@Component
public class CrudDao extends GenericDao {

    /**
     * Construtor que recebe o session factory do hibernate.
     * @param sf
     */
    @Autowired
    public CrudDao(SessionFactory sf) {
        setSessionFactory(sf);
    }

}
