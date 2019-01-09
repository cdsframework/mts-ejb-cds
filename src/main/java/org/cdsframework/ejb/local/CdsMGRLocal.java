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
package org.cdsframework.ejb.local;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.WebServiceException;
import org.cdsframework.cds.service.OpenCdsService;
import org.cdsframework.cds.util.CdsObjectFactory;
import org.cdsframework.cds.vmr.CdsObjectAssist;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptDeploymentLogDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.dto.ValueSetSubValueSetRelDTO;
import org.cdsframework.ejb.bo.CdsCodeSystemBO;
import org.cdsframework.ejb.bo.OpenCdsConceptBO;
import org.cdsframework.ejb.bo.OpenCdsConceptDeploymentLogBO;
import org.cdsframework.ejb.bo.ValueSetBO;
import org.cdsframework.enumeration.DeploymentAction;
import org.cdsframework.enumeration.DeploymentEnvironment;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.group.Add;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.LogUtils;
import org.opencds.config.schema.Concept;
import org.opencds.config.schema.ConceptDeterminationMethod;
import org.opencds.config.schema.ConceptMapping;
import org.opencds.config.schema.NamespacedConcept;
import org.opencds.config.schema.ObjectFactory;
import org.opencds.vmr.v1_0.schema.CDSInput;
import org.opencds.vmr.v1_0.schema.CDSOutput;
import org.opencds.vmr.v1_0.schema.RelatedClinicalStatement;
import org.opencds.vmr.v1_0.schema.SubstanceAdministrationEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CdsMGRLocal {

    private final static LogUtils logger = LogUtils.getLogger(CdsMGRLocal.class);
    @EJB
    private PropertyMGRLocal propertyMGRLocal;
    @EJB
    private OpenCdsConceptBO openCdsConceptBO;
    @EJB
    private ValueSetBO valueSetBO;
    @EJB
    private CdsCodeSystemBO cdsCodeSystemBO;
    @EJB
    private OpenCdsConceptDeploymentLogBO openCdsConceptDeploymentLogBO;

    /**
     * Retrieve a handle to the OpenCDS wen service using the system properties
     *
     * @return
     */
    private OpenCdsService getOpenCdsService() {
        long start = System.nanoTime();
        final String METHODNAME = "getOpenCdsService ";
        int requestTimeout = (Integer) propertyMGRLocal.get("CDS_TIMEOUT");
        String endPoint = (String) propertyMGRLocal.get("CDS_ENDPOINT");
        OpenCdsService openCdsService = OpenCdsService.getOpenCdsService(endPoint, requestTimeout, requestTimeout);
        logger.logDuration(METHODNAME, start);
        return openCdsService;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public CDSOutput cdsEvaluateCdsInput(CDSInput cdsInputObject, String scopingEntityId, String businessId, String version, Date executionDate)
            throws MtsException {
        final String METHODNAME = "cdsEvaluateCdsInput ";
        CDSOutput response = null;
        logger.logBegin(METHODNAME);
        long start = System.nanoTime();
        String cdsBusinessId;
        String cdsEntityId;
        String cdsKmVersion;
        logger.trace(METHODNAME, "begin");
        try {
            if (propertyMGRLocal.get("CDS_ENABLED", Boolean.class)) {
                if (logger.isDebugEnabled()) {
                    logger.logDuration("CDS Prep", start);
                }

                if (businessId == null || scopingEntityId == null || version == null) {
                    throw new MtsException("businessId or scopingEntityId or version is null: "
                            + businessId + " - " + scopingEntityId + " - " + version);
                } else {
                    logger.info(METHODNAME, "using user supplied business scope: ", businessId, " - ", scopingEntityId, " - ", version);
                    cdsBusinessId = businessId;
                    cdsEntityId = scopingEntityId;
                    cdsKmVersion = version;
                }
                logger.trace(METHODNAME, "cdsBusinessId=", cdsBusinessId);
                logger.trace(METHODNAME, "cdsEntityId=", cdsEntityId);
                logger.trace(METHODNAME, "cdsKmVersion=", cdsKmVersion);

                start = System.nanoTime();
                response = evaluate(cdsInputObject, cdsEntityId, cdsBusinessId, cdsKmVersion, executionDate);
                postProcessOutput(response);
            } else {
                throw new MtsException("CDS service is disabled!");
            }
        } catch (WebServiceException e) {
            logger.error("WebServiceException: error invoking CDS service: ", e);
            try {
                logger.error(new String(CdsObjectAssist.cdsObjectToByteArray(cdsInputObject, CDSInput.class)));
            } catch (Exception ex) {
            }
            throw new MtsException(e.getMessage());
        } catch (CdsException e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            throw new MtsException(e.getMessage());
        } finally {
            logger.logDuration("CDS Evaluate", start);
            logger.logEnd(METHODNAME);
        }
        return response;
    }

    private void postProcessOutput(CDSOutput response) {
        if (response == null) {
            return;
        }
        if (response.getVmrOutput() == null) {
            return;
        }
        if (response.getVmrOutput().getPatient() == null) {
            return;
        }
        if (response.getVmrOutput().getPatient().getClinicalStatements() == null) {
            return;
        }
        if (response.getVmrOutput().getPatient().getClinicalStatements().getSubstanceAdministrationEvents() == null) {
            return;
        }
        if (response.getVmrOutput().getPatient().getClinicalStatements().getSubstanceAdministrationEvents().getSubstanceAdministrationEvent() == null) {
            return;
        }
        List<SubstanceAdministrationEvent> newSubstanceAdministrationEvents = new ArrayList<SubstanceAdministrationEvent>();
        List<SubstanceAdministrationEvent> substanceAdministrationEvents
                = response.getVmrOutput().getPatient().getClinicalStatements().getSubstanceAdministrationEvents().getSubstanceAdministrationEvent();
        Iterator<SubstanceAdministrationEvent> iterator = substanceAdministrationEvents.iterator();
        while (iterator.hasNext()) {
            SubstanceAdministrationEvent substanceAdministrationEvent = iterator.next();
            int observationResultCount = 0;
            int substanceAdministrationCount = 0;
            List<RelatedClinicalStatement> relatedClinicalStatements = substanceAdministrationEvent.getRelatedClinicalStatement();
            for (RelatedClinicalStatement relatedClinicalStatement : relatedClinicalStatements) {
                if (relatedClinicalStatement.getObservationResult() != null) {
                    observationResultCount++;
                } else if (relatedClinicalStatement.getSubstanceAdministrationEvent() != null) {
                    substanceAdministrationCount++;
                }
            }
            if (substanceAdministrationCount == 0) {
                SubstanceAdministrationEvent newSubstanceAdministrationEvent = CdsObjectFactory.getSubstanceAdministrationEvent();
                newSubstanceAdministrationEvent.setDoseNumber(substanceAdministrationEvent.getDoseNumber());
                newSubstanceAdministrationEvent.setId(substanceAdministrationEvent.getId());
                newSubstanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(substanceAdministrationEvent.getSubstanceAdministrationGeneralPurpose());
                newSubstanceAdministrationEvent.setSubstance(substanceAdministrationEvent.getSubstance());
                newSubstanceAdministrationEvent.setAdministrationTimeInterval(substanceAdministrationEvent.getAdministrationTimeInterval());

                RelatedClinicalStatement relatedClinicalStatement = CdsObjectFactory.getRelatedClinicalStatement("PERT");

                relatedClinicalStatement.setSubstanceAdministrationEvent(substanceAdministrationEvent);

                newSubstanceAdministrationEvent.getRelatedClinicalStatement().add(relatedClinicalStatement);
                newSubstanceAdministrationEvents.add(newSubstanceAdministrationEvent);
            } else {
                newSubstanceAdministrationEvents.add(substanceAdministrationEvent);
            }
            iterator.remove();
        }
        substanceAdministrationEvents.addAll(newSubstanceAdministrationEvents);
    }

    public CDSOutput evaluate(CDSInput cdsInput, String scopingEntityId, String businessId, String version, Date executionDate)
            throws CdsException {
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(cdsInput, CDSInput.class);
        byte[] evaluation = evaluate(cdsObjectToByteArray, scopingEntityId, businessId, version, executionDate);
        CDSOutput cdsOutput = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CDSOutput.class);
        return cdsOutput;
    }

    public byte[] evaluate(byte[] payload, String scopingEntityId, String businessId, String version, Date executionDate) throws
            CdsException {
        return getOpenCdsService().evaluate(payload, scopingEntityId, businessId, version, executionDate);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ConceptDeterminationMethod getConceptDeterminationMethod(
            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO,
            String codeSystem,
            DeploymentEnvironment environment,
            boolean audit,
            SessionDTO sessionDTO,
            PropertyBagDTO propertyBagDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException, ConstraintViolationException {
        final String METHODNAME = "getConceptDeterminationMethod ";
        logger.info(METHODNAME, "codeSystem=", codeSystem);
        logger.info(METHODNAME, "environment=", environment);
        logger.info(METHODNAME, "audit=", audit);
        ObjectFactory objectFactory = new ObjectFactory();
        ConceptDeterminationMethod result = objectFactory.createConceptDeterminationMethod();

        result.setCode(conceptDeterminationMethodDTO.getCode());
        result.setCodeSystem(codeSystem);
        result.setDisplayName(conceptDeterminationMethodDTO.getDisplayName());

        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            result.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException e) {
            throw new MtsException("Error creating xml date for timestamp", e);
        }

        result.setUserId("GENERATED");
        result.setVersion("1.0");

        addConceptMappings(conceptDeterminationMethodDTO, result, codeSystem, environment, audit, objectFactory, sessionDTO, propertyBagDTO);

        return result;
    }

    private void addConceptMappings(
            ConceptDeterminationMethodDTO conceptDeterminationMethodDTO,
            ConceptDeterminationMethod conceptDeterminationMethod,
            String codeSystem,
            DeploymentEnvironment environment,
            boolean audit,
            ObjectFactory objectFactory,
            SessionDTO sessionDTO,
            PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException {

        final String METHODNAME = "addConceptMappings ";
        Map<String, ConceptMapping> addedConceptMappings = new HashMap<>();
        Map<String, Map<String, ConceptMapping.FromConcepts>> addedMappedCodeSystems = new HashMap<>();
        Map<ConceptMapping.FromConcepts, List<String>> addedMappedCodeSystemCodes;
        List<Class> childClasses = new ArrayList<>();
        childClasses.add(OpenCdsConceptRelDTO.class);
        childClasses.add(ValueSetSubValueSetRelDTO.class);
        childClasses.add(ValueSetCdsCodeRelDTO.class);
        childClasses.add(CdsCodeDTO.class);
        OpenCdsConceptDTO openCdsConceptQueryDTO = new OpenCdsConceptDTO();
        openCdsConceptQueryDTO.getQueryMap().put("cdm_code", conceptDeterminationMethodDTO.getCode());
        List<OpenCdsConceptDTO> openCdsConceptDTOs = openCdsConceptBO.findByQueryListMain(openCdsConceptQueryDTO, OpenCdsConceptDTO.ByConceptDeterminationMethod.class, childClasses, sessionDTO, propertyBagDTO);

        for (OpenCdsConceptDTO openCdsConceptDTO : openCdsConceptDTOs) {
            if (audit) {
                OpenCdsConceptDeploymentLogDTO logDTO = new OpenCdsConceptDeploymentLogDTO();
                logDTO.setCodeId(openCdsConceptDTO.getCodeId());
                logDTO.setDeploymentAction(DeploymentAction.DEPLOYED);
                logDTO.setDeploymentEnvironment(environment);
                openCdsConceptDeploymentLogBO.addMain(logDTO, Add.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
            }
            addedMappedCodeSystemCodes = new HashMap<>();
            boolean testPresent = false;
            List<OpenCdsConceptRelDTO> openCdsConceptRelDTOs = openCdsConceptDTO.getOpenCdsConceptRelDTOs();
            if (environment == DeploymentEnvironment.TEST) {
                for (OpenCdsConceptRelDTO openCdsConceptRelDTO : openCdsConceptRelDTOs) {
                    if (openCdsConceptRelDTO.getDeploymentEnvironment() == DeploymentEnvironment.TEST) {
                        testPresent = true;
                        break;
                    }
                }
            }
            for (OpenCdsConceptRelDTO openCdsConceptRelDTO : openCdsConceptRelDTOs) {
                if (openCdsConceptRelDTO.isDisabled()) {
                    logger.warn(METHODNAME, "skipping production relationshipId ", openCdsConceptRelDTO.getRelationshipId(), " - it is disabled.");
                    continue;
                }
                if (testPresent && openCdsConceptRelDTO.getDeploymentEnvironment() == DeploymentEnvironment.PRODUCTION) {
                    logger.warn(METHODNAME, "skipping production relationshipId ", openCdsConceptRelDTO.getRelationshipId(), " - there is a test version of it.");
                    continue;
                }
                if (environment == DeploymentEnvironment.PRODUCTION && openCdsConceptRelDTO.getDeploymentEnvironment() == DeploymentEnvironment.TEST) {
                    logger.warn(METHODNAME, "skipping test relationshipId ", openCdsConceptRelDTO.getRelationshipId(), " - this is a production run cdm.");
                    continue;
                }
                if (null != openCdsConceptRelDTO.getMappingType()) {
                    if (audit) {
                        OpenCdsConceptDeploymentLogDTO logRelDTO = new OpenCdsConceptDeploymentLogDTO();
                        logRelDTO.setCodeId(openCdsConceptDTO.getCodeId());
                        logRelDTO.setRelationshipId(openCdsConceptRelDTO.getRelationshipId());
                        logRelDTO.setDeploymentAction(DeploymentAction.DEPLOYED);
                        logRelDTO.setDeploymentEnvironment(environment);
                        openCdsConceptDeploymentLogBO.addMain(logRelDTO, Add.class, AuthenticationUtils.getInternalSessionDTO(), propertyBagDTO);
                    }
                    switch (openCdsConceptRelDTO.getMappingType()) {
                        case CODE:
                            // send it up as is
                            addConceptMapping(
                                    conceptDeterminationMethod,
                                    openCdsConceptDTO,
                                    openCdsConceptRelDTO,
                                    objectFactory,
                                    addedConceptMappings,
                                    addedMappedCodeSystems,
                                    addedMappedCodeSystemCodes,
                                    codeSystem);
                            break;
                        case VALUE_SET: {
                            ValueSetDTO valueSetDTO = openCdsConceptRelDTO.getValueSetDTO();
                            if (valueSetDTO != null) {
                                for (ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO : valueSetDTO.getAllValueSetCdsCodeRelDTOs()) {
                                    // create temporary OpenCdsConceptRelDTOs for each value set mapping
                                    CdsCodeDTO cdsCodeDTO = valueSetCdsCodeRelDTO.getCdsCodeDTO();
                                    OpenCdsConceptRelDTO tempOpenCdsConceptRelDTO = new OpenCdsConceptRelDTO();
                                    CdsCodeSystemDTO tempCdsCodeSystemDTO = new CdsCodeSystemDTO();
                                    tempCdsCodeSystemDTO.setOid(cdsCodeDTO.getCodeSystem());
                                    tempCdsCodeSystemDTO.setName(cdsCodeDTO.getCodeSystemName());
                                    tempOpenCdsConceptRelDTO.setCdsCodeSystemDTO(tempCdsCodeSystemDTO);
                                    tempOpenCdsConceptRelDTO.setCdsCodeDTO(cdsCodeDTO);

                                    // send it up
                                    addConceptMapping(
                                            conceptDeterminationMethod,
                                            openCdsConceptDTO,
                                            tempOpenCdsConceptRelDTO,
                                            objectFactory,
                                            addedConceptMappings,
                                            addedMappedCodeSystems,
                                            addedMappedCodeSystemCodes,
                                            codeSystem);
                                }
                            } else {
                                logger.error(METHODNAME,
                                        "case VALUE_SET valueSetDTO is null for mapping: ", openCdsConceptRelDTO.getRelationshipId(),
                                        " - cdm: ", openCdsConceptRelDTO.getConceptDeterminationMethodDTO(),
                                        " - concept name: ", openCdsConceptRelDTO.getName());
                            }
                            break;
                        }
                        case CODE_SYSTEM: {
                            CdsCodeSystemDTO cdsCodeSystemDTO = openCdsConceptRelDTO.getCdsCodeSystemDTO();
                            if (cdsCodeSystemDTO != null) {
                                for (CdsCodeDTO cdsCodeDTO : cdsCodeSystemDTO.getCdsCodeDTOs()) {
                                    // create temporary OpenCdsConceptRelDTOs for each code system mapping
                                    OpenCdsConceptRelDTO tempOpenCdsConceptRelDTO = new OpenCdsConceptRelDTO();
                                    tempOpenCdsConceptRelDTO.setCdsCodeSystemDTO(cdsCodeSystemDTO);
                                    tempOpenCdsConceptRelDTO.setCdsCodeDTO(cdsCodeDTO);

                                    // send it up
                                    addConceptMapping(
                                            conceptDeterminationMethod,
                                            openCdsConceptDTO,
                                            tempOpenCdsConceptRelDTO,
                                            objectFactory,
                                            addedConceptMappings,
                                            addedMappedCodeSystems,
                                            addedMappedCodeSystemCodes,
                                            codeSystem);
                                }
                            } else {
                                logger.error(METHODNAME,
                                        "case CODE_SYSTEM cdsCodeSystemDTO is null for mapping: ", openCdsConceptRelDTO.getRelationshipId(),
                                        " - cdm: ", openCdsConceptRelDTO.getConceptDeterminationMethodDTO(),
                                        " - concept name: ", openCdsConceptRelDTO.getName());
                            }
                            break;
                        }
                        default:
                            logger.error(METHODNAME, "unsupported mapping type: ", openCdsConceptRelDTO.getMappingType());
                            break;
                    }
                } else {
                    logger.error(METHODNAME, "null mapping type");
                }
            }
        }
    }

    private void addConceptMapping(
            ConceptDeterminationMethod conceptDeterminationMethod,
            OpenCdsConceptDTO openCdsConceptDTO,
            OpenCdsConceptRelDTO openCdsConceptRelDTO,
            ObjectFactory objectFactory,
            Map<String, ConceptMapping> addedConceptMappings,
            Map<String, Map<String, ConceptMapping.FromConcepts>> addedMappedCodeSystems,
            Map<ConceptMapping.FromConcepts, List<String>> addedMappedCodeSystemCodes,
            String codeSystem) {

        final String METHODNAME = "addConceptMapping ";
        // grab the concept mapping if it has already been initialized - otherwise create it
        ConceptMapping conceptMapping = addedConceptMappings.get(openCdsConceptDTO.getCode());

        if (conceptMapping == null) {
            conceptMapping = objectFactory.createConceptMapping();

            NamespacedConcept toConcept = objectFactory.createNamespacedConcept();
            conceptMapping.setToConcept(toConcept);
            toConcept.setCode(openCdsConceptDTO.getCode());
            toConcept.setDisplayName(openCdsConceptDTO.getDisplayName());
            toConcept.setCodeSystem(codeSystem);

            addedConceptMappings.put(openCdsConceptDTO.getCode(), conceptMapping);
            conceptDeterminationMethod.getConceptMapping().add(conceptMapping);
        }

        // grab the mapped code system map - otherwise create it
        Map<String, ConceptMapping.FromConcepts> codeSystemMap = addedMappedCodeSystems.get(openCdsConceptDTO.getCode());
        if (codeSystemMap == null) {
            codeSystemMap = new HashMap<String, ConceptMapping.FromConcepts>();
            addedMappedCodeSystems.put(openCdsConceptDTO.getCode(), codeSystemMap);
        }

        // grab the fromconcept object - otherwise create it
        ConceptMapping.FromConcepts fromConcept = codeSystemMap.get(openCdsConceptRelDTO.getCdsCodeSystemDTO().getOid());
        if (fromConcept == null) {
            fromConcept = objectFactory.createConceptMappingFromConcepts();
            fromConcept.setCodeSystem(openCdsConceptRelDTO.getCdsCodeSystemDTO().getOid());
            fromConcept.setCodeSystemName(openCdsConceptRelDTO.getCdsCodeSystemDTO().getName());
            codeSystemMap.put(openCdsConceptRelDTO.getCdsCodeSystemDTO().getOid(), fromConcept);
            conceptMapping.getFromConcepts().add(fromConcept);
        }

        List<String> mappedCodeSystemCodes = addedMappedCodeSystemCodes.get(fromConcept);

        if (mappedCodeSystemCodes == null) {
            mappedCodeSystemCodes = new ArrayList<String>();
            addedMappedCodeSystemCodes.put(fromConcept, mappedCodeSystemCodes);
        }

        if (!mappedCodeSystemCodes.contains(openCdsConceptRelDTO.getCdsCodeDTO().getCode())) {
            mappedCodeSystemCodes.add(openCdsConceptRelDTO.getCdsCodeDTO().getCode());
            Concept concept = objectFactory.createConcept();
            concept.setCode(openCdsConceptRelDTO.getCdsCodeDTO().getCode());
            concept.setDisplayName(openCdsConceptRelDTO.getCdsCodeDTO().getDisplayName());
            fromConcept.getConcept().add(concept);
        } else {
            logger.info(METHODNAME, "skipping code b/c it was already added: ", openCdsConceptDTO.getCode(), " - ", openCdsConceptRelDTO.getCdsCodeSystemDTO().getOid(), " - ", openCdsConceptRelDTO.getCdsCodeDTO().getCode());
        }
    }
}
