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
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseBO;
import org.cdsframework.cds.util.MarshalUtils;
import org.cdsframework.dto.CdsBusinessScopeDTO;
import org.cdsframework.dto.CdsVersionConceptDeterminationMethodRelDTO;
import org.cdsframework.dto.CdsVersionDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.CoreErrorCode;
import org.cdsframework.enumeration.Status;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Add;
import org.cdsframework.group.SimpleExchange;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.BrokenRule;
import org.cdsframework.util.ClassUtils;
import org.cdsframework.util.FileUtils;
import org.cdsframework.util.comparator.CdsVersionComparator;
import org.cdsframework.util.support.data.cds.version.CdsVersionSpecificationFile;
import org.omg.spec.cdss._201105.dss.EntityIdentifier;
import org.opencds.vmr.v1_0.schema.CD;

/**
 *
 * @author HLN Consulting, LLC
 */
@LocalBean
@Stateless
public class CdsVersionBO extends BaseBO<CdsVersionDTO> {

    @EJB
    private CdsBusinessScopeBO cdsBusinessScopeBO;
    @EJB
    private ConceptDeterminationMethodBO conceptDeterminationMethodBO;
//    private final String versionPattern = "org.cdsframework^ICE^";

    @Override
    protected void initialize() throws MtsException {
        setSelfReferencing(true);
    }

    @Override
    protected CdsVersionDTO customQuery(CdsVersionDTO parentDTO, Class queryClass, List<Class> childClassDTOs, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        if (queryClass == CdsVersionDTO.NextVersion.class) {
            return getNextVersion(parentDTO, sessionDTO, propertyBagDTO);
        }
        return super.customQuery(parentDTO, queryClass, childClassDTOs, sessionDTO, propertyBagDTO);
    }

    /**
     * Gets the next version of a knowledge module - if it exists, return it -
     * otherwise - create it.
     *
     * @param cdsVersionDTO
     * @param sessionDTO
     * @param propertyBagDTO
     * @return
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     */
    private CdsVersionDTO getNextVersion(CdsVersionDTO cdsVersionDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        final String METHODNAME = "getNextVersion ";
        CdsVersionDTO result;

        try {
            logger.info(METHODNAME, "cdsVersionDTO=", cdsVersionDTO);
            if (cdsVersionDTO == null) {
                throw new ValidationException(new BrokenRule(CoreErrorCode.FieldCanNotBeEmpty, "cdsVersionDTO was null!"));
            }

            // get refreshed cdsBusinessScopeDTO
            CdsBusinessScopeDTO cdsBusinessScopeDTO = new CdsBusinessScopeDTO();
            cdsBusinessScopeDTO.setBusinessScopeId(cdsVersionDTO.getBusinessScopeId());
            cdsBusinessScopeDTO = cdsBusinessScopeBO.findByPrimaryKeyMain(cdsBusinessScopeDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);

            logger.info(METHODNAME, "cdsBusinessScopeDTO=", cdsBusinessScopeDTO);
            if (cdsBusinessScopeDTO == null) {
                throw new ValidationException(new BrokenRule(CoreErrorCode.FieldCanNotBeEmpty, "cdsBusinessScopeDTO was null!"));
            }
            logger.info(METHODNAME, "cdsBusinessScopeDTO.getBusinessScopeId()=", cdsBusinessScopeDTO.getBusinessScopeId());
            if (cdsBusinessScopeDTO.getBusinessScopeId() == null) {
                throw new ValidationException(new BrokenRule(CoreErrorCode.FieldCanNotBeEmpty, "cdsBusinessScopeDTO.getBusinessScopeId() was null!"));
            }
            logger.info(METHODNAME, "cdsVersionDTO.getLabel()=", cdsVersionDTO.getLabel());

            CdsVersionDTO queryDTO = new CdsVersionDTO();
            queryDTO.setBusinessScopeId(cdsBusinessScopeDTO.getBusinessScopeId());
            queryDTO.setBusinessId(cdsBusinessScopeDTO.getBusinessId());
            queryDTO.setScopingEntityId(cdsBusinessScopeDTO.getScopingEntityId());
            queryDTO.setStatus(Status.ACTIVE);

            List<CdsVersionDTO> existingVersionList = findByQueryListMain(
                    queryDTO,
                    CdsVersionDTO.ByBusinessScopeIdStatus.class,
                    new ArrayList(),
                    sessionDTO,
                    propertyBagDTO);

            logger.info(METHODNAME, "existingVersionList=", existingVersionList);

            logger.info(METHODNAME, "existingVersionList.isEmpty()=", existingVersionList.isEmpty());
            if (existingVersionList.isEmpty()) {
                result = new CdsVersionDTO();
                result.setBusinessScopeId(cdsBusinessScopeDTO.getBusinessScopeId());
                result.setBusinessId(cdsBusinessScopeDTO.getBusinessId());
                result.setScopingEntityId(cdsBusinessScopeDTO.getScopingEntityId());
                result.setVersion("1.0.0");
                result.setStatus(Status.ACTIVE);
                result.setName("Initial Version");
                result.setDescription(result.getName());
                // TODO: add code for adding a default cdm
            } else {
                logger.info(METHODNAME, "existingVersionList: ", existingVersionList);

                Collections.sort(existingVersionList, new CdsVersionComparator());
                CdsVersionDTO lastCdsVersionDTO = existingVersionList.get(existingVersionList.size() - 1);
                String nextVersionNumber = getNextVersionNumber(lastCdsVersionDTO);
                logger.info(METHODNAME, "using derived version: ", nextVersionNumber);

                result = new CdsVersionDTO();
                result.setBusinessScopeId(cdsBusinessScopeDTO.getBusinessScopeId());
                result.setBusinessId(cdsBusinessScopeDTO.getBusinessId());
                result.setScopingEntityId(cdsBusinessScopeDTO.getScopingEntityId());
                result.setVersion(nextVersionNumber);

                try {
                    result = findByQueryMain(result, CdsVersionDTO.ByBusinessScopeIdVersion.class, new ArrayList(), sessionDTO, propertyBagDTO);
                } catch (NotFoundException e) {
                    result = null;
                }

                if (result == null) {
                    logger.info(METHODNAME, "version doesn't exist - setting up a new one...");
                    result = new CdsVersionDTO();
                    result.setBusinessScopeId(cdsBusinessScopeDTO.getBusinessScopeId());
                    result.setBusinessId(cdsBusinessScopeDTO.getBusinessId());
                    result.setScopingEntityId(cdsBusinessScopeDTO.getScopingEntityId());
                    result.setVersion(nextVersionNumber);
                    result.setStatus(Status.ACTIVE);
                    result.setName(lastCdsVersionDTO.getName());
                    result.setDescription(lastCdsVersionDTO.getDescription());
                    for (CdsVersionConceptDeterminationMethodRelDTO item : lastCdsVersionDTO.getCdsVersionConceptDeterminationMethodRelDTOs()) {
                        CdsVersionConceptDeterminationMethodRelDTO cdsVersionConceptDeterminationMethodRelDTO = new CdsVersionConceptDeterminationMethodRelDTO();
                        cdsVersionConceptDeterminationMethodRelDTO.setConceptDeterminationMethodDTO(item.getConceptDeterminationMethodDTO());
                        result.addOrUpdateChildDTO(cdsVersionConceptDeterminationMethodRelDTO);
                    }
                }
            }
            if (result.isNew()) {
                result = addMain(result, Add.class, sessionDTO, propertyBagDTO);
            }
            logger.info(METHODNAME, "result=", result);
            logger.info(METHODNAME, "result.getLabel()=", result.getLabel());
        } catch (ValidationException ex) {
            logger.error(ex);
            throw ex;
        } catch (NotFoundException ex) {
            logger.error(ex);
            throw ex;
        } catch (MtsException ex) {
            logger.error(ex);
            throw ex;
        } catch (ConstraintViolationException ex) {
            logger.error(ex);
            throw ex;
        } catch (AuthorizationException ex) {
            logger.error(ex);
            throw ex;
        } catch (AuthenticationException ex) {
            logger.error(ex);
            throw ex;
        } catch(NullPointerException ex) {
            logger.error(ex);
            throw new MtsException(ex.getMessage());
        }
        return result;
    }

    /**
     * Gets the next version number.
     *
     * @param lastCdsVersionDTO
     * @return
     * @throws ValidationException
     */
    private String getNextVersionNumber(CdsVersionDTO lastCdsVersionDTO) throws ValidationException {
        final String METHODNAME = "getNextVersionNumber ";
        String result = null;
        if (lastCdsVersionDTO != null) {
            String version = lastCdsVersionDTO.getVersion();
            if (version != null) {
                logger.info(METHODNAME, "got version: ", version);
                String[] split = version.split("\\.");
                if (split.length > 1) {
                    split[split.length - 1] = "" + (Integer.valueOf(split[split.length - 1]) + 1);
                    result = "";
                    for (String item : split) {
                        result += item + ".";
                    }
                    result = result.substring(0, result.length() - 1);
                    logger.info(METHODNAME, "next version: ", result);
                } else {
                    throw new ValidationException(new BrokenRule(CoreErrorCode.ValueError, "Couldn't get next version number for: " + version));
                }
            }
        }
        return result;
    }

    /**
     * Export CdsVersionDTO instances as a Map.
     *
     * @param baseDTO the CdsVersionDTO query instance
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the zip file containing the
     * CdsVersionDTOs that were the result of a query.
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    public Map<String, byte[]> exportData(CdsVersionDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "exportData ";
        if (queryClass == SimpleExchange.class) {
            return simpleExchangeExport(baseDTO, queryClass, sessionDTO, propertyBagDTO);
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
        }
    }

    /**
     * Export CdsVersionDTO instances as a Map. This method expects to be passed
     * a query CdsVersionDTO instance.
     *
     * @param baseDTO the CdsVersionDTO query instance
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the zip file containing the
     * CdsVersionDTOs that were the result of a query.
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private Map<String, byte[]> simpleExchangeExport(CdsVersionDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "simpleExchangeExport ";
        Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
        if (baseDTO != null) {
            if (propertyBagDTO != null) {
                List<CdsVersionDTO> cdsVersionDTOs = new ArrayList<CdsVersionDTO>();

                if (baseDTO.getVersionId() != null) {
                    CdsVersionDTO cdsVersionDTO = findByPrimaryKeyMain(baseDTO, (List) propertyBagDTO.getChildClassDTOs(), sessionDTO, propertyBagDTO);
                    if (cdsVersionDTO != null) {
                        cdsVersionDTOs.add(cdsVersionDTO);
                    } else {
                        throw new NotFoundException(logger.error(METHODNAME, "findByPrimaryKey returned null!"));
                    }
                } else {
                    List<CdsVersionDTO> findByQueryList = findByQueryListMain(
                            baseDTO,
                            ClassUtils.dtoClassForName(baseDTO, propertyBagDTO.getQueryClass()),
                            (List) propertyBagDTO.getChildClassDTOs(),
                            sessionDTO, propertyBagDTO);
                    if (findByQueryList != null) {
                        cdsVersionDTOs.addAll(findByQueryList);
                    } else {
                        throw new NotFoundException(logger.error(METHODNAME, "findByQueryList returned null!"));
                    }
                }
                for (CdsVersionDTO cdsVersionDTO : cdsVersionDTOs) {
                    fileMap.putAll(exportVersionDTOAsMap(cdsVersionDTO, sessionDTO, propertyBagDTO));
                }
                String xsd = FileUtils.getStringFromJarFile("cdsVersionSpecificationFile.xsd");
                fileMap.put("cdsVersionSpecificationFile.xsd", xsd.getBytes());
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
     * Export a single CdsVersionDTO instance as a Map with file name as the key
     * and byte[] as the value. This method expects to be passed a complete
     * CdsVersionDTO instance.
     *
     * @param cdsVersionDTO the CdsVersionDTO instance
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the CdsVersionDTO in a map
     * with the filename as the key
     * @throws ValidationException
     * @throws org.cdsframework.exceptions.MtsException
     * @throws org.cdsframework.exceptions.NotFoundException
     * @throws org.cdsframework.exceptions.AuthenticationException
     * @throws org.cdsframework.exceptions.AuthorizationException
     */
    private Map<String, byte[]> exportVersionDTOAsMap(CdsVersionDTO cdsVersionDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, MtsException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "exportVersionDTOAsMap ";
        Map<String, byte[]> result = new HashMap<String, byte[]>();

        byte[] fileData = exportVersionDTO(cdsVersionDTO, sessionDTO, propertyBagDTO);

        CdsBusinessScopeDTO cdsBusinessScopeDTO = new CdsBusinessScopeDTO();
        cdsBusinessScopeDTO.setBusinessScopeId(cdsVersionDTO.getBusinessScopeId());
        cdsBusinessScopeDTO = cdsBusinessScopeBO.findByPrimaryKeyMain(cdsBusinessScopeDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);

        String fileName = String.format(
                "%s^%s^%s.xml",
                cdsBusinessScopeDTO.getScopingEntityId(),
                cdsBusinessScopeDTO.getBusinessId(),
                cdsVersionDTO.getVersion());

        result.put(fileName, fileData);

        return result;
    }

    /**
     * Export a single CdsVersionDTO instance as XML. This method expects to be
     * passed a complete CdsVersionDTO instance.
     *
     * @param cdsVersionDTO the CdsVersionDTO instance
     * @param sessionDTO
     * @param propertyBagDTO
     * @return the XML byte data representation of the CdsVersionDTO
     * @throws ValidationException
     * @throws org.cdsframework.exceptions.MtsException
     * @throws org.cdsframework.exceptions.NotFoundException
     * @throws org.cdsframework.exceptions.AuthenticationException
     * @throws org.cdsframework.exceptions.AuthorizationException
     */
    private byte[] exportVersionDTO(CdsVersionDTO cdsVersionDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, MtsException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "exportVersionDTO ";
        byte[] result = null;
        validateCdsVersionDTO(cdsVersionDTO, sessionDTO, propertyBagDTO);

        CdsVersionSpecificationFile cdsVersionSpecificationFile = getCdsVersionSpecificationFileFromCdsVersionDTO(cdsVersionDTO, sessionDTO, propertyBagDTO);

        try {
            result = MarshalUtils.marshalObject(cdsVersionSpecificationFile);
        } catch (CdsException e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        }

        return result;
    }

    /**
     * Convert a CdsVersionDTO to the exportable CdsVersionSpecificationFile
     * class for export.
     *
     * @param cdsVersionDTO
     * @param sessionDTO
     * @param propertyBagDTO
     * @return
     * @throws ValidationException
     * @throws MtsException
     * @throws org.cdsframework.exceptions.NotFoundException
     * @throws org.cdsframework.exceptions.AuthenticationException
     * @throws org.cdsframework.exceptions.AuthorizationException
     */
    private CdsVersionSpecificationFile getCdsVersionSpecificationFileFromCdsVersionDTO(CdsVersionDTO cdsVersionDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, MtsException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getCdsVersionSpecificationFileFromCdsVersionDTO ";
        CdsVersionSpecificationFile result = new CdsVersionSpecificationFile();
        validateCdsVersionDTO(cdsVersionDTO, sessionDTO, propertyBagDTO);

        for (ConceptDeterminationMethodDTO conceptDeterminationMethodDTO : cdsVersionDTO.getConceptDeterminationMethodDTOs()) {
            CD conceptDeterminationMethod = new CD();
            conceptDeterminationMethod.setCode(conceptDeterminationMethodDTO.getCode());
            conceptDeterminationMethod.setDisplayName(conceptDeterminationMethodDTO.getDisplayName());
            result.getConceptDeterminationMethods().add(conceptDeterminationMethod);
        }

        CdsBusinessScopeDTO cdsBusinessScopeDTO = new CdsBusinessScopeDTO();
        cdsBusinessScopeDTO.setBusinessScopeId(cdsVersionDTO.getBusinessScopeId());
        cdsBusinessScopeDTO = cdsBusinessScopeBO.findByPrimaryKeyMain(cdsBusinessScopeDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);

        EntityIdentifier entityIdentifier = new EntityIdentifier();
        entityIdentifier.setBusinessId(cdsBusinessScopeDTO.getBusinessId());
        entityIdentifier.setScopingEntityId(cdsBusinessScopeDTO.getScopingEntityId());
        entityIdentifier.setVersion(cdsVersionDTO.getVersion());

        result.setEntityIdentifier(entityIdentifier);
        result.setVersionId(cdsVersionDTO.getVersionId());
        result.setVersionName(cdsVersionDTO.getName());
        result.setBusinessIdDescription(cdsBusinessScopeDTO.getDescription());
        result.setVersionDescription(cdsVersionDTO.getDescription());

        return result;
    }

    /**
     * Return a valid instance of CdsVersionDTO for either an existing or new
     * version.
     *
     * @param openCdsBusinessVersionIdentifier
     * @param createIfNotFound
     * @return
     * @throws ValidationException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     * @throws NotFoundException
     */
    public CdsVersionDTO getCdsVersioDTOFromOpenCdsBusinessVersionIdentifier(
            String openCdsBusinessVersionIdentifier,
            boolean createIfNotFound)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException,
            NotFoundException {
        final String METHODNAME = "getCdsVersioDTOFromCdsVersion ";
        CdsVersionSpecificationFile cdsVersionSpecificationFile = new CdsVersionSpecificationFile();
        logger.info(METHODNAME, "attempting to find: ", openCdsBusinessVersionIdentifier);
        String scopingEntityId = null;
        String businessId = null;
        String version = null;
        if (openCdsBusinessVersionIdentifier != null) {
            String[] items = openCdsBusinessVersionIdentifier.split("\\^");
            if (items.length == 3) {
                scopingEntityId = items[0];
                businessId = items[1];
                version = items[2];
            }
        }
        EntityIdentifier entityIdentifier = new EntityIdentifier();
        entityIdentifier.setBusinessId(businessId);
        entityIdentifier.setScopingEntityId(scopingEntityId);
        entityIdentifier.setVersion(version);
        cdsVersionSpecificationFile.setEntityIdentifier(entityIdentifier);
        return getCdsVersioDTOFromCdsVersion(cdsVersionSpecificationFile, createIfNotFound);
    }

    /**
     * Return a valid instance of CdsVersionDTO for either an existing or new
     * version.
     *
     * @param cdsVersionSpecificationFile
     * @param createIfNotFound
     * @return
     * @throws ValidationException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     * @throws NotFoundException
     */
    private CdsVersionDTO getCdsVersioDTOFromCdsVersion(CdsVersionSpecificationFile cdsVersionSpecificationFile, boolean createIfNotFound)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException,
            NotFoundException {
        final String METHODNAME = "getCdsVersioDTOFromCdsVersion ";
        CdsVersionDTO result = new CdsVersionDTO();

        // construct a CdsBusinessScopeDTO for searching
        CdsBusinessScopeDTO cdsBusinessScopeDTO = new CdsBusinessScopeDTO();
        EntityIdentifier entityIdentifier = cdsVersionSpecificationFile.getEntityIdentifier();
        cdsBusinessScopeDTO.setBusinessId(entityIdentifier.getBusinessId());
        cdsBusinessScopeDTO.setScopingEntityId(entityIdentifier.getScopingEntityId());
        cdsBusinessScopeDTO.setDescription(cdsVersionSpecificationFile.getBusinessIdDescription());
        try {
            cdsBusinessScopeDTO = cdsBusinessScopeBO.findByQueryMain(
                    cdsBusinessScopeDTO,
                    CdsBusinessScopeDTO.ByScopeBusinessId.class,
                    new ArrayList<Class>(),
                    AuthenticationUtils.getInternalSessionDTO(),
                    new PropertyBagDTO());
        } catch (NotFoundException e) {
            logger.warn(METHODNAME, "version business scope not found!");
            if (createIfNotFound) {
                logger.info(METHODNAME,
                        "creating business scope: ",
                        cdsBusinessScopeDTO.getBusinessId(),
                        " - ",
                        cdsBusinessScopeDTO.getScopingEntityId());
                cdsBusinessScopeDTO = cdsBusinessScopeBO.addMain(
                        cdsBusinessScopeDTO,
                        Add.class,
                        AuthenticationUtils.getInternalSessionDTO(),
                        new PropertyBagDTO());
            }
        }
        result.setBusinessScopeId(cdsBusinessScopeDTO.getBusinessScopeId());
        result.setBusinessId(cdsBusinessScopeDTO.getBusinessId());
        result.setScopingEntityId(cdsBusinessScopeDTO.getScopingEntityId());

        // set the concept determination methods
        for (CD conceptDeterminationMethod : cdsVersionSpecificationFile.getConceptDeterminationMethods()) {
            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = new ConceptDeterminationMethodDTO();
            conceptDeterminationMethodDTO.setCode(conceptDeterminationMethod.getCode());
            conceptDeterminationMethodDTO.setDisplayName(conceptDeterminationMethod.getDisplayName());
            try {
                conceptDeterminationMethodDTO = conceptDeterminationMethodBO.findByQueryMain(
                        conceptDeterminationMethodDTO,
                        ConceptDeterminationMethodDTO.ByCode.class,
                        new ArrayList<Class>(),
                        AuthenticationUtils.getInternalSessionDTO(),
                        new PropertyBagDTO());
            } catch (NotFoundException e) {
                logger.warn(METHODNAME, "concept determination method not found!");
                if (createIfNotFound) {
                    logger.info(METHODNAME,
                            "creating concept determination method: ",
                            conceptDeterminationMethodDTO.getCode(),
                            " - ",
                            conceptDeterminationMethodDTO.getDisplayName());
                    conceptDeterminationMethodDTO
                            = conceptDeterminationMethodBO.addMain(
                                    conceptDeterminationMethodDTO,
                                    Add.class, AuthenticationUtils.getInternalSessionDTO(),
                                    new PropertyBagDTO());
                }
            }
            CdsVersionConceptDeterminationMethodRelDTO cdsVersionConceptDeterminationMethodRelDTO
                    = new CdsVersionConceptDeterminationMethodRelDTO();
            cdsVersionConceptDeterminationMethodRelDTO.setConceptDeterminationMethodDTO(conceptDeterminationMethodDTO);
            result.addOrUpdateChildDTO(cdsVersionConceptDeterminationMethodRelDTO);
        }

        // set the rest of the properties on the result and see if it exists
        result.setVersion(entityIdentifier.getVersion());
        result.setDescription(cdsVersionSpecificationFile.getVersionDescription());
        result.setVersionId(cdsVersionSpecificationFile.getVersionId());
        result.setName(cdsVersionSpecificationFile.getVersionName());
        try {
            result = findByQueryMain(
                    result,
                    CdsVersionDTO.ByBusinessScopeIdVersion.class,
                    new ArrayList<Class>(),
                    AuthenticationUtils.getInternalSessionDTO(),
                    new PropertyBagDTO());
        } catch (NotFoundException e) {
            logger.warn(METHODNAME, "version not found!");
            if (createIfNotFound) {
                logger.info(METHODNAME, "creating CDS Version: ", result.getName(), " - ", result.getVersion());
                result = addMain(result, Add.class, AuthenticationUtils.getInternalSessionDTO(), new PropertyBagDTO());
            }
        }
        return result;
    }

    /**
     * Validate the CdsVersionDTO.
     *
     * @param cdsVersionDTO the object to validate.
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws ValidationException
     * @throws org.cdsframework.exceptions.MtsException
     * @throws org.cdsframework.exceptions.NotFoundException
     * @throws org.cdsframework.exceptions.AuthenticationException
     * @throws org.cdsframework.exceptions.AuthorizationException
     */
    private void validateCdsVersionDTO(CdsVersionDTO cdsVersionDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, MtsException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "validateCdsVersionDTO ";
        if (cdsVersionDTO == null) {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "cdsVersionDTO is null!"));
        }

        CdsBusinessScopeDTO cdsBusinessScopeDTO = new CdsBusinessScopeDTO();
        cdsBusinessScopeDTO.setBusinessScopeId(cdsVersionDTO.getBusinessScopeId());
        cdsBusinessScopeDTO = cdsBusinessScopeBO.findByPrimaryKeyMain(cdsBusinessScopeDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);

        if (cdsBusinessScopeDTO == null) {
            throw new ValidationException(CoreErrorCode.FieldCanNotBeEmpty,
                    logger.error(METHODNAME, "cdsBusinessScopeDTO is null!"));
        }

        String scopingEntityId = cdsBusinessScopeDTO.getScopingEntityId();
        if (scopingEntityId == null) {
            throw new ValidationException(CoreErrorCode.FieldCanNotBeEmpty,
                    logger.error(METHODNAME, "scopingEntityId is null!"));
        }

        String businessId = cdsBusinessScopeDTO.getBusinessId();
        if (businessId == null) {
            throw new ValidationException(CoreErrorCode.FieldCanNotBeEmpty,
                    logger.error(METHODNAME, "businessId is null!"));
        }

        String version = cdsVersionDTO.getVersion();
        if (version == null) {
            throw new ValidationException(CoreErrorCode.FieldCanNotBeEmpty,
                    logger.error(METHODNAME, "version is null!"));
        }
        List<ConceptDeterminationMethodDTO> conceptDeterminationMethodDTOs = cdsVersionDTO.getConceptDeterminationMethodDTOs();

        if (conceptDeterminationMethodDTOs.isEmpty()) {
            throw new ValidationException(CoreErrorCode.FieldCanNotBeEmpty,
                    logger.error(METHODNAME, "conceptDeterminationMethodDTOs is empty!"));
        }
    }

//
//    private void importFileDTO(FileDTO fileDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws ValidationException, NotFoundException, ConstraintViolationException, MtsException, AuthenticationException,
//            AuthorizationException,
//            IOException {
//        final String METHODNAME = "importFileDTO ";
//        logger.logBegin(METHODNAME);
//
//        fileDTO.setProcessingStatus(ProcessingStatus.PROCESSING);
//        fileDTO = fileBO.updateMainNew(fileDTO, Update.class, sessionDTO, propertyBagDTO);
//
//        try {
//            List<BrokenRule> brokenRules = new ArrayList<BrokenRule>();
//            boolean createNewVersion = (Boolean) propertyBagDTO.getPropertyMap().get("createNewVersion");
//
//            // To Do, extract version from XML versionFileDTO.getFileData()
//            String version = fileDTO.getFileName().substring(versionPattern.length());
//            int extPos = version.lastIndexOf(".");
//            if (extPos >= 0) {
//                version = version.substring(0, extPos).trim();
//            }
//            logger.info(METHODNAME + "the version=" + version);
//            CdsVersionDTO cdsVersionDTO = null;
//            if (!StringUtils.isEmpty(version)) {
//                CdsVersionDTO queryCdsVersionDTO = new CdsVersionDTO();
//                queryCdsVersionDTO.setVersionId(version);
//                try {
//                    cdsVersionDTO = findByPrimaryKeyMain(queryCdsVersionDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);
//                } catch (NotFoundException e) {
//                    // Create the version
//                }
//                if (cdsVersionDTO == null) {
//                    // Create the version
//                } else {
//                    if (!createNewVersion) {
//                        brokenRules.add(
//                                new BrokenRule(CoreErrorCode.AlreadyExists.toString(),
//                                        "CdsVersion: " + cdsVersionDTO.getVersion() + " already exists")
//                        );
//                    } else {
//                        // Create new Version using existing version
//                    }
//                }
//            } else {
//                brokenRules.add(
//                        new BrokenRule(CoreErrorCode.ValueNotFound.toString(),
//                                "A CdsVersion was not provided in the file " + fileDTO.getFileName())
//                );
//            }
//
//            if (cdsVersionDTO == null) {
//                // Temporary, locate DEFAULT
//                CdsVersionDTO queryCdsVersionDTO = new CdsVersionDTO();
//                queryCdsVersionDTO.setVersionId("DEFAULT");
//                cdsVersionDTO = findByPrimaryKeyMain(queryCdsVersionDTO, new ArrayList<Class>(), sessionDTO, propertyBagDTO);
//                //brokenRules.add(new BrokenRule(CdsErrorCode.CdsVersionNotFound.toString(), "Version did not get created"));
//            }
//
//            if (!brokenRules.isEmpty()) {
//                throw new ValidationException(brokenRules);
//            }
//
//            // Store the version in the propertyBagDTO
//            propertyBagDTO.getPropertyMap().put("cdsVersionDTO", cdsVersionDTO);
//
//            // Update the processing status
//            fileDTO.setProcessingStatus(ProcessingStatus.SUCCESS);
//            fileBO.updateMainNew(fileDTO, Update.class, sessionDTO, propertyBagDTO);
//        } finally {
//            logger.logEnd(METHODNAME);
//        }
//
//    }
//
//    @Override
//    public void importData(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException,
//            ConstraintViolationException {
//        final String METHODNAME = "importData ";
//        if (queryClass == DataExchange.class) {
//            dataExchangeImport(queryClass, sessionDTO, propertyBagDTO);
//        } else {
//            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
//                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
//        }
//    }
//
//    /**
//     * Import operations for data exchange.
//     *
//     * @param queryClass
//     * @param sessionDTO
//     * @param propertyBagDTO
//     * @throws ValidationException
//     * @throws NotFoundException
//     * @throws MtsException
//     * @throws AuthenticationException
//     * @throws AuthorizationException
//     * @throws ConstraintViolationException
//     */
//    private void dataExchangeImport(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
//            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException,
//            ConstraintViolationException {
//        final String METHODNAME = "dataExchangeImport ";
//        if (queryClass == DataExchange.class) {
//            if (propertyBagDTO != null) {
//
//                FileDTO parentFileDTO = (FileDTO) propertyBagDTO.getPropertyMap().get("fileDTO");
//                if (parentFileDTO == null) {
//                    throw new IllegalArgumentException("The fileDTO property was not included in the propertyBagDTO");
//                }
//                List<FileDTO> fileDTOs = new ArrayList<FileDTO>();
//
//                try {
//                    // Search for the version
//                    FileDTO searchCriteriaFileDTO = new FileDTO();
//                    searchCriteriaFileDTO.getQueryMap().put("jobId", parentFileDTO.getFileId());
//                    searchCriteriaFileDTO.getQueryMap().put("fileName", versionPattern + "*");
//                    fileDTOs = fileBO.findByQueryListMain(
//                            searchCriteriaFileDTO,
//                            FileDTO.ByGeneralPropertiesWithFileData.class,
//                            new ArrayList<Class>(),
//                            sessionDTO,
//                            new PropertyBagDTO());
//                    if (fileDTOs.isEmpty()) {
//                        throw new ValidationException(
//                                new BrokenRule(CoreErrorCode.ValueNotFound.toString(),
//                                        "A CdsVersion was not provided in the file " + parentFileDTO.getFileName())
//                        );
//                    } else {
//                        // More then 1 ?
//                        if (fileDTOs.size() > 1) {
//                            throw new ValidationException(
//                                    new BrokenRule(CoreErrorCode.TooMany.toString(),
//                                            fileDTOs.size() + " CdsVersions were found in the file " + parentFileDTO.getFileName())
//                            );
//                        }
//                    }
//                } catch (Exception e) {
//                    ImportUtils.saveAndThrowException(fileBO, parentFileDTO, sessionDTO, propertyBagDTO, e);
//                }
//
//                // Process the fileDTO
//                FileDTO fileDTO = fileDTOs.get(0);
//                try {
//                    logger.info(METHODNAME + "about to import " + fileDTO.getFileName());
//                    importFileDTO(fileDTO, sessionDTO, propertyBagDTO);
//                } catch (Exception e) {
//                    ImportUtils.saveAndThrowException(fileBO, fileDTO, sessionDTO, propertyBagDTO, e);
//                }
//                logger.info(METHODNAME + "finished successfully ");
//
//            } else {
//                throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
//                        logger.error(METHODNAME, "PropertyBagDTO instance was null!"));
//            }
//        } else {
//            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
//                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
//        }
//    }
}
