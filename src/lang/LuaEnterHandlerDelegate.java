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

package com.sylvanaar.idea.Lua.lang;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;

/**
 * Created by IntelliJ IDEA.
 * User: Jon S Akhtar
 * Date: 1/24/11
 * Time: 1:43 AM
 */
public class LuaEnterHandlerDelegate implements EnterHandlerDelegate {
    @Override
    public Result preprocessEnter(PsiFile file, Editor editor, Ref<Integer> caretOffsetRef, Ref<Integer> caretAdvance, DataContext dataContext, EditorActionHandler originalHandler) {
    Document document = editor.getDocument();
    CharSequence text = document.getCharsSequence();
    int caretOffset = caretOffsetRef.get().intValue();
    if (CodeInsightSettings.getInstance().SMART_INDENT_ON_ENTER) {
      // special case: enter inside "()" or "{}"
      if (caretOffset > 3 && caretOffset < text.length() && ((text.subSequence(caretOffset-3, caretOffset).toString().equals("end")))) {
        //originalHandler.execute(editor, dataContext);
        PsiDocumentManager.getInstance(file.getProject()).commitDocument(document);
        int i=0;
          try {
           i = CodeStyleManager.getInstance(file.getProject()).adjustLineIndent(file, caretOffset-3);
           editor.getCaretModel().moveToOffset(i+3);
           originalHandler.execute(editor, dataContext);
        }
        catch (IncorrectOperationException e) {
        }

        return Result.Stop;
      }
    }
    return Result.Continue;
  }
}