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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
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
		
	private static String ELI = new String("https://id.belgium.be" +
								"/_query/eli/match?date={0}&type={1}&q={2}");
	
	private static DateTimeFormatter DATE_FR = 
			DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("fr"));
	
	private static DateTimeFormatter DATE_NL = 
			DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("nl"));
	
	
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
		
		String m3 = matcher.group(3);
		String date = parseDate(m3);
	
		System.err.println(str);
		System.err.println(type);
		System.err.println();
		
		String title = m1 + " " + matcher.group(4);
		
		IRI matched = null;
		
	/*
		try {
			

			URL u = new URL(MessageFormat.format(ELI, type, date, title));
			
			URLConnection conn = u.openConnection();
			conn.setRequestProperty(HttpHeaders.ACCEPT, RDFFormat.NTRIPLES.getDefaultMIMEType());
			InputStream in = conn.getInputStream();
			
			Model m = Rio.parse(in, "http://pubserv.belgif.be", RDFFormat.NTRIPLES);
			m.subjects();
		
		} catch (MalformedURLException ex) {
			LOG.error("Could not build url");
		} catch (IOException ex) {
			LOG.error("Error matching: {}", ex);
		}
	*/	return matched;
	}
}
