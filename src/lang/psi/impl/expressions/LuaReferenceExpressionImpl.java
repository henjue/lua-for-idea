package com.sylvanaar.idea.Lua.lang.psi.impl.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.sylvanaar.idea.Lua.lang.parser.LuaElementTypes;
import com.sylvanaar.idea.Lua.lang.psi.LuaPsiType;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaExpression;
import com.sylvanaar.idea.Lua.lang.psi.expressions.LuaReferenceExpression;
import com.sylvanaar.idea.Lua.lang.psi.impl.LuaPsiElementFactoryImpl;
import com.sylvanaar.idea.Lua.lang.psi.util.ResolveUtil;
import com.sylvanaar.idea.Lua.lang.psi.visitor.LuaElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;///*


/**
 * TODO: implement all reference stuff...
 */
public class LuaReferenceExpressionImpl extends LuaExpressionImpl implements LuaReferenceExpression {
    public LuaReferenceExpressionImpl(ASTNode node) {
        super(node);
    }


    @Nullable
    public LuaExpression getQualifier() {
        final ASTNode[] nodes = getNode().getChildren(LuaElementTypes.EXPRESSION_SET);
        return (LuaExpression) (nodes.length == 1 ? nodes[0].getPsi() : null);
    }

    @Nullable
    public String getReferencedName() {
        final ASTNode nameElement = getNameElement();
        return nameElement != null ? nameElement.getText() : null;
    }

    public PsiElement getElement() {
        return this;
    }

    public PsiReference getReference() {
        return this;
    }

    public TextRange getRangeInElement() {
        final ASTNode nameElement = getNameElement();
        final int startOffset = nameElement != null ? nameElement.getStartOffset() : getNode().getTextRange().getEndOffset();
        return new TextRange(startOffset - getNode().getStartOffset(), getTextLength());
    }

    private ASTNode getNameElement() {
        PsiElement e = findChildByClass(LuaIdentifierImpl.class);

        if (e != null)
            return e.getNode();

        return null;
    }

    public PsiElement resolve() {
        final String referencedName = getReferencedName();
        if (referencedName == null) return null;

        if (getQualifier() != null) {
            return null; // TODO?
        }

        //return ResolveUtil.treeWalkUp(new ResolveUtil.ResolveProcessor(referencedName), this, this, this);
         return ResolveUtil.treeWalkUp(new ResolveUtil.ResolveProcessor(referencedName), this, this, this);
    }

    
//     private static void resolveImpl(LuaReferenceExpressionImpl refExpr, PsiScopeProcessor processor) {
//      LuaExpression qualifier = refExpr.getQualifierExpression();
//      if (qualifier == null) {
//        ResolveUtil.treeWalkUp(refExpr, processor, true);
//        if (!processor.hasCandidates()) {
//          qualifier = PsiImplUtil.getRuntimeQualifier(refExpr);
//          if (qualifier != null) {
//            processQualifier(refExpr, processor, qualifier);
//          }
//        }
//      } else {
//        if (refExpr.getDo() != LuaTokenTypes.ELLIPSIS) {
//          processQualifier(refExpr, processor, qualifier);
//        } else {
//          processQualifierForSpreadDot(refExpr, processor, qualifier);
//        }
//      }
//    }
    
    public String getCanonicalText() {
        return null;
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        final ASTNode nameElement = LuaPsiElementFactoryImpl.getInstance(getProject()).createNameIdentifier(newElementName);
        getNode().replaceChild(getNameElement(), nameElement);
        return this;
    }

    public PsiElement bindToElement(PsiElement element) throws IncorrectOperationException {
        final ASTNode nameElement = LuaPsiElementFactoryImpl.getInstance(getProject()).createNameIdentifier(((PsiNamedElement) element).getName());
        getNode().replaceChild(getNameElement(), nameElement);
        return this;
    }

    public boolean isReferenceTo(PsiElement element) {
        if (element instanceof PsiNamedElement) {
            if (Comparing.equal(getReferencedName(), ((PsiNamedElement) element).getName()))
                return resolve() == element;
        }
        return false;
    }

    @NotNull
    public Object[] getVariants() {
//        if (getQualifier() != null) {
//            return new Object[0]; // TODO?
//        }

        final ResolveUtil.VariantsProcessor processor = new ResolveUtil.VariantsProcessor();
        ResolveUtil.treeWalkUp(processor, this, this, this);
        return processor.getResult();
    }

    public boolean isSoft() {
        return false;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof LuaElementVisitor) {
            ((LuaElementVisitor) visitor).visitReferenceExpression(this);
        } else {
            visitor.visitElement(this);
        }
    }

    public String toString() {
        return "LuaReferenceExpression ("+getReferencedName()+")";
    }

    @Override
    public String getName() {
        return getText();
    }


    @Override
    public PsiElement replaceWithExpression(LuaExpression newCall, boolean b) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LuaPsiType getLuaType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement setName(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PsiElement getNameIdentifierLua() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
  public SearchScope getUseScope() {
    final LuaExpression owner = PsiTreeUtil.getParentOfType(this, LuaExpression.class);
    if (owner != null) return new LocalSearchScope(owner);
    return super.getUseScope();
  }


  public LuaExpression getQualifierExpression() {
    return findChildByClass(LuaExpression.class);
  }

  public boolean isQualified() {
    return getQualifierExpression() != null;
  }
}