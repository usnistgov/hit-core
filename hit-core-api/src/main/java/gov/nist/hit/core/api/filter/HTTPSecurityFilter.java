package gov.nist.hit.core.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@WebFilter(urlPatterns = "*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HTTPSecurityFilter implements Filter {

	/*
	 * flaw: Browser Mime Sniffing - fix: X-Content-Type-Options flaw: Cached
	 * SSL Content - fix: Cache-Control flaw: Cross-Frame Scripting - fix:
	 * X-Frame-Options flaw: Cross-Site Scripting - fix: X-XSS-Protection flaw:
	 * Force SSL - fix: Strict-Transport-Security
	 * 
	 * assure no-cache for login page to prevent IE from caching
	 */

	protected final Log logger = LogFactory.getLog(getClass());

	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (response instanceof HttpServletResponse) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
				httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				httpResponse.setHeader("X-Content-Type-Options", "nosniff");
				httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
				httpResponse.setHeader("Access-Control-Allow-Origin", "*");
				httpResponse.setHeader("Vary", "Origin");
				httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data: *.nist.gov:* ; style-src 'self' 'unsafe-inline' 'unsafe-eval' ; font-src 'self'; frame-src 'self'; object-src 'self'");
				httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
			    httpResponse.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
			    httpResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,appVersion,rsbVersion");
 		}

		chain.doFilter(request, response);

	}

}
