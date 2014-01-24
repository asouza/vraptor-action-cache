package br.com.caelum.vraptor.actioncache;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import br.com.caelum.vraptor.util.test.MockHttpServletResponse;

public class CharArrayWriterResponseTest {

	@Test
	public void shouldKeepHtmlInWriter() throws IOException{
		CharArrayWriterResponse customResponse = new CharArrayWriterResponse(new MockHttpServletResponse());
		customResponse.getWriter().print("<html><body>....</body></html>");
		assertEquals("<html><body>....</body></html>", customResponse.getOutput());
	}
	
	@Test
	public void shouldKeepResponseHeaders() {
		CharArrayWriterResponse customResponse = new CharArrayWriterResponse(new MockHttpServletResponse());
		String[] keys = {"setInt","setDate","addInt","addDate","addHeader"};
		customResponse.setIntHeader(keys[0],1);
		customResponse.setDateHeader(keys[1],Calendar.getInstance().getTimeInMillis());
		customResponse.addIntHeader(keys[2],1);
		customResponse.addDateHeader(keys[3],Calendar.getInstance().getTimeInMillis());
		customResponse.addHeader(keys[4], "value");
		
		for (String key : keys) {
			assertTrue(customResponse.getHeaders().containsKey(key));
		}
		
	}
}
