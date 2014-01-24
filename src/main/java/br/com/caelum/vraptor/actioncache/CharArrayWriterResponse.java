package br.com.caelum.vraptor.actioncache;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.http.VRaptorResponse;

@Vetoed
public class CharArrayWriterResponse extends VRaptorResponse {

	private final CharArrayWriter charArray = new CharArrayWriter();
	private HttpServletResponse originalResponse;
	private Map<String,String> headers = new HashMap<>();

	public CharArrayWriterResponse(HttpServletResponse response) {
		super(response);
		this.originalResponse = response;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(charArray);
	}

	public String getOutput() {
		return charArray.toString();
	}
	
	public HttpServletResponse delegate(){
		return this.originalResponse;
	}
	
	@Override
	public void setContentType(String type) {
		super.setContentType(type);
		keepHeader("Content-Type", type);
	}
	
	@Override
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		super.addHeader(name, value);
		keepHeader(name, value);
	}
	
	@Override
	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		super.addDateHeader(name, date);
		keepHeader(name, String.valueOf(date));
	}
	
	@Override
	public void addIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		super.addIntHeader(name, value);
		keepHeader(name, String.valueOf(value));
	}
	
	@Override
	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		super.setDateHeader(name, date);
		keepHeader(name,String.valueOf(date));
	}
	
	@Override
	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		super.setIntHeader(name, value);
		keepHeader(name, String.valueOf(value));
	}
	
	private void keepHeader(String name,String value){
		headers.put(name,value);
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}

}
