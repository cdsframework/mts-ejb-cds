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
package org.cdsframework.ejb.dao;

import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.cdsframework.base.BaseDAO;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.callback.QueryCallback;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.enumeration.Operator;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.group.ByGeneralProperties;
import org.cdsframework.group.FindAll;
import org.cdsframework.util.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
@LocalBean
public class OpenCdsConceptDAO extends BaseDAO<OpenCdsConceptDTO> {

    @Override
    protected void initialize() throws MtsException {

        registerDML(FindAll.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = getSelectDML();
                String partId = (String) baseDTO.getQueryMap().get("partId");
                logger.info("FindAll partId=", partId);
                if (!StringUtils.isEmpty(partId)) {
                    sql = "select * from v_opencds_concept_collection";
                    setWildcardPredicateValue(baseDTO, " lower(part_id) like :part_id ", "partId");
                    sql += getAndClearPredicateMap(" where ", "", Operator.AND);
                }
                logger.info("FindAll sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "part_id", "partId", namedParameters);
            }

        }, false);

        // This requires the ICE module to be present and is used only by ICE
        registerDML(OpenCdsConceptDTO.ByOpenCdsVaccineMapping.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String vaccineId = (String) baseDTO.getQueryMap().get("vaccineId");
                logger.info("getQueryDML ByOpenCdsVaccineMapping vaccineId=", vaccineId);
                if (!StringUtils.isEmpty(vaccineId)) {
                    setNonWildcardPredicateValue(baseDTO, " vaccine_id = :vaccine_id ", "vaccineId");
                }
                String sql = "select * from vw_vaccine_vers_concept " + getAndClearPredicateMap(" where ", "", Operator.AND);
                logger.info("getQueryDML ByOpenCdsVaccineMapping sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "vaccine_id", "vaccineId", namedParameters);
            }

        }, false);

        // This requires the ICE module to be present and is used only by ICE
        registerDML(OpenCdsConceptDTO.ByOpenCdsVaccineGroupMapping.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String groupId = (String) baseDTO.getQueryMap().get("groupId");
                logger.info("getQueryDML ByOpenCdsVaccineGroupMapping groupId=", groupId);
                logger.info("getQueryDML ByOpenCdsVaccineGroupMapping baseDTO.getQueryMap()=", baseDTO.getQueryMap());
                if (!StringUtils.isEmpty(groupId)) {
                    setNonWildcardPredicateValue(baseDTO, " group_id = :group_id ", "groupId");
                }
                String sql = "select * from vw_vaccine_group_vers_concept " + getAndClearPredicateMap(" where ", "", Operator.AND);
                logger.info("getQueryDML ByOpenCdsVaccineGroupMapping sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "group_id", "groupId", namedParameters);
            }

        }, false);

        registerDML(OpenCdsConceptDTO.ByConceptCollection.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = "select * from v_opencds_concept_collection";
                Map<String, Object> queryMap = baseDTO.getQueryMap();

                boolean whereIncluded = false;
                String text = (String) queryMap.get("text");
                if (!StringUtils.isEmpty(text)) {
                    setWildcardPredicateValue(baseDTO, " lower(display_name) like :text ", "text");
                    setWildcardPredicateValue(baseDTO, " lower(code) like :text ", "text");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where " + "( ", " ) ", Operator.OR);
                    whereIncluded = true;
                }
                String partId = (String) queryMap.get("partId");
                logger.info("ByConceptCollection partId=", partId);
                if (!StringUtils.isEmpty(partId)) {
                    setWildcardPredicateValue(baseDTO, " lower(part_id) like :part_id ", "partId");
                    sql += getAndClearPredicateMap(whereIncluded ? " and " : " where ", "", Operator.AND);
                }
                logger.info("ByConceptCollection sql=", sql);
                return sql;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
                setLowerQueryMapValue(baseDTO, "part_id", "partId", namedParameters);
            }

        }, false);

        this.registerDML(OpenCdsConceptDTO.ByConceptDeterminationMethod.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select distinct oc.* from vw_opencds_concept oc"
                        + " join opencds_concept_rel ocr on ocr.concept_code_id = oc.code_id"
                        + " join concept_determination_method cdm on cdm.code_id = ocr.determination_method"
                        + " where cdm.code = :cdm_code order by oc.code;";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                setExactQueryMapValue(baseDTO, "cdm_code", "cdm_code", namedParameters);
            }

        }, false);

        this.registerDML(OpenCdsConceptDTO.ByCode.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from vw_opencds_concept where code = :code";
            }
        }, false);

        this.registerDML(OpenCdsConceptDTO.ByCodeSystemId.class, new QueryCallback(getDtoTableName()) {

            @Override
            protected String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                return "select * from vw_opencds_concept oc "
                        + " where exists (select * from opencds_concept_rel ocr, cds_code cc "
                        + "    where oc.CODE_ID = ocr.CONCEPT_CODE_ID and ocr.CDS_CODE_ID = cc.CODE_ID and cc.CODE_SYSTEM_ID = :code_system_id )";
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
                    throws MtsException {
                namedParameters.addValue("code_system_id", propertyBagDTO.get("code_system_id"));
            }

        }, false);

        this.registerDML(ByGeneralProperties.class, new QueryCallback(getDtoTableName()) {
            @Override
            public String getQueryDML(BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) {
                String sql = "select * from vw_opencds_concept ";
                if (baseDTO.getQueryMap().containsKey("code_system_id")) {
                    setNonWildcardPredicateValue(baseDTO,
                            " exists (select * from OPENCDS_CONCEPT_REL ocr, cds_code cc "
                            + "       where ocr.cds_code_id = cc.code_id "
                            + "          and opencds_concept.CODE_ID = ocr.CONCEPT_CODE_ID "
                            + "          and cc.code_system_id = :code_system_id) ", "code_system_id");
                }
                if (baseDTO.getQueryMap().containsKey("version_id")) {
                    setNonWildcardPredicateValue(baseDTO,
                            " exists (select * from OPENCDS_CONCEPT_REL ocr, CDS_VERSION_CDM_REL cvcr, CDS_VERSION cv "
                            + "       where opencds_concept.CODE_ID = ocr.CONCEPT_CODE_ID "
                            + "         and cvcr.DETERMINATION_METHOD = ocr.DETERMINATION_METHOD "
                            + "         and cv.VERSION_ID = cvcr.VERSION_ID "
                            + "         and cv.version_id = :version_id) ", "version_id");
                }
                if (baseDTO.getQueryMap().containsKey("cdm_id")) {
                    setNonWildcardPredicateValue(baseDTO,
                            " exists (select * from OPENCDS_CONCEPT_REL ocr "
                            + "       where opencds_concept.CODE_ID = ocr.CONCEPT_CODE_ID "
                            + "        and ocr.DETERMINATION_METHOD = :cdm_id) ", "cdm_id");
                }
                setWildcardPredicateValue(baseDTO, "( lower(code) like :text OR lower(display_name) like :text OR lower(description) like :text )", "text");
                String result = sql + getAndClearPredicateMap(" where ", " ", Operator.AND);
                logger.warn(result);
                return result;
            }

            @Override
            protected void getCallbackNamedParameters(MapSqlParameterSource namedParameters, BaseDTO baseDTO, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO) throws MtsException {
                if (baseDTO.getQueryMap().containsKey("code_system_id")) {
                    setExactQueryMapValue(baseDTO, "code_system_id", "code_system_id", namedParameters);
                }
                if (baseDTO.getQueryMap().containsKey("version_id")) {
                    setExactQueryMapValue(baseDTO, "version_id", "version_id", namedParameters);
                }
                if (baseDTO.getQueryMap().containsKey("cdm_id")) {
                    setExactQueryMapValue(baseDTO, "cdm_id", "cdm_id", namedParameters);
                }
                setLowerQueryMapValue(baseDTO, "text", "text", namedParameters);
            }

        }, false);
    }
}
