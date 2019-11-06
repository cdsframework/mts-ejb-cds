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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cdsframework.cds.util.CdsObjectFactory;
import org.cdsframework.cds.vmr.CdsInputWrapper;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.enumeration.DetectedModelElementType;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.opencds.vmr.v1_0.schema.EncounterEvent;
import org.opencds.vmr.v1_0.schema.IVLTS;
import org.opencds.vmr.v1_0.schema.ObservationOrder;
import org.opencds.vmr.v1_0.schema.ObservationResult;
import org.opencds.vmr.v1_0.schema.PQ;
import org.opencds.vmr.v1_0.schema.Problem;
import org.opencds.vmr.v1_0.schema.ProcedureEvent;
import org.opencds.vmr.v1_0.schema.SubstanceAdministrationEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsInputMapper {

    private static final LogUtils logger = LogUtils.getLogger(CdsInputMapper.class);

    public static void setClinicalStatement(
            CdsInputWrapper cdsInputWrapper,
            String statementId,
            Map<DetectedModelElementType, List<Object>> clinicalStatementMapItem)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "setClinicalStatement ";

        Map<Class, Object> objectMap = new HashMap<Class, Object>();
        logger.info(METHODNAME, "clinicalStatementMapItem=", clinicalStatementMapItem);

        for (DetectedModelElementType detectedModelElementType : clinicalStatementMapItem.keySet()) {
            Class clinicalStatementClass = detectedModelElementType.getClinicalStatementClass();
            logger.info(METHODNAME, "found detectedModelElementType: ", detectedModelElementType, " - ", clinicalStatementClass);

            CdsCodeDTO cdsCodeDTO = null;
            OpenCdsConceptDTO openCdsConceptDTO = null;
            PQ quantity = null;
            IVLTS time = null;

            List<Object> value = clinicalStatementMapItem.get(detectedModelElementType);

            // TODO: support multiple values for anything?
            if (value.size() > 0) {
                openCdsConceptDTO = value.get(0) instanceof OpenCdsConceptDTO ? (OpenCdsConceptDTO) value.get(0) : null;
                cdsCodeDTO = value.get(0) instanceof CdsCodeDTO ? (CdsCodeDTO) value.get(0) : null;
                quantity = value.get(0) instanceof PQ ? (PQ) value.get(0) : null;
                time = value.get(0) instanceof IVLTS ? (IVLTS) value.get(0) : null;
            } else {
                throw new IllegalArgumentException("Value from empty: " + value);
            }

            if (openCdsConceptDTO != null) {
                logger.info(METHODNAME, "found openCdsConceptDTO: ", openCdsConceptDTO.getLabel());
                cdsCodeDTO = OpenCdsConceptUtils.getFirstCdsCodeDTOFromOpenCdsConceptDTO(openCdsConceptDTO);
            }
            if (cdsCodeDTO != null) {
                logger.info(METHODNAME, "found cdsCodeDTO: ", cdsCodeDTO.getLabel());
            }
            if (quantity != null) {
                logger.info(METHODNAME, "found quantity: ", quantity.getValue(), " ", quantity.getUnit());
            }
            if (time != null) {
                logger.info(METHODNAME, "found time: ", time.getLow(), " ", time.getHigh());
            }

            Object clinicalStatementObject = objectMap.get(clinicalStatementClass);
            if (clinicalStatementObject == null) {
                if (clinicalStatementClass == ObservationResult.class) {
                    String templateId = "2.16.840.1.113883.3.1829.11.6.3.15";
                    String idRoot = "4.1.1.1";
                    clinicalStatementObject = CdsInputWrapper.getObservationResult(templateId, idRoot, statementId);
                    objectMap.put(ObservationResult.class, clinicalStatementObject);
                } else if (clinicalStatementClass == ObservationOrder.class) {
                    String templateId = "2.16.840.1.113883.3.1829.11.6.1.3";
                    String idRoot = "4.1.1.1.1.1";
                    clinicalStatementObject = CdsInputWrapper.getObservationOrder(templateId, idRoot, statementId);
                    objectMap.put(ObservationOrder.class, clinicalStatementObject);
                } else if (clinicalStatementClass == EncounterEvent.class) {
                    String templateId = "2.16.840.1.113883.3.1829.11.4.3.1";
                    String idRoot = "2.16.840.1.113883.3.795.5.2.12.2";
                    EncounterEvent encounterEvent = CdsInputWrapper.getEncounterEvent(templateId, idRoot, statementId);
                    // set default encounter date to today
                    String today = org.opencds.support.util.DateUtils.getISODateFormat(new Date());
                    IVLTS todayIvlts = CdsObjectFactory.getIVLTS(today, today);
                    encounterEvent.setEncounterEventTime(todayIvlts);
                    clinicalStatementObject = encounterEvent;
                    objectMap.put(EncounterEvent.class, clinicalStatementObject);
                } else if (clinicalStatementClass == Problem.class) {
                    String templateId = "2.16.840.1.113883.3.1829.11.7.2.21";
                    String idRoot = "4.1.1.1.1.1.1";
                    Problem problem = CdsInputWrapper.getProblem(templateId, idRoot, statementId);
                    clinicalStatementObject = problem;
                    objectMap.put(Problem.class, clinicalStatementObject);
                } else if (clinicalStatementClass == SubstanceAdministrationEvent.class) {
                    String templateId = "2.16.840.1.113883.3.795.11.9.1.1";
                    String idRoot = "2.16.840.1.113883.3.795.12.100.10";
                    SubstanceAdministrationEvent substanceAdministrationEvent = CdsInputWrapper.getSubstanceAdministrationEvent(templateId, idRoot, statementId);
                    substanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(CdsObjectFactory.getCD("384810002", "2.16.840.1.113883.6.5"));
                    clinicalStatementObject = substanceAdministrationEvent;
                    objectMap.put(SubstanceAdministrationEvent.class, clinicalStatementObject);
                } else {
                    logger.error(METHODNAME, "UNSUPPORTED CLASS: ", clinicalStatementClass);
                }
            }
            switch (detectedModelElementType) {
                case problemProblemCode: {
                    Problem problem = (Problem) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting problem code: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setProblemCodeOnProblem(problem,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case problemProblemStatus: {
                    Problem problem = (Problem) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting problem status: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setProblemStatusOnProblem(problem,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case problemProblemEffectiveTime: {
                    Problem problem = (Problem) clinicalStatementObject;
                    if (time != null) {
                        logger.debug(METHODNAME, "setting problem effective time: ", time.getLow(), " - ", time.getHigh());
                        CdsInputWrapper.setProblemEffectiveTimeOnProblem(problem, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case resultInterpretation: {
                    ObservationResult observationResult = (ObservationResult) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting interpretation code: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setInterpretationOnObservationResult(observationResult,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case resultObservationEventTime: {
                    ObservationResult observationResult = (ObservationResult) clinicalStatementObject;
                    if (time != null) {
                        logger.debug(METHODNAME, "setting observation time: ", time.getLow(), " - ", time.getHigh());
                        CdsInputWrapper.setObservationEventTimeOnObservationResult(observationResult, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case resultObservationFocus: {
                    ObservationResult observationResult = (ObservationResult) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting observation focus: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setObservationFocusOnObservationResult(observationResult,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case resultObservationValuePhysicalQuantity: {
                    ObservationResult observationResult = (ObservationResult) clinicalStatementObject;
                    if (quantity != null) {
                        logger.debug(METHODNAME, "setting observation value quantity: ", quantity.getValue(), " ", quantity.getUnit());
                        CdsInputWrapper.setPhysicalQuantityValueOnObservationResult(observationResult, quantity.getValue() + "", quantity.getUnit());
                    } else {
                        logger.error(METHODNAME, "quantity was null! ");
                    }
                    break;
                }
                case resultObservationValueConcept: {
                    ObservationResult observationResult = (ObservationResult) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting observation value concept: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setConceptValueOnObservationResult(observationResult,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case encounterType: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting encounter type: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setEncounterTypeOnEncounterEvent(encounterEvent,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case encounterEventTime: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    if (time != null) {
                        logger.debug(METHODNAME, "setting encounter event time: ", time.getLow(), " - ", time.getHigh());
                        CdsInputWrapper.setEncounterEventTimeOnEncounterEvent(encounterEvent, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case encounterProblemCode: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    String problemRelationshipCode = "COMP";
                    String problemRelationshipOid = "2.16.840.1.113883.3.795.12.3.2.1";
                    String problemTemplateId = "2.16.840.1.113883.3.1829.11.7.2.18";
                    String problemIdRoot = "2.16.840.1.113883.3.795.5.2.3.6";
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting problem code: ", cdsCodeDTO.getLabel());
                        Problem problem = CdsInputWrapper.getProblemFromEncounterEvent(encounterEvent, problemTemplateId, problemIdRoot, statementId,
                                problemRelationshipCode, problemRelationshipOid, null, null);
                        CdsInputWrapper.setProblemCodeOnProblem(problem,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                        String today = org.opencds.support.util.DateUtils.getISODateFormat(new Date());
                        IVLTS todayIvlts = CdsObjectFactory.getIVLTS(today, today);
                        if (problem.getProblemEffectiveTime() == null) {
                            CdsInputWrapper.setProblemEffectiveTimeOnProblem(problem, todayIvlts.getLow(), todayIvlts.getHigh());
                        }
                        if (problem.getDiagnosticEventTime() == null) {
                            CdsInputWrapper.setProblemDiagnosticEventTimeOnProblem(problem, todayIvlts.getLow(), todayIvlts.getHigh());
                        }
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case encounterProblemStatus: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    String problemRelationshipCode = "COMP";
                    String problemRelationshipOid = "2.16.840.1.113883.3.795.12.3.2.1";
                    String problemTemplateId = "2.16.840.1.113883.3.1829.11.7.2.18";
                    String problemIdRoot = "2.16.840.1.113883.3.795.5.2.3.6";
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting problem code: ", cdsCodeDTO.getLabel());
                        Problem problem = CdsInputWrapper.getProblemFromEncounterEvent(encounterEvent, problemTemplateId, problemIdRoot, statementId,
                                problemRelationshipCode, problemRelationshipOid, null, null);
                        CdsInputWrapper.setProblemStatusOnProblem(problem,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case encounterProblemEffectiveTime: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    String problemRelationshipCode = "COMP";
                    String problemRelationshipOid = "2.16.840.1.113883.3.795.12.3.2.1";
                    String problemTemplateId = "2.16.840.1.113883.3.1829.11.7.2.18";
                    String problemIdRoot = "2.16.840.1.113883.3.795.5.2.3.6";
                    if (time != null) {
                        logger.debug(METHODNAME, "setting problem time: ", time.getLow(), " - ", time.getHigh());
                        Problem problem = CdsInputWrapper.getProblemFromEncounterEvent(encounterEvent, problemTemplateId, problemIdRoot, statementId,
                                problemRelationshipCode, problemRelationshipOid, null, null);
                        CdsInputWrapper.setProblemEffectiveTimeOnProblem(problem, time.getLow(), time.getHigh());
                        if (problem.getDiagnosticEventTime() == null) {
                            CdsInputWrapper.setProblemDiagnosticEventTimeOnProblem(problem, time.getLow(), time.getHigh());
                        }
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case encounterProblemDiagnosticEventTime: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    String problemRelationshipCode = "COMP";
                    String problemRelationshipOid = "2.16.840.1.113883.3.795.12.3.2.1";
                    String problemTemplateId = "2.16.840.1.113883.3.1829.11.7.2.18";
                    String problemIdRoot = "2.16.840.1.113883.3.795.5.2.3.6";
                    if (time != null) {
                        logger.debug(METHODNAME, "setting problem diagnostic event time: ", time.getLow(), " - ", time.getHigh());
                        Problem problem = CdsInputWrapper.getProblemFromEncounterEvent(encounterEvent, problemTemplateId, problemIdRoot, statementId,
                                problemRelationshipCode, problemRelationshipOid, null, null);
                        CdsInputWrapper.setProblemDiagnosticEventTimeOnProblem(problem, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case encounterProcedureCode: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    String relationshipCode = "COMP";
                    String relationshipOid = "2.16.840.1.113883.3.795.12.3.2.1";
                    String templateId = "2.16.840.1.113883.3.1829.11.8.1.2";
                    String idExtension = "";
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting procedure code: ", cdsCodeDTO.getLabel());
                        ProcedureEvent procedureEvent = CdsInputWrapper.getProcedureEventFromEncounterEvent(encounterEvent, templateId, statementId, idExtension,
                                relationshipCode, relationshipOid, null, null);
                        CdsInputWrapper.setProcedureCodeOnProcedureEvent(procedureEvent,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case encounterProcedureTime: {
                    EncounterEvent encounterEvent = (EncounterEvent) clinicalStatementObject;
                    String relationshipCode = "COMP";
                    String relationshipOid = "2.16.840.1.113883.3.795.12.3.2.1";
                    String templateId = "2.16.840.1.113883.3.1829.11.8.1.2";
                    String idExtension = "";
                    if (time != null) {
                        logger.debug(METHODNAME, "setting procedure time: ", time.getLow(), " - ", time.getHigh());
                        ProcedureEvent procedureEvent = CdsInputWrapper.getProcedureEventFromEncounterEvent(encounterEvent, templateId, statementId, idExtension,
                                relationshipCode, relationshipOid, null, null);
                        CdsInputWrapper.setProcedureTimeOnProcedureEvent(procedureEvent, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case orderObservationFocus: {
                    ObservationOrder observationOrder = (ObservationOrder) clinicalStatementObject;
                    if (cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting observation focus: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setObservationFocusOnObservationOrder(observationOrder,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case orderEventTime: {
                    ObservationOrder observationOrder = (ObservationOrder) clinicalStatementObject;
                    if (time != null) {
                        logger.debug(METHODNAME, "setting order event time: ", time.getLow(), " - ", time.getHigh());
                        CdsInputWrapper.setOrderEventTimeOnObservationOrder(observationOrder, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                case substanceAdministrationSubstanceCode: {
                    SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) clinicalStatementObject;
                    if (substanceAdministrationEvent != null && cdsCodeDTO != null) {
                        logger.debug(METHODNAME, "setting substance code: ", cdsCodeDTO.getLabel());
                        CdsInputWrapper.setSubstanceCodeOnSubstanceAdministrationEvent(substanceAdministrationEvent,
                                cdsCodeDTO.getCode(), cdsCodeDTO.getCodeSystem(), cdsCodeDTO.getDisplayName(), cdsCodeDTO.getCodeSystemName());
                        substanceAdministrationEvent.getSubstance().setId(CdsObjectFactory.getII());
                    } else {
                        logger.error(METHODNAME, "cdsCodeDTO was null! ");
                    }
                    break;
                }
                case substanceAdministrationTimeInterval: {
                    SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) clinicalStatementObject;
                    if (time != null) {
                        logger.debug(METHODNAME, "setting administration event time: ", time.getLow(), " - ", time.getHigh());
                        CdsInputWrapper.setAdministrationTimeIntervalOnSubstanceAdministrationEvent(substanceAdministrationEvent, time.getLow(), time.getHigh());
                    } else {
                        logger.error(METHODNAME, "time was null! ");
                    }
                    break;
                }
                default:
                    logger.error(METHODNAME, "UNHANDLED DetectedModelElementType: ", detectedModelElementType);
                    break;
            }
        }
        for (Class clinicalStatementClass : objectMap.keySet()) {
            logger.info(METHODNAME, "clinicalStatementClass=", clinicalStatementClass);
            if (clinicalStatementClass == ObservationResult.class) {
                logger.info(METHODNAME, "adding ObservationResult");
                cdsInputWrapper.setObservationResultClinicalStatement((ObservationResult) objectMap.get(clinicalStatementClass));
            } else if (clinicalStatementClass == Problem.class) {
                logger.info(METHODNAME, "adding Problem");
                cdsInputWrapper.setProblemClinicalStatement((Problem) objectMap.get(clinicalStatementClass));
            } else if (clinicalStatementClass == EncounterEvent.class) {
                logger.info(METHODNAME, "adding EncounterEvent");
                cdsInputWrapper.setEncounterEventClinicalStatement((EncounterEvent) objectMap.get(clinicalStatementClass));
            } else if (clinicalStatementClass == ObservationOrder.class) {
                logger.info(METHODNAME, "adding ObservationOrder");
                cdsInputWrapper.setObservationOrderClinicalStatement((ObservationOrder) objectMap.get(clinicalStatementClass));
            } else if (clinicalStatementClass == SubstanceAdministrationEvent.class) {
                logger.info(METHODNAME, "adding SubstanceAdministrationEvent");
                SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) objectMap.get(clinicalStatementClass);
                // add default date if none
                IVLTS administrationTimeInterval = substanceAdministrationEvent.getAdministrationTimeInterval();
                if (administrationTimeInterval != null) {
                    if (StringUtils.isEmpty(administrationTimeInterval.getLow()) || StringUtils.isEmpty(administrationTimeInterval.getHigh())) {
                        String today = org.opencds.support.util.DateUtils.getISODateFormat(new Date());
                        substanceAdministrationEvent.setAdministrationTimeInterval(CdsObjectFactory.getIVLTS(today, today));
                    }
                } else {
                    String today = org.opencds.support.util.DateUtils.getISODateFormat(new Date());
                    substanceAdministrationEvent.setAdministrationTimeInterval(CdsObjectFactory.getIVLTS(today, today));
                }

                cdsInputWrapper.addSubstanceAdministrationEvent(substanceAdministrationEvent);
            }
        }
    }

    public static void processMap(
            CdsInputWrapper cdsInputWrapper,
            String statementId,
            Map<String, Map<DetectedModelElementType, List<Object>>> clinicalStatementMap)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "processMap ";
        logger.info(METHODNAME, "clinicalStatementMap=", clinicalStatementMap);
        for (String label : clinicalStatementMap.keySet()) {
            logger.info(METHODNAME, "processing: ", label);
            setClinicalStatement(cdsInputWrapper, statementId, clinicalStatementMap.get(label));
        }
    }

}
