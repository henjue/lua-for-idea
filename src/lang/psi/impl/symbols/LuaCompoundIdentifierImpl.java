/*
 * Copyright 2011 Jon S Akhtar (Sylvanaar)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sylvanaar.idea.Lua.lang.psi.impl.symbols;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.LuaFunctionDefinition;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaFieldIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaIdentifierList;
import com.sylvanaar.idea.Lua.lang.psi.symbols.LuaCompoundIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.symbols.LuaIdentifier;
import com.sylvanaar.idea.Lua.lang.psi.symbols.LuaSymbol;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 1/20/11
 * Time: 3:44 AM
 */
public class LuaCompoundIdentifierImpl extends LuaIdentifierImpl
        implements LuaCompoundIdentifier {
    private String operator;

    public LuaCompoundIdentifierImpl(ASTNode node) {
        super(node);
    }


    @Override
    public void accept(LuaElementVisitor visitor) {
        visitor.visitCompoundIdentifier(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof LuaElementVisitor) {
            ((LuaElementVisitor) visitor).visitCompoundIdentifier(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Nullable
    public LuaExpression getRightSymbol() {
        LuaExpression[] e = findChildrenByClass(LuaExpression.class);
        return e.length>1?e[1]:null;
    }

    @Nullable
    public LuaExpression getLeftSymbol() {
        LuaExpression[] e = findChildrenByClass(LuaExpression.class);
        return e.length>0?e[0]:null;
    }

    @Nullable
    @Override
    public String toString() {
        try {
        return "GetTable: " +  getLeftSymbol().getText() + getOperator() + getRightSymbol().getText();
        } catch (Throwable t) { return "err"; }
    }

    public String getOperator() {
        try {
        return findChildByType(LuaElementTypes.DOT).getText();
        } catch (Throwable t) { return "err"; }
    }

    @Override
    public LuaCompoundIdentifier getEnclosingIdentifier() {
        LuaCompoundIdentifier s = this;

        while (s.getParent() instanceof LuaCompoundIdentifier)
            s = (LuaCompoundIdentifier) getParent();

        return s;
    }

    public boolean isCompoundDeclaration() {
        PsiElement e = getParent().getParent();
        return e instanceof LuaIdentifierList || e instanceof LuaFunctionDefinition;
    }


    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                       @NotNull ResolveState state, PsiElement lastParent,
                                       @NotNull PsiElement place) {
        PsiElement e = getParent().getParent();
        
        if (isCompoundDeclaration()) {
            if (!processor.execute(this,state)) return false;
        }

        return true;// super.processDeclarations(processor, state, lastParent, place);
    }

    @Override
    public PsiElement getScopeIdentifier() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LuaFieldIdentifier getLeftMostField() {
        return findChildByClass(LuaFieldIdentifier.class);
    }

    @Override
    public boolean isSameKind(LuaSymbol symbol) {
        return symbol instanceof LuaCompoundIdentifier;
    }
    
    @Override
    public LuaIdentifier getNameSymbol() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDefinedName() {
        return getName();  
    }
}
