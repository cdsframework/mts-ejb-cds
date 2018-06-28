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
package org.cdsframework.factory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.ejb.bo.CdsCodeBO;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.util.AuthenticationUtils;
import org.cdsframework.util.EJBUtils;
import org.cdsframework.util.LogUtils;
import org.opencds.vmr.v1_0.schema.CD;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsCodeDTOObjectFactory {

    private final static LogUtils logger = LogUtils.getLogger(CdsCodeDTOObjectFactory.class);

    private final static LoadingCache<CacheKey, CdsCodeDTO> codeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<CacheKey, CdsCodeDTO>() {
                @Override
                public CdsCodeDTO load(CacheKey key) throws ValidationException, MtsException, NotFoundException, AuthenticationException, AuthorizationException {
                    CdsCodeDTO result = null;
                    CdsCodeDTO queryCodeDTO = new CdsCodeDTO();
                    queryCodeDTO.setCode(key.getCode());
                    queryCodeDTO.setCodeSystem(key.getCodeSystem());
                    CdsCodeBO cdsCodeBO = (CdsCodeBO) EJBUtils.getDtoBo(CdsCodeDTO.class);
                    result = cdsCodeBO.findByQueryMain(
                            queryCodeDTO,
                            CdsCodeDTO.ByCodeSystemCode.class,
                            new ArrayList<Class>(),
                            AuthenticationUtils.getInternalSessionDTO(),
                            new PropertyBagDTO());
                    return result;
                }
            });

    public static CD getCDFromCdsCodeDTO(CdsCodeDTO cdsCodeDTO) {
        CD result = new CD();
        result.setCode(cdsCodeDTO.getCode());
        result.setDisplayName(cdsCodeDTO.getDisplayName());
        result.setCodeSystem(cdsCodeDTO.getCodeSystem());
        result.setCodeSystemName(cdsCodeDTO.getCodeSystemName());
        return result;
    }

    public static CdsCodeDTO getCdsCodeDTOFromCD(CD cd) {
        CdsCodeDTO result = new CdsCodeDTO();
        result.setCode(cd.getCode());
        result.setDisplayName(cd.getDisplayName());
        result.setCodeSystem(cd.getCodeSystem());
        result.setCodeSystemName(cd.getCodeSystemName());
        return result;
    }

    public static CdsCodeDTO getCdsCodeDTO(String codeSystem, String code) throws MtsException {
        final String METHODNAME = "getCdsCodeDTO ";
        if (codeSystem == null) {
            throw new MtsException("codeSystem is null!");
        }
        if (code == null) {
            throw new MtsException("code is null!");
        }
        CdsCodeDTO result = null;
        CacheKey key = new CacheKey();
        key.setCode(code);
        key.setCodeSystem(codeSystem);
        try {
            result = codeCache.get(key);
        } catch (ExecutionException e) {
            logger.error(METHODNAME, e);
                    throw new MtsException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(METHODNAME, "unexpected exception: ", e);
            throw new MtsException("unexpected exception: " + e.getMessage(), e);
        }
        if (result == null) {
            throw new MtsException("code is null for: " + codeSystem + "/" + code);
        }
        return result;
    }

    private static class CacheKey {

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        private String codeSystem;

        public String getCodeSystem() {
            return codeSystem;
        }

        public void setCodeSystem(String codeSystem) {
            this.codeSystem = codeSystem;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + (this.code != null ? this.code.hashCode() : 0);
            hash = 83 * hash + (this.codeSystem != null ? this.codeSystem.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CacheKey other = (CacheKey) obj;
            if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
                return false;
            }
            if ((this.codeSystem == null) ? (other.codeSystem != null) : !this.codeSystem.equals(other.codeSystem)) {
                return false;
            }
            return true;
        }

    }
}
