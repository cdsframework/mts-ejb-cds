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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.apache.commons.io.IOUtils;
import org.cdsframework.base.BaseBO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.cds.util.MarshalUtils;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeOpenCdsConceptRelDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.CdsVersionDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.CdsListVersionRelDTO;
import org.cdsframework.dto.CdsVersionConceptDeterminationMethodRelDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.dto.ValueSetSubValueSetRelDTO;
import org.cdsframework.enumeration.CdsListType;
import org.cdsframework.enumeration.DTOState;
import org.cdsframework.enumeration.CoreErrorCode;
import org.cdsframework.enumeration.MappingType;
import org.cdsframework.enumeration.Operation;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Add;
import org.cdsframework.group.FindAll;
import org.cdsframework.group.Update;
import org.cdsframework.group.SimpleExchange;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.BrokenRule;
import org.cdsframework.util.ClassUtils;
import org.cdsframework.util.FileUtils;
import org.cdsframework.util.StringUtils;
import org.cdsframework.util.comparator.CdsListItemComparator;
import org.cdsframework.util.support.data.cds.list.CdsListItem;
import org.cdsframework.util.support.data.cds.list.CdsListItemConceptMapping;
import org.cdsframework.util.support.data.cds.list.CdsListSpecificationFile;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class CdsListBO extends BaseBO<CdsListDTO> {

    @EJB
    private CdsCodeSystemBO cdsCodeSystemBO;
    @EJB
    private ValueSetBO valueSetBO;
    @EJB
    private OpenCdsConceptBO openCdsConceptBO;
    @EJB
    private CdsListVersionRelBO cdsListVersionRelBO;
    @EJB
    private CdsVersionBO cdsVersionBO;
    @EJB
    private CdsListItemBO cdsListItemBO;

    /**
     * At bean initialization - resync all java enum-based lists.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void verifyEnumLists() {
        final String METHODNAME = "verifyEnumLists ";
        long start = System.nanoTime();
        CdsListDTO queryCriteria = new CdsListDTO();
        queryCriteria.setListType(CdsListType.JAVA_ENUM);
        try {
            synchronizeCdsListsByQueryCriteria(queryCriteria, CdsListDTO.ByCdsListType.class);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            logger.logDuration(METHODNAME, start);
        }
    }

    /**
     * When debug is enabled - this override logs the output of a test of the list and list item retrieval routines.
     *
     * @param parentDTO
     * @param baseDTOs
     * @param operation
     * @param queryClass
     * @param validationClasses
     * @param childClassDTOs
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws MtsException
     * @throws ValidationException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    protected void postProcessBaseDTOs(
            BaseDTO parentDTO,
            List<CdsListDTO> baseDTOs,
            Operation operation,
            Class queryClass,
            List<Class> validationClasses,
            List<Class> childClassDTOs,
            SessionDTO sessionDTO,
            PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "verifyEnumLists ";
//        long start = System.nanoTime();
        if (logger.isDebugEnabled() && queryClass != FindAll.class) {
            for (CdsListDTO baseDTO : baseDTOs) {
                try {
                    for (CdsVersionDTO cdsVersionDTO : baseDTO.getRelatedVersions()) {
                        CdsListDTO cdsListDTO = getCdsListDTOByCodeVersion(baseDTO.getCode(), cdsVersionDTO);
                        logger.info("cdsListDTO: ", cdsListDTO.equals(baseDTO));
                        for (CdsListItemDTO item : baseDTO.getCdsListItemDTOs()) {
                            try {
                                CdsListItemDTO cdsListItemDTO = baseDTO.getCdsListItemDTOByValueVersion(item.getDslrValue(), cdsVersionDTO);
                                logger.info("cdsListItemDTO: ", item.equals(cdsListItemDTO));
                            } catch (NotFoundException e) {
                                logger.error("cdsListItemDTO NFE on ", item.getDslrValue(), " - ", cdsVersionDTO.getOpenCdsVersionIdentifier());
                            }
                        }
                    }
                } catch (NotFoundException e) {
                    logger.error("cdsListDTO NFE on ", baseDTO.getCode());
                }
            }
        }
//        logger.logDuration(METHODNAME, start);
    }
//
//    /**
//     * Synchronize list items for all lists associated with the supplied Code System.
//     *
//     * @param cdsCodeSystemDTO
//     * @param queryClass
//     * @param sessionDTO
//     * @param propertyBagDTO
//     * @throws ConstraintViolationException
//     * @throws NotFoundException
//     * @throws AuthorizationException
//     * @throws ValidationException
//     * @throws AuthenticationException
//     * @throws MtsException
//     */
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void synchronizeCdsCodeSystemDTOLists(CdsCodeSystemDTO cdsCodeSystemDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
//            AuthorizationException {
//        CdsListDTO queryCriteria = new CdsListDTO();
//        queryCriteria.setCdsCodeSystemDTO(cdsCodeSystemDTO);
//        synchronizeCdsListsByQueryCriteria(queryCriteria, CdsListDTO.ByOid.class);
//    }
//
//    /**
//     * Synchronize list items for all lists associated with the supplied OpenCDS concept.
//     *
//     * @param openCdsConceptDTO
//     * @param queryClass
//     * @param sessionDTO
//     * @param propertyBagDTO
//     * @throws ConstraintViolationException
//     * @throws NotFoundException
//     * @throws AuthorizationException
//     * @throws ValidationException
//     * @throws AuthenticationException
//     * @throws MtsException
//     */
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void synchronizeOpenCdsConceptDTOLists(OpenCdsConceptDTO openCdsConceptDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
//            AuthorizationException {
//        CdsListDTO queryCriteria = new CdsListDTO();
//        queryCriteria.getQueryMap().put("openCdsConceptDTO", openCdsConceptDTO);
//        synchronizeCdsListsByQueryCriteria(queryCriteria, CdsListDTO.ByOpenCdsConcept.class);
//    }
//
//    /**
//     * If an OpenCdsConceptDTO has it's type changed then sync any lists based on its old type. Then update any lists with the new
//     * value. Also called from the OpenCdsConceptDTO ConceptDeterminationMethodDTO child relationship class in case the version data
//     * changes on the concepts.
//     *
//     * @param baseDTO
//     * @param queryClass
//     * @param sessionDTO
//     * @param propertyBagDTO
//     * @throws NotFoundException
//     * @throws ValidationException
//     * @throws MtsException
//     * @throws AuthenticationException
//     * @throws AuthorizationException
//     * @throws ConstraintViolationException
//     */
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void synchronizeOpenCdsConceptTypeDTOLists(OpenCdsConceptDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws NotFoundException, ValidationException, MtsException, AuthenticationException, AuthorizationException,
//            ConstraintViolationException {
//        String propertyName = OpenCdsConceptDTO.PropertyName.openCdsConceptTypeDTO.name();
//        if (baseDTO.isPropertyChanged(propertyName)) {
//            PropertyChangeEvent propertyChangeEvent = baseDTO.getPropertyChangeEvent(propertyName);
//            Object oldValue = propertyChangeEvent.getOldValue();
//            if (oldValue != null) {
//                OpenCdsConceptTypeDTO openCdsConceptTypeDTO = (OpenCdsConceptTypeDTO) oldValue;
//                CdsListDTO cdsListDTO = new CdsListDTO();
//                cdsListDTO.setOpenCdsConceptTypeDTO(openCdsConceptTypeDTO);
//                synchronizeCdsListsByQueryCriteria(cdsListDTO, CdsListDTO.ByOpenCdsConceptType.class);
//            }
//        }
//        OpenCdsConceptTypeDTO openCdsConceptTypeDTO = baseDTO.getOpenCdsConceptTypeDTO();
//        CdsListDTO cdsListDTO = new CdsListDTO();
//        cdsListDTO.setOpenCdsConceptTypeDTO(openCdsConceptTypeDTO);
//        synchronizeCdsListsByQueryCriteria(cdsListDTO, CdsListDTO.ByOpenCdsConceptType.class);
//    }
//
//    /**
//     * Synchronize list items for all lists associated with the supplied value set.
//     *
//     * @param valueSetDTO
//     * @param queryClass
//     * @param sessionDTO
//     * @param propertyBagDTO
//     * @throws ConstraintViolationException
//     * @throws NotFoundException
//     * @throws AuthorizationException
//     * @throws ValidationException
//     * @throws AuthenticationException
//     * @throws MtsException
//     */
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void synchronizeValueSetDTOLists(ValueSetDTO valueSetDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
//            AuthorizationException {
//        final String METHODNAME = "synchronizeValueSetDTOLists ";
//        for (ValueSetDTO item : valueSetDTO.getValueSetDTOs()) {
//            CdsListDTO queryCriteria = new CdsListDTO();
//            queryCriteria.setValueSetDTO(item);
//            List<CdsListDTO> resultSet = findByQueryListMain(queryCriteria, CdsListDTO.ByValueSet.class, new ArrayList<Class>(), sessionDTO, propertyBagDTO);
//            for (CdsListDTO cdsListDTO : resultSet) {
//                logger.info(METHODNAME, "synchronizing: ", cdsListDTO.getCode());
//                synchronizeCdsListItems(cdsListDTO, sessionDTO, propertyBagDTO);
//                updateMain(cdsListDTO, Update.class, sessionDTO, propertyBagDTO);
//            }
//        }
//    }

    /**
     * If the list is new or updated - sync its list items.
     *
     * @param baseDTO
     * @param operation
     * @param queryClass
     * @param validationClasses
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws MtsException
     * @throws ValidationException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    protected void processBegin(
            CdsListDTO baseDTO,
            Operation operation,
            Class queryClass,
            List<Class> validationClasses,
            SessionDTO sessionDTO,
            PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "processBegin ";
        // initialize the list items on adds
        if (baseDTO != null) {
            if (operation == Operation.ADD || operation == Operation.UPDATE) {
                if (isCodeExists(baseDTO, operation)) {
                    throw new MtsException("A list with that code already exists.");
                }
            }
            if (operation == Operation.ADD) {
                initializeCdsListItems(baseDTO);
            } else if (operation == Operation.UPDATE) {
                setCdsListChildType(baseDTO);
                if (baseDTO.isPropertyChanged("cdsCodeSystemDTO")) {
                    logger.info(METHODNAME, "detected cdsCodeSystemDTO change - resyncing");
                    synchronizeCdsListItems(baseDTO, sessionDTO, propertyBagDTO);
                } else if (baseDTO.isPropertyChanged("enumClass")) {
                    logger.info(METHODNAME, "detected enumClass change - resyncing");
                    synchronizeCdsListItems(baseDTO, sessionDTO, propertyBagDTO);
                } else if (baseDTO.isPropertyChanged("valueSetDTO")) {
                    logger.info(METHODNAME, "detected valueSetDTO change - resyncing");
                    synchronizeCdsListItems(baseDTO, sessionDTO, propertyBagDTO);
                }
            }
        } else {
            logger.warn(METHODNAME, "baseDTO was null!");
        }
    }

    /**
     * Initializes a newly created list's list items.
     *
     * @param baseDTO
     * @throws MtsException
     * @throws ValidationException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private void initializeCdsListItems(CdsListDTO baseDTO) throws
            MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "initializeCdsListItems ";
        long start = System.nanoTime();
        CdsListType listType = baseDTO.getListType();
        logger.info(METHODNAME, "listType=", listType);
        List<CdsListItemDTO> cdsListItems = new ArrayList<CdsListItemDTO>();
        try {
            if (null != listType) {
                switch (listType) {
                    case JAVA_ENUM:
                        logger.info(METHODNAME, "Adding enum list items: ", baseDTO.getEnumClass());
                        cdsListItems = getEnumCdsListItems(baseDTO);
                        break;
                    case CODE_SYSTEM:
                        logger.info(METHODNAME, "Adding code system list items: ", baseDTO.getCdsCodeSystemDTO());
                        cdsListItems = getCodeSystemCdsListItems(baseDTO);
                        break;
                    case CONCEPT:
                        logger.info(METHODNAME, "Adding concept list items: ", baseDTO.getCdsCodeSystemDTO());
                        cdsListItems = getConceptCdsListItems(baseDTO);
                        break;
                    case VALUE_SET:
                        logger.info(METHODNAME, "Adding value set list items: ", baseDTO.getValueSetDTO());
                        cdsListItems = getValueSetCdsListItems(baseDTO);
                        break;
                    case AD_HOC:
                    case AD_HOC_CONCEPT:
                        setCdsListChildType(baseDTO);
                        break;
                    default:
                        break;
                }
            }

            for (CdsListItemDTO item : cdsListItems) {
                logger.info(METHODNAME, "Adding list item value for list: ", baseDTO.getCode(), " - ", item.getDslrValue());
                baseDTO.addOrUpdateChildDTO(item);
            }
        } catch (NullPointerException e) {
            throw new MtsException("NullPointer exception!", e);
        } finally {
            logger.logDuration(METHODNAME, start);
        }
    }

    /**
     * Synchronizes a list for an update.
     *
     * @param baseDTO
     * @throws MtsException
     * @throws ValidationException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private void synchronizeCdsListItems(CdsListDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "synchronizeCdsListItems ";
        long start = System.nanoTime();
        if (baseDTO == null) {
            throw new MtsException(METHODNAME + "baseDTO is null!");
        }

        CdsListItemDTO queryDTO = new CdsListItemDTO();
        queryDTO.setListId(baseDTO.getListId());
        List<CdsListItemDTO> oldCdsListItemDTOs = cdsListItemBO.findByQueryListMain(queryDTO, CdsListItemDTO.ByCdsListId.class, new ArrayList(), sessionDTO, propertyBagDTO);
        logger.info(METHODNAME, "oldCdsListItemDTOs=", oldCdsListItemDTOs);

        CdsListType listType = baseDTO.getListType();
        logger.info(METHODNAME, "listType=", listType);

        // initialize the new list
        List<CdsListItemDTO> newCdsListItemDTOs = new ArrayList<CdsListItemDTO>();
        if (null != listType) {
            switch (listType) {
                case JAVA_ENUM:
                    newCdsListItemDTOs = getEnumCdsListItems(baseDTO);
                    break;
                case CONCEPT:
                    newCdsListItemDTOs = getConceptCdsListItems(baseDTO);
                    break;
                case CODE_SYSTEM:
                    newCdsListItemDTOs = getCodeSystemCdsListItems(baseDTO);
                    break;
                case VALUE_SET:
                    newCdsListItemDTOs = getValueSetCdsListItems(baseDTO);
                    break;
                default:
                    break;
            }
        }

        // if not user managed then auto set deletes and adds
        if (listType != CdsListType.AD_HOC
                && listType != CdsListType.AD_HOC_CONCEPT) {

            // set deleted any old items missing in new list
            for (CdsListItemDTO oldItem : oldCdsListItemDTOs) {
                boolean oldItemFound = false;
                for (CdsListItemDTO newItem : newCdsListItemDTOs) {
                    if (newItem.getDslrValue().equals(oldItem.getDslrValue())) {
                        oldItemFound = true;
                        break;
                    }
                }
                if (!oldItemFound) {
                    oldItem.delete(true);
                }
                baseDTO.addOrUpdateChildDTO(oldItem);
            }

            for (CdsListItemDTO newItem : newCdsListItemDTOs) {
                boolean newItemFound = false;
                for (CdsListItemDTO oldItem : oldCdsListItemDTOs) {
                    if (oldItem.getDslrValue().equals(newItem.getDslrValue())) {
                        newItemFound = true;
                        break;
                    }
                }
                if (!newItemFound) {
                    baseDTO.addOrUpdateChildDTO(newItem);
                }
            }
        } else {
            for (CdsListItemDTO cdsListItemDTO : baseDTO.getCdsListItemDTOs()) {
                // for user managed lists - set the initial list type on new dtos
                if (cdsListItemDTO.isNew()) {
                    cdsListItemDTO.setItemType(listType);
                }
            }
        }
        for (CdsListItemDTO cdsListItemDTO : baseDTO.getCdsListItemDTOs()) {
            logger.info(METHODNAME, "final list item: ", cdsListItemDTO.getDslrValue(), " - ", cdsListItemDTO.getDTOState());
        }
        logger.logDuration(METHODNAME, start);
    }

    /**
     * Gets the list items for an JAVA_ENUM type list.
     *
     * @param baseDTO
     * @throws MtsException
     */
    private List<CdsListItemDTO> getEnumCdsListItems(CdsListDTO baseDTO) throws ValidationException, MtsException {
        final String METHODNAME = "getEnumCdsListItems ";
        long start = System.nanoTime();
        List<CdsListItemDTO> result = new ArrayList<CdsListItemDTO>();
        String enumClass = baseDTO.getEnumClass();
        if (enumClass != null) {
            Class enumClassClass;
            try {
                enumClassClass = ClassUtils.classForName(enumClass);
            } catch (Exception e) {
                throw new ValidationException(new BrokenRule("enumClassNotValid", "The class canonical name could not be converted to a class!"));
            }
            if (enumClassClass.isEnum()) {
                for (Object object : enumClassClass.getEnumConstants()) {
                    CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
                    cdsListItemDTO.setAdHocId(enumClassClass.getSimpleName() + "." + ((Enum) object).name());
                    cdsListItemDTO.setAdHocLabel(((Enum) object).name());
                    cdsListItemDTO.setItemType(CdsListType.JAVA_ENUM);
                    result.add(cdsListItemDTO);
                }
            } else {
                throw new ValidationException(new BrokenRule("enumClassNotAnEnum", "The class value was not an enumeration!"));
            }
        } else {
            throw new ValidationException(new BrokenRule("enumClassNull", "The class value was empty or null!"));
        }
        logger.logDuration(METHODNAME, start);
        return result;
    }

    /**
     * Gets the list items for a CODE_SYSTEM type list.
     *
     * @param baseDTO
     * @return
     * @throws MtsException
     * @throws ValidationException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private List<CdsListItemDTO> getCodeSystemCdsListItems(CdsListDTO baseDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getCodeSystemCdsListItems ";
        long start = System.nanoTime();
        CdsCodeSystemDTO cdsCodeSystemDTO = baseDTO.getCdsCodeSystemDTO();
        if (cdsCodeSystemDTO == null) {
            throw new ValidationException(new BrokenRule("codeSystemMissing", "A code system was not selected!"));
        }
        List<CdsListItemDTO> result = new ArrayList<CdsListItemDTO>();
        try {
            cdsCodeSystemDTO = cdsCodeSystemBO.findByPrimaryKeyMain(cdsCodeSystemDTO, cdsCodeSystemBO.getDtoChildClasses(), AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
        } catch (NotFoundException e) {
            throw new NotFoundException(METHODNAME + "Code System not found: " + cdsCodeSystemDTO.getOid());
        }
        logger.debug(METHODNAME, "cdsCodeSystemDTO.getCdsCodeDTOs(): ", cdsCodeSystemDTO.getCdsCodeDTOs());
        for (CdsCodeDTO cdsCodeDTO : cdsCodeSystemDTO.getCdsCodeDTOs()) {
            CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
            cdsListItemDTO.setCdsCodeDTO(cdsCodeDTO);
            cdsListItemDTO.setItemType(CdsListType.CODE_SYSTEM);
            result.add(cdsListItemDTO);
        }
        logger.logDuration(METHODNAME, start);
        return result;
    }

    /**
     * Gets the list items for a CONCEPT type list.
     *
     * @param baseDTO
     * @throws MtsException
     * @throws NotFoundException
     * @throws ValidationException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private List<CdsListItemDTO> getConceptCdsListItems(CdsListDTO baseDTO)
            throws MtsException, NotFoundException, ValidationException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getConceptCdsListItems ";
        long start = System.nanoTime();
        CdsCodeSystemDTO cdsCodeSystemDTO = baseDTO.getCdsCodeSystemDTO();
        if (cdsCodeSystemDTO == null) {
            throw new ValidationException(new BrokenRule("codeSystemMissing", "A code system was not selected!"));
        }
        List<CdsListItemDTO> result = new ArrayList<CdsListItemDTO>();
        List<ConceptDeterminationMethodDTO> conceptDeterminationMethodDTOs = getConceptDeterminationMethodDTOs(baseDTO);
        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.put("code_system_id", cdsCodeSystemDTO.getCodeSystemId());
        List<OpenCdsConceptDTO> openCdsConceptDTOs = openCdsConceptBO.findByQueryListMain(
                new OpenCdsConceptDTO(),
                OpenCdsConceptDTO.ByCodeSystemId.class,
                openCdsConceptBO.getDtoChildClasses(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (OpenCdsConceptDTO item : openCdsConceptDTOs) {
            if (item != null) {
                for (OpenCdsConceptRelDTO openCdsConceptRelDTO : item.getOpenCdsConceptRelDTOs()) {
                    if (conceptDeterminationMethodDTOs.contains(openCdsConceptRelDTO.getConceptDeterminationMethodDTO())) {
                        CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
                        cdsListItemDTO.setOpenCdsConceptDTO(item);
                        cdsListItemDTO.setItemType(CdsListType.CONCEPT);
                        result.add(cdsListItemDTO);
                        break;
                    }
                }
            }
        }
        logger.logDuration(METHODNAME, start);
        return result;
    }

    private List<ConceptDeterminationMethodDTO> getConceptDeterminationMethodDTOs(CdsListDTO baseDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getConceptCdsListItems ";
        long start = System.nanoTime();
        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        List<Class> childClasses = new ArrayList<Class>();
        childClasses.add(CdsVersionConceptDeterminationMethodRelDTO.class);
        CdsListVersionRelDTO queryDTO = new CdsListVersionRelDTO();
        queryDTO.setListId(baseDTO.getListId());
        List<CdsListVersionRelDTO> cdsListVersionRelDTOs = cdsListVersionRelBO.findByQueryListMain(queryDTO,
                CdsListVersionRelDTO.ByCdsListId.class,
                childClasses,
                AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        logger.info(METHODNAME, "cdsListVersionRelDTOs=", cdsListVersionRelDTOs);
        List<ConceptDeterminationMethodDTO> conceptDeterminationMethodDTOs = new ArrayList<ConceptDeterminationMethodDTO>();
        for (CdsListVersionRelDTO cdsListVersionRelDTO : cdsListVersionRelDTOs) {
            logger.info(METHODNAME, "cdsListVersionRelDTO=", cdsListVersionRelDTO);
            logger.info(METHODNAME, "!cdsListVersionRelDTO.isDeleted()=", !cdsListVersionRelDTO.isDeleted());
            if (!cdsListVersionRelDTO.isDeleted()) {
                logger.info(METHODNAME, "cdsListVersionRelDTO.getCdsVersionDTO()=", cdsListVersionRelDTO.getCdsVersionDTO());
                CdsVersionDTO cdsVersionDTO = cdsListVersionRelDTO.getCdsVersionDTO();
                logger.info(METHODNAME, "cdsVersionDTO.getConceptDeterminationMethodDTOs()=", cdsVersionDTO.getConceptDeterminationMethodDTOs());
                conceptDeterminationMethodDTOs.addAll(cdsVersionDTO.getConceptDeterminationMethodDTOs());
            }
        }
        logger.logDuration(METHODNAME, start);
        return conceptDeterminationMethodDTOs;
    }

    /**
     * Sets the list items for a VALUE_SET type list.
     *
     * @param baseDTO
     * @throws MtsException
     * @throws ValidationException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private List<CdsListItemDTO> getValueSetCdsListItems(CdsListDTO baseDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getValueSetCdsListItems ";
        long start = System.nanoTime();
        ValueSetDTO valueSetDTO = baseDTO.getValueSetDTO();
        if (valueSetDTO == null) {
            throw new ValidationException(new BrokenRule("valueSetMissing", "A value set was not selected!"));
        }
        try {
            valueSetDTO = valueSetBO.findByPrimaryKeyMain(valueSetDTO, valueSetBO.getDtoChildClasses(), AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
        } catch (NotFoundException e) {
            throw new NotFoundException(METHODNAME + "Value set not found: " + valueSetDTO.getName());
        }
        List<CdsListItemDTO> result = new ArrayList<CdsListItemDTO>();
        List<CdsCodeDTO> addedCodes = new ArrayList<CdsCodeDTO>();
        for (ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO : valueSetDTO.getAllValueSetCdsCodeRelDTOs()) {
            if (valueSetCdsCodeRelDTO != null
                    && valueSetCdsCodeRelDTO.getCdsCodeDTO() != null
                    && !addedCodes.contains(valueSetCdsCodeRelDTO.getCdsCodeDTO())) {
                CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
                cdsListItemDTO.setValueSetCdsCodeRelDTO(valueSetCdsCodeRelDTO);
                cdsListItemDTO.setItemType(CdsListType.VALUE_SET);
                result.add(cdsListItemDTO);
                addedCodes.add(valueSetCdsCodeRelDTO.getCdsCodeDTO());
            }
        }
        logger.logDuration(METHODNAME, start);
        return result;
    }

    /**
     * Export CdsListDTO instances as a Map.
     *
     * @param baseDTO the CdsListDTO query instance
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the zip file containing the CdsListDTOs that were the result of a query.
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    public Map<String, byte[]> exportData(CdsListDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws
            ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "exportData ";
        if (queryClass == SimpleExchange.class) {
            return simpleExchangeExport(baseDTO, queryClass, sessionDTO, propertyBagDTO);
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
        }
    }

    /**
     * Export CdsListDTO instances as a Map. This method expects to be passed a query CdsListDTO instance.
     *
     * @param baseDTO the CdsListDTO query instance
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the zip file containing the CdsListDTOs that were the result of a query.
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private Map<String, byte[]> simpleExchangeExport(CdsListDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws
            ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "simpleExchangeExport ";
        Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
        if (baseDTO != null) {
            if (propertyBagDTO != null) {
                List<CdsListDTO> cdsListDTOs = new ArrayList<CdsListDTO>();
                if (baseDTO.getListId() != null) {
                    if ("ALL".equals(baseDTO.getListId())) {
                        List<CdsListDTO> findByQueryList = findByQueryListMain(baseDTO, FindAll.class, getDtoChildClasses(), sessionDTO, propertyBagDTO);
                        cdsListDTOs.addAll(findByQueryList);
                    } else {
                        CdsListDTO cdsListDTO = findByPrimaryKeyMain(baseDTO, getDtoChildClasses(), sessionDTO, propertyBagDTO);
                        if (cdsListDTO != null) {
                            cdsListDTOs.add(cdsListDTO);
                        } else {
                            throw new NotFoundException(logger.error(METHODNAME, "findByPrimaryKey returned null!"));
                        }
                    }
                } else {
                    List<CdsListDTO> findByQueryList = findByQueryListMain(baseDTO, ClassUtils.dtoClassForName(baseDTO, propertyBagDTO.getQueryClass()), getDtoChildClasses(), sessionDTO, propertyBagDTO);
                    if (findByQueryList != null) {
                        cdsListDTOs.addAll(findByQueryList);
                    } else {
                        throw new NotFoundException(logger.error(METHODNAME, "findByQueryList returned null!"));
                    }
                }
                for (CdsListDTO cdsListDTO : cdsListDTOs) {
                    fileMap.putAll(exportCdsListAsMap(cdsListDTO, queryClass, sessionDTO, propertyBagDTO));
                }
                String xsd = FileUtils.getStringFromJarFile("cdsListSpecificationFile.xsd");
                fileMap.put("cdsListSpecificationFile.xsd", xsd.getBytes());
            } else {
                throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                        logger.error(METHODNAME, "PropertyBagDTO instance was null!"));
            }
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "CdsVersionDTO instance was null!"));
        }
        return fileMap;
    }

    /**
     * Export a single CdsListDTO instance as a Map with file name as the key and byte[] as the value. This method expects to be
     * passed a complete CdsVersionDTO instance.
     *
     * @param cdsListDTO the CdsListDTO instance
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the CdsListDTO in a map with the filename as the key
     * @throws ValidationException
     * @throws MtsException
     */
    private Map<String, byte[]> exportCdsListAsMap(CdsListDTO cdsListDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException {
        final String METHODNAME = "exportCdsListAsMap ";
        Map<String, byte[]> result = new HashMap<String, byte[]>();

        byte[] fileData = exportCdsList(cdsListDTO, queryClass, sessionDTO, propertyBagDTO);

        result.put(cdsListDTO.getListId() + ".xml", fileData);

        return result;
    }

    /**
     * Export a single CdsListDTO instance as XML. This method expects to be passed a complete CdsListDTO instance.
     *
     * @param cdsListDTO the CdsListDTO instance
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the CdsListDTO
     * @throws ValidationException
     * @throws MtsException
     */
    private byte[] exportCdsList(CdsListDTO cdsListDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException {
        final String METHODNAME = "exportCdsList ";
        byte[] result = null;

        CdsListSpecificationFile cdsListSpecificationFile = new CdsListSpecificationFile();

        cdsListSpecificationFile.setListId(cdsListDTO.getListId());
        cdsListSpecificationFile.setName(cdsListDTO.getName());
        cdsListSpecificationFile.setCode(cdsListDTO.getCode());
        cdsListSpecificationFile.setListType(cdsListDTO.getListType().name());
        cdsListSpecificationFile.setDescription(cdsListDTO.getDescription());

        CdsCodeSystemDTO cdsCodeSystemDTO = null;
        if (cdsListDTO.getCdsCodeSystemDTO() != null) {
            cdsCodeSystemDTO = cdsListDTO.getCdsCodeSystemDTO();
            cdsListSpecificationFile.setCodeSystem(cdsCodeSystemDTO.getOid());
            cdsListSpecificationFile.setCodeSystemName(cdsCodeSystemDTO.getName());
        }

        if (cdsListDTO.getEnumClass() != null) {
            Class enumClassClass = ClassUtils.classForName(cdsListDTO.getEnumClass());
            cdsListSpecificationFile.setEnumClass(enumClassClass.getCanonicalName());
        }

        if (cdsListDTO.getValueSetDTO() != null) {
            cdsListSpecificationFile.setValueSet(cdsListDTO.getValueSetDTO().getOid());
        }

        // for a couple list types we actually export their list items
        if (cdsListDTO.getListType() == CdsListType.AD_HOC || cdsListDTO.getListType() == CdsListType.AD_HOC_CONCEPT) {
            for (CdsListItemDTO cdsListItemDTO : cdsListDTO.getCdsListItemDTOs()) {
                if (cdsListItemDTO != null) {
                    CdsListItem cdsListItem = new CdsListItem();
                    if (cdsListDTO.getListType() == CdsListType.AD_HOC) {
                        cdsListItem.setCdsListItemKey(cdsListItemDTO.getAdHocId());
                        cdsListItem.setCdsListItemValue(cdsListItemDTO.getAdHocLabel());
                    } else if (cdsListDTO.getListType() == CdsListType.AD_HOC_CONCEPT) {
                        OpenCdsConceptDTO openCdsConceptDTO = cdsListItemDTO.getOpenCdsConceptDTO();
                        cdsListItem.setCdsListItemKey(openCdsConceptDTO.getCode());
                        cdsListItem.setCdsListItemValue(openCdsConceptDTO.getDisplayName());
                        logger.debug(METHODNAME, "openCdsConceptDTO.getOpenCdsConceptRelDTOs(): ", openCdsConceptDTO.getOpenCdsConceptRelDTOs());
                        for (OpenCdsConceptRelDTO openCdsConceptRelDTO : openCdsConceptDTO.getOpenCdsConceptRelDTOs()) {
                            if (openCdsConceptRelDTO.getMappingType() == MappingType.CODE) {
                                if (cdsCodeSystemDTO != null
                                        && cdsCodeSystemDTO.getOid().equals(openCdsConceptRelDTO.getCdsCodeDTO().getCodeSystem())) {
                                    logger.debug(METHODNAME, "openCdsConceptRelDTO.getConceptDeterminationMethodDTO().getCode(): ",
                                            openCdsConceptRelDTO.getConceptDeterminationMethodDTO().getCode());
                                    logger.debug(METHODNAME, "openCdsConceptRelDTO.getCode(): ", openCdsConceptRelDTO.getCdsCodeDTO() != null ? openCdsConceptRelDTO.getCdsCodeDTO().getCode() : null);
                                    CdsListItemConceptMapping cdsListItemConceptMapping = new CdsListItemConceptMapping();
                                    cdsListItemConceptMapping.setConceptDeterminationMethod(openCdsConceptRelDTO.getConceptDeterminationMethodDTO().getCode());
                                    cdsListItemConceptMapping.setCode(openCdsConceptRelDTO.getCdsCodeDTO() != null ? openCdsConceptRelDTO.getCdsCodeDTO().getCode() : null);
                                    cdsListItemConceptMapping.setDisplayName(openCdsConceptRelDTO.getCdsCodeDTO() != null ? openCdsConceptRelDTO.getCdsCodeDTO().getDisplayName() : null);
                                    cdsListItemConceptMapping.setCodeSystem(openCdsConceptRelDTO.getCdsCodeSystemDTO() != null ? openCdsConceptRelDTO.getCdsCodeSystemDTO().getOid() : null);
                                    cdsListItemConceptMapping.setCodeSystemName(openCdsConceptRelDTO.getCdsCodeSystemDTO() != null ? openCdsConceptRelDTO.getCdsCodeSystemDTO().getName() : null);
                                    cdsListItem.getCdsListItemConceptMappings().add(cdsListItemConceptMapping);
                                }
                            } else if (openCdsConceptRelDTO.getMappingType() == MappingType.CODE_SYSTEM) {
                                throw new UnsupportedOperationException("Mapping type CODE_SYSTEM not yet implemented!");
                            } else if (openCdsConceptRelDTO.getMappingType() == MappingType.VALUE_SET) {
                                throw new UnsupportedOperationException("Mapping type VALUE_SET not yet implemented!");
                            }
                        }
                    } else {
                        throw new MtsException(METHODNAME + "This should never happen...");
                    }
                    cdsListSpecificationFile.getCdsListItems().add(cdsListItem);
                } else {
                    logger.error(METHODNAME, "cdsListItemDTO was null! ", cdsListDTO);
                }
            }
        } else if (cdsListDTO.getListType() == CdsListType.CODE_SYSTEM || cdsListDTO.getListType() == CdsListType.VALUE_SET) {
            for (CdsListItemDTO item : cdsListDTO.getCdsListItemDTOs()) {
                CdsCodeDTO cdsCodeDTO;
                if (item.getItemType() == CdsListType.CODE_SYSTEM) {
                    cdsCodeDTO = item.getCdsCodeDTO();
                } else {
                    cdsCodeDTO = item.getValueSetCdsCodeRelDTO().getCdsCodeDTO();
                }
                CdsListItem cdsListItem = new CdsListItem();
                cdsListItem.setCdsListItemKey(cdsCodeDTO.getCode());
                cdsListItem.setCdsListItemValue(cdsCodeDTO.getDisplayName());
                cdsListSpecificationFile.getCdsListItems().add(cdsListItem);
                for (CdsCodeOpenCdsConceptRelDTO cdsCodeOpenCdsConceptRelDTO : cdsCodeDTO.getCdsCodeOpenCdsConceptRelDTOs()) {
                    OpenCdsConceptDTO openCdsConceptDTO = new OpenCdsConceptDTO();
                    try {
                        openCdsConceptDTO.setCodeId(cdsCodeOpenCdsConceptRelDTO.getOpenCdsCodeId());
                        openCdsConceptDTO = openCdsConceptBO.findByPrimaryKeyMain(openCdsConceptDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);
                    } catch (NotFoundException e) {
                        continue;
                    }
                    if (openCdsConceptDTO != null) {
                        CdsListItemConceptMapping cdsListItemConceptMapping = new CdsListItemConceptMapping();
                        cdsListItemConceptMapping.setCode(openCdsConceptDTO.getCode());
                        cdsListItemConceptMapping.setDisplayName(openCdsConceptDTO.getDisplayName());
                        cdsListItem.getCdsListItemConceptMappings().add(cdsListItemConceptMapping);
                    }
                }
            }
        } else if (cdsListDTO.isConceptBased()) {
            for (CdsListItemDTO item : cdsListDTO.getCdsListItemDTOs()) {
                OpenCdsConceptDTO openCdsConceptDTO = item.getOpenCdsConceptDTO();
                CdsListItem cdsListItem = new CdsListItem();
                cdsListItem.setCdsListItemKey(openCdsConceptDTO.getCode());
                cdsListItem.setCdsListItemValue(openCdsConceptDTO.getDisplayName());
                cdsListSpecificationFile.getCdsListItems().add(cdsListItem);
                logger.debug(METHODNAME, "openCdsConceptDTO.getOpenCdsConceptRelDTOs(): ", openCdsConceptDTO.getOpenCdsConceptRelDTOs());
                for (OpenCdsConceptRelDTO openCdsConceptRelDTO : openCdsConceptDTO.getOpenCdsConceptRelDTOs()) {
                    if (openCdsConceptRelDTO.getMappingType() == MappingType.CODE) {
                        if (cdsCodeSystemDTO != null
                                && cdsCodeSystemDTO.getOid().equals(openCdsConceptRelDTO.getCdsCodeDTO().getCodeSystem())) {
                            logger.debug(METHODNAME, "openCdsConceptRelDTO.getConceptDeterminationMethodDTO().getCode(): ",
                                    openCdsConceptRelDTO.getConceptDeterminationMethodDTO().getCode());
                            logger.debug(METHODNAME, "openCdsConceptRelDTO.getCode(): ", openCdsConceptRelDTO.getCdsCodeDTO() != null ? openCdsConceptRelDTO.getCdsCodeDTO().getCode() : null);
                            CdsListItemConceptMapping cdsListItemConceptMapping = new CdsListItemConceptMapping();
                            cdsListItemConceptMapping.setConceptDeterminationMethod(openCdsConceptRelDTO.getConceptDeterminationMethodDTO().getCode());
                            cdsListItemConceptMapping.setCode(openCdsConceptRelDTO.getCdsCodeDTO() != null ? openCdsConceptRelDTO.getCdsCodeDTO().getCode() : null);
                            cdsListItemConceptMapping.setDisplayName(openCdsConceptRelDTO.getCdsCodeDTO() != null ? openCdsConceptRelDTO.getCdsCodeDTO().getDisplayName() : null);
                            cdsListItemConceptMapping.setCodeSystem(openCdsConceptRelDTO.getCdsCodeSystemDTO() != null ? openCdsConceptRelDTO.getCdsCodeSystemDTO().getOid() : null);
                            cdsListItemConceptMapping.setCodeSystemName(openCdsConceptRelDTO.getCdsCodeSystemDTO() != null ? openCdsConceptRelDTO.getCdsCodeSystemDTO().getName() : null);
                            cdsListItem.getCdsListItemConceptMappings().add(cdsListItemConceptMapping);
                        }
                    } else if (openCdsConceptRelDTO.getMappingType() == MappingType.CODE_SYSTEM) {
                        throw new UnsupportedOperationException("Mapping type CODE_SYSTEM not yet implemented!");
                    } else if (openCdsConceptRelDTO.getMappingType() == MappingType.VALUE_SET) {
                        throw new UnsupportedOperationException("Mapping type VALUE_SET not yet implemented!");
                    }
                }
            }
        }

        // export the versions this list is assigned to
        for (CdsVersionDTO cdsVersionDTO : cdsListDTO.getRelatedVersions()) {
            cdsListSpecificationFile.getCdsVersions().add(cdsVersionDTO.getOpenCdsVersionIdentifier());
        }

        try {
            result = MarshalUtils.marshalObject(cdsListSpecificationFile);
        } catch (CdsException e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        }

        return result;
    }

    @Override
    public void importData(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException,
            ConstraintViolationException {
        final String METHODNAME = "importData ";
        if (queryClass == SimpleExchange.class) {
            simpleExchangeImport(queryClass, sessionDTO, propertyBagDTO);
        } else {
            throw new MtsException("Unsupported queyClass: " + queryClass);
        }
    }

    public void simpleExchangeImport(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException,
            ConstraintViolationException {
        final String METHODNAME = "simpleExchangeImport ";
        byte[] payload = propertyBagDTO.get("payload", byte[].class);
        if (payload != null) {
            // payload is base64 byte array of zip file - decode and unzip it and get the XML files out of it.
            List<String> data = new ArrayList(FileUtils.getDataMapFromBase64ZipByteArray(payload, "xml").values());
            try {
                for (String xmlData : data) {
                    CdsListSpecificationFile cdsListSpecificationFile = MarshalUtils.unmarshal(IOUtils.toInputStream(xmlData), CdsListSpecificationFile.class);
                    CdsListDTO cdsListDTO = new CdsListDTO();
                    cdsListDTO.setListId(cdsListSpecificationFile.getListId());
                    cdsListDTO.setName(cdsListSpecificationFile.getName());
                    cdsListDTO.setCode(cdsListSpecificationFile.getCode());
                    cdsListDTO.setDescription(cdsListSpecificationFile.getDescription());
                    cdsListDTO.setListType(CdsListType.valueOf(cdsListSpecificationFile.getListType()));

                    // code system types and certain concept based types have this set
                    if (!StringUtils.isEmpty(cdsListSpecificationFile.getCodeSystem())) {
                        logger.info(METHODNAME, "got Code System OID: ", cdsListSpecificationFile.getCodeSystem());
                        CdsCodeSystemDTO cdsCodeSystemDTO = new CdsCodeSystemDTO();
                        cdsCodeSystemDTO.setOid(cdsListSpecificationFile.getCodeSystem());
                        try {
                            List<Class> childClasses = new ArrayList<Class>();
                            childClasses.add(CdsCodeDTO.class);
                            cdsCodeSystemDTO = cdsCodeSystemBO.findByQueryMain(cdsCodeSystemDTO, CdsCodeSystemDTO.ByOid.class, childClasses, sessionDTO, propertyBagDTO);
                            logger.info(METHODNAME, "found Code System OID: ", cdsCodeSystemDTO.getOid());
                        } catch (NotFoundException e) {
                            // if the code system doesn't exist and we have enough info then create it.
                            if (cdsListSpecificationFile.getCodeSystem() != null && cdsListSpecificationFile.getCodeSystemName() != null) {
                                cdsCodeSystemDTO = new CdsCodeSystemDTO();
                                cdsCodeSystemDTO.setOid(cdsListSpecificationFile.getCodeSystem());
                                cdsCodeSystemDTO.setName(cdsListSpecificationFile.getCodeSystemName());
                                cdsCodeSystemDTO = cdsCodeSystemBO.addMain(cdsCodeSystemDTO, Add.class, sessionDTO, propertyBagDTO);
                                logger.info(METHODNAME, "created Code System OID: ", cdsCodeSystemDTO.getOid());
                            } else {
                                logger.error(METHODNAME, "Code System OID not found and unable to create it from supplied data: ", cdsListSpecificationFile.getCodeSystem(), " - ", cdsListSpecificationFile.getCodeSystemName());
                            }
                        }

                        // check if the items listed in the cdsListItems list all exist - create them if they don't
                        for (CdsListItem cdsListItem : cdsListSpecificationFile.getCdsListItems()) {
                            boolean found = false;
                            for (CdsCodeDTO item : cdsCodeSystemDTO.getCdsCodeDTOs()) {
                                if (item.getCode().equalsIgnoreCase(cdsListItem.getCdsListItemKey())) {
                                    found = true;
                                    break;
                                }
                            }
                            // if not found add the code to the code system
                            if (!found) {
                                CdsCodeDTO cdsCodeDTO = new CdsCodeDTO();
                                cdsCodeDTO.setCode(cdsListItem.getCdsListItemKey());
                                cdsCodeDTO.setDisplayName(cdsListItem.getCdsListItemValue());
                                cdsCodeSystemDTO.addOrUpdateChildDTO(cdsCodeDTO);
                                cdsCodeSystemDTO = cdsCodeSystemBO.updateMain(cdsCodeSystemDTO, Update.class, sessionDTO, propertyBagDTO);
                            }
                        }

                        if (cdsCodeSystemDTO == null) {
                            logger.error(METHODNAME, "cdsCodeSystemDTO was null!");
                        } else {
                            cdsListDTO.setCdsCodeSystemDTO(cdsCodeSystemDTO);
                        }
                    }

                    // for types by value set
                    // TODO: add create support of valueset doesn't exist or underlying code system doesn't exist
                    if (!StringUtils.isEmpty(cdsListSpecificationFile.getValueSet())) {
                        logger.info(METHODNAME, "got Value Set OID: ", cdsListSpecificationFile.getValueSet());
                        List<Class> childClasses = new ArrayList<Class>();
                        childClasses.add(ValueSetSubValueSetRelDTO.class);
                        childClasses.add(ValueSetCdsCodeRelDTO.class);
                        ValueSetDTO valueSetDTO = new ValueSetDTO();
                        valueSetDTO.setOid(cdsListSpecificationFile.getValueSet());
                        try {
                            valueSetDTO = valueSetBO.findByQueryMain(valueSetDTO, ValueSetDTO.ByOid.class, childClasses, sessionDTO, propertyBagDTO);
                        } catch (NotFoundException e) {
                            logger.error(METHODNAME, "value set not found: ", cdsListSpecificationFile.getValueSet());
                        }
                        if (valueSetDTO == null) {
                            logger.error(METHODNAME, "valueSetDTO was null!");
                        } else {
                            cdsListDTO.setValueSetDTO(valueSetDTO);
                        }
                    }

                    // for java enum types
                    if (cdsListDTO.getListType() == CdsListType.JAVA_ENUM) {

                        if (StringUtils.isEmpty(cdsListSpecificationFile.getEnumClass())) {
                            throw new MtsException("list type is ENUM but class is null!");
                        } else {
                            cdsListDTO.setEnumClass(cdsListSpecificationFile.getEnumClass());
                        }
                    }

                    // for exportable list items
                    for (CdsListItem cdsListItem : cdsListSpecificationFile.getCdsListItems()) {
                        if (cdsListDTO.getListType() == CdsListType.AD_HOC) {
                            CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
                            cdsListItemDTO.setAdHocId(cdsListItem.getCdsListItemKey());
                            cdsListItemDTO.setAdHocLabel(cdsListItem.getCdsListItemValue());
                            cdsListDTO.addOrUpdateChildDTO(cdsListItemDTO);
                        } else if (cdsListDTO.getListType() == CdsListType.AD_HOC_CONCEPT) {
                            CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
                            OpenCdsConceptDTO openCdsConceptDTO = new OpenCdsConceptDTO();
                            openCdsConceptDTO.setCode(cdsListItem.getCdsListItemKey());
                            try {
                                openCdsConceptDTO = openCdsConceptBO.findByQueryMain(openCdsConceptDTO, OpenCdsConceptDTO.ByCode.class, new ArrayList<Class>(), sessionDTO, propertyBagDTO);
                            } catch (NotFoundException e) {
                                logger.error(METHODNAME, "concept: ", cdsListItem.getCdsListItemKey());
                            }
                            if (openCdsConceptDTO == null) {
                                logger.error(METHODNAME, "openCdsConceptDTO was null!");
                            } else {
                                cdsListItemDTO.setOpenCdsConceptDTO(openCdsConceptDTO);
                            }
                            cdsListDTO.addOrUpdateChildDTO(cdsListItemDTO);
                        } else if (cdsListDTO.getListType() == CdsListType.CODE_SYSTEM) {
                            // this is ok
                        } else {
                            throw new MtsException(METHODNAME + "This should never happen...");
                        }
                    }

                    // for versions
                    for (String cdsVersion : cdsListSpecificationFile.getCdsVersions()) {
                        CdsVersionDTO cdsVersionDTO = cdsVersionBO.getCdsVersioDTOFromOpenCdsBusinessVersionIdentifier(cdsVersion, true);
                        logger.info(METHODNAME, "getCdsVersioDTOFromOpenCdsBusinessVersionIdentifier: ", cdsVersionDTO);
                        CdsListVersionRelDTO cdsListVersionRelDTO = new CdsListVersionRelDTO();
                        cdsListVersionRelDTO.setCdsVersionDTO(cdsVersionDTO);
                        cdsListDTO.addOrUpdateChildDTO(cdsListVersionRelDTO);
                    }
                    addMain(cdsListDTO, Add.class, sessionDTO, propertyBagDTO);
                }
            } catch (CdsException e) {
                throw new MtsException(e.getMessage(), e);
            }
        } else {
            throw new MtsException("payload was null!");
        }
    }

    /**
     * Returns a CdsListDTO based on its code and version.
     *
     * @param code
     * @param cdsVersionDTO
     * @return
     * @throws MtsException
     * @throws NotFoundException
     * @throws org.cdsframework.exceptions.ValidationException
     * @throws org.cdsframework.exceptions.AuthenticationException
     * @throws org.cdsframework.exceptions.AuthorizationException
     */
    public CdsListDTO getCdsListDTOByCodeVersion(String code, CdsVersionDTO cdsVersionDTO)
            throws MtsException, NotFoundException, ValidationException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getCdsListDTOByCodeVersion ";
        long start = System.nanoTime();
        List<Class> childClasses = new ArrayList<Class>();
        childClasses.add(CdsListItemDTO.class);
        childClasses.add(CdsListVersionRelDTO.class);
        CdsListDTO query = new CdsListDTO();
        query.setCode(code);
        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.put("version_id", cdsVersionDTO.getVersionId());
        CdsListDTO result = findByQueryMain(
                query,
                CdsListDTO.ByCodeVersionId.class,
                childClasses,
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        logger.logDuration(METHODNAME, start);
        return result;
    }

    /**
     * Returns a map of CdsListDTOs keyed by the code in lowercase.
     *
     * @return
     * @throws MtsException
     * @throws NotFoundException
     * @throws org.cdsframework.exceptions.ValidationException
     * @throws org.cdsframework.exceptions.AuthenticationException
     * @throws org.cdsframework.exceptions.AuthorizationException
     */
    public Map<String, CdsListDTO> getCdsListDTOMapByCode()
            throws MtsException, NotFoundException, ValidationException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getCdsListDTOMapByCode ";
        long start = System.nanoTime();
        List<Class> childClasses = new ArrayList<Class>();
        childClasses.add(CdsListItemDTO.class);
        childClasses.add(CdsListVersionRelDTO.class);
        Map<String, CdsListDTO> result = new HashMap<String, CdsListDTO>();
        List<CdsListDTO> results = findByQueryListMain(new CdsListDTO(), FindAll.class, childClasses, AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
        for (CdsListDTO item : results) {
            result.put(item.getCode().toLowerCase(), item);
        }
        logger.logDuration(METHODNAME, start);
        return result;
    }

    /**
     * Preset new children to the parents list type.
     *
     * @param baseDTO
     */
    private void setCdsListChildType(CdsListDTO baseDTO) {
        final String METHODNAME = "setCdsListChildType ";
        long start = System.nanoTime();
        List<CdsListItemDTO> childrenDTOs = (List) baseDTO.getChildrenDTOs(CdsListItemDTO.ByCdsListId.class, DTOState.NEW);
        for (CdsListItemDTO item : childrenDTOs) {
            if (item.getItemType() == null) {
                item.setItemType(baseDTO.getListType());
            }
        }
        logger.logDuration(METHODNAME, start);
    }

    /**
     * Utility method for a given query criteria and class to sync the list result set.
     *
     * @param queryCriteria
     * @param queryClass
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     */
    private void synchronizeCdsListsByQueryCriteria(CdsListDTO queryCriteria, Class queryClass)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException,
            ConstraintViolationException {
        final String METHODNAME = "synchronizeCdsListsByQueryCriteria ";
        long start = System.nanoTime();
        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        List<Class> childClasses = new ArrayList<Class>();
        childClasses.add(CdsListItemDTO.class);
        childClasses.add(CdsListVersionRelDTO.class);
        List<CdsListDTO> resultSet = findByQueryListMain(queryCriteria, queryClass, childClasses, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        for (CdsListDTO item : resultSet) {
            synchronizeCdsListItems(item, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
            if (item.getOperationDTOState() != DTOState.UNSET) {
                updateMain(item, Update.class, AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
            }
        }
        logger.logDuration(METHODNAME, start);
    }

    /**
     * Determines if an code exists already.
     *
     * @param baseDTO
     * @param operation
     * @return
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private boolean isCodeExists(CdsListDTO baseDTO, Operation operation)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "isCodeExists ";
        long start = System.nanoTime();
        boolean result = false;
        if ((operation == Operation.UPDATE && baseDTO.isPropertyChanged("code"))
                || (operation == Operation.ADD && baseDTO.isNew())) {
            CdsListDTO query = new CdsListDTO();
            query.setCode(baseDTO.getCode() != null ? baseDTO.getCode().toLowerCase() : null);

            try {
                List<CdsListDTO> results = findByQueryListMain(
                        query,
                        CdsListDTO.ByLowerCode.class,
                        new ArrayList<Class>(),
                        AuthenticationUtils.getInternalSessionDTO(),
                        new PropertyBagDTO());
                if (results != null) {
                    if (!results.isEmpty()) {
                        if (results.size() > 1) {
                            result = true;
                        } else if (results.size() == 1 && !results.get(0).getListId().equals(baseDTO.getListId())) {
                            result = true;
                        }
                    }
                }
            } catch (NotFoundException e) {
                // nada
            }
        }
        logger.logDuration(METHODNAME, start);
        return result;
    }

    @Override
    protected void postFindBy(CdsListDTO baseDTO, Class queryClass, List<Class> childClassDTOs, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws NotFoundException, MtsException, ValidationException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "postFindBy ";
        CdsListItemComparator cdsListItemComparator = new CdsListItemComparator();
        Collections.sort(baseDTO.getCdsListItemDTOs(), cdsListItemComparator);
    }
}
