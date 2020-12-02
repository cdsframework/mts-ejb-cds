/**
 * The MTS cds EJB project is the base framework for the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for more details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the
 * New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * to have (without restriction, limitation, and warranty) complete irrevocable
 * access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING, BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR
 * OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see
 * https://www.hln.com/services/open-source/ or send correspondence to
 * ice@hln.com.
 */
package org.cdsframework.ejb.bo;

import ihe.iti.svs._2008.RetrieveMultipleValueSetsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseBO;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.ejb.local.PropertyMGRLocal;
import org.cdsframework.enumeration.CdsListType;
import org.cdsframework.enumeration.CoreErrorCode;
import org.cdsframework.enumeration.DTOState;
import org.cdsframework.enumeration.ValueSetImportType;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Add;
import org.cdsframework.group.Delete;
import org.cdsframework.group.SimpleExchange;
import org.cdsframework.group.Update;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.DTOUtils;
import org.cdsframework.util.FileUtils;
import org.cdsframework.util.PhinVadsUtils;
import org.cdsframework.util.StringUtils;
import org.cdsframework.util.VsacUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class ValueSetBO extends BaseBO<ValueSetDTO> {

    @EJB
    private CdsListBO cdsListBO;
    @EJB
    private CdsCodeSystemBO cdsCodeSystemBO;
    @EJB
    private CdsCodeBO cdsCodeBO;
    @EJB
    private PropertyMGRLocal propertyMGRLocal;

    @Override
    protected void initialize() throws MtsException {
        setSelfReferencing(true);
    }

    /**
     * Delete any linputStreamt items related to thinputStream instance before
     * deleting thinputStream instance.
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
    protected void preDelete(ValueSetDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        CdsListDTO cdsListDTO = new CdsListDTO();
        cdsListDTO.setListType(CdsListType.VALUE_SET);
        cdsListDTO.setValueSetDTO(baseDTO);
        logger.info("Attempting to delete lists with the value set: ", baseDTO != null ? baseDTO.getName() : null);
        List<CdsListDTO> cdsLists = cdsListBO.findByQueryListMain(
                cdsListDTO,
                CdsListDTO.ByValueSet.class,
                new ArrayList<>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (CdsListDTO item : cdsLists) {
            logger.info("Deleting list: ", item.getCode());
            item.delete(true);
            cdsListBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }
    }

    @Override
    public void importData(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException,
            ConstraintViolationException {
        final String METHODNAME = "importData ";
        if (queryClass == SimpleExchange.class) {
            simpleExchangeImport(queryClass, sessionDTO, propertyBagDTO);
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
        }
    }

    /**
     * Import a Value Set - create any code systems or codes.
     *
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     */
    private void simpleExchangeImport(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ValidationException,
            NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        final String METHODNAME = "simpleExchangeImport ";

        // Get the supplied import type.
        ValueSetImportType importType = null;
        Object importTypeObject = propertyBagDTO.get("importType");
        if (importTypeObject != null) {
            if (importTypeObject instanceof ValueSetImportType) {
                importType = (ValueSetImportType) importTypeObject;
            } else if (importTypeObject instanceof String) {
                importType = ValueSetImportType.valueOf((String) importTypeObject);
            }
        }

        // Perform the correct data import based on the supplied import type...
        if (importType != null) {
            if (importType == ValueSetImportType.PHINVADS) {
                simpleExchangeImportPhinVads(queryClass, sessionDTO, propertyBagDTO);
            } else if (importType == ValueSetImportType.VSAC) {
                simpleExchangeImportVsac(queryClass, sessionDTO, propertyBagDTO);
            }
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "import type was null."));
        }
    }

    /**
     * Import a Value Set - create any code systems or codes.
     *
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     */
    private void simpleExchangeImportPhinVads(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ValidationException,
            NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        final String METHODNAME = "simpleExchangeImportPhinVads ";
        ValueSetDTO valueSetDTO;

        byte[] payload = propertyBagDTO.get("payload", byte[].class);
        if (payload == null) {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "payload was null!"));
        }

        if (logger.isDebugEnabled()) {
            logger.info(METHODNAME, "got payload: ", new String(payload));
        }

        // payload inputStream base64 byte array of zip file - decode and unzip it and get the files out of it.
        Map<String, String> valueSetData = FileUtils.getDataMapFromBase64ZipByteArray(payload, null);

        try {

            logger.info(METHODNAME, "about to process: ", valueSetData.size());

            boolean isPhinVads = PhinVadsUtils.isFileMapPhinVads(valueSetData);

            for (Entry<String, String> fileEntry : valueSetData.entrySet()) {
                String fileName = fileEntry.getKey();
                String fileData = fileEntry.getValue();
                if (fileName != null) {
                    if (fileData != null) {
                        if (!isPhinVads) {
                            isPhinVads = PhinVadsUtils.isFilePhinVads(fileName, fileData);
                        }
                        // if this is a PHIN VADS source file
                        if (isPhinVads) {
                            valueSetDTO = PhinVadsUtils.getValueSetDTOFromPhinVadsExport(fileData);
                            if (valueSetDTO != null) {

                                // determine if the value set already exists
                                ValueSetDTO liveValueSetDTO = new ValueSetDTO();
                                liveValueSetDTO.setOid(valueSetDTO.getOid());
                                try {
                                    liveValueSetDTO = findByQueryMain(
                                            liveValueSetDTO,
                                            ValueSetDTO.ByOid.class,
                                            getDtoChildClasses(),
                                            sessionDTO,
                                            propertyBagDTO);
                                } catch (NotFoundException e) {
                                    liveValueSetDTO = null;
                                }
                                // if it doesn't exist - create it
                                if (liveValueSetDTO == null) {
                                    liveValueSetDTO = new ValueSetDTO();
                                    liveValueSetDTO.setName(valueSetDTO.getName());
                                    liveValueSetDTO.setOid(valueSetDTO.getOid());
                                    liveValueSetDTO.setCode(valueSetDTO.getCode());
                                    liveValueSetDTO.setDescription(valueSetDTO.getDescription());
                                    liveValueSetDTO.setVersion(valueSetDTO.getVersion());
                                    liveValueSetDTO.setVersionDescription(valueSetDTO.getVersionDescription());
                                    liveValueSetDTO.setVersionStatus(valueSetDTO.getVersionStatus());
                                    liveValueSetDTO = addMain(liveValueSetDTO, Add.class, sessionDTO, propertyBagDTO);
                                }

                                // iterate over the codes and code systems to determine if they exist - if not - create them
                                for (CdsCodeDTO item : valueSetDTO.getCdsCodeDTOs()) {

                                    String originalCode = item.getCode();

                                    // first check the code system
                                    CdsCodeSystemDTO codeSystemDTO = new CdsCodeSystemDTO();
                                    codeSystemDTO.setOid(item.getCodeSystem());
                                    try {
                                        codeSystemDTO = cdsCodeSystemBO.findByQueryMain(
                                                codeSystemDTO,
                                                CdsCodeSystemDTO.ByOid.class,
                                                cdsCodeSystemBO.getDtoChildClasses(),
                                                sessionDTO,
                                                propertyBagDTO);
                                    } catch (NotFoundException e) {
                                        codeSystemDTO = null;
                                    }
                                    if (codeSystemDTO == null) {
                                        codeSystemDTO = new CdsCodeSystemDTO();
                                        codeSystemDTO.setName(item.getCodeSystemName());
                                        codeSystemDTO.setOid(item.getCodeSystem());
                                        codeSystemDTO = cdsCodeSystemBO.addMain(codeSystemDTO, Add.class, sessionDTO, propertyBagDTO);
                                    }

                                    // next, check the code
                                    CdsCodeDTO foundCdsCodeDTO = null;
                                    for (CdsCodeDTO cdsCodeDTO : codeSystemDTO.getCdsCodeDTOs()) {
                                        if (cdsCodeDTO.getCode().equals(item.getCode())) {
                                            foundCdsCodeDTO = cdsCodeDTO;
                                            // update the display name
                                            if (!StringUtils.isEmpty(item.getDisplayName())) {
                                                cdsCodeDTO.setDisplayName(item.getDisplayName().trim());
                                            }
                                            break;
                                        }
                                    }

                                    // add this code if it doesn't already exist
                                    if (foundCdsCodeDTO == null) {
                                        codeSystemDTO.addOrUpdateChildDTO(item);
                                    }
                                    if (codeSystemDTO.getOperationDTOState() == DTOState.UPDATED) {
                                        codeSystemDTO = cdsCodeSystemBO.updateMain(codeSystemDTO, Update.class, sessionDTO, propertyBagDTO);
                                    }

                                    // grab the updated CdsCodeDTO and add it to the liveValueSetDTO instance
                                    for (CdsCodeDTO cdsCodeDTO : codeSystemDTO.getCdsCodeDTOs()) {
                                        if (cdsCodeDTO.getCode().equals(originalCode)) {
                                            // check to see if the relationship already exists
                                            boolean relExists = false;
                                            for (CdsCodeDTO liveCdsCodeDTO : liveValueSetDTO.getCdsCodeDTOs()) {
                                                if (liveCdsCodeDTO.equals(cdsCodeDTO)) {
                                                    relExists = true;
                                                    break;
                                                }
                                            }

                                            // only add the relationship if it doesn't exist yet
                                            if (!relExists) {
                                                ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO = new ValueSetCdsCodeRelDTO();
                                                valueSetCdsCodeRelDTO.setCdsCodeDTO(cdsCodeDTO);
                                                liveValueSetDTO.addOrUpdateChildDTO(valueSetCdsCodeRelDTO);
                                            }
                                            break;
                                        }
                                    }
                                }

                                // finally update the value set
                                liveValueSetDTO = updateMain(liveValueSetDTO, Update.class, sessionDTO, propertyBagDTO);
                                logger.info(METHODNAME, "finished processing: ", liveValueSetDTO.getName(), " - ", liveValueSetDTO.getOid());

                            } else {
                                logger.info(METHODNAME, "valueSetDTO is null!");
                            }
                        } else {
                            throw new UnsupportedOperationException("File format not supported: " + fileName);
                        }
                    } else {
                        logger.info(METHODNAME, "fileData is null!");
                    }
                } else {
                    logger.info(METHODNAME, "fileName is null!");
                }
            }
        } catch (UnsupportedOperationException | AuthenticationException | AuthorizationException | ConstraintViolationException | MtsException | NotFoundException | ValidationException e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        }
    }

    /**
     * Import a Value Set - create any code systems or codes.
     *
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     * @throws ConstraintViolationException
     */
    private void simpleExchangeImportVsac(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        final String METHODNAME = "simpleExchangeImportVsac ";

        // Extract the parameters from the property bag...
        String baseUri = propertyMGRLocal.get("VSAC_BASE_URI", String.class);
        String apiKey = propertyMGRLocal.get("VSAC_APIKEY", String.class);

        String oid = propertyBagDTO.get("oid", String.class);
        String profile = propertyBagDTO.get("profile", String.class);
        String version = propertyBagDTO.get("version", String.class);
        boolean includeDraft = (propertyBagDTO.get("includeDraft") != null);
        logger.info(METHODNAME, "oid=", oid);
        logger.info(METHODNAME, "profile=", profile);
        logger.info(METHODNAME, "version=", version);
        logger.info(METHODNAME, "includeDraft=", includeDraft);

        // Check that the required parameters are all available...
        if (oid == null) {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "oid was null."));
        }

        try {
            logger.info(METHODNAME, "about to process: ");

            // Get value set data from the vsac service.
            RetrieveMultipleValueSetsResponse vsacData = null;
            if (profile != null) {
                vsacData = VsacUtils.getValueSetByOidAndProfile(baseUri, apiKey, oid, profile, includeDraft);
            } else if (version != null) {
                vsacData = VsacUtils.getValueSetByOidAndVersion(baseUri, apiKey, oid, version);
            } else {
                throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                        logger.error(METHODNAME, "no profile or version was was supplied."));
            }

            if (vsacData != null) {
                logger.info(METHODNAME, "successfully retrieved data from the service");
                logger.info(METHODNAME, "vsacData=", vsacData);
                if (logger.isDebugEnabled()) {
                }
            } else {
                logger.info(METHODNAME, "vsac service returned no result set");
            }

            // Translate the vsac data to a new value set object...
            ValueSetDTO newValueSet = null;
            if (vsacData != null) {
                logger.info(METHODNAME, "attempting to translate vsac data to new value set dto");
                newValueSet = VsacUtils.getValueSetFromVsacData(vsacData, profile, version);
                if (newValueSet != null) {
                    logger.info(METHODNAME, "successfully translated vsac data to new value set dto");
                    logger.info(METHODNAME, "newValueSet=", newValueSet);
                } else {
                    logger.info(METHODNAME, "vsac data was NOT successfully translated to a value set dto");
                }
            } else {
                throw new ValidationException(CoreErrorCode.ValueNotFound,
                        logger.error(METHODNAME, "No value sets were found for the supplied criteria."));
            }

            // Save the new value set...
            if (newValueSet != null) {
                logger.info(METHODNAME, "profile=", profile);
                logger.info(METHODNAME, "version=", version);
                logger.info(METHODNAME, "newValueSet.getName()=", newValueSet.getName());
                logger.info(METHODNAME, "newValueSet.getOid()=", newValueSet.getOid());
                logger.info(METHODNAME, "newValueSet.getCode()=", newValueSet.getCode());
                logger.info(METHODNAME, "newValueSet.getDescription()=", newValueSet.getDescription());
                logger.info(METHODNAME, "newValueSet.getVersion()=", newValueSet.getVersion());
                logger.info(METHODNAME, "newValueSet.getVersionStatus()=", newValueSet.getVersionStatus());
                logger.info(METHODNAME, "newValueSet.getVersionDescription()=", newValueSet.getVersionDescription());


//                //Determine if the value set already exists (by oid and version)...
//                existingValueSet = new ValueSetDTO();
//                existingValueSet.setOid(newValueSet.getOid());
//                existingValueSet.setVersion(newValueSet.getVersion());
//                existingValueSet.setCode(newValueSet.getCode());
//                existingValueSet.setVersionStatus(newValueSet.getVersionStatus());
//                try {
//                    existingValueSet = findByQueryMain(
//                            existingValueSet,
//                            ValueSetDTO.ByCodeOidVersionVersionStatus.class,
//                            getDtoChildClasses(),
//                            sessionDTO,
//                            propertyBagDTO);
//                } catch (NotFoundException e) {
//                    existingValueSet = null;
//                }
                ValueSetDTO queryDTO = new ValueSetDTO();
                queryDTO.setOid(newValueSet.getOid());
                List<ValueSetDTO> results = findByQueryListMain(
                        queryDTO,
                        ValueSetDTO.ByOid.class,
                        new ArrayList<>(),
                        sessionDTO,
                        new PropertyBagDTO());

                ValueSetDTO existingValueSet = VsacUtils.getExistingValueSetFromList(results, newValueSet.getVersion(), newValueSet.getVersionStatus());

                // scenario #4
                // If it does not exist, create it...
                if (existingValueSet == null) {
                    logger.info(METHODNAME, "no matching dto found - creating new one. scenario #4");
                    existingValueSet = new ValueSetDTO();
                    existingValueSet.setName(newValueSet.getName());
                    existingValueSet.setOid(newValueSet.getOid());
                    existingValueSet.setCode(newValueSet.getCode());
                    existingValueSet.setDescription(newValueSet.getDescription());
                    existingValueSet.setVersion(newValueSet.getVersion());
                    existingValueSet.setVersionDescription(newValueSet.getVersionDescription());
                    existingValueSet.setVersionStatus(newValueSet.getVersionStatus());
                    logger.info(METHODNAME, "createing new value set ", existingValueSet.getName(), " - ", existingValueSet.getCode(), " - ", existingValueSet.getOid());
                    existingValueSet = addMain(existingValueSet, Add.class, sessionDTO, new PropertyBagDTO());
                } else {
                    existingValueSet = findByPrimaryKeyMain(existingValueSet, getDtoChildClasses(), sessionDTO, propertyBagDTO);
                    logger.info(METHODNAME, "matching dto found - updating...");
                    existingValueSet.setName(newValueSet.getName());
                    existingValueSet.setCode(newValueSet.getCode());
                    existingValueSet.setVersion(newValueSet.getVersion());
                    existingValueSet.setDescription(newValueSet.getDescription());
                    existingValueSet.setVersionStatus(newValueSet.getVersionStatus());
                    existingValueSet.setVersionDescription(newValueSet.getVersionDescription());
                    DTOUtils.setDTOState(existingValueSet, DTOState.UPDATED);
                    existingValueSet = updateMain(existingValueSet, Update.class, sessionDTO, new PropertyBagDTO());
                }

                // iterate over the codes and code systems to determine if they exist - if not - create them
                for (CdsCodeDTO item : newValueSet.getCdsCodeDTOs()) {

                    String originalCode = item.getCode();

                    // first check the code system
                    CdsCodeSystemDTO codeSystemDTO = new CdsCodeSystemDTO();
                    codeSystemDTO.setOid(item.getCodeSystem());
                    try {
                        codeSystemDTO = cdsCodeSystemBO.findByQueryMain(
                                codeSystemDTO,
                                CdsCodeSystemDTO.ByOid.class,
                                new ArrayList<>(),
                                sessionDTO,
                                propertyBagDTO);
                    } catch (NotFoundException e) {
                        codeSystemDTO = null;
                    }
                    if (codeSystemDTO == null) {
                        codeSystemDTO = new CdsCodeSystemDTO();
                        codeSystemDTO.setName(item.getCodeSystemName());
                        codeSystemDTO.setOid(item.getCodeSystem());
                        codeSystemDTO = cdsCodeSystemBO.addMain(codeSystemDTO, Add.class, sessionDTO, propertyBagDTO);
                    }

                    // next, check the code
                    CdsCodeDTO foundCdsCodeDTO = null;
                    try {
                        foundCdsCodeDTO = cdsCodeBO.findByQueryMain(item, CdsCodeDTO.ByCodeSystemCode.class, new ArrayList<>(), sessionDTO, propertyBagDTO);
                    } catch (NotFoundException e) {
                        // no hacer nada
                    }

                    if (foundCdsCodeDTO == null) {
                        // create the code
                        CdsCodeDTO newCodeDTO = new CdsCodeDTO();
                        newCodeDTO.setCodeSystemId(!StringUtils.isEmpty(codeSystemDTO.getCodeSystemId()) ? codeSystemDTO.getCodeSystemId().trim() : null);
                        newCodeDTO.setCode(!StringUtils.isEmpty(item.getCode()) ? item.getCode().trim() : null);
                        newCodeDTO.setDisplayName(!StringUtils.isEmpty(item.getDisplayName()) ? item.getDisplayName().trim() : null);
                        foundCdsCodeDTO = cdsCodeBO.addMain(newCodeDTO, Add.class, sessionDTO, propertyBagDTO);
                        ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO = new ValueSetCdsCodeRelDTO();
                        valueSetCdsCodeRelDTO.setCdsCodeDTO(foundCdsCodeDTO);
                        existingValueSet.addOrUpdateChildDTO(valueSetCdsCodeRelDTO);
                    } else {

                        // if found - see if the display name needs updating
                        if (!StringUtils.isEmpty(item.getDisplayName())) {
                            foundCdsCodeDTO.setDisplayName(item.getDisplayName().trim());
                        }
                        if (foundCdsCodeDTO.getDTOState() == DTOState.UPDATED) {
                            foundCdsCodeDTO = cdsCodeBO.updateMain(foundCdsCodeDTO, Update.class, sessionDTO, propertyBagDTO);
                        }

                        // see if there is already a mapping to the code in the existing value set
                        ValueSetCdsCodeRelDTO foundValueSetCdsCodeRelDTO = null;
                        for (ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO : existingValueSet.getValueSetCdsCodeRelDTOs()) {
                            if (valueSetCdsCodeRelDTO.getCdsCodeDTO().getCodeId().equals(foundCdsCodeDTO.getCodeId())) {
                                foundValueSetCdsCodeRelDTO = valueSetCdsCodeRelDTO;
                                break;
                            }
                        }
                        // if not then all it
                        if (foundValueSetCdsCodeRelDTO == null) {
                            ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO = new ValueSetCdsCodeRelDTO();
                            valueSetCdsCodeRelDTO.setCdsCodeDTO(foundCdsCodeDTO);
                            existingValueSet.addOrUpdateChildDTO(valueSetCdsCodeRelDTO);
                        }
                    }

                }

                // figure out if there is anything to delete
                for (ValueSetCdsCodeRelDTO existingValueSetCdsCodeRelDTO : existingValueSet.getValueSetCdsCodeRelDTOs()) {
                    if (!existingValueSetCdsCodeRelDTO.isNew()) {
                        CdsCodeDTO existingCdsCodeDTO = existingValueSetCdsCodeRelDTO.getCdsCodeDTO();
                        String existingCode = null;
                        String existingOid = null;
                        if (existingCdsCodeDTO != null && existingCdsCodeDTO.getCode() != null) {
                            if (!StringUtils.isEmpty(existingCdsCodeDTO.getCode())) {
                                existingCode = existingCdsCodeDTO.getCode().trim();
                            }
                            if (!StringUtils.isEmpty(existingCdsCodeDTO.getCodeSystem())) {
                                existingOid = existingCdsCodeDTO.getCodeSystem().trim();
                            }
                        }
                        if (existingCode != null && existingOid != null) {
                            // iterate over the new value set to see if the existing member is still there - if not delete it
                            ValueSetCdsCodeRelDTO foundValueSetCdsCodeRelDTO = null;
                            for (ValueSetCdsCodeRelDTO newValueSetCdsCodeRelDTO : newValueSet.getValueSetCdsCodeRelDTOs()) {
                                CdsCodeDTO newCdsCodeDTO = newValueSetCdsCodeRelDTO.getCdsCodeDTO();
                                String newCode = null;
                                String newOid = null;
                                if (newCdsCodeDTO != null && newCdsCodeDTO.getCode() != null) {
                                    if (!StringUtils.isEmpty(newCdsCodeDTO.getCode())) {
                                        newCode = newCdsCodeDTO.getCode().trim();
                                    }
                                    if (!StringUtils.isEmpty(newCdsCodeDTO.getCodeSystem())) {
                                        newOid = newCdsCodeDTO.getCodeSystem().trim();
                                    }
                                }
                                logger.debug(METHODNAME, "evaluating new value set: ", newOid, " - ", newCode);
                                if (newCode != null && newOid != null) {
                                    if (newCode.equals(existingCode) && newOid.equals(existingOid)) {
                                        foundValueSetCdsCodeRelDTO = newValueSetCdsCodeRelDTO;
                                        logger.debug(METHODNAME, "found existing code relationship in new valueset: ", newOid, " - ", newCode);
                                        break;
                                    }
                                } else {
                                    logger.error(METHODNAME, "new oid or code was null: ", newOid, " - ", newCode);
                                }
                            }
                            if (foundValueSetCdsCodeRelDTO == null) {
                                logger.warn(METHODNAME, "deleting existing code relationship: ", existingOid, " - ", existingCode);
                                existingValueSetCdsCodeRelDTO.delete();
                            }
                        } else {
                            logger.error(METHODNAME, "existing oid or code was null: ", existingOid, " - ", existingCode);
                        }
                    }
                }

                logger.info(METHODNAME, "existingValueSet.getName()=", existingValueSet.getName());
                logger.info(METHODNAME, "existingValueSet.getOid()=", existingValueSet.getOid());
                logger.info(METHODNAME, "existingValueSet.getCode()=", existingValueSet.getCode());
                logger.info(METHODNAME, "existingValueSet.getDescription()=", existingValueSet.getDescription());
                logger.info(METHODNAME, "existingValueSet.getVersion()=", existingValueSet.getVersion());
                logger.info(METHODNAME, "existingValueSet.getVersionStatus()=", existingValueSet.getVersionStatus());
                logger.info(METHODNAME, "existingValueSet.getVersionDescription()=", existingValueSet.getVersionDescription());

                // Save the new value set...
                existingValueSet = updateMain(existingValueSet, Update.class, sessionDTO, propertyBagDTO);
                logger.info(METHODNAME, "finished processing existing: ", existingValueSet.getName(), " - ", existingValueSet.getCode(), " - ", existingValueSet.getOid());
            } else {
                throw new MtsException("No value set data was received or could be parsed from VSAC service response.");
            }

        } catch (UnsupportedOperationException | AuthenticationException | AuthorizationException | ConstraintViolationException | MtsException | NotFoundException | ValidationException e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        }
    }

}
