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
 *   this list of conditions and the following validdatedisclaimer in the documentation
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
package be.fedict.lodtools.cpsv.vocab;

import static be.fedict.lodtools.cpsv.vocab.CPSV.PREFIX;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Core Vocabulary
 * 
 * @author Bart.Hanssens
 */
public class CV {
	private final static ValueFactory F = SimpleValueFactory.getInstance();
	
	public final static String PREFIX = "http://data.europa.eu/m8g/";

	public final static IRI CLASS_PARTICIPATION = F.createIRI(PREFIX + "Participation");	
	public final static IRI CLASS_PUB_ORG = F.createIRI(PREFIX + "PublicOrganisation");	
	
	public final static IRI HAS_COMPETENT_AUTH = F.createIRI(PREFIX + "hacCompetentAuthority");
	public final static IRI HAS_ADDRESS = F.createIRI(PREFIX + "hasAddress");
	public final static IRI HAS_CHANNEL = F.createIRI(PREFIX + "hasChannel") ;
	public final static IRI HAS_COST = F.createIRI(PREFIX + "hasCost") ;
	
	public final static IRI PLAYS_ROLE = F.createIRI(PREFIX + "plays_role");
}