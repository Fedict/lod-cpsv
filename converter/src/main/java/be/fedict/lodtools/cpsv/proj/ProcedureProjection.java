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
package be.fedict.lodtools.cpsv.proj;

import be.fedict.lodtools.cpsv.proj.MunicipalityProjection;
import java.util.List;
import org.xmlbeam.annotation.XBRead;

/**
 * XMLBeam "projector" (parser) interface
 * 
 * @author Bart.Hanssens
 */
public interface ProcedureProjection {
	@XBRead("/fedict.edrl.domain.Procedure/contentId")
	public String getID();

	@XBRead("/fedict.edrl.domain.Procedure/language")
	public String getLanguage();

	@XBRead("/fedict.edrl.domain.Procedure/lifecycle")
	public String getLifecycle();

	@XBRead("/fedict.edrl.domain.Procedure/title")
	public String getTitle();
	
	@XBRead("/fedict.edrl.domain.Procedure/shortDescription")
	public String getDesc();
	
	@XBRead("/fedict.edrl.domain.Procedure/applicationSummary")
	public String getSummary();
	
	@XBRead("/fedict.edrl.domain.Procedure/applicableTo")
	public String getApplies();
	
	@XBRead("/fedict.edrl.domain.Procedure/applicableExceptions")
	public String getAppliesExcept();
	
	@XBRead("/fedict.edrl.domain.Procedure/authorizationPeriod")
	public String getAuthPeriod();
	
	@XBRead("/fedict.edrl.domain.Procedure/conditions")
	public String getCondition();
	
	@XBRead("/fedict.edrl.domain.Procedure/formalities")
	public String getFormalities();

	@XBRead("/fedict.edrl.domain.Procedure/forms/fedict.edrl.domain.Link")
	public List<LinkProjection> getForms();
	
	@XBRead("/fedict.edrl.domain.Procedure/legalBases/fedict.edrl.domain.Link")
	public List<LinkProjection> getLegal();
	
	@XBRead("/fedict.edrl.domain.Procedure/indicators")
	public IndicatorProjection getIndicators();

	@XBRead("/fedict.edrl.domain.Procedure/responsibleAdministration/municipalities/fedict.edrl.domain.Municipality")	
	public List<MunicipalityProjection> getCities();
	
	@XBRead("/fedict.edrl.domain.Procedure/responsibleAdministration")	
	public ResponsibleProjection getResponsible();

	@XBRead("/fedict.edrl.domain.Procedure/frequency")
	public String getFrequency();
	
	@XBRead("/fedict.edrl.domain.Procedure/price")
	public String getPrice();
	
	@XBRead("/fedict.edrl.domain.Procedure/commonSectors/fedict.edrl.domain.Sector")
	public List<SectorProjection> getSectors();
	
	@XBRead("/fedict.edrl.domain.Procedure/commonActivities/fedict.edrl.domain.Activity")
	public List<ActivityProjection> getActivities();
}
