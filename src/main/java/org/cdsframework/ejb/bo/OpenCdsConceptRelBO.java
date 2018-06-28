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
package org.cdsframework.ejb.bo;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseBO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO.MappingType;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.CdsListType;
import org.cdsframework.enumeration.DTOState;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Update;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.DTOUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class OpenCdsConceptRelBO extends BaseBO<OpenCdsConceptRelDTO> {

    @EJB
    private CdsListBO cdsListBO;
    @EJB
    private CdsListItemBO cdsListItemBO;

    @Override
    protected void preAdd(OpenCdsConceptRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException, AuthorizationException {
        preAddUpdate(baseDTO, queryClass, sessionDTO, propertyBagDTO);
    }

    @Override
    protected void preUpdate(OpenCdsConceptRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException, AuthorizationException {
        preAddUpdate(baseDTO, queryClass, sessionDTO, propertyBagDTO);
    }

    private void preAddUpdate(OpenCdsConceptRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException, AuthorizationException {
        OpenCdsConceptRelDTO openCdsConceptRelDTO = (OpenCdsConceptRelDTO) baseDTO;
        if (openCdsConceptRelDTO.getMappingType() == MappingType.CODE_SYSTEM) {
            openCdsConceptRelDTO.setCdsCodeDTO(null);
            openCdsConceptRelDTO.setValueSetDTO(null);
        }
        else if (openCdsConceptRelDTO.getMappingType() == MappingType.VALUE_SET) {
            openCdsConceptRelDTO.setCdsCodeSystemDTO(null);
            openCdsConceptRelDTO.setCdsCodeDTO(null);
        }
        else if (openCdsConceptRelDTO.getMappingType() == MappingType.CODE) {
            openCdsConceptRelDTO.setValueSetDTO(null);
        }
        
    }
    
    /**
     * If an OpenCdsConceptRelDTO is added then add it to any relevant CONCEPT based lists.
     *
     * @param baseDTO
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws ConstraintViolationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws ValidationException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    protected void postAdd(OpenCdsConceptRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        final String METHODNAME = "postAdd ";

        // add this concept to any list that is based on concept that is based on the code system mapping of this concept.
        CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
        cdsCodeSystemDTO.setCodeSystemId(baseDTO.getCdsCodeSystemDTO() != null ? baseDTO.getCdsCodeSystemDTO().getCodeSystemId() : null);

        CdsListDTO cdsListDTO = new CdsListDTO();
        cdsListDTO.setListType(CdsListType.CONCEPT);
        cdsListDTO.setCdsCodeSystemDTO(cdsCodeSystemDTO);

        List<CdsListDTO> cdsListDTOs = cdsListBO.findByQueryListMain(
                cdsListDTO,
                CdsListDTO.ByCodeSystemListType.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);

        for (CdsListDTO item : cdsListDTOs) {
            logger.info(
                    METHODNAME,
                    "found code system based list: ",
                    item.getCode(),
                    " (", item.getCdsCodeSystemDTO().getName(), ")");

            OpenCdsConceptDTO openCdsConceptDTO = new OpenCdsConceptDTO();
            openCdsConceptDTO.setCodeId(baseDTO.getConceptCodeId());

            CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
            cdsListItemDTO.setItemType(CdsListType.CONCEPT);
            cdsListItemDTO.setOpenCdsConceptDTO(openCdsConceptDTO);
            cdsListItemDTO.setListId(item.getListId());
            List<CdsListItemDTO> cdsListItemDTOs = cdsListItemBO.findByQueryListMain(
                    cdsListItemDTO,
                    CdsListItemDTO.ByOpenCdsCodeIdCdsListId.class,
                    new ArrayList<Class>(),
                    AuthenticationUtils.getInternalSessionDTO(),
                    propertyBagDTO);
            logger.info(METHODNAME, "# of cdsListItemDTOs: ", cdsListItemDTOs.size());
            if (cdsListItemDTOs.isEmpty()) {
                logger.info(METHODNAME, "adding concept to list: ", baseDTO.getCdsCodeDTO() != null ? baseDTO.getCdsCodeDTO().getCode() : null, " - ", item.getCode());
                DTOUtils.setDTOState(cdsListItemDTO, DTOState.NEW);
                cdsListItemDTO.setListId(null);
                item.addOrUpdateChildDTO(cdsListItemDTO);
                cdsListBO.updateMain(item, Update.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
            }
        }
    }

    /**
     * Delete any list items related to this instance before deleting this instance.
     *
     * @param baseDTO
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws ConstraintViolationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws ValidationException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    protected void preDelete(OpenCdsConceptRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        final String METHODNAME = "preDelete ";

        // delete list items related to this concept if this is the last item responsible for the relationship.

        CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
        cdsCodeSystemDTO.setCodeSystemId(baseDTO.getCdsCodeSystemDTO() != null ? baseDTO.getCdsCodeSystemDTO().getCodeSystemId() : null);

        CdsListDTO cdsListDTO = new CdsListDTO();
        cdsListDTO.setListType(CdsListType.CONCEPT);
        cdsListDTO.setCdsCodeSystemDTO(cdsCodeSystemDTO);

        List<CdsListDTO> cdsListDTOs = cdsListBO.findByQueryListMain(
                cdsListDTO,
                CdsListDTO.ByCodeSystemListType.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);

        for (CdsListDTO item : cdsListDTOs) {
            logger.info(
                    METHODNAME,
                    "found code system/CONCEPT based list: ",
                    item.getCode(),
                    " (", item.getCdsCodeSystemDTO().getName(), ")");

            OpenCdsConceptDTO openCdsConceptDTO = new OpenCdsConceptDTO();
            openCdsConceptDTO.setCodeId(baseDTO.getConceptCodeId());

            OpenCdsConceptRelDTO openCdsConceptRelDTO = new OpenCdsConceptRelDTO();
            openCdsConceptRelDTO.setConceptCodeId(baseDTO.getConceptCodeId());
            openCdsConceptRelDTO.setCdsCodeDTO(baseDTO.getCdsCodeDTO());

            // see how many OpenCdsConceptRelDTOs there are for this OpenCdsConceptDTO for the code system in questions
            Integer openCdsConceptRelDTOCount = findObjectByQueryMain(openCdsConceptRelDTO, OpenCdsConceptRelDTO.CountByConceptCodeIdCodeSystemId.class, AuthenticationUtils.getInternalSessionDTO(), Integer.class, propertyBagDTO);
            logger.info(METHODNAME, "# of openCdsConceptRelDTOs: ", openCdsConceptRelDTOCount);

            // if there is only one OpenCdsConceptRelDTO in the OpenCdsConceptDTO that is mapped to the code system then we delete the concept list item.
            if (openCdsConceptRelDTOCount == 1) {
                CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
                cdsListItemDTO.setItemType(CdsListType.CONCEPT);
                cdsListItemDTO.setOpenCdsConceptDTO(openCdsConceptDTO);
                cdsListItemDTO.setListId(item.getListId());

                // get the list item
                List<CdsListItemDTO> cdsListItemDTOs = cdsListItemBO.findByQueryListMain(
                        cdsListItemDTO,
                        CdsListItemDTO.ByOpenCdsCodeIdCdsListId.class,
                        new ArrayList<Class>(),
                        AuthenticationUtils.getInternalSessionDTO(),
                        propertyBagDTO);

                // if there is only one then delete it.
                logger.info(METHODNAME, "# of cdsListItemDTOs: ", cdsListItemDTOs.size());
                if (cdsListItemDTOs.size() == 1) {
                    logger.info(METHODNAME, "deleting concept from list: ", baseDTO.getCdsCodeDTO() != null ? baseDTO.getCdsCodeDTO().getCode() : null, " - ", item.getCode());
                    cdsListItemDTO = cdsListItemDTOs.get(0);
                    item.addOrUpdateChildDTO(cdsListItemDTO);
                    cdsListItemDTO.delete(true);
                    cdsListBO.updateMain(item, Update.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
                }
            }
        }
    }
}
