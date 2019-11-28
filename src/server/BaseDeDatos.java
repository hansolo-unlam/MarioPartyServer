package server;

import java.net.Socket;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;

//import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class BaseDeDatos {

	private static Configuration cfg;
	private static SessionFactory factory;
	private static Session session;
	private static Transaction tx;
	
	public BaseDeDatos() {
		
		
	}
	
	public static boolean datosValidos(String user, String pass) {
		
		cfg = new Configuration();
		cfg.configure("marioparty.cfg.xml");
		factory = cfg.buildSessionFactory();
		session = factory.openSession();
		
		tx = session.beginTransaction();
		
		try {
			tx.commit();
			
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
			
			cq.from(Usuario.class);
			Root<Usuario> ru = cq.from(Usuario.class);
			cq.select(ru).where(cb.equal(ru.get("user"), user));
			List<Usuario> lista = session.createQuery(cq).getResultList();
			
			Usuario aux = new Usuario(user, pass);
			
			if(lista.size() > 0) 
				if(Integer.valueOf(lista.get(0).getPass()) == aux.getPassHash()) 
					return true;
			
			return false;
					
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
			factory.close();
		}
		return false;
	}

}
