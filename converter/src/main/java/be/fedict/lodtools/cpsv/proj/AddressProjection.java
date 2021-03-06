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

import org.xmlbeam.annotation.XBRead;

/**
 * XMLBeam "projector" (parser) interface
 * 
 * @author Bart.Hanssens
 */
public interface AddressProjection {
	@XBRead("addressId/mainCode")
	public String getMainCode();

	@XBRead("addressId/subCode")
	public String getSubCode();
	
	@XBRead("nisCode")
	public String getNisCode();
	
	@XBRead("postcode")
	public String getZipCode();

	@XBRead("municipality")
	public String getCity();
	
	@XBRead("building")
	public String getBuilding();

	@XBRead("street")
	public String getStreet();
	
	@XBRead("number")
	public String getNumber();
	
	@XBRead("box")
	public String getBox();

	@XBRead("telephone")
	public String getPhone();
	
	@XBRead("email")
	public String getEmail();
	
	@XBRead("website")
	public String getWebsite();
	
	@XBRead("contactCenterTelephone")
	public String getContactPhone();
	
	@XBRead("contactCenter")
	public String getContactSite();
}
