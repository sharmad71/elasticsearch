/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.painless.ir;

import org.elasticsearch.painless.ClassWriter;
import org.elasticsearch.painless.MethodWriter;
import org.elasticsearch.painless.phase.IRTreeVisitor;
import org.elasticsearch.painless.symbol.WriteScope;

import static org.elasticsearch.painless.WriterConstants.CLASS_TYPE;

/**
 * Represents reading a value from a member field from
 * the main class.
 */
public class MemberFieldLoadNode extends ExpressionNode {

    /* ---- begin node data ---- */

    protected String name;
    protected boolean isStatic;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    /* ---- end node data, begin visitor ---- */

    @Override
    public <Input, Output> Output visit(IRTreeVisitor<Input, Output> irTreeVisitor, Input input) {
        return irTreeVisitor.visitMemberFieldLoad(this, input);
    }

    /* ---- end visitor ---- */

    @Override
    public void write(ClassWriter classWriter, MethodWriter methodWriter, WriteScope writeScope) {
        methodWriter.writeDebugInfo(location);

        if (isStatic) {
            methodWriter.getStatic(CLASS_TYPE, name, MethodWriter.getType(getExpressionType()));
        } else {
            methodWriter.loadThis();
            methodWriter.getField(CLASS_TYPE, name, MethodWriter.getType(getExpressionType()));
        }
    }
}
