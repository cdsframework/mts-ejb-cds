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
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseBO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.enumeration.CdsListType;
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
import org.cdsframework.util.BrokenRule;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class ValueSetCdsCodeRelBO extends BaseBO<ValueSetCdsCodeRelDTO> {

    @EJB
    private CdsListItemBO cdsListItemBO;
    @EJB
    CdsListBO cdsListBO;

    @Override
    protected void initialize() throws MtsException {
        setSelfReferencing(true);
    }

    @Override
    protected Map<String, Object> preAddOrUpdate(ValueSetCdsCodeRelDTO baseDTO, Operation operation, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, ConstraintViolationException, MtsException, AuthenticationException, AuthorizationException {
        if (baseDTO == null || baseDTO.getCdsCodeDTO() == null || baseDTO.getCdsCodeDTO().getCodeId() == null) {
            BrokenRule brokenRule = new BrokenRule("ValueSetCdsCodeRelDTOCdsCodeMissing", "A code must be selected.");
            throw new ValidationException(brokenRule);
        }
        return super.preAddOrUpdate(baseDTO, operation, queryClass, sessionDTO, propertyBagDTO);
    }

    @Override
    protected void postAdd(ValueSetCdsCodeRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        final String METHODNAME = "postAdd ";
        ValueSetDTO valueSetDTO = new ValueSetDTO();
        valueSetDTO.setValueSetId(baseDTO.getValueSetId());
        CdsListDTO cdsListDTO = new CdsListDTO();
        cdsListDTO.setListType(CdsListType.VALUE_SET);
        cdsListDTO.setValueSetDTO(valueSetDTO);
        List<CdsListDTO> cdsListDTOs = cdsListBO.findByQueryListMain(
                cdsListDTO,
                CdsListDTO.ByValueSet.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (CdsListDTO item : cdsListDTOs) {
            logger.info(METHODNAME, "processing value set item add to list: ", item.getCode());
            CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
            cdsListItemDTO.setListId(item.getListId());
            cdsListItemDTO.setItemType(CdsListType.VALUE_SET);
            cdsListItemDTO.setValueSetCdsCodeRelDTO(baseDTO);
            List<CdsListItemDTO> listItems = cdsListItemBO.findByQueryListMain(
                    cdsListItemDTO,
                    CdsListItemDTO.ByValueSetCdsCodeRelIdCdsListId.class,
                    new ArrayList<Class>(),
                    AuthenticationUtils.getInternalSessionDTO(),
                    propertyBagDTO);
            logger.info(METHODNAME, "number of child list items present: ", listItems.size());
            if (listItems.isEmpty()) {
                cdsListItemDTO = new CdsListItemDTO();
                cdsListItemDTO.setItemType(CdsListType.VALUE_SET);
                cdsListItemDTO.setValueSetCdsCodeRelDTO(baseDTO);
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
    protected void preDelete(ValueSetCdsCodeRelDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        final String METHODNAME = "preDelete ";
        CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
        cdsListItemDTO.setItemType(CdsListType.VALUE_SET);
        cdsListItemDTO.setValueSetCdsCodeRelDTO(baseDTO);
        logger.info(METHODNAME, "Attempting to delete list items with the value set cds code rel id: ", baseDTO != null ? baseDTO.getValueSetCdsCodeRelId() : null);
        List<CdsListItemDTO> listItems = cdsListItemBO.findByQueryListMain(
                cdsListItemDTO,
                CdsListItemDTO.ByValueSetCdsCodeRelId.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (CdsListItemDTO item : listItems) {
            logger.info(METHODNAME, "Deleting list item from list: ", item.getDslrValue(), " - ", item.getListId());
            item.delete(true);
            cdsListItemBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }
        logger.info(METHODNAME, "finished");
    }
}
