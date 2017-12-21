/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.lodtools.cpsv;

import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bart.Hanssens
 */
public class EliMatcher {
	private final static Logger LOG = LoggerFactory.getLogger(EliMatcher.class);
	private final static Pattern p = 
				Pattern.compile("(Wet|Loi|Decreet|Décret) (van|de|du) " +
						"(\\d{1,2} \\w+ \\d{4}) (.*)");
		
	private final static String ELI = "https://id.belgium.be" +
								"/_query/eli/match?date={0}&type={1}&q={2}";
	
	private final static DateTimeFormatter DATE_FR = 
			DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("fr"));
	
	private final static DateTimeFormatter DATE_NL = 
			DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("nl"));
	
	/**
	 * Try to match framework using date, type and title
	 * 
	 * @param date publication date
	 * @param type document type
	 * @param title title of the document
	 * @return 
	 */
	private static IRI match(String date, String type, String title) {
		IRI matched = null;
		
		URLConnection conn;
		
		try {
			URL u = new URL(MessageFormat.format(ELI, date, type, title));
			conn = u.openConnection();
			conn.setRequestProperty(HttpHeaders.ACCEPT, 
									RDFFormat.NTRIPLES.getDefaultMIMEType());
			
			try (InputStream in = conn.getInputStream()) {
				Model m = Rio.parse(in, "http://pubserv.belgif.be", RDFFormat.NTRIPLES);
				Set<Resource> subjects = m.subjects();
				for(Resource s: subjects) {
					System.err.println(s.stringValue());
				}
			}
			
		} catch (MalformedURLException ex) {
			LOG.error("Could not build url");
		} catch (IOException ex) {
			LOG.error("Error matching: {}", ex);
		}
		return matched;
	}
	
	/**
	 * Parse string to date
	 * 
	 * @param str
	 * @return 
	 */
	private static String parseDate(String str) {
		LocalDate date = null;
		try {
			date = LocalDate.parse(str, DATE_FR);	
		} catch (DateTimeParseException dte) {
			try {
				date = LocalDate.parse(str, DATE_NL);
			} catch (DateTimeParseException dte2) {
				LOG.error("Exception in parsing {} to date", str);
			}
		}
		return (date != null) ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
	} 
	
	/**
	 * Find a match with
	 * 
	 * @param str
	 * @return matched
	 */
	public static IRI match(String str) {
		Matcher matcher = p.matcher(str);
		if (! matcher.matches()) {
			return null;
		}
		
		String m1 = matcher.group(1);
		String type = (m1.equals("Wet") || m1.equals("Loi")) ? "LAW" : "DECREE";
		
		String date = parseDate(matcher.group(3));
		String title = m1 + " " + matcher.group(4);
	
		return match(date, type, title);
	}
}
