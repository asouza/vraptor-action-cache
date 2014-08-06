package br.com.caelum.vraptor.actioncache;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@Vetoed
public class CachedWriter extends PrintWriter {

	private final CharArrayWriter charArray = new CharArrayWriter();
	private final PrintWriter printWriter;

	public CachedWriter(HttpServletResponse response) throws IOException {
		super(response.getWriter());
		printWriter = new PrintWriter(charArray);
	}


	@Override
	public void write(char[] cbuf, int off, int len) {
		super.write(cbuf, off, len);
		printWriter.write(cbuf, off, len);
	}

	@Override
	public void flush() {
		super.flush();
		printWriter.flush();
	}

	@Override
	public void close() {
		super.close();
		printWriter.close();
	}

	@Override
	public void print(String s) {
		super.print(s);
		printWriter.print(s);
	}

	public String getOutput() {
		return charArray.toString();
	}
}
