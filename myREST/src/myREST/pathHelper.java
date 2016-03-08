package myREST;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;



@Path("/")
//http://localhost:8080/myREST/
public class pathHelper {
	//http://localhost:8080/myREST/echo/hello?q=query
	@Path("/echo/{path}")
	@GET 
	@Consumes("text/plain")

	@Resource 
	public String echo(@PathParam("path") String path,
			@DefaultValue("q") @QueryParam("q") String q,
			@Context HttpServletRequest request)
	{
		String echo = null;
		try {
			echo = path+" "+splitQueryJSON(request.getQueryString());
		} catch (UnsupportedEncodingException | JSONException e) {
			echo = "Error in parsing"+ e.toString();
		}
		return echo;
	}
	public JSONObject splitQueryJSON(String query) throws UnsupportedEncodingException, JSONException{
		JSONObject json = new JSONObject();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			json.put(key, value);
		}
		return json;
	}
	public static Map<String, List<String>> splitQuery(String query) throws UnsupportedEncodingException {
		final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
		final String[] pairs = query.split("&");
		for (String pair : pairs) {
			final int idx = pair.indexOf("=");
			final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			if (!query_pairs.containsKey(key)) {
				query_pairs.put(key, new LinkedList<String>());
			}
			final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			query_pairs.get(key).add(value);
		}
		return query_pairs;
	}
	//http://localhost:8080/myREST/echo/hello?q=query
	@Path("/info")
	@GET 
	@Produces(MediaType.APPLICATION_JSON)

	@Resource 
	public JSONObject Info(@Context HttpServletRequest request)
	{	
		JSONObject json = new JSONObject(); 
		try {
			Enumeration<?> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				json.put(key, value);
			}
			json.put("IP", (request.getHeader("X-FORWARDED-FOR") == null) ? request.getRemoteAddr():request.getHeader("X-FORWARDED-FOR"));
			//http://getcitydetails.geobytes.com/GetCityDetails?fqcn=0.0.0.0
			//json.put("user-agent", request.getHeader("user-agent"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
