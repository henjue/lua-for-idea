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

package com.sylvanaar.idea.Lua.lang.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.sylvanaar.idea.Lua.LuaFileType;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiElement;
import com.sylvanaar.idea.Lua.lang.psi.util.LuaPsiUtils;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import org.jetbrains.annotations.NotNull;

public class LuaPsiElementImpl extends ASTWrapperPsiElement implements LuaPsiElement {
 private static final Logger log = Logger.getInstance("#LuaPsiElementImpl");
	public LuaPsiElementImpl(ASTNode node) {
		super(node);
	}

	public String toString() {
		return getNode().getElementType().toString();
	}

	@NotNull
	public Language getLanguage() {
		return LuaFileType.LUA_LANGUAGE;
	}


    public void accept(LuaElementVisitor visitor) {
      visitor.visitElement(this);
    }
//    public void accept(@NotNull PsiElementVisitor visitor) {
//        if (visitor instanceof LuaElementVisitor) {
//            ((LuaElementVisitor) visitor).visitElement(this);
//        } else {
//            visitor.visitElement(this);
//        }
//    }


     public SearchScope getUseScope() {
       //This is true as long as we have no inter-file references
       return new LocalSearchScope(getContainingFile());
     } 



//    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
//                                       @NotNull ResolveState resolveState,
//                                       PsiElement lastParent,
//                                       @NotNull PsiElement place) {
//
//       // log.info("decls " + this);
//        final PsiElement[] children = getChildren();
//        for (PsiElement child : children) {
//            if (child == lastParent) break;
//            if (!child.processDeclarations(processor, resolveState, lastParent, place)) return false;
//        }
//        return true;
//    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        return LuaPsiUtils.processChildDeclarations(this, processor, state, lastParent, place);
    }

    public void acceptChildren(LuaElementVisitor visitor) {
      PsiElement child = getFirstChild();
      while (child != null) {
        if (child instanceof LuaPsiElement) {
          ((LuaPsiElement) child).accept(visitor);
        }

        child = child.getNextSibling();
      }
    }

//    @Override
//    public LuaPsiElement replace(LuaPsiElement replacement) {
//        return null;
//    }
//
//    @Override
//    public LuaPsiElement addBefore(LuaPsiElement replacement, LuaPsiElement original) {
//        return null;
//    }


}
