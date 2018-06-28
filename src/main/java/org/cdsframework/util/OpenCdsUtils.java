/**
 * The MTS cds EJB project is the base framework for the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see <http://www.gnu.org/licenses/> for more
 * details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
 * Department of Health and Mental Hygiene, Bureau of Immunization to have (without restriction,
 * limitation, and warranty) complete irrevocable access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see https://www.hln.com/services/open-source/ or send
 * correspondence to ice@hln.com.
 */
package org.cdsframework.util;

import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.factory.CdsCodeDTOObjectFactory;
import org.opencds.config.schema.Concept;
import org.opencds.config.schema.ConceptDeterminationMethod;
import org.opencds.config.schema.ConceptMapping;
import org.opencds.config.schema.NamespacedConcept;
import org.opencds.term.conceptMappings.MembersForCodeSystem;
import org.opencds.term.supportedConcepts.OpenCdsConcept;
import org.opencds.vmr.v1_0.schema.CD;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsUtils {

    public static ConceptDeterminationMethodDTO getConceptDeterminationMethodDTOFromOpenCdsConcept(OpenCdsConcept openCdsConcept) {
        ConceptDeterminationMethodDTO result = new ConceptDeterminationMethodDTO();
        result.setCode(openCdsConcept.getCode());
        result.setDisplayName(openCdsConcept.getDisplayName());
        return result;
    }

    public static OpenCdsConcept getOpenCdsConceptFromConceptDeterminationMethodDTO(ConceptDeterminationMethodDTO conceptDeterminationMethodDTO) {
        OpenCdsConcept result = new OpenCdsConcept();
        result.setCode(conceptDeterminationMethodDTO.getCode());
        result.setDisplayName(conceptDeterminationMethodDTO.getDisplayName());
        return result;
    }

    public static ConceptDeterminationMethodDTO getConceptDeterminationMethodDTOFromConceptDeterminationMethod(ConceptDeterminationMethod cdm) {
        ConceptDeterminationMethodDTO result = new ConceptDeterminationMethodDTO();
        result.setCode(cdm.getCode());
        result.setDisplayName(cdm.getDisplayName());
        return result;
    }

    public static OpenCdsConceptDTO getOpenCdsConceptDTOFromNamespacedConcept(NamespacedConcept namespacedConcept) {
        OpenCdsConceptDTO openCdsConceptDTO = new OpenCdsConceptDTO();
        openCdsConceptDTO.setCode(namespacedConcept.getCode());
        openCdsConceptDTO.setDisplayName(namespacedConcept.getDisplayName());
        return openCdsConceptDTO;
    }

    public static CdsCodeSystemDTO getCdsCodeSystemDTOFromConceptMappingFromConcepts(ConceptMapping.FromConcepts fromConcepts) {
        CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
        cdsCodeSystemDTO.setOid(fromConcepts.getCodeSystem());
        cdsCodeSystemDTO.setName(fromConcepts.getCodeSystemName());
        return cdsCodeSystemDTO;
    }

    public static CdsCodeSystemDTO getCdsCodeSystemDTOFromMembersForCodeSystem(MembersForCodeSystem membersForCodeSystem) {
        CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
        cdsCodeSystemDTO.setOid(membersForCodeSystem.getCodeSystem());
        cdsCodeSystemDTO.setName(membersForCodeSystem.getCodeSystemName());
        return cdsCodeSystemDTO;
    }

    public static OpenCdsConceptDTO getOpenCdsConceptDTOFromOpenCdsConcept(OpenCdsConcept openCdsConcept) {
        OpenCdsConceptDTO openCdsConceptDTO = new OpenCdsConceptDTO();
        openCdsConceptDTO.setCode(openCdsConcept.getCode());
        openCdsConceptDTO.setDisplayName(openCdsConcept.getDisplayName());
        return openCdsConceptDTO;
    }

    public static CdsCodeDTO getCdsCodeDTOFromConcept(Concept concept, CdsCodeSystemDTO cdsCodeSystemDTO) {
        CdsCodeDTO cdsCodeDTO = new CdsCodeDTO();
        cdsCodeDTO.setCode(concept.getCode());
        cdsCodeDTO.setDisplayName(concept.getDisplayName());
        cdsCodeDTO.setCodeSystem(cdsCodeSystemDTO.getOid());
        cdsCodeDTO.setCodeSystemId(cdsCodeSystemDTO.getCodeSystemId());
        cdsCodeDTO.setCodeSystemName(cdsCodeSystemDTO.getName());
        return cdsCodeDTO;
    }

    public static CdsCodeDTO getCdsCodeDTOFromCD(CD member, CdsCodeSystemDTO cdsCodeSystemDTO) {
            CdsCodeDTO cdsCodeDTO = CdsCodeDTOObjectFactory.getCdsCodeDTOFromCD(member);
            cdsCodeDTO.setCodeSystem(cdsCodeSystemDTO.getOid());
            cdsCodeDTO.setCodeSystemName(cdsCodeSystemDTO.getName());
            return cdsCodeDTO;
    }

    public static OpenCdsConceptRelDTO getOpenCdsConceptRelDTO(
            CdsCodeDTO cdsCodeDTO,
            OpenCdsConceptDTO openCdsConceptDTO,
            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO) {
        OpenCdsConceptRelDTO openCdsConceptRelDTO = new OpenCdsConceptRelDTO();
        openCdsConceptRelDTO.setCdsCodeDTO(cdsCodeDTO);
        openCdsConceptRelDTO.setConceptDeterminationMethodDTO(conceptDeterminationMethodDTO);
        openCdsConceptRelDTO.setConceptCodeId(openCdsConceptDTO.getCodeId());
        return openCdsConceptRelDTO;
    }
}
