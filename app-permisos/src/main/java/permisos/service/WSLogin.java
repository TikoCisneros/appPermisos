package permisos.service;

import java.io.BufferedReader;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import permisos.model.manager.ManagerWSLogin;

/**
 * Servlet implementation class WSLogin
 */
@WebServlet(description = "Contiene los servicios web REST de permisos", urlPatterns = { "/WSLogin/getPermisos", "/WSLogin/postPermisos" })
public class WSLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @EJB
    private ManagerWSLogin mngLog;
    
    private static String url = "/WSLogin/";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WSLogin() {
        super();
    }
    
    /**
     * Añade las cabeceras para que las aplicaciones puedan acceder a los recursos web
     * @param response
     */
    private void addCorsHeader(HttpServletResponse response) { 	
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET");
		response.addHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept");
		response.setContentType (MediaType.APPLICATION_JSON+";charset=utf-8");
	}
    
    /**
     * Método para procesar las llamadas get
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequestGET(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		addCorsHeader(response);
		// switch para cada caso de servicos
		String path = request.getServletPath();
		if (path.equalsIgnoreCase(url+"getPermisos"))
			getLogin(request, response);
	}
    
    /**
     * Método para procesar las llamadas post
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequestPOST(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		addCorsHeader(response);
		JSONObject data = getBodyData(request);
		// switch para cada caso de servicos
		String path = request.getServletPath();
		if (path.equalsIgnoreCase(url+"postPermisos"))
			postLogin(request, response, data);
    }	
    
    /**
     * Devuelve los permisos de un usuario a una aplicación median el método GET
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void getLogin (HttpServletRequest request,
    		HttpServletResponse response) throws IOException, ServletException {
    	try {
    		String usuLogin = request.getParameter("usr");
    		String usuPass = request.getParameter("pwd");
    		String aplId = request.getParameter("apl");
    		if(!mngLog.existeUsuPass(usuLogin, usuPass))
    			response.getWriter().write(jsonMensajes("WARN", "", "Usuario o contraseña incorrectos."));
    		else{
    			JSONArray permisos = mngLog.arrayPermisos(aplId, usuLogin);
    			if(permisos.isEmpty())
    				response.getWriter().write(jsonMensajes("WARN", "", "No posee permisos para esta aplicación."));
    			else
    				response.getWriter().write(jsonMensajes("OK", permisos, "Consulta correcta."));
    			permisos = null;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					jsonMensajes("ERROR", "", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
    }
    
    /**
     * Devuelve los permisos de un usuario a una aplicación median el método POST
     * @param request
     * @param response
     * @param data
     * @throws IOException
     * @throws ServletException
     */
    public void postLogin (HttpServletRequest request,
    		HttpServletResponse response, JSONObject data) throws IOException, ServletException {
    	try {
    		if(data.get("usr")==null || data.get("pwd")==null || data.get("apl")==null 
				|| data.get("usr").toString().trim().isEmpty() || data.get("pwd").toString().trim().isEmpty()
				|| data.get("apl").toString().trim().isEmpty()){
				response.getWriter().write(jsonMensajes("WARN", "", "La estructura de la consulta es incorrecta."));
			}else{
    			String usuLogin = data.get("usr").toString();
	    		String usuPass = data.get("pwd").toString();
	    		String aplId = data.get("apl").toString();
	    		if(!mngLog.existeUsuPass(usuLogin, usuPass))
	    			response.getWriter().write(jsonMensajes("WARN", "", "Usuario o contraseña incorrectos."));
	    		else{
	    			JSONArray permisos = mngLog.arrayPermisos(aplId, usuLogin);
	    			if(permisos.isEmpty())
	    				response.getWriter().write(jsonMensajes("WARN", "", "No posee permisos para esta aplicación."));
	    			else
	    				response.getWriter().write(jsonMensajes("OK", permisos, "Consulta correcta."));
	    			permisos = null;
	    		}
			}
    	} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					jsonMensajes("ERROR", "", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
    }
    
    /**
	 * Devuelve la respuesta del servicio
	 * 
	 * @param status
	 * @param value
	 * @param mensaje
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String jsonMensajes(String status, Object value, String mensaje) {
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("value", value);
		obj.put("mensaje", mensaje);
		return obj.toJSONString();
	}
	
	/**
	 * Transforma objeto JSON para leerlo
	 * @param request
	 * @return
	 */
	private JSONObject getBodyData(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		JSONObject o = null;
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONParser parser = new JSONParser();
			System.out.println(sb.toString());
			o = (JSONObject) parser.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequestGET(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequestPOST(request, response);
	}

}
