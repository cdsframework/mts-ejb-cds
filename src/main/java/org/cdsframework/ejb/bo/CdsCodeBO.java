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
import org.cdsframework.base.BaseDTO;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.enumeration.CdsListType;
import org.cdsframework.enumeration.DTOState;
import org.cdsframework.enumeration.Operation;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Delete;
import org.cdsframework.group.Update;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.DTOUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class CdsCodeBO extends BaseBO<CdsCodeDTO> {
    
    @EJB
    private CdsListBO cdsListBO;
    @EJB
    private CdsCodeSystemBO cdsCodeSystemBO;
    @EJB
    private CdsListItemBO cdsListItemBO;
    @EJB
    private ValueSetCdsCodeRelBO valueSetCdsCodeRelBO;
    @EJB
    private OpenCdsConceptRelBO openCdsConceptRelBO;

    /**
     * If a CdsCodeDTO is added then add it to any relevant CODE_SYSTEM based lists.
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
    protected void postAdd(CdsCodeDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "postAdd ";

        // add this concept to any list that is based on concept that is based on the code system mapping of this concept.
        CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
        cdsCodeSystemDTO.setCodeSystemId(baseDTO.getCodeSystemId());
        
        CdsListDTO cdsListDTO = new CdsListDTO();
        cdsListDTO.setListType(CdsListType.CODE_SYSTEM);
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
            
            CdsCodeDTO cdsCodeDTO = new CdsCodeDTO();
            cdsCodeDTO.setCodeId(baseDTO.getCodeId());
            
            CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
            cdsListItemDTO.setItemType(CdsListType.CODE_SYSTEM);
            cdsListItemDTO.setCdsCodeDTO(cdsCodeDTO);
            cdsListItemDTO.setListId(item.getListId());
            List<CdsListItemDTO> cdsListItemDTOs = cdsListItemBO.findByQueryListMain(
                    cdsListItemDTO,
                    CdsListItemDTO.ByCdsCodeIdCdsListId.class,
                    new ArrayList<Class>(),
                    AuthenticationUtils.getInternalSessionDTO(),
                    propertyBagDTO);
            logger.info(METHODNAME, "# of cdsListItemDTOs: ", cdsListItemDTOs.size());
            if (cdsListItemDTOs.isEmpty()) {
                logger.info(METHODNAME, "adding code to list: ", baseDTO.getCode(), " - ", item.getCode());
                DTOUtils.setDTOState(cdsListItemDTO, DTOState.NEW);
                cdsListItemDTO.setListId(null);
                item.addOrUpdateChildDTO(cdsListItemDTO);
                cdsListBO.updateMain(item, Update.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
            }
        }
    }

    /**
     * Delete any concept mappings, list items or value set mappings related to this instance before deleting this instance.
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
    protected void preDelete(CdsCodeDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {

        // first deal with list items
        CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
        cdsListItemDTO.setItemType(CdsListType.CODE_SYSTEM);
        cdsListItemDTO.setCdsCodeDTO(baseDTO);
        logger.info("Attempting to delete list items with the code: ", baseDTO != null ? baseDTO.getCode() : null);
        List<CdsListItemDTO> listItems = cdsListItemBO.findByQueryListMain(
                cdsListItemDTO,
                CdsListItemDTO.ByCdsCodeId.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (CdsListItemDTO item : listItems) {
            logger.info("Deleting list item: ", item.getDslrValue());
            item.delete(true);
            cdsListItemBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }

        // second deal with value sets
        ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO = new ValueSetCdsCodeRelDTO();
        valueSetCdsCodeRelDTO.setCdsCodeDTO(baseDTO);
        logger.info("Attempting to delete value set mappings with the code: ", baseDTO != null ? baseDTO.getCode() : null);
        List<ValueSetCdsCodeRelDTO> valueSetCdsCodeRelDTOs = valueSetCdsCodeRelBO.findByQueryListMain(
                valueSetCdsCodeRelDTO,
                ValueSetCdsCodeRelDTO.ByCodeId.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (ValueSetCdsCodeRelDTO item : valueSetCdsCodeRelDTOs) {
            logger.info("Deleting value set mapping: ", item.getCdsCodeDTO() != null ? item.getCdsCodeDTO().getCode() : null);
            item.delete(true);
            valueSetCdsCodeRelBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }

        // third deal with concept mappings
        OpenCdsConceptRelDTO openCdsConceptRelDTO = new OpenCdsConceptRelDTO();
        openCdsConceptRelDTO.setCdsCodeDTO(baseDTO);
        logger.info("Attempting to delete concept mappings with the code: ", baseDTO != null ? baseDTO.getCode() : null);
        List<OpenCdsConceptRelDTO> openCdsConceptRelDTOs = openCdsConceptRelBO.findByQueryListMain(
                openCdsConceptRelDTO,
                OpenCdsConceptRelDTO.ByCodeId.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (OpenCdsConceptRelDTO item : openCdsConceptRelDTOs) {
            logger.info("Deleting concept mapping: ", item.getCdsCodeDTO() != null ? item.getCdsCodeDTO().getCode() : null);
            item.delete(true);
            openCdsConceptRelBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }
        
    }
    
    @Override
    protected void postProcessBaseDTOs(
            BaseDTO parentDTO,
            List<CdsCodeDTO> baseDTOs,
            Operation operation,
            Class queryClass,
            List<Class> validationClasses,
            List<Class> childClassDTOs,
            SessionDTO sessionDTO,
            PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
//        if (parentDTO instanceof CdsCodeSystemDTO) {
//            CdsCodeSystemDTO cdsCodeSystemDTO = (CdsCodeSystemDTO) parentDTO;
//            for (CdsCodeDTO item : baseDTOs) {
//                item.setCodeSystem(cdsCodeSystemDTO.getOid());
//                item.setCodeSystemName(cdsCodeSystemDTO.getName());
//            }
//        } else {
//            logger.info("NOT SETTING CdsCodeSystemDTO on children from lookup");
//            for (CdsCodeDTO item : baseDTOs) {
//                CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
//                cdsCodeSystemDTO.setCodeSystemId(item.getCodeSystemId());
//                cdsCodeSystemDTO = cdsCodeSystemBO.findByPrimaryKeyMain(cdsCodeSystemDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);
//                item.setCodeSystem(cdsCodeSystemDTO.getOid());
//                item.setCodeSystemName(cdsCodeSystemDTO.getName());
//            }
//        }
    }
    
}
