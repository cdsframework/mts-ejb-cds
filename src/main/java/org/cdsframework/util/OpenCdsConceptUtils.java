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

import java.util.ArrayList;
import java.util.List;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.ejb.bo.CdsCodeBO;
import org.cdsframework.ejb.bo.OpenCdsConceptRelBO;
import org.cdsframework.ejb.bo.ValueSetCdsCodeRelBO;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.util.support.CoreConstants;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsConceptUtils {

    private static final LogUtils logger = LogUtils.getLogger(OpenCdsConceptUtils.class);

    public static CdsCodeDTO getFirstCdsCodeDTOFromOpenCdsConceptDTO(OpenCdsConceptDTO openCdsConceptDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getFirstCdsCodeDTOFromOpenCdsConceptDTO ";

        OpenCdsConceptRelBO openCdsConceptRelBO = (OpenCdsConceptRelBO) EJBUtils.getDtoBo(OpenCdsConceptRelDTO.class);
        CdsCodeBO cdsCodeBO = (CdsCodeBO) EJBUtils.getDtoBo(CdsCodeDTO.class);
        ValueSetCdsCodeRelBO valueSetCdsCodeRelBO = (ValueSetCdsCodeRelBO) EJBUtils.getDtoBo(ValueSetCdsCodeRelDTO.class);
        logger.info(METHODNAME, "Processing concept: ", openCdsConceptDTO.getLabel());

        OpenCdsConceptRelDTO openCdsConceptRelDTO = new OpenCdsConceptRelDTO();
        openCdsConceptRelDTO.setConceptCodeId(openCdsConceptDTO.getCodeId());
        openCdsConceptRelDTO.getQueryMap().put(CoreConstants.LAZY, true);
        openCdsConceptRelDTO.getQueryMap().put(CoreConstants.LAZY_PAGE_SIZE, "2");
        openCdsConceptRelDTO.getQueryMap().put(CoreConstants.LAZY_ROW_OFFSET, "0");

        List<OpenCdsConceptRelDTO> openCdsConceptRelDTOs
                = openCdsConceptRelBO.findByQueryListMain(openCdsConceptRelDTO, OpenCdsConceptRelDTO.ByOpenCdsConceptId.class, new ArrayList<Class>(), AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
        logger.info(METHODNAME, "Got openCdsConceptRelDTOs: ", openCdsConceptRelDTOs);

        CdsCodeDTO cdsCodeDTO = null;

        if (openCdsConceptRelDTOs != null && !openCdsConceptRelDTOs.isEmpty()) {
            logger.info(METHODNAME, "Got openCdsConceptRelDTOs.size(): ", openCdsConceptRelDTOs.size());
            for (OpenCdsConceptRelDTO relDTO : openCdsConceptRelDTOs) {
                logger.info(METHODNAME, "listing concept code mapping: ", relDTO.getCdsCodeDTO());
                logger.info(METHODNAME, "listing concept code system mapping: ", relDTO.getCdsCodeSystemDTO());
                logger.info(METHODNAME, "listing concept value set mapping: ", relDTO.getValueSetDTO());
                cdsCodeDTO = relDTO.getCdsCodeDTO();
                if (cdsCodeDTO != null) {
                    break;
                }
                CdsCodeSystemDTO cdsCodeSystemDTO = relDTO.getCdsCodeSystemDTO();
                if (cdsCodeSystemDTO != null) {
                    CdsCodeDTO queryCodeDTO = new CdsCodeDTO();
                    queryCodeDTO.setCodeSystemId(cdsCodeSystemDTO.getCodeSystemId());
                    queryCodeDTO.getQueryMap().put(CoreConstants.LAZY, true);
                    queryCodeDTO.getQueryMap().put(CoreConstants.LAZY_PAGE_SIZE, "2");
                    queryCodeDTO.getQueryMap().put(CoreConstants.LAZY_ROW_OFFSET, "0");
                    List<CdsCodeDTO> cdsCodeDTOs
                            = cdsCodeBO.findByQueryListMain(queryCodeDTO, CdsCodeDTO.ByCodeSystemId.class, new ArrayList<Class>(), AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
                    for (CdsCodeDTO codeDTO : cdsCodeDTOs) {
                        if (codeDTO != null) {
                            cdsCodeDTO = codeDTO;
                            break;
                        }
                    }
                    if (cdsCodeDTO != null) {
                        break;
                    }
                }
                ValueSetDTO valueSetDTO = relDTO.getValueSetDTO();
                if (valueSetDTO != null) {
                    ValueSetCdsCodeRelDTO queryDTO = new ValueSetCdsCodeRelDTO();
                    queryDTO.setValueSetId(valueSetDTO.getValueSetId());
                    queryDTO.getQueryMap().put(CoreConstants.LAZY, true);
                    queryDTO.getQueryMap().put(CoreConstants.LAZY_PAGE_SIZE, "2");
                    queryDTO.getQueryMap().put(CoreConstants.LAZY_ROW_OFFSET, "0");
                    List<ValueSetCdsCodeRelDTO> valueSetCdsCodeRelDTOs
                            = valueSetCdsCodeRelBO.findByQueryListMain(queryDTO, ValueSetCdsCodeRelDTO.ByValueSetId.class, new ArrayList<Class>(), AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
                    for (ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO : valueSetCdsCodeRelDTOs) {
                        if (valueSetCdsCodeRelDTO != null && valueSetCdsCodeRelDTO.getCdsCodeDTO() != null) {
                            cdsCodeDTO = valueSetCdsCodeRelDTO.getCdsCodeDTO();
                            break;
                        }
                    }
                    if (cdsCodeDTO != null) {
                        break;
                    }
                }
            }
            logger.info(METHODNAME, "derived concept sample code=", cdsCodeDTO);
        } else {
            logger.warn(METHODNAME, "openCdsConceptRelDTOs is null or empty!");
        }
        return cdsCodeDTO;
    }
}
