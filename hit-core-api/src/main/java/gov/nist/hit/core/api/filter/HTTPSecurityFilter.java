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

import gov.nist.hit.core.api.util.NonceGenerator;

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

		String scriptHashes = "'sha256-f9h3HRjL1VUqG5+6Pw2xrc2lksENHrSDJStWs/Qrh04='";//"'sha256-MwG2cQ2U9AYTcxzDnJ272QPMX6fhfnMgrSKJs25/zWw=' 'sha256-HSx22tN7dkvAJ5oPJefULXe6d/M6XWfIzGWpAGVQiaY=' 'sha256-MwG2cQ2U9AYTcxzDnJ272QPMX6fhfnMgrSKJs25/zWw='";
		String styleHashes = "";//"'sha256-l8f2Ogvw1E5G8aO3MY9EK5zg260DdyPZ9Dcl0zLLP4c=' 'sha256-HSx22tN7dkvAJ5oPJefULXe6d/M6XWfIzGWpAGVQiaY='";

		
		if (response instanceof HttpServletResponse) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				String nonce = NonceGenerator.generateNonce();
		        request.setAttribute("nonce", nonce);
		        
				httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
				httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				httpResponse.setHeader("X-Content-Type-Options", "nosniff");
				httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
//				httpResponse.setHeader("Access-Control-Allow-Origin", "");
				httpResponse.setHeader("Vary", "Origin");
				//old 
				httpResponse.setHeader("Content-Security-Policy", "default-src 'self';  script-src 'self' https://*.googletagmanager.com https://www.youtube.com https://www.google-analytics.com https://ssl.google-analytics.com https://dap.digitalgov.gov 'unsafe-inline' 'unsafe-eval'; img-src 'self' https://*.google-analytics.com https://*.analytics.google.com https://*.googletagmanager.com https://*.g.doubleclick.net https://*.google.com data: *.nist.gov:* ; style-src 'self' 'unsafe-inline' 'unsafe-eval' ; font-src 'self'; frame-src 'self'; object-src 'self'; connect-src 'self' https://*.google-analytics.com https://*.analytics.google.com https://*.googletagmanager.com https://*.g.doubleclick.net https://*.google.com https://dap.digitalgov.gov");

				//new
//				httpResponse.setHeader("Content-Security-Policy", "default-src 'self';  script-src 'self'  https://www.youtube.com  https://www.googletagmanager.com https://googletagmanager.com https://dap.digitalgov.gov 'unsafe-hashes' "+scriptHashes+"; img-src 'self' data: https://*.google-analytics.com https://*.googletagmanager.com https://*.g.doubleclick.net https://*.google.com ; style-src 'self' 'unsafe-inline' 'unsafe-eval' "+styleHashes+"; font-src 'self'; frame-src 'self' https://td.doubleclick.net https://www.googletagmanager.com; object-src 'none'; connect-src 'self' https://*.google-analytics.com https://*.googletagmanager.com https://*.g.doubleclick.net https://*.google.com  https://pagead2.googlesyndication.com https://dap.digitalgov.gov");
				httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
			    httpResponse.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
			    httpResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,appVersion,rsbVersion");
			    
			    httpResponse.setHeader("X-CSP-Nonce", nonce); // Pass nonce to client
			    httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
 		}
		

		chain.doFilter(request, response);

	}

}
