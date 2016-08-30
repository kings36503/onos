/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.lisp.msg.protocols;

import com.google.common.testing.EqualsTester;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;
import org.onosproject.lisp.msg.exceptions.LispParseError;
import org.onosproject.lisp.msg.exceptions.LispReaderException;
import org.onosproject.lisp.msg.exceptions.LispWriterException;
import org.onosproject.lisp.msg.protocols.DefaultLispMapRegister.RegisterReader;
import org.onosproject.lisp.msg.protocols.DefaultLispMapRegister.RegisterWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for DefaultLispMapRegister class.
 */
public final class DefaultLispMapRegisterTest {

    private LispMapRegister register1;
    private LispMapRegister sameAsRegister1;
    private LispMapRegister register2;

    @Before
    public void setup() {

        LispMapRegister.RegisterBuilder builder1 =
                        new DefaultLispMapRegister.DefaultRegisterBuilder();

        register1 = builder1
                        .withIsProxyMapReply(true)
                        .withIsWantMapNotify(false)
                        .withKeyId((short) 1)
                        .withNonce(1L)
                        .build();

        LispMapRegister.RegisterBuilder builder2 =
                        new DefaultLispMapRegister.DefaultRegisterBuilder();

        sameAsRegister1 = builder2
                        .withIsProxyMapReply(true)
                        .withIsWantMapNotify(false)
                        .withKeyId((short) 1)
                        .withNonce(1L)
                        .build();

        LispMapRegister.RegisterBuilder builder3 =
                        new DefaultLispMapRegister.DefaultRegisterBuilder();

        register2 = builder3
                        .withIsProxyMapReply(true)
                        .withIsWantMapNotify(false)
                        .withKeyId((short) 2)
                        .withNonce(2L)
                        .build();
    }

    @Test
    public void testEquality() {
        new EqualsTester()
                .addEqualityGroup(register1, sameAsRegister1)
                .addEqualityGroup(register2).testEquals();
    }

    @Test
    public void testConstruction() {
        DefaultLispMapRegister register = (DefaultLispMapRegister) register1;

        assertThat(register.isProxyMapReply(), is(true));
        assertThat(register.isWantMapNotify(), is(false));
        assertThat(register.getKeyId(), is((short) 1));
        assertThat(register.getNonce(), is(1L));
    }

    @Test
    public void testSerialization() throws LispReaderException, LispWriterException, LispParseError {
        ByteBuf byteBuf = Unpooled.buffer();

        RegisterWriter writer = new RegisterWriter();
        writer.writeTo(byteBuf, register1);

        RegisterReader reader = new RegisterReader();
        LispMapRegister deserialized = reader.readFrom(byteBuf);

        new EqualsTester()
                .addEqualityGroup(register1, deserialized).testEquals();
    }
}
