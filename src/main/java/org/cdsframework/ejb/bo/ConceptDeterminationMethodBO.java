/**
 * The MTS cds EJB project is the base framework for the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
 * for more details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
 * Department of Health and Mental Hygiene, Bureau of Immunization to have (without
 * restriction, limitation, and warranty) complete irrevocable access and rights to this
 * project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see https://www.hln.com/services/open-source/ or
 * send correspondence to ice@hln.com.
 */
package org.cdsframework.ejb.bo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.cdsframework.base.BaseBO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.ejb.local.CdsMGRLocal;
import org.cdsframework.enumeration.DeploymentEnvironment;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.opencds.config.schema.ConceptDeterminationMethod;
import org.opencds.config.schema.ConceptDeterminationMethods;
import org.opencds.config.schema.ObjectFactory;

/**
 *
 * @author HLN Consulting, LLC
 */
@Stateless
public class ConceptDeterminationMethodBO extends BaseBO<ConceptDeterminationMethodDTO> {

    @EJB
    private CdsMGRLocal cdsMGRLocal;

    @Override
    public Map<String, byte[]> exportData(ConceptDeterminationMethodDTO baseDTO, Class queryClass, SessionDTO sessionDTO, PropertyBagDTO propertyBagDTO)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "exportData ";

        Map<String, byte[]> fileMap = new HashMap<>();

        if (ConceptDeterminationMethodDTO.OpenCdsExport.class == queryClass) {
            logger.info(METHODNAME, "OpenCDS export of Concept Determination Method: ", baseDTO.getCode());

            String codeSystem = (String) baseDTO.getQueryMap().get("codeSystem");
            logger.info(METHODNAME, "codeSystem: ", codeSystem);

            DeploymentEnvironment environment = (DeploymentEnvironment) baseDTO.getQueryMap().get("environment");
            if (environment == null) {
                environment = DeploymentEnvironment.PRODUCTION;
            }
            logger.info(METHODNAME, "environment: ", environment);


            String cdmId = propertyBagDTO.get("cdmId", String.class);
            logger.info(METHODNAME, "cdmId: ", cdmId);

            ConceptDeterminationMethod conceptDeterminationMethod;
            try {
                conceptDeterminationMethod = cdsMGRLocal.getConceptDeterminationMethod(baseDTO, codeSystem, environment, sessionDTO, propertyBagDTO);
            } catch (ConstraintViolationException e) {
                logger.error(e);
                throw new MtsException(e.getMessage());
            }

            ObjectFactory objectFactory = new ObjectFactory();

            ConceptDeterminationMethods conceptDeterminationMethods = objectFactory.createConceptDeterminationMethods();
            conceptDeterminationMethods.getConceptDeterminationMethod().add(conceptDeterminationMethod);

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ConceptDeterminationMethods.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                jaxbMarshaller.marshal(conceptDeterminationMethods, outputStream);
                String output = outputStream.toString("UTF-8");
                output = output.replaceAll("[^\\x20-\\x7e]", "");
                fileMap.put("cdm.xml", output.getBytes("UTF-8"));
                outputStream.close();

            } catch (JAXBException e) {
                throw new MtsException(e.getMessage(), e);
            } catch (IOException e) {
                throw new MtsException(e.getMessage(), e);
            }
        }

        return fileMap;
    }

}
