/*
 * Copyright (c) 2005, Oracle and/or its affiliates. All rights reserved.
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
 * $Id: SignatureMethodParameterSpec.java,v 1.3 2005/05/10 16:40:17 mullan Exp $
 */
package javax.xml.crypto.dsig.spec;

import javax.xml.crypto.dsig.SignatureMethod;
import java.security.spec.AlgorithmParameterSpec;

/**
 * A specification of algorithm parameters for an XML {@link SignatureMethod} 
 * algorithm. The purpose of this interface is to group (and provide type 
 * safety for) all signature method parameter specifications. All signature
 * method parameter specifications must implement this interface.
 *
 * @author Sean Mullan
 * @author JSR 105 Expert Group
 * @since 1.6
 * @see SignatureMethod
 */
public interface SignatureMethodParameterSpec extends AlgorithmParameterSpec {}
