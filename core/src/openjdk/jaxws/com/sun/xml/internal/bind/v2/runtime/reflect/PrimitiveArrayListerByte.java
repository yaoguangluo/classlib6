/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/**
 * {@link Lister} for primitive type arrays.
 *
 * <p>
 * B y t e ArrayLister is used as the master to generate the rest of the
 * lister classes. Do not modify the generated copies.
 */
final class PrimitiveArrayListerByte<BeanT> extends Lister<BeanT,byte[],Byte,PrimitiveArrayListerByte.ByteArrayPack> {
    
    private PrimitiveArrayListerByte() {
    }

    /*package*/ static void register() {
        Lister.primitiveArrayListers.put(Byte.TYPE,new PrimitiveArrayListerByte());
    }

    public ListIterator<Byte> iterator(final byte[] objects, XMLSerializer context) {
        return new ListIterator<Byte>() {
            int idx=0;
            public boolean hasNext() {
                return idx<objects.length;
            }

            public Byte next() {
                return objects[idx++];
            }
        };
    }

    public ByteArrayPack startPacking(BeanT current, Accessor<BeanT, byte[]> acc) {
        return new ByteArrayPack();
    }

    public void addToPack(ByteArrayPack objects, Byte o) {
        objects.add(o);
    }

    public void endPacking( ByteArrayPack pack, BeanT bean, Accessor<BeanT,byte[]> acc ) throws AccessorException {
        acc.set(bean,pack.build());
    }

    public void reset(BeanT o,Accessor<BeanT,byte[]> acc) throws AccessorException {
        acc.set(o,new byte[0]);
    }

    static final class ByteArrayPack {
        byte[] buf = new byte[16];
        int size;

        void add(Byte b) {
            if(buf.length==size) {
                // realloc
                byte[] nb = new byte[buf.length*2];
                System.arraycopy(buf,0,nb,0,buf.length);
                buf = nb;
            }
            if(b!=null)
            buf[size++] = b;
        }

        byte[] build() {
            if(buf.length==size)
                // if we are lucky enough
                return buf;

            byte[] r = new byte[size];
            System.arraycopy(buf,0,r,0,size);
            return r;
        }
    }
}
