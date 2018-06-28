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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseBO;
import org.cdsframework.cds.util.MarshalUtils;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.CdsListType;
import org.cdsframework.enumeration.CoreErrorCode;
import org.cdsframework.enumeration.Operation;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.factory.CdsCodeDTOObjectFactory;
import org.cdsframework.group.Delete;
import org.cdsframework.group.FindAll;
import org.cdsframework.group.SimpleExchange;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.FileUtils;
import org.opencds.term.codeSystem.CodeSystem;
import org.opencds.term.codeSystem.CodeSystems;
import org.opencds.term.codes.Codes;
import org.opencds.vmr.v1_0.schema.CD;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class CdsCodeSystemBO extends BaseBO<CdsCodeSystemDTO> {

    @EJB
    private CdsListBO cdsListBO;
    private final List<Class> childClasses = Arrays.asList(new Class[]{CdsCodeDTO.class});

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
    protected void preDelete(CdsCodeSystemDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        CdsListDTO cdsListDTO = new CdsListDTO();
        cdsListDTO.setListType(CdsListType.CODE_SYSTEM);
        cdsListDTO.setCdsCodeSystemDTO(baseDTO);
        logger.info("Attempting to delete lists with the code system: ", baseDTO != null ? baseDTO.getOid() : null);
        List<CdsListDTO> cdsLists = cdsListBO.findByQueryListMain(
                cdsListDTO,
                CdsListDTO.ByCodeSystem.class,
                cdsListBO.getDtoChildClasses(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (CdsListDTO item : cdsLists) {
            logger.info("Deleting list: ", item.getCode());
            item.delete(true);
            cdsListBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }
    }

    /**
     * Export one or more code systems.
     *
     * @param baseDTO
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @return
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    @Override
    public Map<String, byte[]> exportData(CdsCodeSystemDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws
            ValidationException, NotFoundException,
            MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "exportData ";
        if (queryClass == SimpleExchange.class) {
            return simpleExchangeExport(baseDTO, queryClass, sessionDTO, propertyBagDTO);
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
        }
    }

    /**
     * Export one or more code systems based on a criteria.
     *
     * @param baseDTO
     * @param queryClass
     * @param sessionDTO
     * @param propertyBagDTO
     * @return
     * @throws ValidationException
     * @throws NotFoundException
     * @throws MtsException
     * @throws AuthenticationException
     * @throws AuthorizationException
     */
    private Map<String, byte[]> simpleExchangeExport(CdsCodeSystemDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "simpleExchangeExport ";
        Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
        if (baseDTO != null) {
            List<CdsCodeSystemDTO> cdsCodeSystemDTOs = new ArrayList<CdsCodeSystemDTO>();
            if ("ALL".equalsIgnoreCase(baseDTO.getCodeSystemId())) {
                List<CdsCodeSystemDTO> findByQueryList = findByQueryListMain(new CdsCodeSystemDTO(), FindAll.class, childClasses, sessionDTO, new PropertyBagDTO());
                if (findByQueryList != null) {
                    cdsCodeSystemDTOs.addAll(findByQueryList);
                } else {
                    logger.error(METHODNAME, "findByQueryList is null!");
                }
            } else {
                CdsCodeSystemDTO cdsCodeSystemDTO = findByPrimaryKeyMain(baseDTO, childClasses, sessionDTO, new PropertyBagDTO());
                if (cdsCodeSystemDTO != null) {
                    cdsCodeSystemDTOs.add(cdsCodeSystemDTO);
                } else {
                    logger.error(METHODNAME, "cdsCodeSystemDTO is null!");
                }
            }
            logger.info("Found cdsCodeSystemDTOs: ", cdsCodeSystemDTOs.size());
            // start a file for all code systems
            CodeSystems codeSystemsFile = new CodeSystems();
            List<CodeSystem> codeSystems = codeSystemsFile.getCodeSystems();
            try {
                for (CdsCodeSystemDTO cdsCodeSystemDTO : cdsCodeSystemDTOs) {
                    if (cdsCodeSystemDTO != null) {
                        // Add code system
                        CodeSystem codeSystem = new CodeSystem();
                        codeSystem.setCodeSystemOID(cdsCodeSystemDTO.getOid());
                        codeSystem.setCodeSystemDisplayName(cdsCodeSystemDTO.getName());
                        codeSystems.add(codeSystem);

                        // start a code file for all codes in this code system
                        Codes codeFile = new Codes();
                        List<CD> codes = codeFile.getCDS();
                        for (CdsCodeDTO cdsCodeDTO : cdsCodeSystemDTO.getCdsCodeDTOs()) {
                            if (cdsCodeDTO != null) {
                                CD code = CdsCodeDTOObjectFactory.getCDFromCdsCodeDTO(cdsCodeDTO);
                                codes.add(code);
                            } else {
                                logger.error(METHODNAME, "cdsCodeDTO is null!");
                            }
                        }

                        byte[] marshalledXml = MarshalUtils.marshalObject(codeFile);
                        fileMap.put(cdsCodeSystemDTO.getName() + "-codes.xml", marshalledXml);
                    } else {
                        logger.error(METHODNAME, "cdsCodeSystemDTO is null!");
                    }

                }
                byte[] marshalledXml = MarshalUtils.marshalObject(codeSystemsFile);
                fileMap.put("codeSystems.xml", marshalledXml);
                String xsd = FileUtils.getStringFromJarFile("codeSystem.xsd");
                fileMap.put("codeSystem.xsd", xsd.getBytes());
            } catch (CdsException e) {
                logger.error(e);
                throw new MtsException(e.getMessage());
            }
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "baseDTO was null! "));
        }
        return fileMap;
    }

    /**
     * Determines if an OID exists already.
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
    private boolean isCodeSystemOidExists(CdsCodeSystemDTO baseDTO, Operation operation)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "isCodeSystemOidExists ";
        boolean result = false;
        logger.info(METHODNAME, "operation=", operation);
        logger.info(METHODNAME, "is oid property changed: ", baseDTO.isPropertyChanged("oid"));
        if ((operation == Operation.UPDATE && baseDTO.isPropertyChanged("oid"))
                || (operation == Operation.ADD && baseDTO.isNew())) {
            CdsCodeSystemDTO query = new CdsCodeSystemDTO();
            query.setOid(baseDTO.getOid() != null ? baseDTO.getOid().toLowerCase() : null);

            try {
                List<CdsCodeSystemDTO> results = findByQueryListMain(
                        query,
                        CdsCodeSystemDTO.ByOid.class,
                        new ArrayList<Class>(),
                        AuthenticationUtils.getInternalSessionDTO(),
                        new PropertyBagDTO());
                if (results != null) {
                    if (!results.isEmpty()) {
                        if (results.size() > 1) {
                            result = true;
                        } else if (results.size() == 1 && !results.get(0).getCodeSystemId().equals(baseDTO.getCodeSystemId())) {
                            result = true;
                        }
                    }
                }
            } catch (NotFoundException e) {
                // nada
            }
        }
        return result;
    }

    /**
     * Determines if a code system name exists already.
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
    private boolean isCodeSystemNameExists(CdsCodeSystemDTO baseDTO, Operation operation)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "isCodeSystemNameExists ";
        boolean result = false;
        logger.info(METHODNAME, "operation=", operation);
        logger.info(METHODNAME, "is name property changed: ", baseDTO.isPropertyChanged("name"));
        if ((operation == Operation.UPDATE && baseDTO.isPropertyChanged("name"))
                || (operation == Operation.ADD && baseDTO.isNew())) {
            CdsCodeSystemDTO query = new CdsCodeSystemDTO();
            query.setName(baseDTO.getName() != null ? baseDTO.getName().toLowerCase() : null);

            try {
                List<CdsCodeSystemDTO> results = findByQueryListMain(
                        query,
                        CdsCodeSystemDTO.ByLowerName.class,
                        new ArrayList<Class>(),
                        AuthenticationUtils.getInternalSessionDTO(),
                        new PropertyBagDTO());
                if (results != null) {
                    if (!results.isEmpty()) {
                        if (results.size() > 1) {
                            result = true;
                        } else if (results.size() == 1 && !results.get(0).getCodeSystemId().equals(baseDTO.getCodeSystemId())) {
                            result = true;
                        }
                    }
                }
            } catch (NotFoundException e) {
                // nada
            }
        }
        return result;
    }

    @Override
    protected void processBegin(
            CdsCodeSystemDTO baseDTO,
            Operation operation,
            Class queryClass,
            List<Class> validationClasses,
            SessionDTO sessionDTO,
            PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        if (baseDTO != null) {
            if (operation == Operation.ADD || operation == Operation.UPDATE) {
                logger.debug("isCodeSystemNameExists: ", isCodeSystemNameExists(baseDTO, operation), " - ", baseDTO.getName());
                logger.debug("isCodeSystemOidExists: ", isCodeSystemOidExists(baseDTO, operation), " - ", baseDTO.getOid());
                if (isCodeSystemNameExists(baseDTO, operation)) {
                    throw new MtsException("A code system with that name already exists.");
                } else if (isCodeSystemOidExists(baseDTO, operation)) {
                    throw new MtsException("A code system with that OID already exists.");
                }
            }
        }
    }

}
