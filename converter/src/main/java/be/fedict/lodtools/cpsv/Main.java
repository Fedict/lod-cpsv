/*
 * Copyright (c) 2016, Bart Hanssens <bart.hanssens@fedict.be>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package be.fedict.lodtools.cpsv;

import be.fedict.lodtools.cpsv.proj.ProcedureProjection;
import be.fedict.lodtools.cpsv.proj.MunicipalityProjection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import java.text.SimpleDateFormat;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xmlbeam.XBProjector;

/**
 * Convert EDRL / business.belgium.be to Core Public Service Vocab RDF Triples.
 * 
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class Main {
	private final static Logger LOG = LoggerFactory.getLogger(Main.class);
 
	private final static XBProjector proj = new XBProjector();
	
    private final static ValueFactory F = SimpleValueFactory.getInstance();
	private final static SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
    
	private final static String DOM_BELGIF = "http://pubserv.belgif.be";
	private final static String PREFIX_LANG = "http://publications.europa.eu/resource/authority/language/";
	private final static String PREFIX_DDEIO = "http://dd.eionet.europa.eu/vocabulary/lau2/be/";
	private final static String PREFIX_CPSV = "http://purl.org/vocab/cpsv#";
	
    private static String domain = null;
    
	
	/**
	 * Create IRI identifier for service
	 * 
	 * @param id short code
	 * @return IRI
	 */
	private static IRI createID(String id) {
		return F.createIRI(DOM_BELGIF + "/fedict/" + id);
	}
	
	private static IRI createLangID(String code) {
		String term = "";
		switch(code) {
			case "NL": term = "NED"; break;
			case "FR": term = "FRA"; break;
			case "ENG": term = "ENG"; break;
			case "DE": term = "DEU"; break;
		}
		return F.createIRI(PREFIX_LANG + term);
	}
	
	private static IRI createCityID(String nis) {
		return F.createIRI(PREFIX_DDEIO + nis);
	}
	
	/**
	 * Process one of the EDRL / XML files, adding info to the RDF model.
	 * 
	 * @param f file to process
	 * @param m model
	 * @throws IOException 
	 */
	private static void processFile(File f, Model m) throws IOException {
		LOG.info("Reading XML file {}", f);
					
		ProcedureProjection p = proj.io().file(f).read(ProcedureProjection.class);
		if (p == null || p.getID() == null) {
			LOG.warn("Not a procedure");
			return;
		}
		IRI id = createID(p.getID());
		String lang = p.getLanguage().toLowerCase();
		
		m.add(id, RDFS.CLASS, F.createIRI(PREFIX_CPSV + "PublicService"));
		m.add(id, DCTERMS.TITLE, F.createLiteral(p.getTitle(), lang));
		m.add(id, DCTERMS.DESCRIPTION, F.createLiteral(p.getDesc(), lang));
		m.add(id, DCTERMS.ABSTRACT, F.createLiteral(p.getApplies(), lang));
		m.add(id, DCTERMS.LANGUAGE, createLangID(lang));
		
		for (MunicipalityProjection c: p.getCities()) {
			m.add(id, DCTERMS.SPATIAL, createCityID(c.getNisCode()));
		}
		
	//	m.add(id, DCTERMS.DESCRIPTION, F.createLiteral(p.getLanguage()));
	}
	
    /**
     * Main
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: cpsv <input_dir> <output_dir> [IRI_domain]");
            System.exit(-1);
        }
        
        File base = new File(args[0]);
        File outf = new File(args[1], "cpsv.nt");
        
        if (args.length > 2 && args[2].startsWith("http")) {
            domain = args[2];
        } else {
            domain = DOM_BELGIF;
        }
		
        LOG.info("--- START ---");
		LOG.info("Params in = {}, out = {}, domain = {}", base, outf, domain);
		
        try (BufferedWriter w = new BufferedWriter(new FileWriter(outf))){
			Model m = new LinkedHashModel();

			String [] langs = new String[]{ "NL", "FR", "EN", "DE" };
			File[] lst = new File(base, langs[0]).listFiles();

	        for(File f: lst) {
				for (String lang: langs) {
					File langDir = new File(base, lang);
					File file = new File(langDir, f.getName());
					processFile(file, m);
				}
            }
			Rio.write(m, w, RDFFormat.NTRIPLES);
        }
		LOG.info("--- END ---");
    }
}

