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

import be.fedict.lodtools.cpsv.proj.ActivityProjection;
import be.fedict.lodtools.cpsv.proj.AddressProjection;
import be.fedict.lodtools.cpsv.proj.ProcedureProjection;
import be.fedict.lodtools.cpsv.proj.MunicipalityProjection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.security.MessageDigest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;

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
    private static String domain = null;
     
	private static Set<IRI> Activities = new HashSet();
	
	/**
	 * Create IRI identifier for service
	 * 
	 * @param id short code
	 * @return IRI
	 */
	private static IRI serviceID(String id) {
		return F.createIRI(DOM_BELGIF + "/service/" + id);
	}
	
	/**
	 * Create IRI identifier for activity
	 * 
	 * @param sector sector code
	 * @param activity activity code
	 * @return IRI
	 */
	private static IRI sectorID(String sector, String activity) {
		return F.createIRI(DOM_BELGIF + "/sector/" 
				+ sector.substring(0, 2) + "/" + activity.substring(0, 2));
	}
	
	/**
	 * Create IRI for a cost 
	 * 
	 * @param cost text
	 * @return IRI
	 */
	private static IRI costID(String cost) {
		// Try to map different wordings of "free" to the same IRI
		String id = Consts.FREE.contains(cost) ? "zero" : DigestUtils.sha1Hex(cost);
		return F.createIRI(DOM_BELGIF + "/cost/" + id);
	}
	/*
	private static IRI legalBase(String id) {
		
	}*/
	/**
	 * Create language IRI identifier
	 * 
	 * @param code short language code
	 * @return IRI
	 */
	private static IRI createLangID(String code) {
		String term = "";
		switch(code) {
			case "NL": term = "NED"; break;
			case "FR": term = "FRA"; break;
			case "EN": term = "ENG"; break;
			case "DE": term = "DEU"; break;
		}
		return F.createIRI(Consts.PREFIX_LANG + term);
	}
	
	private static IRI lifecycleID(String code) {
		String term = "";
		switch(code) {
			case "START":
			case "ACTIVE":
			case "STOP":
				term = code;
				break;
		}
		return F.createIRI(Consts.PREFIX_LIFE + term);
	}
	/**
	 * Process the list of cities and match to a complete region(s) in Belgium.
	 * 
	 * @param p list of mapped municipalities
	 */
	private static List<IRI> regionalize(List<MunicipalityProjection> p) {
		List<IRI> regions = new ArrayList<>();
		// cheating: just count them
		switch (p.size()) {
			case 9:
				regions.add(Consts.ID_GER);
				break;
			case 19:
				regions.add(Consts.ID_BXL);
				break;
			case 253:
				regions.add(Consts.ID_WAL_EX_GER);
				break;
			case 262:
				regions.add(Consts.ID_WAL);
				break;
			case 281:
				regions.add(Consts.ID_BXL);
				regions.add(Consts.ID_WAL);
				break;
			case 308: 
				regions.add(Consts.ID_VLA);
				break;
			case 327:
				regions.add(Consts.ID_BXL);
				regions.add(Consts.ID_VLA);
				break;
			case 589: 
				regions.add(Consts.ID_BXL);
				regions.add(Consts.ID_VLA);
				regions.add(Consts.ID_WAL);
				break;
			default:
				LOG.error("Not found for {}", p.size());
		}
		return regions;
	}
	
	/**
	 * Process one of the EDRL / XML files, adding info to the RDF model.
	 * 
	 * @param f file to process
	 * @param m RDF model
	 * @throws IOException 
	 */
	private static void processFile(File f, Model m) throws IOException {
		LOG.info("Reading XML file {}", f);
					
		ProcedureProjection p = proj.io().file(f).read(ProcedureProjection.class);
		if (p == null || p.getID() == null) {
			LOG.warn("Not a procedure");
			return;
		}
		IRI id = serviceID(p.getID());
		String lang = p.getLanguage().toLowerCase();
		
		m.add(id, RDFS.CLASS, Consts.CLASS_CPSV);
		m.add(id, DCTERMS.TITLE, F.createLiteral(p.getTitle(), lang));
		m.add(id, DCTERMS.DESCRIPTION, F.createLiteral(p.getDesc(), lang));
		m.add(id, DCTERMS.ABSTRACT, F.createLiteral(p.getApplies(), lang));
		m.add(id, DCTERMS.LANGUAGE, createLangID(lang));

		for (IRI region: regionalize(p.getCities())) {
			m.add(id, DCTERMS.SPATIAL, region);
		}
		
		String price = p.getPrice();
		IRI cost = costID(price);
		m.add(id, Consts.HAS_COST, cost);
		m.add(cost, RDFS.CLASS, Consts.CLASS_COST);
		m.add(cost, DCTERMS.DESCRIPTION, F.createLiteral(price, lang));
		
		String event = p.getLifecycle();
		IRI cycle = lifecycleID(event);
		m.add(id, Consts.GROUPED_BY, cycle);
		
		//System.err.println(p.getFormalities());
	
		for (ActivityProjection a: p.getActivities()) {
			IRI activity = sectorID(a.getSector(), a.getCode());
			Activities.add(activity);
			m.add(id, Consts.HAS_SECTOR, activity);
		}
	
		AddressProjection a = p.getAdministration().getAddress();
		if (a != null) {
			System.err.println(p.getAdministration().getAddress().getStreet());
		}
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
			
			System.err.println("** ACTIVITIES ");
			Activities.stream().sorted().forEach(a -> System.err.println(a.toString()));
			
			Rio.write(m, w, RDFFormat.NTRIPLES);
        }
		LOG.info("--- END ---");
    }
}

