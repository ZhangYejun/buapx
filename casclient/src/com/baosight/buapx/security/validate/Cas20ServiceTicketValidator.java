package com.baosight.buapx.security.validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

import com.baosight.buapx.security.common.ConstString;


public class Cas20ServiceTicketValidator  implements TicketValidator{
	/**
     * Commons Logging instance.
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * Hostname verifier used when making an SSL request to the CAS server.
     */
    protected HostnameVerifier hostnameVerifier;

    /**
     * Prefix for the CAS server.   Should be everything up to the url endpoint, including the /.
     *
     * i.e. https://cas.rutgers.edu/
     */
    private final String casServerUrlPrefix;

    /**
     * Whether the request include a renew or not.
     */
    private boolean renew;

    /**
     * A map containing custom parameters to pass to the validation url.
     */
    private Map customParameters;

    private String encoding;


    public final void setCustomParameters(final Map customParameters) {
        this.customParameters = customParameters;
    }
    public Cas20ServiceTicketValidator(String casServerUrl){
    	this.casServerUrlPrefix=casServerUrl;
    }

	public Assertion validate(String ticket, String service)
			throws TicketValidationException {
		final String validationUrl = constructValidationUrl(ticket, service);
		 if (log.isDebugEnabled()) {
	            log.debug("Constructing validation url: " + validationUrl);
	        }

	        log.debug("Retrieving response from server.");
			final String serverResponse = getResponseFromServer(validationUrl,"UTF-8");

			if (serverResponse == null) {
			    throw new TicketValidationException("The CAS server returned no response.");
			}

			if (log.isDebugEnabled()) {
				log.debug("Server response: " + serverResponse);
			}

			return parseResponseFromServer(serverResponse);
	}

	 protected final String encodeUrl(final String url) {
	    	if (url == null) {
	    		return null;
	    	}

	        try {
	            return URLEncoder.encode(url, "UTF-8");
	        } catch (final UnsupportedEncodingException e) {
	            return url;
	        }
	    }
	 protected final String constructValidationUrl(final String ticket, final String serviceUrl) {
	        final Map urlParameters = new HashMap();

	        log.debug("Placing URL parameters in map.");
	        urlParameters.put("ticket", ticket);
	        urlParameters.put("service", encodeUrl(serviceUrl));

	        if (this.renew) {
	            urlParameters.put("renew", "true");
	        }

	        log.debug("Calling template URL attribute map.");

	        log.debug("Loading custom parameters from configuration.");
	        if (this.customParameters != null) {
	            urlParameters.putAll(this.customParameters);
	        }

	        final String suffix = getUrlSuffix();
	        final StringBuffer buffer = new StringBuffer(urlParameters.size()*10 + this.casServerUrlPrefix.length() + suffix.length() +1);

	        int i = 0;

	        buffer.append(this.casServerUrlPrefix);
	        if (!this.casServerUrlPrefix.endsWith("/")) {
	            buffer.append("/");
	        }
	        buffer.append(suffix);

	        Iterator it=urlParameters.entrySet().iterator();
	        Map.Entry entry=null;
	        while(it.hasNext()){
	        	entry=(Entry) it.next();
	        	final String key = (String) entry.getKey();
	            final String value = (String) entry.getValue();

	            if (value != null) {
	                buffer.append(i++ == 0 ? "?" : "&");
	                buffer.append(key);
	                buffer.append("=");
	                buffer.append(value);
	            }
	        }

	        return buffer.toString();

	    }
	  protected String getUrlSuffix() {
	        return "serviceValidate";
	    }


	  protected final Assertion parseResponseFromServer(final String response) throws TicketValidationException {
	        final String error = XmlUtils.getTextForElement(response,
	                "authenticationFailure");

	        if (CommonUtils.isNotBlank(error)) {
	            throw new TicketValidationException(error);
	        }

	        final String principal = XmlUtils.getTextForElement(response, "user");
	      /*  final String proxyGrantingTicketIou = XmlUtils.getTextForElement(
	                response, "proxyGrantingTicket");
	        final String proxyGrantingTicket = this.proxyGrantingTicketStorage != null ? this.proxyGrantingTicketStorage.retrieve(proxyGrantingTicketIou) : null;*/

	        if (CommonUtils.isEmpty(principal)) {
	            throw new TicketValidationException("No principal was found in the response from the CAS server.");
	        }

	        final Assertion assertion;
	        final Map attributes = extractCustomAttributes(response);
	        attributes.put(ConstString.CAS_USER_ATTRIBUTE_NAME, XmlUtils.getTextForElement(response, "casuser"));
	        attributes.put(ConstString.CAS_USERTYPE_ATTRIBUTE_NAME, XmlUtils.getTextForElement(response, "userType"));
	        assertion = new AssertionImpl(new AttributePrincipalImpl(principal, attributes));
	        return assertion;
	    }

	  /**
	     * Default attribute parsing of attributes that look like the following:
	     * &lt;cas:attributes&gt;
	     *  &lt;cas:attribute1&gt;value&lt;/cas:attribute1&gt;
	     *  &lt;cas:attribute2&gt;value&lt;/cas:attribute2&gt;
	     * &lt;/cas:attributes&gt;
	     * <p>
	     * This code is here merely for sample/demonstration purposes for those wishing to modify the CAS2 protocol.  You'll
	     * probably want a more robust implementation or to use SAML 1.1
	     *
	     * @param xml the XML to parse.
	     * @return the map of attributes.
	     */
	    protected Map extractCustomAttributes(final String xml) {
	    	final int pos1 = xml.indexOf("<cas:attributes>");
	    	final int pos2 = xml.indexOf("</cas:attributes>");

	    	if (pos1 == -1) {
	    		return new HashMap();
	    	}

	    	final String attributesText = xml.substring(pos1+16, pos2);

	    	final Map attributes = new HashMap();
	    	final BufferedReader br = new BufferedReader(new StringReader(attributesText));

	    	String line;
	    	final List attributeNames = new ArrayList();
	    	try {
		    	while ((line = br.readLine()) != null) {
		    		final String trimmedLine = line.trim();
		    		if (trimmedLine.length() > 0) {
			    		final int leftPos = trimmedLine.indexOf(":");
			    		final int rightPos = trimmedLine.indexOf(">");
			    		attributeNames.add(trimmedLine.substring(leftPos+1, rightPos));
		    		}
		    	}
		    	br.close();
	    	} catch (final IOException e) {
	    		//ignore
	    	}
	      String name=null;
	        for (int i=0;i<attributeNames.size();i++) {
	        	name=(String) attributeNames.get(i);
	            final List values = XmlUtils.getTextForElements(xml, name);

	            if (values.size() == 1) {
	                attributes.put(name, values.get(0));
	            } else {
	                attributes.put(name, values);
	            }
	    	}

	    	return attributes;
	    }

	    public static String getResponseFromServer(String constructedUrl, String encoding) {
			URLConnection conn = null;
			try {
				URL url = new URL(constructedUrl);
				conn = url.openConnection();
				BufferedReader in;
				if (StringUtils.isEmpty(encoding))
					in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				else {
					in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
				}

				StringBuffer stringBuffer = new StringBuffer(255);
				String line;
				while ((line = in.readLine()) != null) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
				}
				String str1 = stringBuffer.toString();
				return str1;
			} catch (MalformedURLException e) {
				//log.debug(e);
				throw new RuntimeException(e);
			} catch (IOException e) {
				//log.debug(e);
				throw new RuntimeException(e);
			} finally {
				if ((conn != null) && ((conn instanceof HttpURLConnection)))
					((HttpURLConnection) conn).disconnect();
			}
		}

}
