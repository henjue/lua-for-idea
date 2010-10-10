///*
// * Copyright 2010 Jon S Akhtar (Sylvanaar)
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//package org.jetbrains.plugins.Lua.lang.surroundWith.surrounders;
//
//import com.intellij.openapi.util.Key;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.psi.PsiElement;
//import com.intellij.util.IncorrectOperationException;
//import org.jetbrains.plugins.Lua.lang.psi.*;
//import org.jetbrains.plugins.Lua.lang.psi.api.LuaResolveResult;
//import org.jetbrains.plugins.Lua.lang.psi.api.statements.expressions.GrReferenceExpression;
//import org.jetbrains.plugins.Lua.lang.psi.api.statements.expressions.path.GrMethodCallExpression;
//import org.jetbrains.plugins.Lua.lang.psi.api.statements.blocks.GrClosableBlock;
//
///**
// * User: Dmitry.Krasilschikov
// * Date: 22.05.2007
// */
//public class LuaSurrounderByClosure extends LuaManyStatementsSurrounder {
//  private static final Key<LuaResolveResult> REF_RESOLVE_RESULT_KEY = Key.create("REF_RESOLVE_RESULT");
//
//  public String getTemplateDescription() {
//    return "{ -> ... }.call()";
//  }
//
//  protected LuaPsiElement doSurroundElements(PsiElement[] elements) throws IncorrectOperationException {
//    for (PsiElement element : elements) {
//      if (element instanceof LuaPsiElement) {
//        ((LuaPsiElement) element).accept(new MyMemoizingVisitor());
//      }
//    }
//
//    LuaPsiElementFactory factory = LuaPsiElementFactory.getInstance(elements[0].getProject());
//    final GrMethodCallExpression call = (GrMethodCallExpression) factory.createTopElementFromText("{ -> }.call()");
//    final GrClosableBlock closure = (GrClosableBlock) ((GrReferenceExpression) call.getInvokedExpression()).getQualifierExpression();
//    addStatements(closure, elements);
//    return call;
//  }
//
//  protected TextRange getSurroundSelectionRange(LuaPsiElement element) {
//    element.accept(new MyRestoringVisitor());
//    assert element instanceof GrMethodCallExpression;
//
//    final int offset = element.getTextRange().getEndOffset();
//    return new TextRange(offset, offset);
//  }
//
//  private static class MyMemoizingVisitor extends LuaRecursiveElementVisitor {
//    public void visitReferenceExpression(GrReferenceExpression ref) {
//      if (ref.getQualifierExpression() == null) { //only unqualified references could change their targets
//        final LuaResolveResult resolveResult = ref.advancedResolve();
//        ref.putCopyableUserData(REF_RESOLVE_RESULT_KEY, resolveResult);
//      }
//      super.visitReferenceExpression(ref);
//    }
//  }
//
//  private static class MyRestoringVisitor extends LuaRecursiveElementVisitor {
//    public void visitReferenceExpression(GrReferenceExpression ref) {
//      final LuaResolveResult oldResult = ref.getCopyableUserData(REF_RESOLVE_RESULT_KEY);
//      if (oldResult != null) {
//        assert ref.getQualifierExpression() == null;
//        final LuaResolveResult newResult = ref.advancedResolve();
//        final PsiElement oldElement = oldResult.getElement();
//        final PsiElement newElement = newResult.getElement();
//        if (oldElement != newElement || oldResult.getCurrentFileResolveContext() != newResult.getCurrentFileResolveContext()) {
//          final GrReferenceExpression qualifier = LuaPsiElementFactory.getInstance(ref.getProject()).createReferenceExpressionFromText("owner");
//          ref.setQualifierExpression(qualifier);
//        }
//      }
//      super.visitReferenceExpression(ref);
//    }
//  }
//}