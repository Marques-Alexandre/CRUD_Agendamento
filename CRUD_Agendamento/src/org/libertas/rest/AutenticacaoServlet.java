package org.libertas.rest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.libertas.model.Response;
import org.libertas.model.Usuario;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Servlet implementation class AutenticacaoServlet
 */
@WebServlet("/autenticacao")
public class AutenticacaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private static final String FRASE_SEGREDO = "Essa_e_a_frase_de_segredo_para_seguranca_do_"
    		+ "codigo_ela_precisa_de_ter_no_minimo_128_caracteres_ou_seja_512_bites_essa_frase_"
    		+ "nunca_deve_ser_compartilhada";
    public AutenticacaoServlet() {
        super();
       
    }

    private void enviaResposta(HttpServletResponse response, String json, int codigo) throws IOException{
    	response.addHeader("Content-Type", "application/text; charset=UTF-8");
    	response.setStatus(codigo);
    	
    	BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
    	out.write(json.getBytes("UTF-8"));
    	out.close();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			String json = request.getReader().lines().collect(Collectors.joining());
			Usuario u  = gson.fromJson(json, Usuario.class);
			if (u.getUsuario().equals("admin") && u.getSenha().equals("123")) {
				String token = gerarToken(u.getUsuario(), 1);
				enviaResposta(response, token, 200);
			}else {
				enviaResposta(response, gson.toJson(new Response(false, "Unauthorized")), 401);
			}
		} catch (Exception e) {
			e.printStackTrace();
			enviaResposta(response, gson.toJson(new Response(false, "Internal Server Error")), 500);
		}
	}
	
	public String gerarToken(String login, int expiraEmDias) {
		SignatureAlgorithm algo =  SignatureAlgorithm.HS512;
		Date agora = new Date();
		Calendar expira = Calendar.getInstance();
		expira.add(Calendar.DAY_OF_MONTH, expiraEmDias);
		byte[] secret = DatatypeConverter.parseBase64Binary(FRASE_SEGREDO);
		
		SecretKeySpec key = new SecretKeySpec(secret, algo.getJcaName());
		
		JwtBuilder jwt = Jwts.builder()
				.setIssuedAt(agora)
				.setIssuer(login)
				.signWith(key, algo)
				.setExpiration(expira.getTime());
		
		return jwt.compact();
	}
	
	public boolean validaToken(String autorizationHeader) {
		try {
			if(autorizationHeader == null || !autorizationHeader.startsWith("Bearer ")) {
				return false;				
			}
			String token = autorizationHeader.substring("Bearer".length()).trim();
			byte[] key = DatatypeConverter.parseBase64Binary(FRASE_SEGREDO);
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
			
			if(claims == null) {
				return false;
			}
			
			System.out.println(claims.getIssuer());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
