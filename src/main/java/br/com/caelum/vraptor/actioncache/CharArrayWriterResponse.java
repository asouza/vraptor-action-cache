package br.com.caelum.vraptor.actioncache;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.http.VRaptorResponse;

@Vetoed
public class CharArrayWriterResponse extends VRaptorResponse {

	private final CharArrayWriter charArray = new CharArrayWriter();
	private HttpServletResponse originalResponse;

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

}
