/**
 * The MTS core EJB project is the base framework for the CDS Framework Middle Tier Service.
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

import java.util.Properties;

/**
 *
 * @author HLN Consulting, LLC
 */
public class EjbCdsConfiguration {
    private static final LogUtils logger = LogUtils.getLogger(EjbCdsConfiguration.class);
    private static final String CDS_ENDPOINT;
    
    static {
        final String METHODNAME = "EjbCdsConfiguration static contructor ";

        if (EjbCoreConfiguration.getInstanceProperties() != null) {
            CDS_ENDPOINT = EjbCoreConfiguration.getInstanceProperties().getProperty("CDS_ENDPOINT");        
        }
        else {
            CDS_ENDPOINT = null;
        }
        if (StringUtils.isEmpty(CDS_ENDPOINT)) {
            throw new IllegalStateException("CDS_ENDPOINT is empty! It needs to be present in the ejb-core.properties file!");
        }
        logger.info(METHODNAME, "CDS_ENDPOINT=", CDS_ENDPOINT);
    }
    
    public static Properties getInstanceProperties() {
        return EjbCoreConfiguration.getInstanceProperties() ;
    }
    
    public static boolean isMtsMaster() {
        return EjbCoreConfiguration.isMtsMaster();
    }
    public static String getCdsEndPoint() {
        return CDS_ENDPOINT;
    }
}
