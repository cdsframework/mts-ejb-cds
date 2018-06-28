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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.io.IOUtils;
import org.cdsframework.base.BaseBO;
import org.cdsframework.cds.util.MarshalUtils;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.CdsListType;
import org.cdsframework.enumeration.CoreErrorCode;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Add;
import org.cdsframework.group.Delete;
import org.cdsframework.group.FindAll;
import org.cdsframework.group.SimpleExchange;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.FileUtils;
import org.cdsframework.util.OpenCdsUtils;
import org.cdsframework.util.support.DeepCopy;
import org.opencds.config.schema.Concept;
import org.opencds.config.schema.ConceptDeterminationMethod;
import org.opencds.config.schema.ConceptDeterminationMethods;
import org.opencds.config.schema.ConceptMapping;
import org.opencds.term.conceptMappings.MembersForCodeSystem;
import org.opencds.term.conceptMappings.OpenCdsConceptMappingSpecificationFile;
import org.opencds.term.supportedConcepts.OpenCdsConcept;
import org.opencds.vmr.v1_0.schema.CD;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class OpenCdsConceptBO extends BaseBO<OpenCdsConceptDTO> {
    //TODO: add in support to force the selection of one and only one reverse mapped mapping per version or concept determination method

    @EJB
    private OpenCdsConceptRelBO openCdsConceptRelBO;
    @EJB
    private CdsCodeBO cdsCodeBO;
    @EJB
    private CdsCodeSystemBO cdsCodeSystemBO;
    @EJB
    private ConceptDeterminationMethodBO conceptDeterminationMethodBO;
    @EJB
    private CdsListItemBO cdsListItemBO;

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
    protected void preDelete(OpenCdsConceptDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ConstraintViolationException, NotFoundException, MtsException, ValidationException, AuthenticationException,
            AuthorizationException {
        final String METHODNAME = "preDelete ";
        CdsListItemDTO cdsListItemDTO = new CdsListItemDTO();
        cdsListItemDTO.setItemType(CdsListType.CONCEPT);
        cdsListItemDTO.setOpenCdsConceptDTO(baseDTO);
        logger.info(
                METHODNAME,
                "Attempting to delete list items with the opencds concept: ",
                baseDTO != null ? baseDTO.getCode() : null,
                " (", baseDTO != null ? baseDTO.getCodeId() : null, ")");
        List<CdsListItemDTO> listItems = cdsListItemBO.findByQueryListMain(
                cdsListItemDTO,
                CdsListItemDTO.ByOpenCdsCodeId.class,
                new ArrayList<Class>(),
                AuthenticationUtils.getInternalSessionDTO(),
                propertyBagDTO);
        for (CdsListItemDTO item : listItems) {
            logger.info(METHODNAME, "Deleting list item: ", item.getDslrValue());
            item.delete(true);
            cdsListItemBO.deleteMain(item, Delete.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
        }
    }

    /**
     * Export OpenCDS Concept DTO(s).
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
    public Map<String, byte[]> exportData(OpenCdsConceptDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws
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
     * Export OpenCDS Concept DTO(s) given a supplied search criteria DTO
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
    private Map<String, byte[]> simpleExchangeExport(OpenCdsConceptDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws
            ValidationException, NotFoundException,
            MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "simpleExchangeExport ";
        Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
        if (baseDTO != null) {
            List<OpenCdsConceptDTO> openCdsConceptDTOs = new ArrayList<OpenCdsConceptDTO>();
            if ("ALL".equalsIgnoreCase(baseDTO.getCodeId())) {
                List<OpenCdsConceptDTO> findByQueryList = findByQueryListMain(baseDTO, FindAll.class, getDtoChildClasses(), sessionDTO, new PropertyBagDTO());
                if (findByQueryList != null) {
                    openCdsConceptDTOs.addAll(findByQueryList);
                } else {
                    logger.error(METHODNAME, "openCdsConceptDTOs is null!");
                }
            } else {
                OpenCdsConceptDTO openCdsConceptDTO = findByPrimaryKeyMain(baseDTO, getDtoChildClasses(), sessionDTO, new PropertyBagDTO());
                if (openCdsConceptDTO != null) {
                    openCdsConceptDTOs.add(openCdsConceptDTO);
                } else {
                    logger.error(METHODNAME, "openCdsConceptDTO is null!");
                }
            }
            logger.info("Found openCdsConceptDTOs: ", openCdsConceptDTOs.size());
            // TODO: add generation of Supported concepts for type
            // TODO: generate reverse map file
//            try {
                // Concept -> Code System -> Concept Determination Method -> list of CdsCodeDTO
                Map<OpenCdsConceptDTO, Map<String, Map<String, List<CdsCodeDTO>>>> conceptMappings = new HashMap<OpenCdsConceptDTO, Map<String, Map<String, List<CdsCodeDTO>>>>();

                for (OpenCdsConceptDTO openCdsConceptDTO : openCdsConceptDTOs) {
                    for (OpenCdsConceptRelDTO openCdsConceptRelDTO : openCdsConceptDTO.getOpenCdsConceptRelDTOs()) {
                        ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = openCdsConceptRelDTO.getConceptDeterminationMethodDTO();
                        CdsCodeDTO cdsCodeDTO = openCdsConceptRelDTO.getCdsCodeDTO();

                        OpenCdsConceptMappingSpecificationFile conceptFile = new OpenCdsConceptMappingSpecificationFile();

                        OpenCdsConcept determinationMethod = new OpenCdsConcept();
                        determinationMethod.setCode(conceptDeterminationMethodDTO.getCode());
                        determinationMethod.setDisplayName(conceptDeterminationMethodDTO.getDisplayName());
                        conceptFile.setConceptDeterminationMethod(determinationMethod);

                        OpenCdsConcept concept = new OpenCdsConcept();
                        concept.setCode(openCdsConceptDTO.getCode());
                        concept.setDisplayName(openCdsConceptDTO.getDisplayName());
                        conceptFile.setOpenCdsConcept(concept);

                        if (openCdsConceptRelDTO.getSpecificationNotes() == null) {
                            conceptFile.setSpecificationNotes("Generated by CDS Framework;");
                        } else {
                            conceptFile.setSpecificationNotes(openCdsConceptRelDTO.getSpecificationNotes() + "; Generated by CDS Framework;");
                        }

                        if (openCdsConceptDTO.getDescription() != null) {
                            conceptFile.setSpecificationNotes(conceptFile.getSpecificationNotes() + " " + openCdsConceptDTO.getDescription().trim());
                        }

                        MembersForCodeSystem membersForCodeSystem = new MembersForCodeSystem();
                        membersForCodeSystem.setCodeSystem(cdsCodeDTO.getCodeSystem());
                        membersForCodeSystem.setCodeSystemName(cdsCodeDTO.getCodeSystemName());
                        conceptFile.setMembersForCodeSystem(membersForCodeSystem);

//                        List<CD> members = membersForCodeSystem.getCDS();
//                        CD code = new CD();
//                        code.setCodeSystem(openCdsConceptRelDTO.getCodeSystem());
//                        code.setCodeSystemName(openCdsConceptRelDTO.getCodeSystemName());
//                        code.setCode(openCdsConceptRelDTO.getCode());
//                        code.setDisplayName(openCdsConceptRelDTO.getCodeDisplayName());
//                        members.add(code);
//
//                        byte[] marshalledXml = MarshalUtils.marshalObject(conceptFile);
//                        fileMap.put(conceptFile.getOpenCdsConcept().getCode() + "_" + openCdsConceptRelDTO.getCode() + "_" + conceptFile.getConceptDeterminationMethod().getCode() + ".xml", marshalledXml);
                    }

                }
                String xsd = FileUtils.getStringFromJarFile("conceptMapping.xsd");
                fileMap.put("conceptMapping.xsd", xsd.getBytes());
//            } catch (CdsException e) {
//                logger.error(e);
//                throw new MtsException(e.getMessage());
//            }
        } else {
            logger.error(METHODNAME, "baseDTO was null!");
        }
        return fileMap;
    }

    private void exportOpenCdsConcepts(ConceptDeterminationMethods conceptDeterminationMethods, SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException,
            ConstraintViolationException {
        final String METHODNAME = "exportOpenCdsConcepts ";
//        for (ConceptDeterminationMethod cdm : conceptDeterminationMethods.getConceptDeterminationMethod()) {
//
//            // find or add determination method
//            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = findOrAddConceptDeterminationMethodDTO(
//                    OpenCdsUtils.getConceptDeterminationMethodDTOFromConceptDeterminationMethod(cdm),
//                    sessionDTO);
//
//            for (ConceptMapping mapping : cdm.getConceptMapping()) {
//
//                // find or add concept
//                OpenCdsConceptDTO openCdsConceptDTO = findOrAddOpenCdsConceptDTO(
//                        OpenCdsUtils.getOpenCdsConceptDTOFromNamespacedConcept(mapping.getToConcept()),
//                        sessionDTO);
//                logger.info(METHODNAME, "importing concept: ", openCdsConceptDTO.getCode());
//
//                for (ConceptMapping.FromConcepts fromConcepts : mapping.getFromConcepts()) {
//                    logger.info(METHODNAME,
//                            "importing concept mappings from code system: ",
//                            fromConcepts.getCodeSystem(),
//                            " - ", fromConcepts.getCodeSystemName(),
//                            " - ", fromConcepts.getDisplayName());
//
//                    // find or add code system
//                    CdsCodeSystemDTO cdsCodeSystemDTO = findOrAddCodeSystemDTO(
//                            OpenCdsUtils.getCdsCodeSystemDTOFromConceptMappingFromConcepts(fromConcepts), sessionDTO);
//
//                    for (Concept concept : fromConcepts.getConcept()) {
//                        // find or add cds code
//                        CdsCodeDTO cdsCodeDTO = findOrAddCdsCodeDTO(
//                                OpenCdsUtils.getCdsCodeDTOFromConcept(concept, cdsCodeSystemDTO), cdsCodeSystemDTO, sessionDTO);
//                        // find or add concept/code mapping
//                        OpenCdsConceptRelDTO openCdsConceptRelDTO = findOrAddOpenCdsConceptRelDTO(
//                                OpenCdsUtils.getOpenCdsConceptRelDTO(cdsCodeDTO, openCdsConceptDTO, conceptDeterminationMethodDTO),
//                                cdsCodeDTO,
//                                openCdsConceptDTO,
//                                conceptDeterminationMethodDTO,
//                                sessionDTO);
//                    }
//                }
//            }
//        }
    }

    /**
     * Import a OpenCDS Concept - create any concept determination methods used or any missing code systems or codes.
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
    @Override
    public void importData(Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws ValidationException,
            NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        final String METHODNAME = "importData ";
        if (queryClass == SimpleExchange.class) {
            simpleExchangeImport(queryClass, sessionDTO, propertyBagDTO);
        } else {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "Unsupported queryClass: ", queryClass));
        }
    }

    /**
     * Import a OpenCDS Concept - create any concept determination methods used or any missing code systems or codes.
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

        byte[] payload = propertyBagDTO.get("payload", byte[].class);
        if (payload == null) {
            throw new ValidationException(CoreErrorCode.ParameterCanNotBeNull,
                    logger.error(METHODNAME, "payload was null!"));
        }

        if (logger.isDebugEnabled()) {
            logger.info(METHODNAME, "got payload: ", new String(payload));
        }

        // payload is base64 byte array of zip file - decode and unzip it and get the XML files out of it.
        List<String> conceptData = new ArrayList(FileUtils.getDataMapFromBase64ZipByteArray(payload, "xml").values());

        try {

            logger.info(METHODNAME, "about to process: ", conceptData.size());
            for (String fileData : conceptData) {

                try {
                    ConceptDeterminationMethods conceptDeterminationMethods = MarshalUtils.unmarshal(IOUtils.toInputStream(fileData), true, ConceptDeterminationMethods.class);
                    importOpenCdsConcepts(conceptDeterminationMethods, sessionDTO);
                } catch (Exception e) {
                    logger.error(e);
                    OpenCdsConceptMappingSpecificationFile openCdsConceptMappingSpecificationFile = MarshalUtils.unmarshal(IOUtils.toInputStream(fileData), false, OpenCdsConceptMappingSpecificationFile.class);
                    importCustomConcepts(openCdsConceptMappingSpecificationFile, sessionDTO);
                }

            }
        } catch (Exception e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        }

    }

    private void importOpenCdsConcepts(ConceptDeterminationMethods conceptDeterminationMethods, SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException,
            ConstraintViolationException {
        final String METHODNAME = "importOpenCdsConcepts ";
        for (ConceptDeterminationMethod cdm : conceptDeterminationMethods.getConceptDeterminationMethod()) {

            // find or add determination method
            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = findOrAddConceptDeterminationMethodDTO(
                    OpenCdsUtils.getConceptDeterminationMethodDTOFromConceptDeterminationMethod(cdm),
                    sessionDTO);

            for (ConceptMapping mapping : cdm.getConceptMapping()) {

                // find or add concept
                OpenCdsConceptDTO openCdsConceptDTO = findOrAddOpenCdsConceptDTO(
                        OpenCdsUtils.getOpenCdsConceptDTOFromNamespacedConcept(mapping.getToConcept()),
                        sessionDTO);
                logger.info(METHODNAME, "importing concept: ", openCdsConceptDTO.getCode());

                for (ConceptMapping.FromConcepts fromConcepts : mapping.getFromConcepts()) {
                    logger.info(METHODNAME,
                            "importing concept mappings from code system: ",
                            fromConcepts.getCodeSystem(),
                            " - ", fromConcepts.getCodeSystemName(),
                            " - ", fromConcepts.getDisplayName());

                    // find or add code system
                    CdsCodeSystemDTO cdsCodeSystemDTO = findOrAddCodeSystemDTO(
                            OpenCdsUtils.getCdsCodeSystemDTOFromConceptMappingFromConcepts(fromConcepts), sessionDTO);

                    for (Concept concept : fromConcepts.getConcept()) {
                        // find or add cds code
                        CdsCodeDTO cdsCodeDTO = findOrAddCdsCodeDTO(
                                OpenCdsUtils.getCdsCodeDTOFromConcept(concept, cdsCodeSystemDTO), cdsCodeSystemDTO, sessionDTO);
                        // find or add concept/code mapping
                        OpenCdsConceptRelDTO openCdsConceptRelDTO = findOrAddOpenCdsConceptRelDTO(
                                OpenCdsUtils.getOpenCdsConceptRelDTO(cdsCodeDTO, openCdsConceptDTO, conceptDeterminationMethodDTO),
                                cdsCodeDTO,
                                openCdsConceptDTO,
                                conceptDeterminationMethodDTO,
                                sessionDTO);
                    }
                }
            }
        }
    }

    private void importCustomConcepts(OpenCdsConceptMappingSpecificationFile openCdsConceptMappingSpecificationFile, SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException, ConstraintViolationException {
        final String METHODNAME = "importCustomConcepts ";

        // find or add concept
        OpenCdsConceptDTO openCdsConceptDTO = findOrAddOpenCdsConceptDTO(
                OpenCdsUtils.getOpenCdsConceptDTOFromOpenCdsConcept(
                        openCdsConceptMappingSpecificationFile.getOpenCdsConcept()),
                sessionDTO);
        logger.info(METHODNAME, "importing concept: ", openCdsConceptDTO.getCode());

        // find or add determination method
        ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = findOrAddConceptDeterminationMethodDTO(
                OpenCdsUtils.getConceptDeterminationMethodDTOFromOpenCdsConcept(
                        openCdsConceptMappingSpecificationFile.getConceptDeterminationMethod()),
                sessionDTO);

        // find or add code system
        CdsCodeSystemDTO cdsCodeSystemDTO = findOrAddCodeSystemDTO(
                OpenCdsUtils.getCdsCodeSystemDTOFromMembersForCodeSystem(
                        openCdsConceptMappingSpecificationFile.getMembersForCodeSystem()),
                sessionDTO);

        // iterate over the mappings
        for (CD member : openCdsConceptMappingSpecificationFile.getMembersForCodeSystem().getCDS()) {
            logger.info(
                    METHODNAME,
                    "importing concept mapping: ",
                    member.getCode(),
                    " - ", member.getDisplayName(),
                    " - ", member.getCodeSystem(),
                    " - ", member.getCodeSystemName());

            // find or add cds code
            CdsCodeDTO cdsCodeDTO = findOrAddCdsCodeDTO(
                    OpenCdsUtils.getCdsCodeDTOFromCD(member, cdsCodeSystemDTO), cdsCodeSystemDTO, sessionDTO);

            // find or add concept/code mapping
            OpenCdsConceptRelDTO openCdsConceptRelDTO = findOrAddOpenCdsConceptRelDTO(
                    OpenCdsUtils.getOpenCdsConceptRelDTO(cdsCodeDTO, openCdsConceptDTO, conceptDeterminationMethodDTO),
                    cdsCodeDTO, openCdsConceptDTO, conceptDeterminationMethodDTO, sessionDTO);

        }
    }

    private CdsCodeSystemDTO findOrAddCodeSystemDTO(CdsCodeSystemDTO cdsCodeSystemDTO, SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthorizationException, AuthenticationException, NotFoundException, ConstraintViolationException {
        final String METHODNAME = "findOrAddCodeSystemDTO ";
        CdsCodeSystemDTO queryDTO = DeepCopy.copy(cdsCodeSystemDTO);
        try {
            cdsCodeSystemDTO = cdsCodeSystemBO.findByQueryMain(queryDTO, CdsCodeSystemDTO.ByOid.class, new ArrayList<Class>(), sessionDTO, new PropertyBagDTO());
            logger.info(
                    METHODNAME,
                    "found code system: ",
                    queryDTO.getOid(),
                    " - ", queryDTO.getName());
        } catch (NotFoundException e) {
            logger.info(
                    METHODNAME,
                    "did not find code system: ",
                    queryDTO.getOid(),
                    " - ", queryDTO.getName());
            cdsCodeSystemDTO = null;
        }
        if (cdsCodeSystemDTO == null) {
            logger.info(METHODNAME, "creating code system for: ", queryDTO.getOid(), " - ", queryDTO.getName());
            cdsCodeSystemDTO = new CdsCodeSystemDTO();
            cdsCodeSystemDTO.setOid(queryDTO.getOid());
            cdsCodeSystemDTO.setName(queryDTO.getName());
            cdsCodeSystemDTO = cdsCodeSystemBO.addMain(cdsCodeSystemDTO, Add.class, sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "created code system for: ", queryDTO.getOid(), " - ", queryDTO.getName());
        }
        return cdsCodeSystemDTO;
    }

    private OpenCdsConceptDTO findOrAddOpenCdsConceptDTO(OpenCdsConceptDTO openCdsConceptDTO, SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException, ConstraintViolationException {
        final String METHODNAME = "findOrAddOpenCdsConceptDTO ";

        OpenCdsConceptDTO queryDTO = DeepCopy.copy(openCdsConceptDTO);
        // if it isn't found set it to null
        try {
            openCdsConceptDTO = findByQueryMain(queryDTO, OpenCdsConceptDTO.ByCode.class, new ArrayList<Class>(), sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "found concept: ", queryDTO.getCode());
        } catch (NotFoundException e) {
            logger.info(METHODNAME, "did not find concept: ", queryDTO.getCode());
            openCdsConceptDTO = null;
        }

        // if it is null then we are going to add a new one
        if (openCdsConceptDTO == null) {
            openCdsConceptDTO = new OpenCdsConceptDTO();
            openCdsConceptDTO.setCode(queryDTO.getCode());
            openCdsConceptDTO.setDisplayName(queryDTO.getDisplayName());
            openCdsConceptDTO = addMain(openCdsConceptDTO, Add.class, sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "added new opencds concept for import: ", queryDTO.getCode(), " - ", queryDTO.getDisplayName());
        }
        return openCdsConceptDTO;
    }

    private ConceptDeterminationMethodDTO findOrAddConceptDeterminationMethodDTO(ConceptDeterminationMethodDTO conceptDeterminationMethodDTO, SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException, ConstraintViolationException {
        final String METHODNAME = "findOrAddConceptDeterminationMethodDTO ";
        ConceptDeterminationMethodDTO queryDTO = DeepCopy.copy(conceptDeterminationMethodDTO);

        try {
            conceptDeterminationMethodDTO
                    = conceptDeterminationMethodBO.findByQueryMain(queryDTO, ConceptDeterminationMethodDTO.ByCode.class, new ArrayList<Class>(), sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "found concept determination method: ", conceptDeterminationMethodDTO.getCode());
        } catch (NotFoundException e) {
            logger.info(METHODNAME, "did not find concept determination method: ", queryDTO.getCode());
            conceptDeterminationMethodDTO = null;
        }

        // if not found - create it
        if (conceptDeterminationMethodDTO == null) {
            conceptDeterminationMethodDTO = new ConceptDeterminationMethodDTO();
            conceptDeterminationMethodDTO.setCode(queryDTO.getCode());
            conceptDeterminationMethodDTO.setDisplayName(queryDTO.getDisplayName());
            conceptDeterminationMethodDTO = conceptDeterminationMethodBO.addMain(conceptDeterminationMethodDTO, Add.class, sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "added new concept determination method for import: ", queryDTO.getCode(), " - ", queryDTO.getDisplayName());
        }
        return conceptDeterminationMethodDTO;
    }

    private CdsCodeDTO findOrAddCdsCodeDTO(CdsCodeDTO cdsCodeDTO, CdsCodeSystemDTO cdsCodeSystemDTO, SessionDTO sessionDTO)
            throws ValidationException, NotFoundException, ConstraintViolationException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "findOrAddCdsCodeDTO ";
        CdsCodeDTO queryDTO = DeepCopy.copy(cdsCodeDTO);

        try {
            cdsCodeDTO = cdsCodeBO.findByQueryMain(queryDTO, CdsCodeDTO.ByCodeSystemCode.class, new ArrayList<Class>(), sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "found cds code: ", cdsCodeDTO.getCode());
        } catch (NotFoundException e) {
            logger.info(METHODNAME, "did not find cds code: ", queryDTO.getCode());
            cdsCodeDTO = null;
        }

        // if not found - create it
        if (cdsCodeDTO == null) {
            cdsCodeDTO = new CdsCodeDTO();
            cdsCodeDTO.setCode(queryDTO.getCode());
            cdsCodeDTO.setDisplayName(queryDTO.getDisplayName());
            cdsCodeDTO.setCodeSystemId(cdsCodeSystemDTO.getCodeSystemId());
            cdsCodeDTO = cdsCodeBO.addMain(cdsCodeDTO, Add.class, sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME, "added new cds code for import: ", queryDTO.getCode(), " - ", queryDTO.getDisplayName());
        }

        return cdsCodeDTO;
    }

    private OpenCdsConceptRelDTO findOrAddOpenCdsConceptRelDTO(
            OpenCdsConceptRelDTO openCdsConceptRelDTO,
            CdsCodeDTO cdsCodeDTO,
            OpenCdsConceptDTO openCdsConceptDTO,
            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO,
            SessionDTO sessionDTO)
            throws ValidationException, MtsException, AuthenticationException, AuthorizationException, NotFoundException,
            ConstraintViolationException {
        final String METHODNAME = "findOrAddOpenCdsConceptRelDTO ";

        OpenCdsConceptRelDTO queryDTO = DeepCopy.copy(openCdsConceptRelDTO);
        try {
            openCdsConceptRelDTO = openCdsConceptRelBO.findByQueryMain(
                    queryDTO,
                    OpenCdsConceptRelDTO.ByOpenCdsConceptIdCodeIdConceptDeterminationMethodId.class,
                    new ArrayList<Class>(),
                    sessionDTO,
                    new PropertyBagDTO());
            logger.info(
                    METHODNAME,
                    "openCdsConceptRelDTO found for: ",
                    cdsCodeDTO.getCode(),
                    " - ", cdsCodeDTO.getCodeSystem(),
                    " - ", conceptDeterminationMethodDTO.getCode(),
                    " - ", openCdsConceptDTO.getCode());
        } catch (NotFoundException e) {
            logger.info(
                    METHODNAME,
                    "openCdsConceptRelDTO not found for: ",
                    cdsCodeDTO.getCode(),
                    " - ", cdsCodeDTO.getCodeSystem(),
                    " - ", conceptDeterminationMethodDTO.getCode(),
                    " - ", openCdsConceptDTO.getCode());
            openCdsConceptRelDTO = null;
        }

        if (openCdsConceptRelDTO == null) {
            openCdsConceptRelDTO = new OpenCdsConceptRelDTO();
            openCdsConceptRelDTO.setCdsCodeDTO(cdsCodeDTO);
            openCdsConceptRelDTO.setConceptDeterminationMethodDTO(conceptDeterminationMethodDTO);
            openCdsConceptRelDTO.setConceptCodeId(openCdsConceptDTO.getCodeId());
            openCdsConceptRelDTO = openCdsConceptRelBO.addMain(openCdsConceptRelDTO, Add.class, sessionDTO, new PropertyBagDTO());
            logger.info(METHODNAME,
                    "added mapping for ",
                    openCdsConceptDTO.getCode(),
                    " - ", cdsCodeDTO.getCode(),
                    " - ", cdsCodeDTO.getCodeSystem(),
                    " - ", conceptDeterminationMethodDTO.getCode());
        }
        return openCdsConceptRelDTO;
    }
}
