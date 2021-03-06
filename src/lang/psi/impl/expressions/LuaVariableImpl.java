/*
* Copyright 2010 Jon S Akhtar (Sylvanaar)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package com.sylvanaar.idea.Lua.lang.psi.impl.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiType;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaReferenceExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaVariable;

/**
* Created by IntelliJ IDEA.
* User: Jon S Akhtar
* Date: Jun 14, 2010
* Time: 11:23:33 PM
*/
public class LuaVariableImpl extends LuaReferenceExpressionImpl implements LuaVariable {
    public LuaVariableImpl(ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return "Variable: " + getText();
    }




    @Override
    public String getCanonicalText() {
        return null;
    }




    @Override
    public boolean isSoft() {
        return false;
    }

    @Override
    public LuaPsiType getLuaType() {
        return null;
    }

    @Override
    public PsiElement replaceWithExpression(LuaExpression newCall, boolean b) {
        return null;
    }

    @Override
    public boolean isGlobal() {
        LuaIdentifier id = findChildByClass(LuaIdentifier.class);

        if (id == null)
            return false;

        return id.isGlobal();
    }

    @Override
    public boolean isLocal() {
        LuaIdentifier id = findChildByClass(LuaIdentifier.class);

        if (id == null)
            return false;

        return id.isLocal();
    }

    @Override
    public boolean isField() {
        LuaIdentifier id = findChildByClass(LuaIdentifier.class);

        if (id == null)
            return false;

        return id.isField();
    }

    @Override
    public boolean isDeclaration() {
        return false;
    }

    @Override
    public LuaReferenceExpression getPrimaryIdentifier() {
        return findChildByClass(LuaReferenceExpression.class);
    }

    @Override
    public LuaExpression getInitializer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
