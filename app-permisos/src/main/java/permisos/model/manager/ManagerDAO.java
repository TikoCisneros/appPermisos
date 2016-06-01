package permisos.model.manager;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Objeto que encapsula la logica basica de acceso a datos mediante JPA. Maneja el patron de dise帽o
 * singleton para administrar los componentes EntityManagerFactory y EntityManager. 
 * @author mrea
 *
 */
@Stateless
public class ManagerDAO {
	//private static EntityManagerFactory factory;

        @PersistenceContext
        EntityManager em;

	/**
	 * Metodo basico para mostrar mensajes de depuracion.
	 * @param nombreMetodo Metodo que genera el mensaje de depuracion.
	 * @param mensaje El mensaje a desplegar.
	 */
	public void mostrarLog(String nombreMetodo,String mensaje) {
		System.out.println("["+this.getClass().getSimpleName()+"/"+nombreMetodo+"]: " + mensaje);
	}

	/**
	 * finder Generico que devuelve todos las entidades de una tabla.
	 * 
	 * @param clase
	 *            La clase que se desea consultar.
	 * @param orderBy
	 *            Propiedad de la entidad por la que se desea ordenar la
	 *            consulta.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findAll(Class clase, String orderBy) {
		mostrarLog("findAll",clase.getSimpleName() + " orderBy " + orderBy);
		Query q;
		List listado;
//		if (!em.getTransaction().isActive()){
//			em.getTransaction().begin();
//		}
		if (orderBy == null || orderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName() + " o");
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o ORDER BY " + orderBy);
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		listado = q.getResultList(); 
//		if(em.getTransaction().isActive()){
//			em.getTransaction().commit();
//		}
		return listado;
	}
	
	/**
	 * finder Gen閞ico que devuelve todos las entidades de una tabla. Esta versi髇
	 * no cierra la transacci髇, para que el Manager que la invoque lo haga.
	 * 
	 * @param clase
	 *            La clase que se desea consultar.
	 * @param orderBy
	 *            Propiedad de la entidad por la que se desea ordenar la
	 *            consulta.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findAllT(Class clase, String orderBy) {
		mostrarLog("findAllT",clase.getSimpleName() + " orderBy " + orderBy);
		Query q;
		List listado;
//		if (!em.getTransaction().isActive()){
//			em.getTransaction().begin();
//			mostrarLog("findAllT","transaccion begin");
//		}
		if (orderBy == null || orderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName() + " o");
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o ORDER BY " + orderBy);
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		listado = q.getResultList();  
		/*if(!em.getTransaction().isActive())
			em.getTransaction().commit();*/
		return listado;
	}

	/**
	 * finder Generico que devuelve todos las entidades de una tabla.
	 * 
	 * @param clase
	 *            La clase que se desea consultar.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findAll(Class clase) {
		mostrarLog("findAll", clase.getSimpleName());
		Query q;
		List listado;
//		if (!em.getTransaction().isActive()){
//			em.getTransaction().begin();
//		}
		q = em.createQuery("SELECT o FROM " + clase.getSimpleName() + " o");
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		listado = q.getResultList(); 
		//em.getTransaction().commit();
		return listado;
	}

	/**
	 * Finder generico que permite aplicar clausulas where y order by.
	 * 
	 * @param clase
	 *            La entidad sobre la que se desea consultar.
	 * @param pClausulaWhere
	 *            Clausula where de tipo JPQL (sin la palabra reservada WHERE).
	 * @param pOrderBy
	 *            Clausula order by de tipo JPQL (sin la palabra reservada ORDER
	 *            BY). Puede ser null.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findWhere(Class clase, String pClausulaWhere, String pOrderBy) {
		Query q;
		List listado;
		
                if (pOrderBy != null && !pOrderBy.isEmpty()){
                    mostrarLog("findWhere", clase.getSimpleName()+" where "+pClausulaWhere+" order by "+pOrderBy);
                    q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere + " ORDER BY " + pOrderBy);
                    
                }else{
                    mostrarLog("findWhere", clase.getSimpleName()+" where "+pClausulaWhere);
                    q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere);
                    
                }
			
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		listado = q.getResultList(); 
		mostrarLog("findWhere",  "Resultado del Listado es: " + listado.size());
		return listado;
	}
	
	/**
	 * Finder generico que permite aplicar clausulas where y order by. Esta versi贸n
	 * no cierra la transacci贸n, para que el Manager que la invoque lo haga.
	 * 
	 * @param clase
	 *            La entidad sobre la que se desea consultar.
	 * @param pClausulaWhere
	 *            Clausula where de tipo JPQL (sin la palabra reservada WHERE).
	 * @param pOrderBy
	 *            Clausula order by de tipo JPQL (sin la palabra reservada ORDER
	 *            BY). Puede ser null.
	 * @return Listado resultante.
	 */
	@SuppressWarnings("rawtypes")
	public List findWhereT(Class clase, String pClausulaWhere, String pOrderBy) {
		mostrarLog("findWhereT", clase.getSimpleName()+" where "+pClausulaWhere+"order by "+pOrderBy);
		Query q;
		List listado;
		if (pOrderBy == null || pOrderBy.length() == 0)
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere);
		else
			q = em.createQuery("SELECT o FROM " + clase.getSimpleName()
					+ " o WHERE " + pClausulaWhere + " ORDER BY " + pOrderBy);
		q.setHint("javax.persistence.cache.storeMode", "REFRESH");
		listado = q.getResultList(); 
		//em.getTransaction().commit();
		return listado;
	}
	
	/**
	 * Finder generico que permite ejecutar cualquier sentencia JPQL. Esta versi贸n
	 * no cierra la transacci贸n, para que el Manager que la invoque lo haga.
	 * 
	 * @param pClausulaJPQL Sentencia JPQL que se va a ejecutar.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findJPQLT(String pClausulaJPQL) {
		mostrarLog("findSQLT", pClausulaJPQL);
		Query q;
		List listado;
		q = em.createQuery(pClausulaJPQL);
		listado = q.getResultList(); 
		//em.getTransaction().commit();
		return listado;
	}
	
	@SuppressWarnings("rawtypes")
	public List findJPQL(String pClausulaJPQL) {
		mostrarLog("findSQL", pClausulaJPQL);
		Query q;
		List listado;
		//if (!em.getTransaction().isActive())
		//	em.getTransaction().begin();
		q = em.createQuery(pClausulaJPQL);
		listado = q.getResultList(); 
		//em.getTransaction().commit();
		return listado;
	}
	
	@SuppressWarnings("rawtypes")
	public List findSQL(String pClausulaSQL) {
		mostrarLog("findSQL", pClausulaSQL);
		Query q;
		List listado;
		q = em.createNativeQuery(pClausulaSQL);
		listado = q.getResultList(); 
		return listado;
	}
	
	/**
	 * Ejecutar DELETE AND UPDATE
	 * @param pClausulaJPQL
	 */
	public void ejectJPQL(String pClausulaJPQL){
		mostrarLog("ejectJPQL", pClausulaJPQL);
		Query q;
		q = em.createQuery(pClausulaJPQL);
		q.executeUpdate();

	}
	
	/**
	 * Ejecuta una consulta JPQL para encontrar un solo resultado
	 * @param pClausulaJPQL
	 * @return
	 */
	public Object singleJPQL(String pClausulaJPQL){
		mostrarLog("singleJPQL", pClausulaJPQL);
		Query q;
		q = em.createQuery(pClausulaJPQL);
		Object result = (Object) q.getSingleResult();

    	return result;
    }
	
	//	function ejecutarvarias (list<sql>)
	//	try
	//		begin
	//		for i to list
	//			ejectJPQL list<i>
	//		end for
	//		commit
	//	cath
	//		rollback
	
	/**
	 * Ejecuta una sentencia sql nativa UPDATE DELETE ALTER_SEQUENCE
	 * @param nativeSQL
	 */
	public void ejectNativeSQL(String nativeSQL){
		mostrarLog("ejectNativeSQL", nativeSQL);
		Query q;
		q = em.createNativeQuery(nativeSQL);
		q.executeUpdate();

	}
	
	/**
	 * Finder generico para buscar un objeto especifico.
	 * 
	 * @param clase
	 *            La clase sobre la que se desea consultar.
	 * @param pID
	 *            Identificador que permitira la busqueda.
	 * @return El objeto solicitado (si existiera).
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findById(Class clase, Object pID) throws Exception {
		mostrarLog("findById", clase.getSimpleName()+" : "+pID);
		if (pID == null)
			throw new Exception(
					"Debe especificar el codigo para buscar el dato.");
		Object o;
		try {
			o = em.find(clase, pID);
		} catch (Exception e) {

			throw new Exception("No se encontro la informacion especificada: "
					+ e.getMessage());
		}

		return o;
	}

	/**
	 * Almacena un objeto en la persistencia.
	 * 
	 * @param pObjeto El objeto a insertar.
	 * @throws Exception
	 */
	public void insertar(Object pObjeto) throws Exception {
		mostrarLog("insertar", pObjeto.getClass().getSimpleName() +" : "+pObjeto);
		try {
			em.persist(pObjeto);
			mostrarLog("insertar","Objeto insertado: "
					+ pObjeto.getClass().getSimpleName() + " " +pObjeto);
		} catch (Exception e) {
			mostrarLog("insertar","No se pudo insertar el objeto especificado: "
					+ pObjeto.getClass().getSimpleName()+" "+pObjeto);
			
			mostrarLog("insertar","transaccion rollback");
			throw new Exception("No se pudo insertar el objeto especificado: "
					+ e.getMessage());
		}		
		mostrarLog("insertar","transaccion commit");
	}

	/**
	 * Elimina un objeto de la persistencia.
	 * 
	 * @param clase
	 *            La clase correspondiente al objeto que se desea eliminar.
	 * @param pID
	 *            El identificador del objeto que se desea eliminar.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void eliminar(Class clase, Object pID) throws Exception {
		if (pID == null) {
			mostrarLog("eliminar","Debe especificar un identificador para eliminar el dato solicitado: "
					+ clase.getSimpleName()+" : "+pID);
			throw new Exception(
					"Debe especificar un identificador para eliminar el dato solicitado.");
		}
		Object o = findById(clase, pID);		
		try {
			em.remove(o);
			mostrarLog("eliminar","Dato eliminado: " + clase.getSimpleName() + " : "
					+ pID.toString());
		} catch (Exception e) {
//			
			mostrarLog("eliminar","No se pudo eliminar el dato: " + clase.getSimpleName()+" : "+pID);
			throw new Exception("No se pudo eliminar el dato: " + e.getMessage());
		}				
	}

	/**
	 * Actualiza la informacion de un objeto en la persistencia.
	 * 
	 * @param pObjeto Objeto que contiene la informacion que se debe actualizar.
	 * @throws Exception
	 */
	public void actualizar(Object pObjeto) throws Exception {
		if (pObjeto == null)
			throw new EJBException("No se puede actualizar un dato null");		
		try {
			em.merge(pObjeto);
			mostrarLog("actualizar","Dato actualizado: "
					+ pObjeto.getClass().getSimpleName()+" : "+pObjeto);
		} catch (Exception e) {
			//
			throw new Exception("No se pudo actualizar el dato: "
					+ e.getMessage());
		}
	}

	public EntityManager getEntityManager() {
		return em;
	}
	
	/**
     * Finder generico para buscar un objeto especifico por una columna
     * especificada.
     *
     * @param clase La clase sobre la que se desea consultar.
     * @param param columna de busqueda.
     * @param value valor de parametro de busqueda.
     * @param orderBy Expresion que indica la propiedad de la entidad por la que
     * se desea ordenar la consulta. Debe utilizar el alias "o" para nombrar a
     * la(s) propiedad(es) por la que se va a ordenar. Puede aceptar null o una
     * cadena vacia, en este caso no ordenara el resultado.
     * @return Lista de objetos solicitados (si existieran).
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes"})
    public List findByParam(Class clase, String param, String value, String orderBy) throws Exception {
        Query q;
        List listado;
        String sentenciaSQL;
        if (orderBy == null || orderBy.length() == 0) {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE " + param + "=:value1";
        } else {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName()
                    + " o WHERE " + param + "=:value1" + " ORDER BY " + orderBy;
        }
        q = em.createQuery(sentenciaSQL).setParameter("value1", value);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        listado = q.getResultList(); 
        return listado;
    }
    
    @SuppressWarnings({"rawtypes"})
    public List findByParam(Class clase, String param, int value, String orderBy) throws Exception {
        Query q;
        List listado;
        String sentenciaSQL;
        if (orderBy == null || orderBy.length() == 0) {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE " + param + "=:value1";
        } else {
            sentenciaSQL = "SELECT o FROM " + clase.getSimpleName()
                    + " o WHERE " + param + "=:value1" + " ORDER BY " + orderBy;
        }
        q = em.createQuery(sentenciaSQL).setParameter("value1", value);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        listado = q.getResultList(); 
        return listado;
    }
    
    /**
     * Ejecuta una b鷖queta analizando una fecha
     * @param clase
     * @param where
     * @param param
     * @param date
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findWhereDate(Class clase, String where, String param, Timestamp date){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+param+" >= :date";
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND "+param+" >= :date";
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("date", date, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDate",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findBeetweenDate(Class clase, String where, String datei, String datef, Timestamp fecha){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE :fecha BETWEEN "+datei+" AND "+datef;
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND :fecha BETWEEN "+datei+" AND "+datef;
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fecha", fecha, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDate",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findBetweenDates(Class clase, String where, String date, Timestamp fechaUno, Timestamp fechaDos){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+date+" BETWEEN :fechaUno AND :fechaDos";
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND "+date+" BETWEEN :fechaUno AND :fechaDos";
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fechaUno", fechaUno, TemporalType.DATE).setParameter("fechaDos", fechaDos, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDates",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findDatesBetweenDates(Class clase, String where, String dateOne, String dateTwo, Timestamp fechaUno, Timestamp fechaDos){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE ( :fechaUno BETWEEN "+dateOne+" AND "+dateTwo+" )"
    				+" OR ( :fechaDos BETWEEN "+dateOne+" AND "+dateTwo+" )";
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where
    				+" AND ( :fechaUno BETWEEN "+dateOne+" AND "+dateTwo+" )"
    				+" OR ( :fechaDos BETWEEN "+dateOne+" AND "+dateTwo+" )";
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fechaUno", fechaUno, TemporalType.DATE).setParameter("fechaDos", fechaDos, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDates",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findBetweenDates(Class clase, String where, String date, Date fechaUno, Date fechaDos, String orderBy){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		if (orderBy == null || orderBy.length() == 0)
    			sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+date+" BETWEEN :fechaUno AND :fechaDos";
    		else
    			sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+date+" BETWEEN :fechaUno AND :fechaDos ORDER BY "+orderBy;
    	}else{
    		if (orderBy == null || orderBy.length() == 0)
    			sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND "+date+" BETWEEN :fechaUno AND :fechaDos";
    		else
    			sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND "+date+" BETWEEN :fechaUno AND :fechaDos ORDER BY "+orderBy;
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fechaUno", fechaUno, TemporalType.DATE).setParameter("fechaDos", fechaDos, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDates",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findWhereDate(Class clase, String where, String param, Date date){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+param+" >= :date";
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND "+param+" >= :date";
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("date", date, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDate",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findBeetweenDate(Class clase, String where, String datei, String datef, Date fecha){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE :fecha BETWEEN "+datei+" AND "+datef;
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND :fecha BETWEEN "+datei+" AND "+datef;
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fecha", fecha, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDate",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findBetweenDates(Class clase, String where, String date, Date fechaUno, Date fechaDos){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+date+" BETWEEN :fechaUno AND :fechaDos";
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where+" AND "+date+" BETWEEN :fechaUno AND :fechaDos";
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fechaUno", fechaUno, TemporalType.DATE).setParameter("fechaDos", fechaDos, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDates",q.toString());
    	return listado;
    }
    
    @SuppressWarnings("rawtypes")
    public List findDatesBetweenDates(Class clase, String where, String dateOne, String dateTwo, Date fechaUno, Date fechaDos){
    	Query q;
    	List listado;
    	String sentenciaSQL;
    	if(where == null || where.length() == 0){
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE ( :fechaUno BETWEEN "+dateOne+" AND "+dateTwo
    				+" OR :fechaDos BETWEEN "+dateOne+" AND "+dateTwo+" )";
    	}else{
    		sentenciaSQL = "SELECT o FROM " + clase.getSimpleName() + " o WHERE "+where
    				+" AND ( :fechaUno BETWEEN "+dateOne+" AND "+dateTwo
    				+" OR :fechaDos BETWEEN "+dateOne+" AND "+dateTwo+" )";
    	}
    	q = em.createQuery(sentenciaSQL).setParameter("fechaUno", fechaUno, TemporalType.DATE).setParameter("fechaDos", fechaDos, TemporalType.DATE);
    	q.setHint("javax.persistence.cache.storeMode", "REFRESH");
    	listado = q.getResultList(); 
    	mostrarLog("findWhereDates",q.toString());
    	return listado;
    }
    
    /**
    * Permite tomar un valor de una consulta JPQL
    * @param pClausulaJPQL
    * @return String (valor)
    */
   @SuppressWarnings("rawtypes")
   public String tomarValorStrJPQL(String pClausulaJPQL) {
           mostrarLog("findSQL", pClausulaJPQL);
           Query q;
           String valor;
           q = em.createQuery(pClausulaJPQL);
           List lista = q.getResultList();
           if(!lista.isEmpty())
                valor = (String) lista.get(0);
           else
                valor = "";
           return valor;
   }
   
   @SuppressWarnings("rawtypes")
    public Integer tomarValorIntJPQL(String pClausulaJPQL) {
            mostrarLog("findSQL", pClausulaJPQL);
            Query q;
            Integer valor;
            q = em.createQuery(pClausulaJPQL);
            List lista = q.getResultList();
            if(!lista.isEmpty())
                valor = (Integer) lista.get(0);
            else
                valor = null;
            return valor;
    }
}
