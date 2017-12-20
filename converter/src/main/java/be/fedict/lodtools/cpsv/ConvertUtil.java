/*
 * Copyright (c) 2017, Bart Hanssens <bart.hanssens@fedict.be>
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

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Conversion util.
 * 
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class ConvertUtil {
	private final static Logger LOG = LoggerFactory.getLogger(ConvertUtil.class);
		
	private final static ValueFactory F = SimpleValueFactory.getInstance();
		
	/**
	 * Make ID for administration
	 * 
	 * @param bce KBO/BCE organization number
	 * @param code other code
	 * @return 
	 */
	public static IRI adminID(String bce, String code) {
		if (bce != null && !bce.isEmpty()) {
			String id = bce.substring(0, 4) + "_" + bce.substring(4, 7) 
											+ "_" + bce.substring(7) + "#id";
			return F.createIRI(Consts.ORG_BELGIF + id);
		}
		if (code != null && !code.isEmpty()) {
			return F.createIRI(Consts.PUBSERV_BELGIF + "org/" + 
											code.replaceAll(" ", "") + "#id");
		}
		return null;
	}

	/**
	 * Make address ID 
	 * 
	 * @param mainCode
	 * @param subCode
	 * @return 
	 */
	public static IRI addrID(String mainCode, String subCode) {
		return F.createIRI(Consts.PUBSERV_BELGIF + "addr/" 
				+ DigestUtils.sha1Hex(mainCode + subCode) + "#id");
	}
	
	/**
	 * Create IRI identifier
	 * 
	 * @param id short code
	 * @return IRI
	 */
	public static IRI genericID(String type, String id) {
		return F.createIRI(Consts.PUBSERV_BELGIF + type + "/" + id + "#id");
	}
	
	/**
	 * Create IRI identifier for activity
	 * 
	 * @param sector sector code
	 * @param activity activity code
	 * @return IRI
	 */
	public static IRI sectorID(String sector, String activity) {
		return F.createIRI(Consts.PUBSERV_BELGIF + "sector/" 
				+ sector.substring(0, 2) + "/" + activity.substring(0, 2).trim() + "#id");
	}
	
	/**
	 * Create IRI for a cost 
	 * 
	 * @param cost text
	 * @return IRI
	 */
	public static IRI costID(String cost) {
		// Try to map different wordings of "free" to the same IRI
		String id = Consts.FREE.contains(cost) ? "zero" : DigestUtils.sha1Hex(cost);
		return genericID("cost", id);
	}
	
/**
	 * Create language IRI identifier
	 * 
	 * @param code short language code
	 * @return IRI
	 */
	public static IRI langID(String code) {
		String term = "";
		switch(code) {
			case "NL": term = "NED"; break;
			case "FR": term = "FRA"; break;
			case "EN": term = "ENG"; break;
			case "DE": term = "DEU"; break;
		}
		return F.createIRI(Consts.PREFIX_LANG + term);
	}
	
	/**
	 * Create lifecycle ID
	 * 
	 * @param code
	 * @return 
	 */
	public static IRI lifecycleID(String code) {
		String term = "";
		switch(code) {
			case "START":
			case "ACTIVE":
			case "STOP":
				term = code;
				break;
			default:
				LOG.error("Event code not found {}", code);
		}
		return F.createIRI(Consts.PREFIX_LIFE + term + "#id");
	}
}