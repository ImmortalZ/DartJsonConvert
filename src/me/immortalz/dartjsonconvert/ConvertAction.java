package me.immortalz.dartjsonconvert;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import me.immortalz.dartjsonconvert.util.GenerateUtil;

public class ConvertAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 得到Editor、Project对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (editor == null || project == null)
            return;

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        int endOffset = selectionModel.getSelectionEnd();

        Document document = editor.getDocument();
        int curLineNumber = document.getLineNumber(endOffset);
        int insertOffset = document.getLineStartOffset(curLineNumber);

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                String insertText = GenerateUtil.generateFromJson(selectedText);
                if (!org.apache.http.util.TextUtils.isBlank(insertText)) {
                    document.insertString(insertOffset, GenerateUtil.generateFromJson(selectedText));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                Messages.showMessageDialog(e1.getMessage().toString(), "json_convert error", Messages.getWarningIcon());
            }
        });

    }



    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        SelectionModel selectionModel = editor.getSelectionModel();
        e.getPresentation().setVisible(selectionModel.hasSelection());
    }
}
