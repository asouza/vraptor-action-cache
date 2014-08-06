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

	private final CachedWriter writer;
	private HttpServletResponse originalResponse;
	private Map<String,String> headers = new HashMap<>();

	public CharArrayWriterResponse(HttpServletResponse response) {
		super(response);
		this.originalResponse = response;
		try {
			this.writer = new CachedWriter(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	public String getOutput() {
		return writer.getOutput();
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
		super.addHeader(name, value);
		keepHeader(name, value);
	}
	
	@Override
	public void addDateHeader(String name, long date) {
		super.addDateHeader(name, date);
		keepHeader(name, String.valueOf(date));
	}
	
	@Override
	public void addIntHeader(String name, int value) {
		super.addIntHeader(name, value);
		keepHeader(name, String.valueOf(value));
	}
	
	@Override
	public void setDateHeader(String name, long date) {
		super.setDateHeader(name, date);
		keepHeader(name,String.valueOf(date));
	}

	@Override
	public void flushBuffer() throws IOException {
		super.flushBuffer();
		System.out.println("flushing");
	}

	@Override
	public void setIntHeader(String name, int value) {
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
