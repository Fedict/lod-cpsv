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

import java.util.Arrays;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Convert EDRL / business.belgium.be to Core Public Service Vocab RDF Triples.
 * 
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class Consts {
    private final static ValueFactory F = SimpleValueFactory.getInstance();
    
	public final static String PREFIX_LANG = "http://publications.europa.eu/resource/authority/language/";
//	public final static String PREFIX_DDEIO = "http://dd.eionet.europa.eu/vocabulary/lau2/be/";

	
	public final static String PREFIX_LIFE = "http://org.belgif.be/vocab/lifecycle/";
	
	// administrations
	public final static String PREFIX_ATU = "http://publications.europa.eu/resource/authority/atu/";
	public final static IRI ID_BXL = F.createIRI(PREFIX_ATU + "BEL_RG_BRU");
	public final static IRI ID_VLA = F.createIRI(PREFIX_ATU + "BEL_RG_VLS");
	public final static IRI ID_WAL = F.createIRI(PREFIX_ATU + "BEL_RG_WAL");
	public final static IRI ID_GER = F.createIRI(PREFIX_ATU + "BEL_CGG_DEU");
	public final static IRI ID_WAL_EX_GER = F.createIRI(PREFIX_ATU + "BEL_RG_WAL_MINUS_CGG_DEU");
	
	public final static String PREFIX_CPSV = "http://purl.org/vocab/cpsv#";
	public final static IRI CLASS_CPSV = F.createIRI(PREFIX_CPSV + "PublicService");
	public final static IRI CLASS_COST = F.createIRI(PREFIX_CPSV + "Cost");
	public final static IRI CLASS_HAS_COST = F.createIRI(PREFIX_CPSV + "hasCost") ;
	public final static IRI GROUPED_BY = F.createIRI(PREFIX_CPSV + "isGroupedBy");
	
	public final static List<String> FREE = Arrays.asList(
											"<div>0 euro</div>",
											"<div>Gratis</div>",
											"<div>Gratuit</div>",
											"<div>Nihil</div>", 
											"<div>o euro</div>");
}

