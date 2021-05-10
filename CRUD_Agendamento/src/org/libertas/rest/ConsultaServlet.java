package org.libertas.rest;

import java.io.BufferedOutputStream;
import java.io.IOException;

import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.libertas.dao.ConsultaDaoHibernate;
import org.libertas.model.Consulta;
import org.libertas.model.Response;

import com.google.gson.Gson;

/**
 * Servlet implementation class ConsultaServlet
 */
@WebServlet(urlPatterns={"/consulta/*"} , name="consulta")
public class ConsultaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private void enviaResposta(HttpServletResponse response, String json, int codigo) throws
	IOException {

	response.addHeader("Content-Type", "application/json; charset=UTF-8");
	response.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5501");
	response.addHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");	
	response.setStatus(codigo);

	BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

	out.write(json.getBytes("UTF-8"));

	out.close();
	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		String authorizationHeader = request.getHeader("AUTHORIZATION");
		AutenticacaoServlet aut = new AutenticacaoServlet();
		if(!aut.validaToken(authorizationHeader)) {
			enviaResposta(response, gson.toJson(new Response(false, "Unauthorized")), 401);
			return;
		}
		
		
		ConsultaDaoHibernate cdao = new ConsultaDaoHibernate();
		System.out.println(request.getPathInfo());
		int id = 0;
		
		if(request.getPathInfo() != null) {
			String info = request.getPathInfo().replace("/", "");
			id = Integer.parseInt(info);
		}
		if (id > 0) {
			enviaResposta(response, gson.toJson(cdao.consultar(id)), 200);
		} else {
			enviaResposta(response, gson.toJson(cdao.listar()), 200);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// inserir
		
		Gson gson = new Gson();
		
		String authorizationHeader = request.getHeader("AUTHORIZATION");
		AutenticacaoServlet aut = new AutenticacaoServlet();
		if(!aut.validaToken(authorizationHeader)) {
			enviaResposta(response, gson.toJson(new Response(false, "Unauthorized")), 401);
			return;
		}
		
		ConsultaDaoHibernate cdao = new ConsultaDaoHibernate();
		String json = request.getReader().lines().collect(Collectors.joining());
		Consulta c = gson.fromJson(json, Consulta.class);
		cdao.inserir(c);
		enviaResposta(response, gson.toJson(new Response(true, "Registro inserido")), 201);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//alterar
		
		Gson gson = new Gson();
		
		String authorizationHeader = request.getHeader("AUTHORIZATION");
		AutenticacaoServlet aut = new AutenticacaoServlet();
		if(!aut.validaToken(authorizationHeader)) {
			enviaResposta(response, gson.toJson(new Response(false, "Unauthorized")), 401);
			return;
		}
		
		int id = 0;
		ConsultaDaoHibernate cdao = new ConsultaDaoHibernate();
	
		if (request.getPathInfo()!=null) {
		String info = request.getPathInfo().replace("/", "");
		id = Integer.parseInt(info);
	
		}
	
		String json = request.getReader().lines().collect(Collectors.joining());
		Consulta c = gson.fromJson(json, Consulta.class);
		c.setIdconsulta(id);
		cdao.alterar(c);
		enviaResposta(response, gson.toJson(new Response(true, "Registro alterado")), 200);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Excluir
		
		Gson gson = new Gson();
		
		String authorizationHeader = request.getHeader("AUTHORIZATION");
		AutenticacaoServlet aut = new AutenticacaoServlet();
		if(!aut.validaToken(authorizationHeader)) {
			enviaResposta(response, gson.toJson(new Response(false, "Unauthorized")), 401);
			return;
		}
		
		ConsultaDaoHibernate cdao = new ConsultaDaoHibernate();
		int id = 0;
		
		if (request.getPathInfo()!= null) {
			String info = request.getPathInfo().replace("/", "");
			id = Integer.parseInt(info);
		}
		System.out.println(id);
		Consulta c = new Consulta();
		c.setIdconsulta(id);
		cdao.excluir(c);
		enviaResposta(response, gson.toJson(new Response(true, "registro excluido")), 200);
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		enviaResposta(response, gson.toJson(new Response(true, "Options")), 200);
	}
	

}
