package org.libertas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.libertas.model.Usuario;

public class UsuarioDao {
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ConexaoHibernate");
	private static EntityManager em = emf.createEntityManager();
	
	public void inserir(Usuario u) {
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();
	}
	public void alterar(Usuario u) {
		em.getTransaction().begin();
		em.merge(u);
		em.getTransaction().commit();
	}
	public void excluir(Usuario u) {
		em.getTransaction().begin();
		em.remove(em.merge(u));
		em.getTransaction().commit();
		//em.remove(l);
	}
	public Usuario consultar(int id) {
		Usuario u = em.find(Usuario.class, id);
		return u;
	}
	
	public List<Usuario> listar() {
		Query query = em.createQuery("SELECT u FROM Usuario u");
		List<Usuario> lista = (List<Usuario>) query.getResultList();
		
		return lista;
	}
	public boolean verificar(Usuario u) {
		Query query = em.createQuery("SELECT u FROM Usuario u " 
				+ "WHERE u.usuario = :usuario AND u.senha = :senha");
		List<Usuario> lista = query.setParameter("usuario", u.getUsuario())
		.setParameter("senha", u.getSenha())
		.getResultList();
		if (lista.size() > 0) {
			return true;
		} else {
			return false;
		}
		
		
	}

}
