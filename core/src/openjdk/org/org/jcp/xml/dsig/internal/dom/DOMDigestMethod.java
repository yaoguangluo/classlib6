/*
 * Copyright (c) 2005, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * $Id: DOMDigestMethod.java,v 1.17 2005/05/10 18:15:32 mullan Exp $
 */
package org.jcp.xml.dsig.internal.dom;

import javax.xml.crypto.*;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DOM-based abstract implementation of DigestMethod.
 *
 * @author Sean Mullan
 */
public abstract class DOMDigestMethod extends DOMStructure 
    implements DigestMethod {

    final static String SHA384 = 
	"http://www.w3.org/2001/04/xmldsig-more#sha384"; // see RFC 4051
    private DigestMethodParameterSpec params;

    /**
     * Creates a <code>DOMDigestMethod</code>.
     *
     * @param params the algorithm-specific params (may be <code>null</code>)
     * @throws InvalidAlgorithmParameterException if the parameters are not
     *    appropriate for this digest method
     */
    DOMDigestMethod(AlgorithmParameterSpec params)
	throws InvalidAlgorithmParameterException {
	if (params != null && !(params instanceof DigestMethodParameterSpec)) {
	    throw new InvalidAlgorithmParameterException
		("params must be of type DigestMethodParameterSpec");
	}
	checkParams((DigestMethodParameterSpec) params);
	this.params = (DigestMethodParameterSpec) params;
    }

    /**
     * Creates a <code>DOMDigestMethod</code> from an element. This constructor
     * invokes the abstract {@link #unmarshalParams unmarshalParams} method to
     * unmarshal any algorithm-specific input parameters.
     *
     * @param dmElem a DigestMethod element
     */
    DOMDigestMethod(Element dmElem) throws MarshalException {
	Element paramsElem = DOMUtils.getFirstChildElement(dmElem);
	if (paramsElem != null) {
	    params = unmarshalParams(paramsElem);
	}
	try {
	    checkParams(params);
	} catch (InvalidAlgorithmParameterException iape) {
	    throw new MarshalException(iape);
	}
    }

    static DigestMethod unmarshal(Element dmElem) throws MarshalException {
        String alg = DOMUtils.getAttributeValue(dmElem, "Algorithm");
        if (alg.equals(DigestMethod.SHA1)) {
            return new SHA1(dmElem);
        } else if (alg.equals(DigestMethod.SHA256)) {
            return new SHA256(dmElem);
        } else if (alg.equals(SHA384)) {
            return new SHA384(dmElem);
        } else if (alg.equals(DigestMethod.SHA512)) {
            return new SHA512(dmElem);
        } else {
            throw new MarshalException
		("unsupported DigestMethod algorithm: " + alg);
        }
    }

    /**
     * Checks if the specified parameters are valid for this algorithm. By
     * default, this method throws an exception if parameters are specified 
     * since most DigestMethod algorithms do not have parameters. Subclasses 
     * should override it if they have parameters.
     *
     * @param params the algorithm-specific params (may be <code>null</code>)
     * @throws InvalidAlgorithmParameterException if the parameters are not
     *    appropriate for this digest method
     */
    void checkParams(DigestMethodParameterSpec params) 
	throws InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException("no parameters " +
                "should be specified for the " + getMessageDigestAlgorithm()
                 + " DigestMethod algorithm");
        }
    }

    public final AlgorithmParameterSpec getParameterSpec() {
	return params;
    }

    /**
     * Unmarshals <code>DigestMethodParameterSpec</code> from the specified 
     * <code>Element</code>. By default, this method throws an exception since
     * most DigestMethod algorithms do not have parameters. Subclasses should 
     * override it if they have parameters.
     *
     * @param paramsElem the <code>Element</code> holding the input params
     * @return the algorithm-specific <code>DigestMethodParameterSpec</code>
     * @throws MarshalException if the parameters cannot be unmarshalled
     */
    DigestMethodParameterSpec 
	unmarshalParams(Element paramsElem) throws MarshalException {
        throw new MarshalException("no parameters should " +
            "be specified for the " + getMessageDigestAlgorithm() + 
	    " DigestMethod algorithm");
    }

    /**
     * This method invokes the abstract {@link #marshalParams marshalParams} 
     * method to marshal any algorithm-specific parameters.
     */
    public void marshal(Node parent, String prefix, DOMCryptoContext context) 
	throws MarshalException {
        Document ownerDoc = DOMUtils.getOwnerDocument(parent);

        Element dmElem = DOMUtils.createElement
	    (ownerDoc, "DigestMethod", XMLSignature.XMLNS, prefix);
        DOMUtils.setAttribute(dmElem, "Algorithm", getAlgorithm());

        if (params != null) {
	    marshalParams(dmElem, prefix);
        }

        parent.appendChild(dmElem);
    }

    public boolean equals(Object o) {
	if (this == o) {
            return true;
	}

        if (!(o instanceof DigestMethod)) {
            return false;
	}
        DigestMethod odm = (DigestMethod) o;

	boolean paramsEqual = (params == null ? odm.getParameterSpec() == null :
	    params.equals(odm.getParameterSpec()));

	return (getAlgorithm().equals(odm.getAlgorithm()) && paramsEqual);
    }

    /**
     * Marshals the algorithm-specific parameters to an Element and
     * appends it to the specified parent element. By default, this method 
     * throws an exception since most DigestMethod algorithms do not have 
     * parameters. Subclasses should override it if they have parameters.
     *
     * @param parent the parent element to append the parameters to
     * @param the namespace prefix to use
     * @throws MarshalException if the parameters cannot be marshalled
     */
    void marshalParams(Element parent, String prefix)
	throws MarshalException {
        throw new MarshalException("no parameters should " +
            "be specified for the " + getMessageDigestAlgorithm() + 
	    " DigestMethod algorithm");
    }

    /**
     * Returns the MessageDigest standard algorithm name.
     */
    abstract String getMessageDigestAlgorithm();

    static final class SHA1 extends DOMDigestMethod {
        SHA1(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException {
            super(params);
        }
        SHA1(Element dmElem) throws MarshalException {
            super(dmElem);
        }
        public String getAlgorithm() {
            return DigestMethod.SHA1;
        }
        String getMessageDigestAlgorithm() {
            return "SHA-1";
        }
    }

    static final class SHA256 extends DOMDigestMethod {        
	SHA256(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException {
            super(params);
        }
        SHA256(Element dmElem) throws MarshalException {
            super(dmElem);
        }
        public String getAlgorithm() {
            return DigestMethod.SHA256;
        }
        String getMessageDigestAlgorithm() {
            return "SHA-256";
        }
    }

    static final class SHA384 extends DOMDigestMethod {        
	SHA384(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException {
            super(params);
        }
        SHA384(Element dmElem) throws MarshalException {
            super(dmElem);
        }
        public String getAlgorithm() {
            return SHA384;
        }
        String getMessageDigestAlgorithm() {
            return "SHA-384";
        }
    }

    static final class SHA512 extends DOMDigestMethod {        
	SHA512(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException {
            super(params);
        }
        SHA512(Element dmElem) throws MarshalException {
            super(dmElem);
        }
        public String getAlgorithm() {
            return DigestMethod.SHA512;
        }
        String getMessageDigestAlgorithm() {
            return "SHA-512";
        }
    }
}
