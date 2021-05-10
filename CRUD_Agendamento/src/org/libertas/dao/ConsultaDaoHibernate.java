package org.libertas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.libertas.model.Consulta;

public class ConsultaDaoHibernate {
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ConexaoHibernate");
	private static EntityManager em = emf.createEntityManager();
	
	public void inserir(Consulta c) {
		em.getTransaction().begin();
		em.persist(c);
		em.getTransaction().commit();
	}
	public void alterar(Consulta c) {
		em.getTransaction().begin();
		em.merge(c);
		em.getTransaction().commit();
	}
	public void excluir(Consulta c) {
		em.getTransaction().begin();
		em.remove(em.merge(c));
		em.getTransaction().commit();
	}
	public Consulta consultar(int id) {
		Consulta l = em.find(Consulta.class, id);
		return l;
	}
	
	public List<Consulta> listar() {
		Query query = em.createQuery("SELECT c FROM Consulta c");
		List<Consulta> lista = (List<Consulta>) query.getResultList();
		
		return lista;
	}

}
